package com.clinica_medica_Desafio.Controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.clinica_medica_Desafio.DTO.EspecialidadeMedicaDTO;
import com.clinica_medica_Desafio.Service.EspecialidadeMedicaService;
import com.clinica_medica_Desafio.Service.Exceptions.DataAcessException;
import com.clinica_medica_Desafio.Service.Exceptions.DuplicateExecption;
import com.clinica_medica_Desafio.Service.Exceptions.EmptyField;
import com.clinica_medica_Desafio.Service.Exceptions.EspecialidadeNotFoundException;
import com.clinica_medica_Desafio.model.Especialidade_Medica;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/especialidades/clinicas")
public class EspecialidadeMedicaController {

	@Autowired
	private EspecialidadeMedicaService especialidadeService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<Especialidade_Medica> getEspecialidadeById(@PathVariable Long id) {
		try {
			Especialidade_Medica especialidade = especialidadeService.findEspecialidade(id);
			return ResponseEntity.ok(especialidade);
		} catch (Exception e) {
			throw new EspecialidadeNotFoundException("Especialidade com ID " + id + " não encontrada.");
		}
	}

	@GetMapping(value = "/page")
	public ResponseEntity<Page<EspecialidadeMedicaDTO>> findPageEspecialidade(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage) {
		try {
			Pageable pageable = PageRequest.of(page, linesPerPage);
			Page<Especialidade_Medica> especialidadesPage = especialidadeService.findAllByOrderByDescricaoAsc(pageable);
			Page<EspecialidadeMedicaDTO> especialidadesDTOPage = especialidadesPage
					.map(e -> new EspecialidadeMedicaDTO(e));

			return ResponseEntity.ok().body(especialidadesDTOPage);
		} catch (DataAcessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping
	public ResponseEntity<String> insert(@RequestBody EspecialidadeMedicaDTO objDto) {
		try {
			Especialidade_Medica obj = especialidadeService.fromDTO(objDto);
			especialidadeService.insertEspecialidade(obj);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId())
					.toUri();
			return ResponseEntity.created(uri).build();
		} catch (EmptyField e) {
			throw e;
		} catch (DuplicateExecption e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro ao processar a solicitação.");
		}
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Especialidade_Medica> updateProduct(@Valid @RequestBody EspecialidadeMedicaDTO objDto,
			@PathVariable Long id) {
		try {
			Especialidade_Medica obj = especialidadeService.fromDTO(objDto);
			obj.setId(id);
			obj = especialidadeService.update(obj);
			return ResponseEntity.noContent().build();
		} catch (DuplicateExecption e) {
			throw e;
		} catch (EmptyField e) {
			throw e;
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		try {
			especialidadeService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (EspecialidadeNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping(value = "/ordenar")
	public ResponseEntity<Page<String>> findOrdenadoPorDescricao(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage) {

		Page<Especialidade_Medica> especialidadesOrdenadas = especialidadeService
				.findAllByOrderByDescricaoAsc(PageRequest.of(page, linesPerPage));
		Page<String> descricoes = especialidadesOrdenadas.map(Especialidade_Medica::getDescricao);

		return ResponseEntity.ok().body(descricoes);
	}
}

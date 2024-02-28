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

import com.clinica_medica_Desafio.DTO.RegionalDTO;
import com.clinica_medica_Desafio.Service.RegionalService;
import com.clinica_medica_Desafio.Service.Exceptions.DataAcessException;
import com.clinica_medica_Desafio.Service.Exceptions.DuplicateExecption;
import com.clinica_medica_Desafio.Service.Exceptions.EmptyField;
import com.clinica_medica_Desafio.Service.Exceptions.EspecialidadeNotFoundException;
import com.clinica_medica_Desafio.Service.Exceptions.RegiaoNotFoundException;
import com.clinica_medica_Desafio.model.Regional;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/regioes")
public class RegionalController {

	@Autowired
	private RegionalService regionalService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<Regional> getRegiaoById(@PathVariable Long id) {
		try {
			Regional regiao = regionalService.findRegional(id);
			return ResponseEntity.ok(regiao);
		} catch (Exception e) {
			throw new RegiaoNotFoundException("Regiao com ID " + id + " não encontrada.");
		}
	}
	@GetMapping(value = "/page")
	public ResponseEntity<Page<RegionalDTO>> findPageRegiao(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage) {
		try {
			Pageable pageable = PageRequest.of(page, linesPerPage);
			Page<Regional> RegiaoPage = regionalService.findAllByOrderByLabel(pageable);
			Page<RegionalDTO> RegiaoDTOPage = RegiaoPage
					.map(e -> new RegionalDTO(e));

			return ResponseEntity.ok().body(RegiaoDTOPage);
		} catch (DataAcessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	@PostMapping
	public ResponseEntity<String> insert(@RequestBody Regional objDto) {
		try {
			Regional obj = regionalService.fromDTO(objDto);
			regionalService.insert(obj);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId())
					.toUri();
			return ResponseEntity.created(uri).build();
		} catch (EmptyField e) {
			throw e;
		} catch (DataAcessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro ao processar a solicitação.");
		}
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<Regional> updateRegioes(@Valid @RequestBody Regional objDto,
			@PathVariable Long id) {
		try {
			Regional obj = regionalService.fromDTO(objDto);
			obj.setId(id);
			obj = regionalService.update(obj);
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
			regionalService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (EspecialidadeNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping(value = "/ordenar")
	public ResponseEntity<Page<String>> findOrdenadoPorLabel(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage) {

		Page<Regional> regiaoOrdenadas = regionalService
				.findAllByOrderByLabel(PageRequest.of(page, linesPerPage));
		Page<String> labol = regiaoOrdenadas.map(Regional::getLabel);

		return ResponseEntity.ok().body(labol);
	}

}



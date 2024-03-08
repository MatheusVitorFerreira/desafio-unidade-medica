package com.clinica_medica_Desafio.Controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.clinica_medica_Desafio.model.Especialidade_Medica;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/especialidades/clinicas", produces = { "application/json" })
public class EspecialidadeMedicaController {

	@Autowired
	private EspecialidadeMedicaService especialidadeService;

	@Operation(summary = "Busca uma especialidade médica no Banco de Dados", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Especialidade médica encontrada com sucesso"),
			@ApiResponse(responseCode = "404", description = "Especialidade médica não encontrada"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar a busca do arquivo"), })
	@GetMapping(value = "/{id}")
	public ResponseEntity<Especialidade_Medica> getEspecialidadeById(@PathVariable Long id) {
		Especialidade_Medica especialidade = especialidadeService.findById(id);
		return ResponseEntity.ok(especialidade);
	}

	@Operation(summary = "Busca paginada de especialidades médicas no Banco de Dados", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Busca de especialidades médicas realizada com sucesso"),
			@ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
			@ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar a busca das Regiões"),
			@ApiResponse(responseCode = "404", description = "Especialidades médicas não encontradas"),
			@ApiResponse(responseCode = "403", description = "Permissão negada para acessar as Regiões"),
			@ApiResponse(responseCode = "503", description = "Serviço indisponível no momento"), })
	@GetMapping(value = "/page")
	public ResponseEntity<Page<EspecialidadeMedicaDTO>> findPageEspecialidade(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage) {
		Pageable pageable = PageRequest.of(page, linesPerPage);
		Page<Especialidade_Medica> especialidadesPage = especialidadeService.findAllByOrderByDescricaoAsc(pageable);
		Page<EspecialidadeMedicaDTO> especialidadesDTOPage = especialidadesPage.map(e -> new EspecialidadeMedicaDTO(e));

		return ResponseEntity.ok().body(especialidadesDTOPage);
	}

	@Operation(summary = "Inserção de especialidade médica no Banco de Dados", method = "POST")
	@ApiResponses(value = { @ApiResponse(responseCode = "20", description = "Especialidade Médica criada com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de requisição inválida. Exemplos: \n* { \"EmptyField\": \"Campo vazio.\" }\n* { \"DuplicateExecption\": \"Especialidade com essa descrição já cadastrada\" }"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar a inserção"),
			@ApiResponse(responseCode = "404", description = "Regiões não encontradas"),
			@ApiResponse(responseCode = "403", description = "Permissão negada para inserir especialidades medicas"),
			@ApiResponse(responseCode = "503", description = "Serviço indisponível no momento"), })
	@PostMapping
	public ResponseEntity<Object> insert(@RequestBody Especialidade_Medica objDto) {
		Especialidade_Medica especialidade = especialidadeService.insertEspecialidade(objDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(especialidade.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@SuppressWarnings("unused")
	@Operation(summary = "Atualização de especialidades medicas no Banco de Dados", method = "PUT")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Especialidade médica Atualizada com sucesso"),
			@ApiResponse(responseCode = "201", description = "Região criada com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de requisição inválida. Exemplos: \n* { \"EmptyField\": \"Campo vazio\" }\n* { \"DuplicateExecption\": \"Especialidade com essa descrição já cadastrada\" }"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar a inserção"),
			@ApiResponse(responseCode = "404", description = "Especialidade médica não encontradas"),
			@ApiResponse(responseCode = "403", description = "Permissão negada para atualizar as especialidades médicas"),
			@ApiResponse(responseCode = "503", description = "Serviço indisponível no momento"), })
	@PutMapping(value = "/{id}")
	public ResponseEntity<Object> updateProduct(@Valid @RequestBody EspecialidadeMedicaDTO objDto,
			@PathVariable Long id) {
		Especialidade_Medica esp = especialidadeService.update(objDto, id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Exclusão de especialidade médica", method = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Especialidade médica excluída com sucesso"),
			@ApiResponse(responseCode = "404", description = "Especialidade médica não encontrada"),
			@ApiResponse(responseCode = "500", description = "Erro ao excluir a especialidade médica"),
			@ApiResponse(responseCode = "403", description = "Permissão negada para Deletar as especialidades médicas"),
			@ApiResponse(responseCode = "503", description = "Serviço indisponível no momento"), })
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		especialidadeService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Ordenar em Ordem Alfabética as especialidades médicas", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista as descrições ordenada por nome obtida com sucesso"),
			@ApiResponse(responseCode = "400", description = "Parâmetros de requisição inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro interno no servidor"),
			@ApiResponse(responseCode = "403", description = "Permissão negada para Ordenar as especialidades médicas"),
			@ApiResponse(responseCode = "503", description = "Serviço indisponível no momento"), })
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

package com.clinica_medica_Desafio.Controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import com.clinica_medica_Desafio.model.Regional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/regioes")
@Tag(name = "Região")
public class RegionalController {

	@Autowired
	private RegionalService regionalService;

	@Operation(summary = "Busca dados de Regiões", method = "GET")

	@ApiResponses(value = { 
			@ApiResponse(responseCode = "404", description = "Região não encontrada"),
			@ApiResponse(responseCode = "500", description = "Erro ao buscar a região") })
	@GetMapping(value = "/{id}")
	public ResponseEntity<Regional> getRegiaoById(@PathVariable Long id) {
		Regional regiao = regionalService.findRegional(id);
		return ResponseEntity.ok(regiao);
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Busca paginada de Regiões realizada com sucesso"),
			@ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar a busca das Regiões"),
			@ApiResponse(responseCode = "503", description = "Serviço indisponível no momento") })
	@GetMapping(value = "/page")
	public ResponseEntity<Page<RegionalDTO>> findPageRegiao(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage) {
		Page<Regional> RegiaoPage = regionalService.findAllByOrderByLabel(PageRequest.of(page, linesPerPage));
		Page<RegionalDTO> RegiaoDTOPage = RegiaoPage.map(RegionalDTO::new);
		return ResponseEntity.ok().body(RegiaoDTOPage);
	}

	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Região criada com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de requisição inválida"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar a inserção"),
			@ApiResponse(responseCode = "503", description = "Serviço indisponível no momento") })
	@PostMapping()
	public ResponseEntity<String> insert(@RequestBody Regional objDto) {
		Regional obj = regionalService.fromDTO(objDto);
		regionalService.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Atualização de Regiões realizada com sucesso"),
			@ApiResponse(responseCode = "201", description = "Região criada com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de requisição inválida"),
			@ApiResponse(responseCode = "404", description = "Regiões não encontradas"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar a inserção"),
			@ApiResponse(responseCode = "403", description = "Permissão negada para atualizar as Regiões"),
			@ApiResponse(responseCode = "503", description = "Serviço indisponível no momento") })
	@PutMapping(value = "/{id}")
	public ResponseEntity<Regional> updateRegioes(@Valid @RequestBody Regional objDto, @PathVariable Long id) {
		Regional obj = regionalService.fromDTO(objDto);
		obj.setId(id);
		obj = regionalService.update(obj);
		return ResponseEntity.noContent().build();
	}

	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Região excluída com sucesso"),
			@ApiResponse(responseCode = "404", description = "Região não encontrada"),
			@ApiResponse(responseCode = "500", description = "Erro ao excluir a região"),
			@ApiResponse(responseCode = "403", description = "Permissão negada para Deletar as Regiões"),
			@ApiResponse(responseCode = "503", description = "Serviço indisponível no momento") })
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		regionalService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de labels ordenada por nome obtida com sucesso"),
			@ApiResponse(responseCode = "400", description = "Parâmetros de requisição inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro interno no servidor"),
			@ApiResponse(responseCode = "403", description = "Permissão negada para Ordenar as Regiões"),
			@ApiResponse(responseCode = "503", description = "Serviço indisponível no momento") })
	@GetMapping(value = "/ordenar")
	public ResponseEntity<Page<String>> findOrdenadoPorLabel(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage) {
		Page<Regional> regiaoOrdenadas = regionalService.findAllByOrderByLabel(PageRequest.of(page, linesPerPage));
		Page<String> labol = regiaoOrdenadas.map(Regional::getLabel);

		return ResponseEntity.ok().body(labol);
	}
}

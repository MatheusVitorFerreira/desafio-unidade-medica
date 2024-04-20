package com.clinicamedicadesafio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinicamedicadesafio.dto.ClinicaDTO;
import com.clinicamedicadesafio.model.Clinica;
import com.clinicamedicadesafio.service.ClinicaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value = "/clinica")
public class ClinicaController {

	@Autowired
	private ClinicaService clinicaService;
	
	@Operation(summary = "Busca uma Clinica médica no Banco de Dados", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Clinica encontrada com sucesso"),
			@ApiResponse(responseCode = "404", description = "Clinica não encontrada"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar a busca do arquivo"), })
	@GetMapping(value = "/{id}")
	public ResponseEntity<ClinicaDTO> findId(@PathVariable Long id) {
		ClinicaDTO dto = clinicaService.findClinica(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@Operation(summary = "Busca paginada de clinicas médicas no Banco de Dados", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Busca de clinicas médicas realizada com sucesso"),
			@ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
			@ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro ao realizar a busca das clinicas médicas"),
			@ApiResponse(responseCode = "404", description = "Especialidades médicas não encontradas"),
			@ApiResponse(responseCode = "403", description = "Permissão negada para acessar as Regiões"),
			@ApiResponse(responseCode = "503", description = "Serviço indisponível no momento"), })
	@PostMapping()
	public ResponseEntity<ClinicaDTO> insertClinica(@RequestBody ClinicaDTO clinicaDTO) {
		ClinicaDTO insertedClinica = clinicaService.insert(clinicaDTO);
		return new ResponseEntity<>(insertedClinica, HttpStatus.CREATED);
	}
	
	@Operation(summary = "Atualização de clínicas médicas no Banco de Dados", method = "PUT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clínica médica atualizada com sucesso"),
        @ApiResponse(responseCode = "201", description = "Clínica médica criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de requisição inválida. Exemplos: \n* { \"EmptyField\": \"Campo vazio\" }\n* { \"DuplicateExecption\": \"Clínicas médicas com essa descrição já cadastrada\" }"),
        @ApiResponse(responseCode = "500", description = "Erro ao realizar a inserção"),
        @ApiResponse(responseCode = "404", description = "Clínica médica não encontrada"),
        @ApiResponse(responseCode = "403", description = "Permissão negada para atualizar as clínicas médicas"),
        @ApiResponse(responseCode = "503", description = "Serviço indisponível no momento")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<ClinicaDTO> update(@PathVariable Long id, @RequestBody ClinicaDTO clinicaDTO) {
        List<Long> especialidadesIds = clinicaDTO.getEspecialidadesIds();
        Long regionalId = clinicaDTO.getRegionalId();
        ClinicaDTO updatedDTO = clinicaService.update(id, clinicaDTO, especialidadesIds, regionalId);
        return ResponseEntity.ok(updatedDTO);
    }

	@Operation(summary = "Adição de especialidades médicas a uma clínica", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidades médicas adicionadas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de requisição inválida. Exemplos: \n* { \"EmptyField\": \"Campo vazio\" }\n* { \"DuplicateExecption\": \"Clínicas médicas com essa descrição já cadastrada\" }"),
        @ApiResponse(responseCode = "404", description = "Clínica médica não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro ao realizar a inserção"),
        @ApiResponse(responseCode = "503", description = "Serviço indisponível no momento")
    })
    @PostMapping("/addEspecialidade/{id}")
    public ResponseEntity<ClinicaDTO> adicionarEspecialidade(@PathVariable Long id,
            @RequestBody List<Long> especialidadesIds) {
        Clinica clinica = clinicaService.adicionarEspecialidade(id, especialidadesIds);
        return ResponseEntity.ok(new ClinicaDTO(clinica));
    }

	@Operation(summary = "Exclusão de clínica médica do Banco de Dados", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Clínica médica excluída com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de requisição inválida. Exemplos: \n* { \"EmptyField\": \"Campo vazio\" }\n* { \"DuplicateExecption\": \"Clínicas médicas com essa descrição já cadastrada\" }"),
        @ApiResponse(responseCode = "404", description = "Clínica médica não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro ao realizar a exclusão"),
        @ApiResponse(responseCode = "503", description = "Serviço indisponível no momento")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        clinicaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
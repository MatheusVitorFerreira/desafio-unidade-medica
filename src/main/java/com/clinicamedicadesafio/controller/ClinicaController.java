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

@RestController
@RequestMapping(value = "/clinica")
public class ClinicaController {

	@Autowired
	private ClinicaService clinicaService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<ClinicaDTO> findId(@PathVariable Long id) {
		ClinicaDTO dto = clinicaService.findClinica(id);
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping()
	public ResponseEntity<ClinicaDTO> insertClinica(@RequestBody ClinicaDTO clinicaDTO) {
		ClinicaDTO insertedClinica = clinicaService.insert(clinicaDTO);
		return new ResponseEntity<>(insertedClinica, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ClinicaDTO> update(@PathVariable Long id, @RequestBody ClinicaDTO clinicaDTO) {
	    List<Long> especialidadesIds = clinicaDTO.getEspecialidadesIds();
	    Long regionalId = clinicaDTO.getRegionalId();
	    ClinicaDTO updatedDTO = clinicaService.update(id, clinicaDTO, especialidadesIds, regionalId);
	    return ResponseEntity.ok(updatedDTO);
	}

	@PostMapping("/addEspecialidade/{id}")
	public ResponseEntity<ClinicaDTO> adicionarEspecialidade(@PathVariable Long id,
			@RequestBody List<Long> especialidadesIds) {
		Clinica clinica = clinicaService.adicionarEspecialidade(id, especialidadesIds);
		return ResponseEntity.ok(new ClinicaDTO(clinica));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		clinicaService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
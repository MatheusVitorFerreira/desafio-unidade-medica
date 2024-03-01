package com.clinica_medica_Desafio.Controller;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinica_medica_Desafio.DTO.ClinicaDTO;
import com.clinica_medica_Desafio.Service.ClinicaService;
import com.clinica_medica_Desafio.model.Clinica;
import com.clinica_medica_Desafio.model.Especialidade_Medica;
import com.clinica_medica_Desafio.model.Regional;

@RestController
@RequestMapping(value = "/clinica")
public class ClinicaController {

	@Autowired
	private ClinicaService clinicaService;
	

	@GetMapping(value = "/{id}")
	public ResponseEntity<ClinicaDTO> find(@PathVariable Long id) {
		Clinica clinica = clinicaService.findClinica(id);
		Set<Especialidade_Medica> especialidades = clinica.getEspecialidades();
		Regional regional = clinica.getRegional();
		ClinicaDTO clinicaDTO = new ClinicaDTO(clinica.getId(),clinica.getRazao_social(), clinica.getCnpj(),
				clinica.getData_inauguracao(), clinica.getAtiva(), clinica.getNome_fantasia(), regional.getLabel(),
				regional, new ArrayList<>(especialidades));
		return ResponseEntity.ok(clinicaDTO);
	}
}

package com.clinica_medica_Desafio.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.clinica_medica_Desafio.DTO.ClinicaDTO;
import com.clinica_medica_Desafio.Repository.ClinicaRepository;
import com.clinica_medica_Desafio.Repository.EspecialidadeMedicaRepository;
import com.clinica_medica_Desafio.Repository.RegionalRepository;
import com.clinica_medica_Desafio.model.Clinica;
import com.clinica_medica_Desafio.model.Especialidade_Medica;

@Service
public class ClinicaService {

	@Autowired
	private ClinicaRepository clinicarepository;
	@Autowired
	private RegionalRepository regionalRepository;

	@Autowired
	private EspecialidadeMedicaRepository especialidadeMedicaRepository;

	public Clinica findClinica(Long id) {
		return clinicarepository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException(id, "Clinica Medica não Encontrada"));
	}

	public Page<Clinica> findPageClinica(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage);
		return clinicarepository.findAll(pageRequest);
	}

	public List<Clinica> findAll() {
		return clinicarepository.findAll();
	}

	public Clinica fromDTO(ClinicaDTO objDto) {
		Clinica clinica = new Clinica(objDto.getId(),objDto.getRazao_social(), objDto.getCnpj(),
				objDto.getNome_fantasia(), objDto.getData_inauguracao(), objDto.getAtiva());
		clinica.setRegional(objDto.getRegional());
		List<Especialidade_Medica> especialidades = new ArrayList<>();
		for (Especialidade_Medica especialidadeDto : objDto.getEspecialidades_medicas()) {
			Especialidade_Medica especialidadeMedica = new Especialidade_Medica(especialidadeDto.getId(),
					especialidadeDto.getDescricao());
			especialidades.add(especialidadeMedica);
		}
		Set<Especialidade_Medica> especialidadesSet = new HashSet<>(especialidades);
		clinica.setEspecialidades(especialidadesSet);
		clinica.setNome_fantasia(objDto.getNome_fantasia());
		return clinica;
	}
}
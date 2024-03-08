package com.clinica_medica_Desafio.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.clinica_medica_Desafio.DTO.ClinicaDTO;
import com.clinica_medica_Desafio.Repository.ClinicaRepository;
import com.clinica_medica_Desafio.Repository.EspecialidadeMedicaRepository;
import com.clinica_medica_Desafio.Repository.RegionalRepository;
import com.clinica_medica_Desafio.Service.Exceptions.ClinicaNotFoundException;
import com.clinica_medica_Desafio.Service.Exceptions.DuplicateExecption;
import com.clinica_medica_Desafio.Service.Exceptions.ErroInsercaoException;
import com.clinica_medica_Desafio.Service.Exceptions.EspecialidadeNotFoundException;
import com.clinica_medica_Desafio.Service.Exceptions.RegiaoNotFoundException;
import com.clinica_medica_Desafio.model.Clinica;
import com.clinica_medica_Desafio.model.Especialidade_Medica;
import com.clinica_medica_Desafio.model.Regional;

import jakarta.transaction.Transactional;

@Service
public class ClinicaService {

	@Autowired
	private ClinicaRepository clinicarepository;

	@Autowired
	private RegionalRepository regionalRepository;

	@Autowired
	private EspecialidadeMedicaRepository especialidadeMedicaRepository;

	@Transactional
	public ClinicaDTO findClinica(Long id) {
		Clinica clinica = clinicarepository.findById(id)
				.orElseThrow(() -> new ClinicaNotFoundException("Clínica não Encontrada"));
		return new ClinicaDTO(clinica);
	}

	public Page<Clinica> findPageClinica(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
		return clinicarepository.findAll(pageRequest);

	}

	public List<Clinica> findAll() {
		return clinicarepository.findAll();
	}

	public Clinica fromDTO(ClinicaDTO objDto) {
		Clinica clinica = new Clinica(objDto.getId(), objDto.getRazao_social(), objDto.getCnpj(),
				objDto.getNomefantasia(), objDto.getData_inauguracao(), objDto.getAtiva());
		if (clinicarepository.existsByNomefantasia(objDto.getNomefantasia())) {
			throw new DuplicateExecption("Nome fantasia já cadastrado");
		}
		Long regionalId = objDto.getRegionalId();
		Regional regional = regionalRepository.findById(regionalId)
				.orElseThrow(() -> new RegiaoNotFoundException("Região não encontrada"));
		clinica.setRegional(regional);
		List<Long> especialidadesIds = objDto.getEspecialidadesIds();
		Set<Especialidade_Medica> especialidades = new HashSet<>();
		for (Long especialidadeId : especialidadesIds) {
			Especialidade_Medica especialidade = especialidadeMedicaRepository.findById(especialidadeId)
					.orElseThrow(() -> new EspecialidadeNotFoundException("Especialidade não encontrada"));
			especialidades.add(especialidade);
		}
		clinica.setEspecialidades(especialidades);
		return clinica;
	}

	@Transactional
	public ClinicaDTO insert(ClinicaDTO clinicaDTO) {

		LocalDateTime now = LocalDateTime.now();
		clinicaDTO.setData_inauguracao(now);

		List<Long> especialidadesIds = clinicaDTO.getEspecialidadesIds();

		if (especialidadesIds.size() < 4) {
			throw new ErroInsercaoException("Mínimo de 5 especialidades é obrigatório.");
		}
		Clinica clinica = fromDTO(clinicaDTO);
		clinica = clinicarepository.save(clinica);

		return new ClinicaDTO(clinica);
	}

	@Transactional
	public ClinicaDTO update(Long id, ClinicaDTO clinicaDTO) {
		Clinica clinica = clinicarepository.findById(id)
				.orElseThrow(() -> new ClinicaNotFoundException("Clínica não Encontrada"));
		clinica.setRazao_social(clinicaDTO.getRazao_social());
		clinica.setCnpj(clinicaDTO.getCnpj());
		clinica.setNomefantasia(clinicaDTO.getNomefantasia());
		Long regionalId = clinicaDTO.getRegionalId();
		Regional regional = regionalRepository.findById(regionalId)
				.orElseThrow(() -> new RegiaoNotFoundException("Região não encontrada"));
		clinica.setRegional(regional);
		Set<Especialidade_Medica> especialidades = new HashSet<>();
		for (Long especialidadeId : clinicaDTO.getEspecialidadesIds()) {
			Especialidade_Medica especialidade = especialidadeMedicaRepository.findById(especialidadeId)
					.orElseThrow(() -> new EspecialidadeNotFoundException("Especialidade não encontrada"));
			especialidades.add(especialidade);
		}
		clinica.setEspecialidades(especialidades);
		List<Long> especialidadesIds = clinicaDTO.getEspecialidadesIds();
		if (especialidadesIds.size() < 5) {
			throw new ErroInsercaoException("Mínimo de 5 especialidades é obrigatório.");
		}
		clinica = clinicarepository.save(clinica);
		return new ClinicaDTO(clinica);

	}

	@Transactional
	public Clinica adicionarEspecialidade(Long idClinica, ClinicaDTO clinicaDTO) {
		try {
			Clinica clinica = clinicarepository.findById(idClinica)
					.orElseThrow(() -> new ClinicaNotFoundException("Clínica não Encontrada"));

			Set<Especialidade_Medica> especialidades = new HashSet<>();
			for (Long especialidadeId : clinicaDTO.getEspecialidadesIds()) {
				Especialidade_Medica especialidade = especialidadeMedicaRepository.findById(especialidadeId)
						.orElseThrow(() -> new EspecialidadeNotFoundException("Especialidade não encontrada"));
				especialidades.add(especialidade);
			}
			clinica.getEspecialidades().addAll(especialidades);

			clinica = clinicarepository.save(clinica);

			return clinica;
		} catch (ObjectNotFoundException e) {
			throw e;
		}
	}

	public void delete(Long id) {
		try {
			ClinicaDTO clinica = findClinica(id);
			if (clinica == null) {
				throw new ClinicaNotFoundException("Clínica não Encontrada");
			}
			clinicarepository.deleteById(id);
		} catch (ObjectNotFoundException e) {
			throw e;
		}
	}
}
package com.clinicamedicadesafio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.clinicamedicadesafio.dto.EspecialidadeMedicaDTO;
import com.clinicamedicadesafio.model.Especialidade_Medica;
import com.clinicamedicadesafio.repository.EspecialidadeMedicaRepository;
import com.clinicamedicadesafio.service.excepetion.DuplicateExecption;
import com.clinicamedicadesafio.service.excepetion.EmptyField;
import com.clinicamedicadesafio.service.excepetion.EspecialidadeNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class EspecialidadeMedicaService {

	@Autowired
	private EspecialidadeMedicaRepository especialidadeRepository;

	public Especialidade_Medica findById(Long id) {
		Optional<Especialidade_Medica> obj = especialidadeRepository.findById(id);
		return obj.orElseThrow(() -> new EspecialidadeNotFoundException("Especialidade Medica não Encontrada"));
	}

	public List<Especialidade_Medica> findAll() {
		return especialidadeRepository.findAll();
	}

	public Page<Especialidade_Medica> findPageEspecialidade(Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
		return especialidadeRepository.findAll(pageRequest);
	}

	@Transactional
	public Especialidade_Medica insertEspecialidade(Especialidade_Medica obj) {
		if (especialidadeRepository.existsByDescricao(obj.getDescricao())) {
			throw new DuplicateExecption("Especialidade com essa descrição já cadastrada");
		}
		if (obj.getDescricao().isEmpty()) {
			throw new EmptyField("A descrição não pode estar vazia.");
		}
		return especialidadeRepository.save(obj);
	}

	@Transactional
	public Especialidade_Medica update(EspecialidadeMedicaDTO objDto, Long id) {
		Especialidade_Medica obj = findById(id);
		if (objDto.getDescricao().trim().isEmpty()) {
			throw new EmptyField("A descrição não pode estar vazia.");
		}
		if (existsByDescricao(objDto.getDescricao())) {
			throw new DuplicateExecption("Já existe uma especialidade com essa descrição.");
		}
		obj.setDescricao(objDto.getDescricao());
		return especialidadeRepository.save(obj);
	}

	public void updateDescricao(EspecialidadeMedicaDTO objDto) {
		Especialidade_Medica obj = especialidadeRepository.findById(objDto.getId())
				.orElseThrow(() -> new EspecialidadeNotFoundException("Especialidade médica não encontrada"));
		obj.setDescricao(objDto.getDescricao());
		especialidadeRepository.save(obj);
	}

	public void delete(Long id) {
		Especialidade_Medica especialidade = findById(id);
		if (especialidade == null) {
			throw new EspecialidadeNotFoundException("Especialidade com ID " + id + " não encontrada.");
		}
		especialidadeRepository.deleteById(id);
	}

	public Especialidade_Medica findEspecialidadeByDescricao(String descricao) {
		Optional<Especialidade_Medica> especialidadeOptional = especialidadeRepository.findEspecialidade(descricao);
		return especialidadeOptional.orElseThrow(() -> new EspecialidadeNotFoundException(
				"Especialidade não encontrada para a descrição: " + descricao));
	}

	public Page<Especialidade_Medica> findAllByOrderByDescricaoAsc(Pageable pageable) {
		return especialidadeRepository.findAllByOrderByDescricaoAsc(pageable);
	}

	public boolean existsByDescricao(String descricao) {
		return especialidadeRepository.existsByDescricao(descricao);
	}

	public EspecialidadeMedicaService(EspecialidadeMedicaRepository especialidadeMedicaRepository) {
		this.especialidadeRepository = especialidadeMedicaRepository;
	}
}

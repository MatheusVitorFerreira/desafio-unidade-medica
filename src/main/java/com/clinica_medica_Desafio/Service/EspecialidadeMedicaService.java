package com.clinica_medica_Desafio.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.clinica_medica_Desafio.DTO.EspecialidadeMedicaDTO;
import com.clinica_medica_Desafio.Repository.EspecialidadeMedicaRepository;
import com.clinica_medica_Desafio.Service.Exceptions.DataAcessException;
import com.clinica_medica_Desafio.Service.Exceptions.DuplicateExecption;
import com.clinica_medica_Desafio.Service.Exceptions.EmptyField;
import com.clinica_medica_Desafio.Service.Exceptions.EspecialidadeNotFoundException;
import com.clinica_medica_Desafio.model.Especialidade_Medica;

import jakarta.transaction.Transactional;

@Service
public class EspecialidadeMedicaService {

	@Autowired
	private EspecialidadeMedicaRepository especialidadeRepository;

	public Especialidade_Medica findEspecialidade(Long id) {
		return especialidadeRepository.findById(id)
				.orElseThrow(() -> new EspecialidadeNotFoundException("Especialidade Medica não Encontrada"));
	}

	public Page<Especialidade_Medica> findPageEspecialidade(Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		try {
			PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
			return especialidadeRepository.findAll(pageRequest);
		} catch (DataAcessException e) {
			throw new DataAcessException("Um Erro Aconteceu!! Acesso Negado", e);
		}
	}

	public List<Especialidade_Medica> findAll() {
		return especialidadeRepository.findAll();
	}

	public Especialidade_Medica fromDTO(EspecialidadeMedicaDTO objDto) {
		try {
			if (especialidadeRepository.existsByDescricao(objDto.getDescricao())) {
				throw new DuplicateExecption("Especialidade com essa descrição já cadastrada.");
			}
			Especialidade_Medica especialidadeMedica = new Especialidade_Medica(objDto.getId(), objDto.getDescricao());
			return especialidadeMedica;
		} catch (DataAcessException e) {
			throw new DataAcessException("Um Erro Aconteceu!! Acesso Negado", e);
		}
	}

	@Transactional
	public Especialidade_Medica insertEspecialidade(Especialidade_Medica obj) {
		try {
			if (obj.getDescricao().isEmpty()) {
				throw new EmptyField("Campo vazio");
			}
			return especialidadeRepository.save(obj);
		} catch (DataAcessException e) {
			throw new DataAcessException("Um Erro Aconteceu!! Acesso Negado", e);
		}
	}

	@Transactional
	public Especialidade_Medica update(Especialidade_Medica obj) {
		if (obj.getDescricao().trim().isEmpty()) {
			throw new EmptyField("A descrição não pode estar vazia.");
		}
		if (existsByDescricao(obj.getDescricao())) {
			throw new DuplicateExecption("Já existe uma especialidade com essa descrição.");
		}
		Especialidade_Medica newObj = findEspecialidade(obj.getId());
		updateDescricao(newObj, obj);
		return especialidadeRepository.save(newObj);
	}

	public boolean existsByDescricao(String descricao) {
		return especialidadeRepository.existsByDescricao(descricao);
	}

	public void updateDescricao(Especialidade_Medica newObj, Especialidade_Medica objDto) {
		newObj.setDescricao(objDto.getDescricao());
	}

	public void delete(Long id) {
		Especialidade_Medica especialidade = findEspecialidade(id);
		if (especialidade == null) {
			throw new EspecialidadeNotFoundException("Especialidade com ID " + id + " não encontrada.");
		}
		especialidadeRepository.deleteById(id);
	}

	public Page<Especialidade_Medica> findAllByOrderByDescricaoAsc(Pageable pageable) {
		return especialidadeRepository.findAllByOrderByDescricaoAsc(pageable);
	}

}

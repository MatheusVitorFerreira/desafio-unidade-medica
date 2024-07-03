package com.clinicamedicadesafio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.clinicamedicadesafio.dto.RegionalDTO;
import com.clinicamedicadesafio.model.Regional;
import com.clinicamedicadesafio.repository.RegionalRepository;
import com.clinicamedicadesafio.service.excepetion.DuplicateExecption;
import com.clinicamedicadesafio.service.excepetion.EmptyField;
import com.clinicamedicadesafio.service.excepetion.RegiaoNotFoundException;

import jakarta.validation.Valid;

@Service
public class RegionalService {

	@Autowired
	private RegionalRepository regionalRepository;

	public Regional findRegional(Long id) {
		return regionalRepository.findById(id).orElseThrow(() -> new RegiaoNotFoundException("Região não Encontrada"));
	}

	public Page<Regional> findPageRegional(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
		return regionalRepository.findAll(pageRequest);
	}

	public List<Regional> findAll() {
		return regionalRepository.findAll();
	}

	public Regional fromDTO(@Valid RegionalDTO objDto) {
		if (objDto.getLabel() == null || objDto.getRegiao() == null) {
			throw new EmptyField("Label é obrigatório.");
		}
		return new Regional(objDto.getId(), objDto.getLabel(), objDto.getRegiao());
	}

	public Regional insert(Regional obj) {
		if (obj.getLabel() == null || obj.getLabel().trim().isEmpty()) {
			throw new EmptyField("O Label não pode estar vazio.");
		}
		if (regionalRepository.existsByLabel(obj.getLabel())) {
			throw new DuplicateExecption("Já existe um label com esse nome");
		} else {
			return regionalRepository.save(obj);
		}
	}

	public Regional update(RegionalDTO objDto, Long id) {
		if (objDto.getLabel() == null || objDto.getLabel().trim().isEmpty()) {
			throw new EmptyField("O Label não pode estar vazio.");
		}
		Regional existingRegional = regionalRepository.findById(id)
				.orElseThrow(() -> new RegiaoNotFoundException("Regional não encontrada com o ID: " + id));
		if (regionalRepository.existsByLabelAndIdNot(objDto.getLabel(), id)) {
			throw new DuplicateExecption("Já existe uma Região com esse nome.");
		}
		existingRegional.setLabel(objDto.getLabel());
		existingRegional.setRegiao(objDto.getRegiao());
		return regionalRepository.save(existingRegional);
	}

	public void updateLabelERegiao(Regional newObj, Regional objDto) {
		newObj.setLabel(objDto.getLabel());
		newObj.setRegiao(objDto.getRegiao());
	}

	public void delete(Long id) {
		Regional regiao = findRegional(id);
		if (regiao == null) {
			throw new RegiaoNotFoundException("Regional com ID " + id + " não encontrada.");
		}
		regionalRepository.deleteById(id);
	}

	public Page<Regional> findAllByOrderByLabel(Pageable pageable) {
		return regionalRepository.findAllByOrderByLabel(pageable);
	}
}

package com.clinica_medica_Desafio.Service;

import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.clinica_medica_Desafio.Repository.RegionalRepository;
import com.clinica_medica_Desafio.Service.Exceptions.DuplicateExecption;
import com.clinica_medica_Desafio.Service.Exceptions.EmptyField;
import com.clinica_medica_Desafio.Service.Exceptions.EspecialidadeNotFoundException;
import com.clinica_medica_Desafio.model.Regional;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class RegionalService {

    @Autowired
    private RegionalRepository regionalRepository;
   
    public Regional findRegional(Long id) {
		return regionalRepository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException(id, "Região não Encontrada"));
	}

	public Page<Regional> findPageRegional(Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage);
		return regionalRepository.findAll(pageRequest);
	}

	public List<Regional> findAll() {
		return regionalRepository.findAll();
	}
    public Regional fromDTO(@Valid Regional objDto) {
        if (objDto.getLabel() == null || objDto.getRegiao() == null) {
            throw new EmptyField("Label é obrigatórios.");
        }
        return new Regional(objDto.getId(), objDto.getLabel(), objDto.getRegiao());
    }

    public Regional insert(Regional obj) {
        if (regionalRepository.existsByLabel(obj.getLabel())) {
            throw new DuplicateExecption("Já existe um label com esse nome");
        } else {
            return regionalRepository.save(obj);
        }
    }
    @Transactional
    public Regional update(Regional obj) {
        if (obj.getLabel().trim().isEmpty()) {
            throw new EmptyField("O Label não pode estar vazia.");
        }
        if (existsByLabelAndIdNot(obj.getLabel(), obj.getId())) {
            throw new DuplicateExecption("Já existe um Label com esse nome.");
        }
        Regional existingRegional = findRegional(obj.getId());
        existingRegional.setLabel(obj.getLabel()); 
        existingRegional.setRegiao(obj.getRegiao()); 
        return regionalRepository.save(existingRegional); 
    }

    private boolean existsByLabelAndIdNot(String label, Long id) {
        return regionalRepository.existsByLabelAndIdNot(label, id);
    }

	public void updateLabelERegiao(Regional newObj, Regional objDto) {
		newObj.setLabel(objDto.getLabel());
		newObj.setLabel(objDto.getRegiao());
	}

	public void delete(Long id) {
		Regional regiao = findRegional(id);
		if (regiao == null) {
			throw new EspecialidadeNotFoundException("regiao com ID " + id + " não encontrada.");
		}
		regionalRepository.deleteById(id); 
	}

	public Page<Regional> findAllByOrderByLabel(Pageable pageable) {
		return regionalRepository.findAllByOrderByLabel(pageable);
	}
}

package com.clinica_medica_Desafio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.clinica_medica_Desafio.DTO.EspecialidadeMedicaDTO;
import com.clinica_medica_Desafio.Repository.EspecialidadeMedicaRepository;
import com.clinica_medica_Desafio.model.Especialidade_Medica;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
class EspecialidadeMedicaRepositoryTest {
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	EspecialidadeMedicaRepository especialidadeMedicaRepository;

	  @Test
	  @DisplayName("Deve retornar true quando a especialidade com a descrição especificada existir")
	  void existsByDescricaoCase1() {
	        EspecialidadeMedicaDTO objDto = new EspecialidadeMedicaDTO(1L,"Cardiologista");
	        this.createEspecialidade(objDto);
	        boolean result = this.especialidadeMedicaRepository.existsByDescricao(objDto.getDescricao());
	        assertThat(result).isTrue();
	    }
	  @Test
	  @DisplayName("Não Deve retornar true quando a especialidade com a descrição especificada existir")
	  void existsByDescricaoCase2() {
	        EspecialidadeMedicaDTO objDto = new EspecialidadeMedicaDTO(1L,"Cardiologista");
	        boolean result = this.especialidadeMedicaRepository.existsByDescricao(objDto.getDescricao());
	        assertFalse(result);
	    }
	  private Especialidade_Medica createEspecialidade(EspecialidadeMedicaDTO objDto) {
		    Especialidade_Medica newEspecialidade = new Especialidade_Medica(objDto.getDescricao());
		    this.entityManager.persist(newEspecialidade);
		    return newEspecialidade;
		}
	}

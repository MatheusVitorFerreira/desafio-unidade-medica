package com.clinica_medica_Desafio.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.clinica_medica_Desafio.model.Especialidade_Medica;

@Repository
public interface EspecialidadeMedicaRepository extends JpaRepository<Especialidade_Medica, Long>{
	List<Especialidade_Medica> findByDescricaoIgnoreCaseContaining(String descricao);
	
	@Query("SELECT obj FROM Especialidade_Medica obj ORDER BY obj.descricao ASC")
	Page<Especialidade_Medica> findAllByOrderByDescricaoAsc(Pageable pageable);
	
	boolean existsByDescricao(String descricao);

}

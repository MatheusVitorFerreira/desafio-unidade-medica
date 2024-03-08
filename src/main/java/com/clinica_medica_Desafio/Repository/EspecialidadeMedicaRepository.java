package com.clinica_medica_Desafio.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clinica_medica_Desafio.model.Especialidade_Medica;

@Repository
public interface EspecialidadeMedicaRepository extends JpaRepository<Especialidade_Medica, Long> {
	List<Especialidade_Medica> findByDescricaoIgnoreCaseContaining(String descricao);

	@Query("SELECT obj FROM Especialidade_Medica obj ORDER BY obj.descricao ASC")
	Page<Especialidade_Medica> findAllByOrderByDescricaoAsc(Pageable pageable);

	boolean existsByDescricao(String descricao);

	@Override
	Page<Especialidade_Medica> findAll(Pageable pageable);

	Especialidade_Medica findByDescricao(String descricao);


	@Query("SELECT obj FROM Especialidade_Medica obj WHERE LOWER(obj.descricao) = LOWER(:descricao)")
	Optional<Especialidade_Medica> findEspecialidade(@Param("descricao") String descricao);
}

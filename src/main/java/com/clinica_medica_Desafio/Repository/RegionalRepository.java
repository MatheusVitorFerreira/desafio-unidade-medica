package com.clinica_medica_Desafio.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.clinica_medica_Desafio.model.Regional;

public interface RegionalRepository extends JpaRepository<Regional, Long> {
	boolean existsByLabel(String label);

	boolean existsByLabelAndIdNot(String label, Long id);

	@Query("SELECT obj FROM Regional obj ORDER BY obj.label ASC")
	Page<Regional> findAllByOrderByLabel(Pageable pageable);

}

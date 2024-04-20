package com.clinicamedicadesafio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clinicamedicadesafio.model.Clinica;

@Repository
public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
	
	boolean existsByNomefantasia(String nomefantasia);

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Clinica c WHERE c.nomefantasia = :nomefantasia AND c.id <> :id")
	boolean existsByNomefantasiaAndIdNot(@Param("nomefantasia") String nomefantasia, @Param("id") Long id);
	
	 @EntityGraph(attributePaths = "especialidades")
	 Optional<Clinica> findById(Long id);
}

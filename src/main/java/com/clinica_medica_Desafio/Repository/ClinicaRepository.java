package com.clinica_medica_Desafio.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinica_medica_Desafio.model.Clinica;

@Repository
public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
	boolean existsByNomeFantasia(String nomeFantasia);

}

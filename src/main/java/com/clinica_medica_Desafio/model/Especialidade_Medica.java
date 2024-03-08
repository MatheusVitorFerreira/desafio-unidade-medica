package com.clinica_medica_Desafio.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Especialidade_Medica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String descricao;

	@ManyToMany(mappedBy = "especialidades")
	private Set<Clinica> clinicas = new HashSet<>();

	public Especialidade_Medica() {

	}

	public Especialidade_Medica(String descricao) {
		this.descricao = descricao;
	}

	public Especialidade_Medica(Long id_especialidade, String descricao) {
		this.id = id_especialidade;
		this.descricao = descricao;
	}

	public Long getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setId(Long id) {
		this.id = id;
	}
}

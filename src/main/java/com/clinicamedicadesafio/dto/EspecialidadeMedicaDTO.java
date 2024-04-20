package com.clinicamedicadesafio.dto;

import java.io.Serializable;

import com.clinicamedicadesafio.model.Especialidade_Medica;

public class EspecialidadeMedicaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String descricao;

	public EspecialidadeMedicaDTO() {
	}

	public EspecialidadeMedicaDTO(Long id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public EspecialidadeMedicaDTO(Especialidade_Medica especialidade) {
		this.id = especialidade.getId();
		this.descricao = especialidade.getDescricao();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public static EspecialidadeMedicaDTO fromModel(Especialidade_Medica model) {
		if (model != null) {
			return new EspecialidadeMedicaDTO(model);
		}
		return null;
	}
}

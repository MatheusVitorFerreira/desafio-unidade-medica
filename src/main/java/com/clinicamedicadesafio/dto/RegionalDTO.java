package com.clinicamedicadesafio.dto;

import java.io.Serializable;

import com.clinicamedicadesafio.model.Regional;

public class RegionalDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String label;
	private String regiao;

	public RegionalDTO(Regional obj) {
		super();
		this.id = obj.getId();
		this.label = obj.getLabel();
		this.regiao = obj.getRegiao();
	}

	public RegionalDTO(Long id, String label, String regiao) {
		this.id = id;
		this.label = label;
		this.regiao = regiao;
	}

	public RegionalDTO() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}
}

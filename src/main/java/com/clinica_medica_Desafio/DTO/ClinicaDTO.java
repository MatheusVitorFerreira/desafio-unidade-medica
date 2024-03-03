package com.clinica_medica_Desafio.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.clinica_medica_Desafio.model.Clinica;
import com.clinica_medica_Desafio.model.Especialidade_Medica;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClinicaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	@NotBlank(message = "Campo Obrigatório")
	private String razao_social;

	@Max(14)
	private String cnpj;

	private Long regionalId;

	private String regiao;

	private LocalDateTime data_inauguracao;

	private Boolean ativa;

	private String nome_fantasia;

	private String regionalLabel;
	private List<Especialidade_Medica> especialidades_medicas = new ArrayList<>();

	public ClinicaDTO(Clinica clinica) {
		this.id = clinica.getId();
		this.nome_fantasia = clinica.getNome_fantasia();
		this.cnpj = clinica.getCnpj();
		this.data_inauguracao = clinica.getData_inauguracao();
		this.ativa = clinica.getAtiva();
		this.razao_social = clinica.getRazao_social();
		this.regiao = clinica.getRegional().getRegiao();
		this.regionalLabel = clinica.getRegional().getLabel();
		this.regionalId = clinica.getRegional().getId();
		this.especialidades_medicas = new ArrayList<>(clinica.getEspecialidades());
	}

	public ClinicaDTO() {
	}

	public String getRazao_social() {
		return razao_social;
	}

	public void setRazao_social(String razao_social) {
		this.razao_social = razao_social;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public LocalDateTime getData_inauguracao() {
		return data_inauguracao;
	}

	public void setData_inauguracao(LocalDateTime data_inauguracao) {
		this.data_inauguracao = data_inauguracao;
	}

	public Boolean getAtiva() {
		return ativa;
	}

	public void setAtiva(Boolean ativa) {
		this.ativa = ativa;
	}

	public String getNome_fantasia() {
		return nome_fantasia;
	}

	public void setNome_fantasia(String nome_fantasia) {
		this.nome_fantasia = nome_fantasia;
	}

	public String getRegionalLabel() {
		return regionalLabel;
	}

	public void setRegionalLabel(String regionalLabel) {
		this.regionalLabel = regionalLabel;
	}

	public List<Especialidade_Medica> getEspecialidades_medicas() {
		return especialidades_medicas;
	}
	@JsonIgnore
	public List<Long> getEspecialidadesIds() {
		List<Long> ids = new ArrayList<>();
		for (Especialidade_Medica especialidade : especialidades_medicas) {
			ids.add(especialidade.getId());
		}
		return ids;
	}
	@JsonIgnore
	public void setEspecialidades(Set<Especialidade_Medica> especialidades) {
		this.especialidades_medicas = new ArrayList<>(especialidades);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRegionalId() {
		return regionalId;
	}

	public void setRegionalId(Long regionalId) {
		this.regionalId = regionalId;
	}

	public void setEspecialidadesIds(List<Long> especialidadesIds) {
	}

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}
}

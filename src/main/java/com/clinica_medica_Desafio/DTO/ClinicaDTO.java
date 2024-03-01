package com.clinica_medica_Desafio.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.clinica_medica_Desafio.Service.Exceptions.InvalidCnpj;
import com.clinica_medica_Desafio.Service.Exceptions.ValidatorCnpj;
import com.clinica_medica_Desafio.model.Especialidade_Medica;
import com.clinica_medica_Desafio.model.Regional;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public class ClinicaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	@NotBlank(message = "Campo Obrigatório")
	private String razao_social;
	@Max(14)
	private String cnpj;
	@NotBlank(message = "Campo Obrigatório")
	private LocalDateTime data_inauguracao;
	private Boolean ativa;
	private String nome_fantasia;
	private String regionalLabel;
	private Long idRegional;
	private Long idEspecialidades;
	@NotBlank(message = "Campo Obrigatório")
	private Regional regional;
	private List<Especialidade_Medica> especialidades_medicas = new ArrayList<>();

	public ClinicaDTO(long id,String razao_social, String cnpj, LocalDateTime data_inauguracao, Boolean ativa,
			String nome_fantasia, String regionalLabel, Regional regional,
			ArrayList<Especialidade_Medica> especialidades) {
		this.razao_social = razao_social;
		this.cnpj = cnpj;
		this.data_inauguracao = data_inauguracao;
		this.ativa = ativa;
		this.nome_fantasia = nome_fantasia;
		this.regionalLabel = regionalLabel;
		this.regional = regional;
		this.especialidades_medicas = especialidades;
		this.id = id;
	}

	public ClinicaDTO(String nomeFantasia) {
		this.nome_fantasia = nomeFantasia;
	}

	public String getRazao_social() {
		return razao_social;
	}

	public Regional getRegional() {
		return regional;
	}

	public void setRegional(Regional regional) {
		this.regional = regional;
	}

	public void setRazao_social(String razao_social) {
		this.razao_social = razao_social;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		if (ValidatorCnpj.isValidCNPJ(cnpj)) {
			this.cnpj = cnpj;
		} else {
			throw new InvalidCnpj(" " + cnpj);
		}
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

	public List<Especialidade_Medica> getEspecialidades_medicas() {
		return especialidades_medicas;
	}

	public void setEspecialidades_medicas(List<Especialidade_Medica> especialidades_medicas) {
		this.especialidades_medicas = especialidades_medicas;
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

	public Long getIdRegional() {
		return idRegional;
	}

	public void setIdRegional(Long idRegional) {
		this.idRegional = idRegional;
	}

	public Long getIdEspecialidades() {
		return idEspecialidades;
	}

	public void setIdEspecialidades(Long idEspecialidades) {
		this.idEspecialidades = idEspecialidades;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}

package com.clinica_medica_Desafio.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.clinica_medica_Desafio.model.Clinica;
import com.clinica_medica_Desafio.model.Regional;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClinicaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String razao_social;
	private String cnpj;
	private LocalDateTime data_inauguracao;
	private Boolean ativa;
	private String nomefantasia;
	private List<Long> especialidadesIds;
	private List<EspecialidadeMedicaDTO> especialidades;
	private Long regionalId;
	private Regional regional;

	public ClinicaDTO(Clinica clinica) {
		this.id = clinica.getId();
		this.razao_social = clinica.getRazao_social();
		this.cnpj = clinica.getCnpj();
		this.data_inauguracao = clinica.getData_inauguracao();
		this.ativa = clinica.getAtiva();
		this.nomefantasia = clinica.getNomefantasia();
	}

	public ClinicaDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getNomefantasia() {
		return nomefantasia;
	}

	public void setNomefantasia(String nomefantasia) {
		this.nomefantasia = nomefantasia;
	}

	public List<Long> getEspecialidadesIds() {
		return especialidadesIds;
	}

	public void setEspecialidadesIds(List<Long> especialidadesIds) {
		this.especialidadesIds = especialidadesIds;
	}

	public Long getRegionalId() {
		return regionalId;
	}

	public void setRegionalId(Long regionalId) {
		this.regionalId = regionalId;
	}

	public List<EspecialidadeMedicaDTO> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(List<EspecialidadeMedicaDTO> especialidades) {
		this.especialidades = especialidades;
	}

	public Regional getRegional() {
		return regional;
	}

	public void setRegional(Regional regional) {
		this.regional = regional;
	}
}
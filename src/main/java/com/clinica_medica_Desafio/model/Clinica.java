package com.clinica_medica_Desafio.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class Clinica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String razao_social;

	private String cnpj;
	private String nomefantasia;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime data_inauguracao;

	private Boolean ativa;
	
	@Transient
    private Set<Long> especialidades_medicas_ids;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "regional_id", nullable = false)
	@JsonIgnore
	private Regional regional;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "clinica_especialidade", joinColumns = @JoinColumn(name = "clinica_id"), inverseJoinColumns = @JoinColumn(name = "especialidade_id"))
	private Set<Especialidade_Medica> especialidades = new HashSet<>();

	public Clinica() {
		
	}

	public Regional getRegional() {
		return regional;
	}

	public void setRegional(Regional regional) {
		this.regional = regional;
	}

	public Set<Especialidade_Medica> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(Set<Especialidade_Medica> especialidades) {
		this.especialidades = especialidades;
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
	 public Set<Long> getEspecialidades_medicas_ids() {
	        return especialidades_medicas_ids;
	    }

	    public void setEspecialidades_medicas_ids(Set<Long> especialidades_medicas_ids) {
	        this.especialidades_medicas_ids = especialidades_medicas_ids;
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
	

}

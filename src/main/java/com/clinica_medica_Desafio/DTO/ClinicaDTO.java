package com.clinica_medica_Desafio.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import com.clinica_medica_Desafio.Repository.EspecialidadeMedicaRepository;
import com.clinica_medica_Desafio.model.Clinica;
import com.clinica_medica_Desafio.model.Especialidade_Medica;
import com.clinica_medica_Desafio.model.Regional;
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

    private LocalDateTime data_inauguracao;

    private Boolean ativa;

    private String nome_fantasia;

    private String regionalLabel;

    private List<Especialidade_Medica> especialidades_medicas = new ArrayList<>();

    @Autowired
    EspecialidadeMedicaRepository especialidadeMedicaRepository;

    public ClinicaDTO(Clinica clinica, Regional regional, List<Long> especialidadesIds) {
        this.id = clinica.getId();
        this.nome_fantasia = clinica.getNome_fantasia();
        this.cnpj = clinica.getCnpj();
        this.data_inauguracao = clinica.getData_inauguracao();
        this.ativa = clinica.getAtiva();
        this.razao_social = clinica.getRazao_social();
        this.regionalLabel = regional.getLabel();
        this.regionalId = regional.getId();
        this.especialidades_medicas = new ArrayList<>();
        for (Long especialidadeId : especialidadesIds) {
            Especialidade_Medica especialidadeMedica = especialidadeMedicaRepository.findById(especialidadeId)
                .orElseThrow(() -> new ObjectNotFoundException(especialidadeId, "Especialidade Médica não encontrada"));
            this.especialidades_medicas.add(especialidadeMedica);
        }
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

	public void setEspecialidades_medicas(List<Especialidade_Medica> especialidades_medicas) {
		this.especialidades_medicas = especialidades_medicas;
	}

	public List<Long> getEspecialidadesIds() {
        List<Long> ids = new ArrayList<>();
        for (Especialidade_Medica especialidade : especialidades_medicas) {
            ids.add(especialidade.getId());
        }
        return ids;
    }

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
}

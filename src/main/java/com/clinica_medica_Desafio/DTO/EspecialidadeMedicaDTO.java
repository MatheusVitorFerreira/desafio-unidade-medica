package com.clinica_medica_Desafio.DTO;


import java.io.Serializable;

import com.clinica_medica_Desafio.model.Especialidade_Medica;

public class EspecialidadeMedicaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String descricao;

    public EspecialidadeMedicaDTO(Especialidade_Medica objDTO) {
        this.id = objDTO.getId();
        this.descricao = objDTO.getDescricao();
    }

    public EspecialidadeMedicaDTO() {

    }
    public EspecialidadeMedicaDTO(long id, String descricao) {
    	this.id =id;
		this.descricao = descricao;
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

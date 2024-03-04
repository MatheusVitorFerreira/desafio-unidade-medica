package com.clinica_medica_Desafio.Exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Modelo de erro para Região não encontrada")
public class ErrorSchema {

    @ApiModelProperty(value = "Código de erro", required = true)
    private int code;

    @ApiModelProperty(value = "Mensagem de erro", required = true)
    private String message;

}

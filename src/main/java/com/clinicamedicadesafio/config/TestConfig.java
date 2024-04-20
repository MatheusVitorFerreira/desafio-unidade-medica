package com.clinicamedicadesafio.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.clinicamedicadesafio.service.DbService;

@Configuration
@Profile("test")
public class TestConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(TestConfig.class);

    @Autowired
    private DbService dbService;

    @Bean
    public boolean instantiateDataBase() {
        logger.info("Inicializando banco de dados...");
        if (dbService != null) {
            logger.info("DbService injetado corretamente.");
            dbService.instantiateDataBase();
            logger.info("Inicialização do banco de dados concluída.");
            return true;
        } else {
            logger.error("Falha ao injetar DbService. Verifique a configuração da injeção de dependência.");
            return false;
        }
    }

}

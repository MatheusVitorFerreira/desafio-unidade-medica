package com.clinicamedicadesafio.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clinicamedicadesafio.model.Clinica;
import com.clinicamedicadesafio.model.Especialidade_Medica;
import com.clinicamedicadesafio.model.Regional;
import com.clinicamedicadesafio.model.User;
import com.clinicamedicadesafio.model.UserRole;
import com.clinicamedicadesafio.repository.ClinicaRepository;
import com.clinicamedicadesafio.repository.EspecialidadeMedicaRepository;
import com.clinicamedicadesafio.repository.RegionalRepository;
import com.clinicamedicadesafio.repository.UserRepository;

@Service
public class DbService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    private RegionalRepository regionalRepository;

    @Autowired
    private EspecialidadeMedicaRepository especialidadeMedicaRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    public void instantiateDataBase() {
        LocalDateTime now = LocalDateTime.now();
        Regional regional = new Regional();
        regional.setLabel("Sudeste");
        regional.setRegiao("Norte");
        regionalRepository.save(regional);
        for (int i = 1; i <= 5; i++) {
            Especialidade_Medica especialidade = new Especialidade_Medica();
            especialidade.setDescricao("Especialidade " + i);
            especialidadeMedicaRepository.save(especialidade);
        }
        String encodedPassword = passwordEncoder.encode("admin");
        User user1 = new User("admin", encodedPassword, UserRole.ADMIN, now);
        userRepository.save(user1);

        Especialidade_Medica especialidade = especialidadeMedicaRepository.findById(1L).orElse(null);

        Clinica clinica = new Clinica();
        clinica.setRazao_social("Razão Social da Clínica");
        clinica.setCnpj("CNPJ da Clínica");
        clinica.setNomefantasia("Nome Fantasia da Clínica");
        clinica.setData_inauguracao(now);
        clinica.setAtiva(true);
        clinica.setRegional(regional);
        clinica.getEspecialidades().add(especialidade);

        clinicaRepository.save(clinica);
    }
}

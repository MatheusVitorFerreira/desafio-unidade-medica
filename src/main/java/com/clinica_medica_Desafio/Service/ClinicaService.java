package com.clinica_medica_Desafio.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.clinica_medica_Desafio.DTO.ClinicaDTO;
import com.clinica_medica_Desafio.Repository.ClinicaRepository;
import com.clinica_medica_Desafio.Repository.EspecialidadeMedicaRepository;
import com.clinica_medica_Desafio.Repository.RegionalRepository;
import com.clinica_medica_Desafio.model.Clinica;
import com.clinica_medica_Desafio.model.Especialidade_Medica;
import com.clinica_medica_Desafio.model.Regional;

@Service
public class ClinicaService {

    @Autowired
    private ClinicaRepository clinicarepository;

    @Autowired
    private RegionalRepository regionalRepository;

    @Autowired
    private EspecialidadeMedicaRepository especialidadeMedicaRepository;

    public ClinicaDTO findClinica(Long id) {
        Clinica clinica = clinicarepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, "Clinica não Encontrada"));

        List<Long> especialidadesIds = new ArrayList<>();
        for (Especialidade_Medica especialidade : clinica.getEspecialidades()) {
            especialidadesIds.add(especialidade.getId());
        }
        Regional regional = clinica.getRegional();

        return new ClinicaDTO(clinica, regional, especialidadesIds);
    }
    public ClinicaService() {
	}
    public Page<Clinica> findPageClinica(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage);
        return clinicarepository.findAll(pageRequest);
    }

    public List<Clinica> findAll() {
        return clinicarepository.findAll();
    }

    public Clinica fromDTO(ClinicaDTO objDto) {
        Clinica clinica = new Clinica(objDto.getId(), objDto.getRazao_social(), objDto.getCnpj(),
                objDto.getNome_fantasia(), objDto.getData_inauguracao(), objDto.getAtiva());
        Long regionalId = objDto.getRegionalId();
        Regional regional = regionalRepository.findById(regionalId)
                .orElseThrow(() -> new ObjectNotFoundException(regionalId, "Região não encontrada"));
        clinica.setRegional(regional);

        List<Long> especialidadesIds = objDto.getEspecialidadesIds();
        Set<Especialidade_Medica> especialidades = new HashSet<>();
        for (Long especialidadeId : especialidadesIds) {
            Especialidade_Medica especialidade = especialidadeMedicaRepository.findById(especialidadeId)
                    .orElseThrow(() -> new ObjectNotFoundException(especialidadeId, "Especialidade não encontrada"));
            especialidades.add(especialidade);
        }
        clinica.setEspecialidades(especialidades);
        return clinica;
    }
    public ClinicaDTO insert(ClinicaDTO clinicaDTO) {
        LocalDateTime now = LocalDateTime.now();
        List<Long> especialidadesIds = clinicaDTO.getEspecialidadesIds();

        if (especialidadesIds.size() < 5) {
            throw new IllegalArgumentException("Mínimo de 5 especialidades é obrigatório.");
        }

        Clinica clinica = fromDTO(clinicaDTO);

        Set<Especialidade_Medica> especialidades = new HashSet<>();
        for (Long especialidadeId : especialidadesIds) {
        	System.out.println(especialidadesIds);
            Especialidade_Medica especialidade = especialidadeMedicaRepository.findById(especialidadeId)
                    .orElseThrow(() -> new ObjectNotFoundException(especialidadeId, "Especialidade não encontrada"));
            especialidades.add(especialidade);
        }
        clinica.setEspecialidades(especialidades);

        clinica = clinicarepository.save(clinica);

        return new ClinicaDTO(clinica, clinica.getRegional(), especialidadesIds);
    }
}

package com.clinicamedicadesafio.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.clinicamedicadesafio.dto.ClinicaDTO;
import com.clinicamedicadesafio.dto.EspecialidadeMedicaDTO;
import com.clinicamedicadesafio.model.Clinica;
import com.clinicamedicadesafio.model.Especialidade_Medica;
import com.clinicamedicadesafio.model.Regional;
import com.clinicamedicadesafio.repository.ClinicaRepository;
import com.clinicamedicadesafio.repository.EspecialidadeMedicaRepository;
import com.clinicamedicadesafio.repository.RegionalRepository;
import com.clinicamedicadesafio.service.excepetion.ClinicaNotFoundException;
import com.clinicamedicadesafio.service.excepetion.DuplicateExecption;
import com.clinicamedicadesafio.service.excepetion.ErroInsercaoException;
import com.clinicamedicadesafio.service.excepetion.EspecialidadeNotFoundException;
import com.clinicamedicadesafio.service.excepetion.MinimoEspecialidadesException;
import com.clinicamedicadesafio.service.excepetion.RegiaoNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class ClinicaService {

    @Autowired
    private RegionalRepository regionalRepository;

    @Autowired
    private EspecialidadeMedicaRepository especialidadeMedicaRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    public ClinicaDTO findClinica(Long id) {
        Clinica clinica = clinicaRepository.findById(id)
                .orElseThrow(() -> new ClinicaNotFoundException("Clínica não encontrada"));
        clinica.getEspecialidades().size();

        ClinicaDTO clinicaDTO = new ClinicaDTO(clinica);
        clinicaDTO.setEspecialidades(mapEspecialidadesToDTO(clinica.getEspecialidades()));
        return clinicaDTO;
    }

    public Page<Clinica> findPageClinica(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return clinicaRepository.findAll(pageRequest);
    }

    public List<Clinica> findAll() {
        return clinicaRepository.findAll();
    }

    @Transactional
    public ClinicaDTO insert(ClinicaDTO clinicaDTO) {
        LocalDateTime now = LocalDateTime.now();
        clinicaDTO.setData_inauguracao(now);

        List<Long> especialidadesIds = clinicaDTO.getEspecialidadesIds();

        if (especialidadesIds.size() < 5) {
            throw new ErroInsercaoException("Mínimo de 5 especialidades é obrigatório.");
        }

        Clinica clinica = new Clinica();
        clinica.setRazao_social(clinicaDTO.getRazao_social());
        clinica.setCnpj(clinicaDTO.getCnpj());
        clinica.setNomefantasia(clinicaDTO.getNomefantasia());
        clinica.setData_inauguracao(now);
        clinica.setAtiva(clinicaDTO.getAtiva());

        Long regionalId = clinicaDTO.getRegionalId();
        Regional regional = regionalRepository.findById(regionalId)
                .orElseThrow(() -> new RegiaoNotFoundException("Região não encontrada"));
        clinica.setRegional(regional);
        List<Especialidade_Medica> especialidades = new ArrayList<>();
        for (Long especialidadeId : especialidadesIds) {
            Especialidade_Medica especialidade = especialidadeMedicaRepository.findById(especialidadeId)
                    .orElseThrow(() -> new EspecialidadeNotFoundException("Especialidade não encontrada"));
            especialidades.add(especialidade);
        }
        clinica.setEspecialidades(especialidades);

        clinica = clinicaRepository.save(clinica);

        return new ClinicaDTO(clinica);
    }

    @Transactional
    public ClinicaDTO update(Long id, ClinicaDTO c, List<Long> especialidadesIds, Long regionalId) {
        LocalDateTime now = LocalDateTime.now();
        Clinica existingClinica = clinicaRepository.findById(id)
                .orElseThrow(() -> new ClinicaNotFoundException("Clínica não encontrada"));

        if (clinicaRepository.existsByNomefantasiaAndIdNot(c.getNomefantasia(), id)) {
            throw new DuplicateExecption("Nome fantasia já cadastrado");
        }

        List<Especialidade_Medica> especialidades = new ArrayList<>();
        for (Long especialidadeId : especialidadesIds) {
            Especialidade_Medica especialidade = especialidadeMedicaRepository.findById(especialidadeId)
                    .orElseThrow(() -> new EspecialidadeNotFoundException("Especialidade não encontrada"));
            especialidades.add(especialidade);
        }

        Regional regional = regionalRepository.findById(regionalId)
                .orElseThrow(() -> new RegiaoNotFoundException("Região não encontrada"));

        if (especialidades.size() < 5) {
            throw new MinimoEspecialidadesException("Mínimo de 5 especialidades é obrigatório.");
        }

        existingClinica.setRazao_social(c.getRazao_social());
        existingClinica.setCnpj(c.getCnpj());
        existingClinica.setNomefantasia(c.getNomefantasia());
        existingClinica.setData_inauguracao(now);
        existingClinica.setAtiva(c.getAtiva());
        existingClinica.setRegional(regional);
        existingClinica.setEspecialidades(especialidades);

        existingClinica = clinicaRepository.save(existingClinica);

        return new ClinicaDTO(existingClinica);
    }

    public void delete(Long id) {
        Optional<Clinica> optionalClinica = clinicaRepository.findById(id);
        if (!optionalClinica.isPresent()) {
            throw new ClinicaNotFoundException("Clínica não encontrada");
        }
        clinicaRepository.deleteById(id);
    }

    @Transactional
    public Clinica adicionarEspecialidade(Long clinicaId, List<Long> especialidadesIds) {
        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new ClinicaNotFoundException("Clínica não encontrada"));

        List<Especialidade_Medica> especialidades = new ArrayList<>();
        for (Long especialidadeId : especialidadesIds) {
            Especialidade_Medica especialidade = especialidadeMedicaRepository.findById(especialidadeId)
                    .orElseThrow(() -> new EspecialidadeNotFoundException("Especialidade não encontrada"));
            especialidades.add(especialidade);
        }

        if (especialidades.size() < 5) {
            throw new MinimoEspecialidadesException("Mínimo de 5 especialidades é obrigatório.");
        }

        clinica.getEspecialidades().addAll(especialidades);
        clinica = clinicaRepository.save(clinica);

        return clinica;
    }

    private EspecialidadeMedicaDTO mapEspecialidadeToDTO(Especialidade_Medica especialidade) {
        EspecialidadeMedicaDTO especialidadeDTO = new EspecialidadeMedicaDTO();
        especialidadeDTO.setId(especialidade.getId());
        especialidadeDTO.setDescricao(especialidade.getDescricao());
        return especialidadeDTO;
    }

    private List<EspecialidadeMedicaDTO> mapEspecialidadesToDTO(List<Especialidade_Medica> especialidades) {
        return especialidades.stream()
                .sorted(Comparator.comparing(Especialidade_Medica::getId))
                .map(this::mapEspecialidadeToDTO)
                .collect(Collectors.toList());
    }
}

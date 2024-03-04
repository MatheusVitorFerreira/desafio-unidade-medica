package com.clinica_medica_Desafio.Controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.clinica_medica_Desafio.DTO.ClinicaDTO;
import com.clinica_medica_Desafio.Service.ClinicaService;
import com.clinica_medica_Desafio.model.Clinica;

@RestController
@RequestMapping(value = "/clinica")
public class ClinicaController {

    @Autowired
    private ClinicaService clinicaService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClinicaDTO> find(@PathVariable Long id) {
        ClinicaDTO dto = clinicaService.findClinica(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<Object> insert(@RequestBody ClinicaDTO clinicaDTO) {
        ClinicaDTO createdDTO = clinicaService.insert(clinicaDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(createdDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ClinicaDTO> update(@PathVariable Long id, @RequestBody ClinicaDTO clinicaDTO) {
        ClinicaDTO updatedDTO = clinicaService.update(id, clinicaDTO);
        return ResponseEntity.ok(updatedDTO);
    }

    @PostMapping("/addEspecialidade/{id}")
    public ResponseEntity<ClinicaDTO> adicionarEspecialidade(@PathVariable Long id,
            @RequestBody ClinicaDTO clinicaDTO) {
        Clinica clinica = clinicaService.adicionarEspecialidade(id, clinicaDTO);
        return ResponseEntity.ok(new ClinicaDTO(clinica));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        clinicaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

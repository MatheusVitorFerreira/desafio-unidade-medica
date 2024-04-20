package com.clinicamedicadesafio.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.clinicamedicadesafio.controller.EspecialidadeMedicaController;
import com.clinicamedicadesafio.dto.EspecialidadeMedicaDTO;
import com.clinicamedicadesafio.model.Especialidade_Medica;
import com.clinicamedicadesafio.repository.EspecialidadeMedicaRepository;
import com.clinicamedicadesafio.service.EspecialidadeMedicaService;

@SpringBootTest
public class EspecialidadeMedicaControllerTest {

	private static final Long ID = 1L;
	private static final String DESCRICAO = "Cardiolista";

	@Mock
	EspecialidadeMedicaRepository especialidadeMedicaRepository;

	@Mock
	private EspecialidadeMedicaService especialidadeMedicaService;

	@InjectMocks
	private EspecialidadeMedicaController controller;


	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

	}

	@Test
	public void testFindById_Success() {
		Especialidade_Medica especialidade = new Especialidade_Medica();
		especialidade.setId(1L);
		especialidade.setDescricao("Cardiologia");
		when(especialidadeMedicaService.findById(1L)).thenReturn(especialidade);
		ResponseEntity<Especialidade_Medica> responseEntity = controller.findById(1L);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(especialidade, responseEntity.getBody());
	}

	@Test
	public void testFindPageEspecialidade_Success() {

		List<Especialidade_Medica> especialidades = new ArrayList<>();
		especialidades.add(new Especialidade_Medica(1L, "Cardiologia"));
		especialidades.add(new Especialidade_Medica(2L, "Dermatologia"));
		Page<Especialidade_Medica> especialidadesPage = new PageImpl<>(especialidades);

		when(especialidadeMedicaService.findAllByOrderByDescricaoAsc(PageRequest.of(0, 24)))
				.thenReturn(especialidadesPage);

		ResponseEntity<Page<EspecialidadeMedicaDTO>> responseEntity = controller.findPageEspecialidade(0, 24);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Page<EspecialidadeMedicaDTO> especialidadesDTOPage = responseEntity.getBody();
		assertEquals(especialidadesPage.getContent().size(), especialidadesDTOPage.getContent().size());
		assertEquals(especialidadesPage.getTotalElements(), especialidadesDTOPage.getTotalElements());
		assertEquals(especialidadesPage.getNumber(), especialidadesDTOPage.getNumber());
		assertEquals(especialidadesPage.getSize(), especialidadesDTOPage.getSize());

	}

	@Test
	public void testInsert_Success() {

		Especialidade_Medica especialidade = new Especialidade_Medica();
		especialidade.setId(1L);
		especialidade.setDescricao("Cardiologia");

		when(especialidadeMedicaService.insertEspecialidade(any(Especialidade_Medica.class))).thenReturn(especialidade);

		ResponseEntity<Object> responseEntity = controller.insert(especialidade);
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

		URI expectedUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(especialidade.getId()).toUri();
		assertEquals(expectedUri, responseEntity.getHeaders().getLocation());
	}

	@Test
	public void testUpdateEspecialidade_Success() {

		EspecialidadeMedicaDTO dto = new EspecialidadeMedicaDTO();
		dto.setId(1L);
		dto.setDescricao("Nova descrição");
		when(especialidadeMedicaService.update(any(EspecialidadeMedicaDTO.class), anyLong()))
				.thenReturn(new Especialidade_Medica());
		ResponseEntity<Object> responseEntity = controller.updateEspecialidade(dto, 1L);

		assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
	}

	@Test
	public void testDelete_Success() {

		doNothing().when(especialidadeMedicaService).delete(anyLong());

		ResponseEntity<String> responseEntity = controller.delete(1L);
		assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
	}

	@Test
	public void testFindOrdenadoPorDescricao_Success() {
		List<Especialidade_Medica> especialidades = new ArrayList<>();
		especialidades.add(new Especialidade_Medica(1L, "Cardiologia"));
		especialidades.add(new Especialidade_Medica(2L, "Ortopedia"));

		Page<Especialidade_Medica> pageEspecialidades = new PageImpl<>(especialidades);

		when(especialidadeMedicaService.findAllByOrderByDescricaoAsc(any(PageRequest.class)))
				.thenReturn(pageEspecialidades);

		ResponseEntity<Page<String>> responseEntity = controller.findOrdenadoPorDescricao(0, 24);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

		assertEquals(especialidades.size(), responseEntity.getBody().getContent().size());
		assertEquals(especialidades.get(0).getDescricao(), responseEntity.getBody().getContent().get(0));
		assertEquals(especialidades.get(1).getDescricao(), responseEntity.getBody().getContent().get(1));
	}
}

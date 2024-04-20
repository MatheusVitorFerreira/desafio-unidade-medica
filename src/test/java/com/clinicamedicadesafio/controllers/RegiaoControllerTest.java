package com.clinicamedicadesafio.controllers;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import com.clinicamedicadesafio.controller.RegionalController;
import com.clinicamedicadesafio.dto.RegionalDTO;
import com.clinicamedicadesafio.model.Regional;
import com.clinicamedicadesafio.service.RegionalService;
import com.clinicamedicadesafio.service.excepetion.RegiaoNotFoundException;

@SpringBootTest
public class RegiaoControllerTest {

	private static final Long ID = 1L;
	private static final String LABEL = "Rio de Janeiro";
	private static final String REGIAO = "Sudeste";

	@InjectMocks
	private RegionalController controller;

	private RegionalDTO regionalDTO;

	private Regional regional;

	@Mock
	private RegionalService regionalService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		StartRegioal();
	}

	@Test
	void whenFindByidThenReturnSucess() {
		
		when(regionalService.findRegional(anyLong())).thenReturn(regional);
		ResponseEntity<Regional> responseEntity = controller.findbyId(ID);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(regional, responseEntity.getBody());
	}

	@Test
	void whenFindByIdThenThrowRegiaoNotFoundException() {

		when(regionalService.findRegional(anyLong())).thenReturn(null);
		assertThrows(RegiaoNotFoundException.class, () -> controller.findbyId(ID));
	}
	@Test
	public void testFindPageRegiao() {
		
	    List<Regional> regionalList = new ArrayList<>();
	    PageRequest pageRequest = PageRequest.of(0, 24);
	    
	    Page<Regional> regionalPage = new PageImpl<>(regionalList, pageRequest, regionalList.size());
	    when(regionalService.findAllByOrderByLabel(pageRequest)).thenReturn(regionalPage);

	    ResponseEntity<Page<RegionalDTO>> responseEntity = controller.findPageRegiao(0, 24);
	    assertEquals(200, responseEntity.getStatusCode().value());
	    assertEquals(regionalPage.map(RegionalDTO::new).getContent(), responseEntity.getBody().getContent());
	}
	@Test
	public void testInsert() throws Exception {
	    RegionalDTO regionalDto = new RegionalDTO(); 

	    Regional obj = new Regional(); 
	    when(regionalService.fromDTO(any(RegionalDTO.class))).thenReturn(obj);

	    when(regionalService.insert(any(Regional.class))).thenReturn(obj);

	    ResponseEntity<String> responseEntity = controller.insert(regionalDto);
	    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
	    assertNotNull(responseEntity.getHeaders().getLocation());
	}
	@Test
	public void testUpdateRegioes() throws Exception {
	    RegionalDTO regionalDto = new RegionalDTO(); 
	    Long id = 1L;

	    Regional updatedRegional = new Regional();
	    when(regionalService.update(any(RegionalDTO.class), anyLong())).thenReturn(updatedRegional);
	    ResponseEntity<Regional> responseEntity = controller.updateRegioes(regionalDto, id);
	    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
	    assertNull(responseEntity.getBody());
	}
	@Test
	public void testDelete() throws Exception {
	    Long id = 1L;
	    doNothing().when(regionalService).delete(id);

	    ResponseEntity<String> responseEntity = controller.delete(id);
	    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
	    assertNull(responseEntity.getBody()); 
	}
	@Test
	public void testFindOrdenadoPorLabel() {
	    List<Regional> regioes = new ArrayList<>();
	    regioes.add(new Regional(1L, "Região A","Rio de Janeiro"));
	    regioes.add(new Regional(2L, "Região B","São Paulo"));
	    Page<Regional> regionalPage = new PageImpl<>(regioes);

	    when(regionalService.findAllByOrderByLabel(any(PageRequest.class))).thenReturn(regionalPage);
	    ResponseEntity<Page<String>> responseEntity = controller.findOrdenadoPorLabel(0, 24);
	    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	    Page<String> labelsPage = responseEntity.getBody();
	    assertNotNull(labelsPage);

	    assertEquals(regionalPage.getTotalElements(), labelsPage.getTotalElements());
	    assertEquals(regionalPage.getSize(), labelsPage.getSize());

	    List<String> expectedLabels = regioes.stream().map(Regional::getLabel).collect(Collectors.toList());
	    List<String> actualLabels = labelsPage.getContent();
	    assertEquals(expectedLabels, actualLabels);
	}

	private void StartRegioal() {
		regional = new Regional(ID, LABEL, REGIAO);
		regionalDTO = new RegionalDTO(ID, LABEL, REGIAO);

	}
}

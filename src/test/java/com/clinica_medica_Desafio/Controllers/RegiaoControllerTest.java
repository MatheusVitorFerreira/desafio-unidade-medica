package com.clinica_medica_Desafio.Controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.clinica_medica_Desafio.Controller.RegionalController;
import com.clinica_medica_Desafio.DTO.RegionalDTO;
import com.clinica_medica_Desafio.Service.RegionalService;
import com.clinica_medica_Desafio.Service.Exceptions.RegiaoNotFoundException;
import com.clinica_medica_Desafio.model.Regional;

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

	private void StartRegioal() {
		regional = new Regional(ID, LABEL, REGIAO);
		regionalDTO = new RegionalDTO(ID, LABEL, REGIAO);

	}
}

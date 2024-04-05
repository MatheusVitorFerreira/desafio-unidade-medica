package com.clinica_medica_Desafio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.clinica_medica_Desafio.DTO.RegionalDTO;
import com.clinica_medica_Desafio.Repository.RegionalRepository;
import com.clinica_medica_Desafio.Service.RegionalService;
import com.clinica_medica_Desafio.Service.Exceptions.DuplicateExecption;
import com.clinica_medica_Desafio.Service.Exceptions.EmptyField;
import com.clinica_medica_Desafio.Service.Exceptions.RegiaoNotFoundException;
import com.clinica_medica_Desafio.model.Regional;

class RegionalServiceTest {

	private static final Long ID = 1L;
	private static final String LABEL = "Rio de Janeiro";
	private static final String REGIAO = "Sudeste";

	@Mock
	RegionalRepository regionalRepository;

	@InjectMocks
	private RegionalService regionalService;
	private RegionalDTO regionalDTO;
	private Regional regional;
	private Optional<Regional> optionalRegional;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		StartRegioal();
	}

	@Test
	@DisplayName("Quando Pedir para buscar uma Regiao por ID")
	void findregional() {
		Mockito.when(regionalRepository.findById(Mockito.anyLong())).thenReturn(optionalRegional);
		Regional response = regionalService.findRegional(ID);
		Assertions.assertThat(response).isInstanceOf(Regional.class);
		assertNotNull(response);
		assertEquals(ID, response.getId());
	}

	@Test
	@DisplayName("Deve lançar exceção ao tentar encontrar uma Regiao que não existe")
	void findByIdEspecialidadeMedicaNotFound() {
		when(regionalRepository.findById(ID)).thenReturn(Optional.empty());
		assertThrows(RegiaoNotFoundException.class, () -> regionalService.findRegional(ID));
	}

	@Test
	@DisplayName("Deve retornar true quando a Pedir para Paginar o servico")
	void findPageRegiao() {
		List<Regional> Regiao = new ArrayList<>();
		Regiao.add(new Regional(ID, LABEL, REGIAO));
		Regiao.add(new Regional(1L, "São PAULO", "Sudeste"));
		Regiao.add(new Regional(1L, "Santos", "Sudeste"));
		Page<Regional> pag = new PageImpl<>(Regiao);
		when(regionalRepository.findAll(PageRequest.of(0, 10, Sort.Direction.ASC, "descricao"))).thenReturn(pag);
		Page<Regional> result = regionalService.findPageRegional(0, 10, "descricao", "ASC");
		assertEquals(pag, result, "A página retornada pelo serviço dev	e ser igual à página simulada");
	}

	@Test
	void FindAll() {
		List<Regional> Regiao = new ArrayList<>();
		Regiao.add(new Regional(ID, LABEL, REGIAO));
		Regiao.add(new Regional(1L, "São PAULO", "Sudeste"));
		Regiao.add(new Regional(1L, "Santos", "Sudeste"));
		Mockito.when(regionalRepository.findAll()).thenReturn(Regiao);
		List<Regional> response = regionalService.findAll();
		assertNotNull(response);
		assertEquals(3, response.size());
		assertEquals(Regional.class, response.get(0).getClass());
	}

	@Test
	@DisplayName("Deve retornar True quando Insirir uma Regiao")
	void insertRegiaoCase1() {
		Regional regiao = new Regional();
		regiao.setId(ID);
		regiao.setLabel(LABEL);
		regiao.setRegiao(REGIAO);

		Mockito.when(regionalRepository.save(Mockito.any(Regional.class))).thenReturn(regiao);
		Regional inserteRegiao = regionalService.insert(regiao);
		assertNotNull(inserteRegiao);
		assertEquals("Rio de Janeiro", inserteRegiao.getLabel());
	}

	@Test
	@DisplayName("Deve lançar DuplicateException ao tentar inserir com Label duplicada")
	void insertRegiaoDuplicateException() {
		Mockito.when(regionalRepository.existsByLabel("Rio De Janeiro")).thenReturn(true);
		Regional regiaoExistente = new Regional();
		regiaoExistente.setLabel("Rio De Janeiro");
		assertThrows(DuplicateExecption.class, () -> regionalService.insert(regiaoExistente));
	}

	@Test
	@DisplayName("Deve lançar EmptyField ao tentar inserir um label com campo vazio")
	void insertRegiaoEmptyField() {
		Mockito.when(regionalRepository.existsByLabel("Rio De Janeiro")).thenReturn(false);
		Regional regiaoVazia = new Regional();
		regiaoVazia.setLabel("");
		regiaoVazia.setRegiao("Alguma região");
		assertThrows(EmptyField.class, () -> regionalService.insert(regiaoVazia));
	}

	@Test
	@DisplayName("Deve atualizar a Região com sucesso")
	void updateRegiao() {
		Long id = 1L;
		RegionalDTO objDto = new RegionalDTO();
		objDto.setId(id);
		objDto.setLabel("Rio de Janeiro");
		objDto.setRegiao("Sudeste");

		Regional regionalUp = new Regional();
		regionalUp.setId(id);
		regionalUp.setLabel("Buzeira");

		when(regionalRepository.findById(id)).thenReturn(Optional.of(regionalUp));
		when(regionalRepository.existsByLabelAndIdNot(eq(objDto.getLabel()), eq(id))).thenReturn(false);
		when(regionalRepository.save(any(Regional.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Regional updatedRegional = regionalService.update(objDto, id);

		assertEquals(objDto.getLabel(), updatedRegional.getLabel());
		verify(regionalRepository, times(1)).findById(id);
	}

	@Test
	@DisplayName("Deve lançar exceção ao tentar atualizar com descrição vazia")
	void updateRegiaoEmpty() {
		Long id = 1L;
		Regional regionalExistente = new Regional(id, "Label existente", "Região existente");
		when(regionalRepository.findById(id)).thenReturn(Optional.of(regionalExistente));

		RegionalDTO objDto = new RegionalDTO();
		objDto.setLabel("");
		objDto.setRegiao("Região vazia");

		assertThrows(EmptyField.class, () -> regionalService.update(objDto, id));
	}

	@Test
	@DisplayName("Deve lançar DuplicateException ao tentar Atualizar com Label duplicada")
	void updateRegiaoDuplicateException() {
		Long id = 1L;
		String existingLabel = "Rio De Janeiro";
		Mockito.when(regionalRepository.existsByLabelAndIdNot(existingLabel, id)).thenReturn(true);
		Mockito.when(regionalRepository.findById(id)).thenReturn(Optional.of(new Regional()));
		RegionalDTO regiaoExistente = new RegionalDTO();
		regiaoExistente.setId(id);
		regiaoExistente.setLabel(existingLabel);

		assertThrows(DuplicateExecption.class, () -> regionalService.update(regiaoExistente, id));
	}

	@Test
	@DisplayName("Deve atualizar label e regiao corretamente")
	void updateLabelERegiao() {
		Regional objt = new Regional();
		objt.setLabel(LABEL);
		objt.setRegiao(REGIAO);

		Regional newObj = new Regional();
		newObj.setLabel("Manaus");
		newObj.setRegiao("Norte");

		regionalService.updateLabelERegiao(objt, newObj);

		assertEquals(objt.getLabel(), newObj.getLabel());
		assertEquals(objt.getRegiao(), newObj.getRegiao());

	}

	@Test
	@DisplayName("Deve lançar EmptyField ao criar Regional a partir de DTO inválido")
	void testFromDTOWithInvalidDto() {
		RegionalDTO invalidDto = new RegionalDTO();
		invalidDto.setId(1L);
		invalidDto.setRegiao("Rio de Janeiro");
		RegionalService regionalService = new RegionalService();
		assertThrows(EmptyField.class, () -> regionalService.fromDTO(invalidDto));
	}

	@Test
	@DisplayName("Deve criar Regional a partir de DTO válido")
	void testFromDTOWithValidDto() {
		RegionalDTO validDto = new RegionalDTO();
		validDto.setId(1L);
		validDto.setLabel("Label válido");
		validDto.setRegiao("Região válida");
		RegionalService regionalService = new RegionalService();
		Regional regional = regionalService.fromDTO(validDto);

		assertNotNull(regional);
		assertEquals(validDto.getId(), regional.getId());
		assertEquals(validDto.getLabel(), regional.getLabel());
		assertEquals(validDto.getRegiao(), regional.getRegiao());
	}

	@Test
	public void TestDeleteCase1() {
		Optional<Regional> optionalRegional = Optional.of(new Regional());
		when(regionalRepository.findById(Mockito.anyLong())).thenReturn(optionalRegional);
		doNothing().when(regionalRepository).deleteById(anyLong());
		regionalService.delete(1L);
		verify(regionalRepository, times(1)).deleteById(anyLong());
	}

	@Test
	public void testDeleteRegiaoNotFoundException() {
		RegionalDTO validDto = new RegionalDTO();
		validDto.setId(1L);
		validDto.setLabel("Label válido");
		validDto.setRegiao("Região válida");
		when(regionalRepository.findById(ID)).thenReturn(Optional.empty());
		assertThrows(RegiaoNotFoundException.class, () -> regionalService.delete(ID));
	}

	@Test
	public void testFindAllByOrderByLabel() {
		List<Regional> regionais = new ArrayList<>();
		regionais.add(new Regional(1L, "Minas Gerais", "Sudeste"));
		regionais.add(new Regional(2L, "Rondonia", "Norte"));
		regionais.add(new Regional(3L, "Ceara", "Nordeste"));
		Page<Regional> page = new PageImpl<>(regionais);
		when(regionalRepository.findAllByOrderByLabel(any(Pageable.class))).thenReturn(page);
		Page<Regional> resultPage = regionalService.findAllByOrderByLabel(Pageable.unpaged());
		assertEquals("Minas Gerais", resultPage.getContent().get(0).getLabel());
		assertEquals("Rondonia", resultPage.getContent().get(1).getLabel());
		assertEquals("Ceara", resultPage.getContent().get(2).getLabel());
	}

	private void StartRegioal() {
		regional = new Regional(ID, LABEL, REGIAO);
		regionalDTO = new RegionalDTO(ID, LABEL, REGIAO);
		optionalRegional = Optional.of(new Regional(ID, LABEL, REGIAO));

	}
}

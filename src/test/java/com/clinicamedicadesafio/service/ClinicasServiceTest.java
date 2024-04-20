package com.clinicamedicadesafio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.clinicamedicadesafio.dto.ClinicaDTO;
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

public class ClinicasServiceTest {

	@Mock
	private ClinicaRepository clinicaRepository;

	@Mock
	private RegionalRepository regionalRepository;

	@Mock
	private EspecialidadeMedicaRepository especialidadeMedicaRepository;

	@InjectMocks
	private ClinicaService clinicaService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindClinica() {
		Long id = 1L;
		Clinica clinica = new Clinica();
		clinica.setId(id);
		when(clinicaRepository.findById(id)).thenReturn(Optional.of(clinica));

		ClinicaDTO clinicaDTO = clinicaService.findClinica(id);

		assertNotNull(clinicaDTO);
	}

	@Test
	void testFindPageClinica() {
		int page = 0;
		int linesPerPage = 10;
		String orderBy = "nomefantasia";
		String direction = "ASC";
		Page<Clinica> pageClinica = Page.empty();
		when(clinicaRepository.findAll(PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy)))
				.thenReturn(pageClinica);

		Page<Clinica> result = clinicaService.findPageClinica(page, linesPerPage, orderBy, direction);

		assertEquals(pageClinica.getTotalElements(), result.getTotalElements());
	}

	@Test
	void testInsertSuccess() {
		Regional regional = new Regional();
		regional.setId(1L);
		regional.setLabel("Rio de Janeiro");
		regional.setRegiao("Sudeste");

		when(regionalRepository.findById(1L)).thenReturn(Optional.of(regional));

		List<Especialidade_Medica> especialidadesMedicas = new ArrayList<>();
		for (long i = 1; i <= 5; i++) {
			Especialidade_Medica especialidadeMedica = new Especialidade_Medica();
			especialidadeMedica.setId(i);
			especialidadesMedicas.add(especialidadeMedica);
			when(especialidadeMedicaRepository.findById(i)).thenReturn(Optional.of(especialidadeMedica));
		}

		LocalDateTime now = LocalDateTime.now();
		ClinicaDTO clinicaDTO = new ClinicaDTO();
		clinicaDTO.setData_inauguracao(now);
		clinicaDTO.setRazao_social("Nova Razão Social");
		clinicaDTO.setAtiva(true);
		clinicaDTO.setCnpj("44.529.4350001-01");
		clinicaDTO.setNomefantasia("Clinica New");
		clinicaDTO.setRegionalId(1L);
		List<Long> especialidadesIds = new ArrayList<>();
		for (long i = 1; i <= 5; i++) {
			especialidadesIds.add(i);
		}
		clinicaDTO.setEspecialidadesIds(especialidadesIds);
		assertNotNull(clinicaDTO);
		when(clinicaRepository.save(any(Clinica.class))).thenAnswer(invocation -> {
			Clinica clinica = invocation.getArgument(0);
			clinica.setId(8L);
			return clinica;
		});
		ClinicaDTO clinicaInserida = clinicaService.insert(clinicaDTO);
		assertNotNull(clinicaInserida);
		assertEquals(clinicaDTO.getRazao_social(), clinicaInserida.getRazao_social());
		assertEquals(clinicaDTO.getCnpj(), clinicaInserida.getCnpj());
		assertEquals(clinicaDTO.getNomefantasia(), clinicaInserida.getNomefantasia());
		assertEquals(clinicaDTO.getAtiva(), clinicaInserida.getAtiva());
	}

	@Test
	void testFindAll() {
		when(clinicaRepository.findAll()).thenReturn(new ArrayList<>());

		assertNotNull(clinicaService.findAll());
	}

	@Test
	void testInsertWithLessThanFiveEspecialidades() {
		ClinicaDTO clinicaDTO = new ClinicaDTO();
		clinicaDTO.setEspecialidadesIds(Arrays.asList(1L, 2L, 3L, 4L)); // Apenas 4 especialidades
		assertThrows(ErroInsercaoException.class, () -> clinicaService.insert(clinicaDTO));
	}

	@Test
	void testInsertWithEspecialidadeNotFound() {
		ClinicaDTO clinicaDTO = new ClinicaDTO();
		clinicaDTO.setEspecialidadesIds(Arrays.asList(1L, 2L, 3L, 4L, 5L));
		when(regionalRepository.findById(any())).thenReturn(Optional.of(new Regional()));
		when(especialidadeMedicaRepository.findById(any())).thenReturn(Optional.empty());
		assertThrows(EspecialidadeNotFoundException.class, () -> clinicaService.insert(clinicaDTO));
	}

	@Test
	void testUpdateSuccess() {
		Regional regional = new Regional();
		regional.setId(1L);
		regional.setLabel("Rio de Janeiro");
		regional.setRegiao("Sudeste");

		when(regionalRepository.findById(1L)).thenReturn(Optional.of(regional));

		List<Especialidade_Medica> especialidadesMedicas = new ArrayList<>();
		for (long i = 1; i <= 5; i++) {
			Especialidade_Medica especialidadeMedica = new Especialidade_Medica();
			especialidadeMedica.setId(i);
			especialidadesMedicas.add(especialidadeMedica);
			when(especialidadeMedicaRepository.findById(i)).thenReturn(Optional.of(especialidadeMedica));
		}

		LocalDateTime now = LocalDateTime.now();
		ClinicaDTO clinicaDTO = new ClinicaDTO();
		clinicaDTO.setData_inauguracao(now);
		clinicaDTO.setRazao_social("Nova Razão Social");
		clinicaDTO.setAtiva(true);
		clinicaDTO.setCnpj("44.529.4350001-01");
		clinicaDTO.setNomefantasia("Clinica Updated");
		clinicaDTO.setRegionalId(1L);
		List<Long> especialidadesIds = new ArrayList<>();
		for (long i = 1; i <= 5; i++) {
			especialidadesIds.add(i);
		}
		Clinica existingClinica = new Clinica();
		existingClinica.setId(1L);
		existingClinica.setRazao_social("Razao Social Antiga");
		existingClinica.setCnpj("CNPJ Antigo");
		existingClinica.setData_inauguracao(now.minusDays(1));
		existingClinica.setAtiva(false);
		existingClinica.setRegional(regional);
		existingClinica.setEspecialidades(new ArrayList<>(especialidadesMedicas)); 

		when(clinicaRepository.findById(1L)).thenReturn(Optional.of(existingClinica));

		when(clinicaRepository.save(any(Clinica.class))).thenAnswer(invocation -> {
			Clinica updatedClinica = invocation.getArgument(0);
			updatedClinica.setRazao_social(clinicaDTO.getRazao_social());
			updatedClinica.setCnpj(clinicaDTO.getCnpj());
			updatedClinica.setData_inauguracao(now);
			updatedClinica.setAtiva(clinicaDTO.getAtiva());
			updatedClinica.setRegional(regional);
			updatedClinica.setEspecialidades(especialidadesMedicas);
			return updatedClinica;
		});

		ClinicaDTO updatedClinicaDTO = clinicaService.update(1L, clinicaDTO, new ArrayList<>(especialidadesIds), 1L);
		assertNotNull(updatedClinicaDTO);
		assertEquals(clinicaDTO.getRazao_social(), updatedClinicaDTO.getRazao_social());
		assertEquals(clinicaDTO.getCnpj(), updatedClinicaDTO.getCnpj());
		assertEquals(clinicaDTO.getNomefantasia(), updatedClinicaDTO.getNomefantasia());
		assertEquals(clinicaDTO.getAtiva(), updatedClinicaDTO.getAtiva());
	}

	@Test
	void testUpdateClinicaNotFound() {
		when(clinicaRepository.findById(1L)).thenReturn(Optional.empty());

		ClinicaDTO clinicaDTO = new ClinicaDTO();
		assertThrows(ClinicaNotFoundException.class, () -> clinicaService.update(1L, clinicaDTO, new ArrayList<>(), 1L));
	}

	@Test
	void testUpdateDuplicateNomeFantasia() {
		Clinica existingClinica = new Clinica();
		existingClinica.setId(1L);
		existingClinica.setNomefantasia("Clinica Antiga");

		when(clinicaRepository.findById(1L)).thenReturn(Optional.of(existingClinica));
		when(clinicaRepository.existsByNomefantasiaAndIdNot(any(), eq(1L))).thenReturn(true);

		ClinicaDTO clinicaDTO = new ClinicaDTO();
		clinicaDTO.setNomefantasia("Clinica Nova");
		assertThrows(DuplicateExecption.class, () -> clinicaService.update(1L, clinicaDTO, new ArrayList<>(), 1L));
	}

	@Test
	void testUpdateEspecialidadesNotFound() {
		Clinica existingClinica = new Clinica();
		existingClinica.setId(1L);

		when(clinicaRepository.findById(1L)).thenReturn(Optional.of(existingClinica));
		when(especialidadeMedicaRepository.findById(any())).thenReturn(Optional.empty());

		ClinicaDTO clinicaDTO = new ClinicaDTO();
		List<Long> especialidadesIds = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L, 5L));
		assertThrows(EspecialidadeNotFoundException.class,
				() -> clinicaService.update(1L, clinicaDTO, especialidadesIds, 1L));
	}

	@Test
	void testUpdateMinimoEspecialidades() {
		Clinica existingClinica = new Clinica();
		existingClinica.setId(1L);

		when(clinicaRepository.findById(1L)).thenReturn(Optional.of(existingClinica));

		Regional regional = new Regional();
		regional.setId(1L);
		when(regionalRepository.findById(1L)).thenReturn(Optional.of(regional));

		List<Especialidade_Medica> especialidadesMedicas = new ArrayList<>();
		for (long i = 1; i <= 5; i++) {
			Especialidade_Medica especialidadeMedica = new Especialidade_Medica();
			especialidadeMedica.setId(i);
			especialidadesMedicas.add(especialidadeMedica);
		}
		when(especialidadeMedicaRepository.findById(any())).thenReturn(Optional.of(new Especialidade_Medica()));

		List<Long> especialidadesIds = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L));

		assertThrows(MinimoEspecialidadesException.class,
				() -> clinicaService.update(1L, new ClinicaDTO(), especialidadesIds, 1L));
	}

	@Test
	void testDeleteExistingClinica() {
		Long id = 1L;
		Clinica clinica = new Clinica();
		clinica.setId(id);
		when(clinicaRepository.findById(id)).thenReturn(Optional.of(clinica));
		clinicaService.delete(id);
		verify(clinicaRepository, times(1)).deleteById(id);
	}

	@Test
	void testAdicionarEspecialidade() {
		for (long i = 1; i <= 5; i++) {
			Especialidade_Medica especialidade = new Especialidade_Medica();
			especialidade.setId(i);
			when(especialidadeMedicaRepository.findById(i)).thenReturn(Optional.of(especialidade));
		}
		Long clinicaId = 1L;
		List<Long> especialidadesIds = new ArrayList<>();
		for (long i = 1; i <= 5; i++) {
			especialidadesIds.add(i);
		}
		Clinica clinica = new Clinica();
		clinica.setId(clinicaId);
		when(clinicaRepository.findById(clinicaId)).thenReturn(Optional.of(clinica));

		when(clinicaRepository.save(any(Clinica.class))).thenReturn(clinica);

		Clinica result = clinicaService.adicionarEspecialidade(clinicaId, especialidadesIds);

		assertNotNull(result);
		assertEquals(5, result.getEspecialidades().size());
		verify(clinicaRepository, times(1)).findById(clinicaId);
		verify(especialidadeMedicaRepository, times(5)).findById(anyLong());
		verify(clinicaRepository, times(1)).save(any(Clinica.class));
	}

	@Test
	void testAdicionarEspecialidade_MinimoEspecialidadesException() {
		Long clinicaId = 1L;
		Clinica clinica = new Clinica();
		clinica.setId(clinicaId);
		when(clinicaRepository.findById(clinicaId)).thenReturn(Optional.of(clinica));
		List<Long> especialidadesIds = new ArrayList<>();
		especialidadesIds.add(1L);
		when(especialidadeMedicaRepository.findById(anyLong())).thenReturn(Optional.of(new Especialidade_Medica()));

		assertThrows(MinimoEspecialidadesException.class,
				() -> clinicaService.adicionarEspecialidade(clinicaId, especialidadesIds));
	}

	@Test
	void testDeleteNonExistingClinica() {
		Long id = 1L;
		when(clinicaRepository.findById(id)).thenReturn(Optional.empty());
		assertThrows(ClinicaNotFoundException.class, () -> clinicaService.delete(id));
	}

	@Test
	public void testDeleteExistingClinica3() {
		Long id = 1L;
		Clinica clinica = new Clinica();
		clinica.setId(id);
		when(clinicaRepository.findById(id)).thenReturn(Optional.of(clinica));

		clinicaService.delete(id);
		verify(clinicaRepository, times(1)).deleteById(id);
	}
}
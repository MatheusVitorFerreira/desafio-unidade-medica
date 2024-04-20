package com.clinicamedicadesafio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
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

import com.clinicamedicadesafio.dto.EspecialidadeMedicaDTO;
import com.clinicamedicadesafio.model.Especialidade_Medica;
import com.clinicamedicadesafio.repository.EspecialidadeMedicaRepository;
import com.clinicamedicadesafio.service.excepetion.DuplicateExecption;
import com.clinicamedicadesafio.service.excepetion.EmptyField;
import com.clinicamedicadesafio.service.excepetion.EspecialidadeNotFoundException;

class EspecialidadeMedicasServiceTest {

	private static final Long ID = 1L;
	private static final String DESCRICAO = "Cardiolista";

	@Mock
	EspecialidadeMedicaRepository especialidadeMedicaRepository;

	@InjectMocks
	private EspecialidadeMedicaService especialidadeMedicaService;
	private EspecialidadeMedicaDTO especialidadeMedicaDTO;
	private Especialidade_Medica especialidadeMedica;
	private Optional<Especialidade_Medica> optionalEspecialidade;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		StartEspecialidade();
	}

	@Test
	@DisplayName("Quando Pedir para buscar uma Especialidade por ID")
	void findEspecialidade() {
		Mockito.when(especialidadeMedicaRepository.findById(Mockito.anyLong())).thenReturn(optionalEspecialidade);
		Especialidade_Medica response = especialidadeMedicaService.findById(ID);
		Assertions.assertThat(response).isInstanceOf(Especialidade_Medica.class);
		assertNotNull(response);
		assertEquals(ID, response.getId());
	}

	@Test
	@DisplayName("Deve lançar exceção ao tentar encontrar uma especialidade médica que não existe")
	void findByIdEspecialidadeMedicaNotFound() {
		when(especialidadeMedicaRepository.findById(ID)).thenReturn(Optional.empty());
		assertThrows(EspecialidadeNotFoundException.class, () -> especialidadeMedicaService.findById(ID));
	}

	@Test
	@DisplayName("Deve retornar true quando a Pedir para Paginar o servico")
	void findPageEspecialidadeCase1() {
		List<Especialidade_Medica> especialidades = new ArrayList<>();
		especialidades.add(new Especialidade_Medica("Cardiologista"));
		especialidades.add(new Especialidade_Medica("Dermatologista"));
		especialidades.add(new Especialidade_Medica("Ortopedista"));
		Page<Especialidade_Medica> pag = new PageImpl<>(especialidades);
		when(especialidadeMedicaRepository.findAll(PageRequest.of(0, 10, Sort.Direction.ASC, "descricao")))
				.thenReturn(pag);
		Page<Especialidade_Medica> result = especialidadeMedicaService.findPageEspecialidade(0, 10, "descricao", "ASC");
		assertEquals(pag, result, "A página retornada pelo serviço deve ser igual à página simulada");
	}

	@Test
	@DisplayName("Deve retornar uma página de especialidades médicas")
	void findPageEspecialidade() {
		List<Especialidade_Medica> especialidades = new ArrayList<>();
		especialidades.add(new Especialidade_Medica(1L, "Cardiologista"));
		especialidades.add(new Especialidade_Medica(2L, "Dermatologista"));
		especialidades.add(new Especialidade_Medica(3L, "Ortopedista"));
		Page<Especialidade_Medica> paginaEspecialidades = new PageImpl<>(especialidades);
		when(especialidadeMedicaRepository.findAll(PageRequest.of(0, 10, Sort.Direction.ASC, "descricao")))
				.thenReturn(paginaEspecialidades);
		Page<Especialidade_Medica> resultado = especialidadeMedicaService.findPageEspecialidade(0, 10, "descricao",
				"ASC");
		assertEquals(paginaEspecialidades, resultado,
				"A página retornada pelo serviço deve ser igual à página simulada");
	}

	@Test
	void FindAll() {
		List<Especialidade_Medica> especialidades = new ArrayList<>();
		especialidades.add(new Especialidade_Medica(1L, "Cardiolista"));
		especialidades.add(new Especialidade_Medica(2L, "Dentista"));
		especialidades.add(new Especialidade_Medica(3L, "Dermatologista"));
		Mockito.when(especialidadeMedicaRepository.findAll()).thenReturn(especialidades);
		List<Especialidade_Medica> response = especialidadeMedicaService.findAll();
		assertNotNull(response);
		assertEquals(3, response.size());
		assertEquals(Especialidade_Medica.class, response.get(0).getClass());
	}

	@Test
	@DisplayName("Deve retornar True quando Insirir uma especilidade")
	void insertEspecialidadeCase1() {
		Especialidade_Medica especialidadeMedica = new Especialidade_Medica();
		especialidadeMedica.setDescricao("Especialidade Teste");
		Mockito.when(especialidadeMedicaRepository.save(Mockito.any(Especialidade_Medica.class)))
				.thenReturn(especialidadeMedica);
		Especialidade_Medica insertedEspecialidade = especialidadeMedicaService
				.insertEspecialidade(especialidadeMedica);
		assertNotNull(insertedEspecialidade);
		assertEquals("Especialidade Teste", insertedEspecialidade.getDescricao());
	}

	@Test
	@DisplayName("Deve lançar DuplicateException ao tentar inserir uma especialidade com descrição duplicada")
	void insertEspecialidadeDuplicateException() {
		Mockito.when(especialidadeMedicaRepository.existsByDescricao("Cardiologista")).thenReturn(true);
		Especialidade_Medica especialidadeExistente = new Especialidade_Medica();
		especialidadeExistente.setDescricao("Cardiologista");
		assertThrows(DuplicateExecption.class,
				() -> especialidadeMedicaService.insertEspecialidade(especialidadeExistente));
	}

	@Test
	@DisplayName("Deve lançar EmptyField ao tentar inserir uma especialidade com campo descrição vazio")
	void insertEspecialidadeEmptyField() {
		Mockito.when(especialidadeMedicaRepository.existsByDescricao("")).thenReturn(false);
		Especialidade_Medica especialidadeVazia = new Especialidade_Medica();
		especialidadeVazia.setDescricao("");
		assertThrows(EmptyField.class, () -> especialidadeMedicaService.insertEspecialidade(especialidadeVazia));
	}

	@Test
	@DisplayName("Deve atualizar a especialidade médica com sucesso")
	void updateEspecialidadeMedica() {
		Long id = 1L;
		EspecialidadeMedicaDTO objDto = new EspecialidadeMedicaDTO();
		objDto.setDescricao("Nova Descrição");

		Especialidade_Medica obj = new Especialidade_Medica();
		obj.setId(id);
		obj.setDescricao("Descrição Antiga");

		when(especialidadeMedicaRepository.findById(id)).thenReturn(Optional.of(obj));
		when(especialidadeMedicaRepository.existsByDescricao(objDto.getDescricao())).thenReturn(false);
		when(especialidadeMedicaRepository.save(obj)).thenReturn(obj);

		Especialidade_Medica updatedEspecialidade = especialidadeMedicaService.update(objDto, id);
		assertEquals(objDto.getDescricao(), updatedEspecialidade.getDescricao());

		verify(especialidadeMedicaRepository, times(1)).findById(id);
		verify(especialidadeMedicaRepository, times(1)).existsByDescricao(objDto.getDescricao());
		verify(especialidadeMedicaRepository, times(1)).save(obj);
	}

	@Test
	@DisplayName("Deve lançar exceção ao tentar atualizar com descrição vazia")
	void updateEspecialidadeMedicaEmptyDescricao() {
		Long id = 1L;

		when(especialidadeMedicaRepository.findById(id))
				.thenReturn(Optional.of(new Especialidade_Medica(id, "Descrição existente")));

		EspecialidadeMedicaDTO objDto = new EspecialidadeMedicaDTO();
		objDto.setDescricao("");
		assertThrows(EmptyField.class, () -> especialidadeMedicaService.update(objDto, id));
	}

	@Test
	@DisplayName("Deve lançar exceção ao tentar atualizar com descrição já existente")
	void updateEspecialidadeMedicaExistingDescricao() {
		Long id = 1L;
		EspecialidadeMedicaDTO objDto = new EspecialidadeMedicaDTO();
		objDto.setDescricao("Descrição Existente");
		Especialidade_Medica obj = new Especialidade_Medica();
		obj.setId(id);
		obj.setDescricao("Outra Descrição");

		when(especialidadeMedicaRepository.findById(id)).thenReturn(Optional.of(obj));
		when(especialidadeMedicaRepository.existsByDescricao(objDto.getDescricao())).thenReturn(true);

		assertThrows(DuplicateExecption.class, () -> especialidadeMedicaService.update(objDto, id));
	}
    @Test
    public void testUpdateDescricao() {
        long id = 1L;
        String novaDescricao = "Nova Descrição";
        EspecialidadeMedicaDTO objDto = new EspecialidadeMedicaDTO();
        objDto.setId(id);
        objDto.setDescricao(novaDescricao);
        Especialidade_Medica obj = new Especialidade_Medica();
        Mockito.when(especialidadeMedicaRepository.existsByDescricao("Cardiologista")).thenReturn(true);
        obj.setId(id);
        obj.setDescricao("Descrição Antiga");
        when(especialidadeMedicaRepository.findById(id)).thenReturn(Optional.of(obj));
        when(especialidadeMedicaRepository.existsByDescricao(novaDescricao)).thenReturn(false);
        especialidadeMedicaService.updateDescricao(objDto);
        assertEquals(novaDescricao, obj.getDescricao());
    }
    @Test
    public void TestDeleteCase1() {
    	when(especialidadeMedicaRepository.findById(Mockito.anyLong())).thenReturn(optionalEspecialidade);
        doNothing().when(especialidadeMedicaRepository).deleteById(anyLong());
        especialidadeMedicaService.delete(ID);
        verify(especialidadeMedicaRepository,times(1)).deleteById(anyLong());
    }

    @Test
    public void testDeleteEspecialidadeNotFoundException() {
        when(especialidadeMedicaRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EspecialidadeNotFoundException.class, () -> especialidadeMedicaService.delete(ID));
    }
    @Test
    public void testFindEspecialidadeByDescricao() {
        String descricao = "Cardiologia";
        Especialidade_Medica especialidade = new Especialidade_Medica();
        especialidade.setId(1L);
        especialidade.setDescricao(descricao);
        when(especialidadeMedicaRepository.findEspecialidade(descricao)).thenReturn(Optional.of(especialidade));
        Especialidade_Medica encontrada = especialidadeMedicaService.findEspecialidadeByDescricao(descricao);
        assertEquals(especialidade, encontrada);
    }

    @Test
    public void testFindEspecialidadeByDescricaoEspecialidadeNaoEncontrada() {
        String descricao = "Cardiologia";
        when(especialidadeMedicaRepository.findEspecialidade(descricao)).thenReturn(Optional.empty());
        assertThrows(EspecialidadeNotFoundException.class, () -> especialidadeMedicaService.findEspecialidadeByDescricao(descricao));
    }
    
    @Test
	@DisplayName("Deve lançar exceção ao tentar encontrar uma especialidade médica que não existe")
	void findByIdEspecialidadeMedicaIdNotFound() {
		when(especialidadeMedicaRepository.findById(ID)).thenReturn(Optional.empty());
		assertThrows(EspecialidadeNotFoundException.class, () -> especialidadeMedicaService.delete(ID));
	}
    @Test
    public void testFindAllByOrderByDescricaoAsc() {
        List<Especialidade_Medica> especialidades = new ArrayList<>();
        especialidades.add(new Especialidade_Medica(1L, "Especialidade A"));
        especialidades.add(new Especialidade_Medica(2L, "Especialidade B"));
        especialidades.add(new Especialidade_Medica(3L, "Especialidade C"));
        
        // Correção aqui
        when(especialidadeMedicaRepository.findAllByOrderByDescricaoAsc(Mockito.any(Pageable.class)))
        .thenReturn(new PageImpl<>(especialidades));

        Page<Especialidade_Medica> paginaEspecialidades = especialidadeMedicaService
                .findAllByOrderByDescricaoAsc(PageRequest.of(0, Integer.MAX_VALUE));
        
        assertEquals(3, paginaEspecialidades.getTotalElements(), "Deve haver 3 especialidades na página");
        
        List<Especialidade_Medica> especialidadesNaPagina = paginaEspecialidades.getContent();
        assertEquals("Especialidade A", especialidadesNaPagina.get(0).getDescricao(),
                "A primeira especialidade deve ser 'Especialidade A'");
        assertEquals("Especialidade B", especialidadesNaPagina.get(1).getDescricao(),
                "A segunda especialidade deve ser 'Especialidade B'");
        assertEquals("Especialidade C", especialidadesNaPagina.get(2).getDescricao(),
                "A terceira especialidade deve ser 'Especialidade C'");
    }

	private void StartEspecialidade() {
		especialidadeMedica = new Especialidade_Medica(ID, DESCRICAO);
		especialidadeMedicaDTO = new EspecialidadeMedicaDTO(ID, DESCRICAO);
		optionalEspecialidade = Optional.of(new Especialidade_Medica(ID, DESCRICAO));

	}
}
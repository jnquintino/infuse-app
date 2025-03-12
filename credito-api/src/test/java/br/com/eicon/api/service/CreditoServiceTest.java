package br.com.eicon.api.service;

import br.com.eicon.api.model.Credito;
import br.com.eicon.api.repository.CreditoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreditoServiceTest {

    @InjectMocks
    private CreditoService creditoService;

    @Mock
    private CreditoRepository creditoRepository;

    private Credito credito;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        credito = new Credito();
        credito.setNumeroCredito("123456");
        credito.setNumeroNfse("7891011");
        credito.setDataConstituicao(LocalDate.of(2024, 2, 25));
        credito.setValorIssqn(new BigDecimal("1500.75"));
        credito.setTipoCredito("ISSQN");
        credito.setSimplesNacional(true);
        credito.setAliquota(new BigDecimal("5.0"));
        credito.setValorFaturado(new BigDecimal("30000.00"));
        credito.setValorDeducao(new BigDecimal("5000.00"));
        credito.setBaseCalculo(new BigDecimal("25000.00"));
    }

    @Test
    void testBuscarPorNfse_ComResultados() {
        when(creditoRepository.findByNumeroNfse("7891011")).thenReturn(List.of(credito));

        List<Credito> resultado = creditoService.buscarPorNfse("7891011");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("123456", resultado.get(0).getNumeroCredito());

        verify(creditoRepository, times(1)).findByNumeroNfse("7891011");
    }

    @Test
    void testBuscarPorNfse_SemResultados() {
        when(creditoRepository.findByNumeroNfse("999999")).thenReturn(List.of());

        List<Credito> resultado = creditoService.buscarPorNfse("999999");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(creditoRepository, times(1)).findByNumeroNfse("999999");
    }

    @Test
    void testBuscarPorNumeroCredito_ComResultado() {
        when(creditoRepository.findByNumeroCredito("123456")).thenReturn(Optional.of(credito));

        Optional<Credito> resultado = creditoService.buscarPorNumeroCredito("123456");

        assertTrue(resultado.isPresent());
        assertEquals("123456", resultado.get().getNumeroCredito());

        verify(creditoRepository, times(1)).findByNumeroCredito("123456");
    }

    @Test
    void testBuscarPorNumeroCredito_SemResultado() {
        when(creditoRepository.findByNumeroCredito("000000")).thenReturn(Optional.empty());

        Optional<Credito> resultado = creditoService.buscarPorNumeroCredito("000000");

        assertFalse(resultado.isPresent());

        verify(creditoRepository, times(1)).findByNumeroCredito("000000");
    }
}

package br.com.eicon.api.controller;

import br.com.eicon.api.model.Credito;
import br.com.eicon.api.service.CreditoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreditoControllerTest {

    @InjectMocks
    private CreditoController creditoController;

    @Mock
    private CreditoService creditoService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

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
        when(creditoService.buscarPorNfse("7891011")).thenReturn(List.of(credito));

        ResponseEntity<List<Credito>> response = creditoController.buscarPorNfse("7891011");

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().size());
        assertEquals("123456", response.getBody().get(0).getNumeroCredito());

        verify(creditoService, times(1)).buscarPorNfse("7891011");
    }

    @Test
    void testBuscarPorNfse_SemResultados() {
        when(creditoService.buscarPorNfse("999999")).thenReturn(List.of());

        ResponseEntity<List<Credito>> response = creditoController.buscarPorNfse("999999");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(creditoService, times(1)).buscarPorNfse("999999");
    }

    @Test
    void testBuscarPorCredito_ComResultados() {
        when(creditoService.buscarPorNumeroCredito("123456")).thenReturn(Optional.of(credito));

        ResponseEntity<Credito> response = creditoController.buscarPorCredito("123456");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("123456", response.getBody().getNumeroCredito());

        verify(creditoService, times(1)).buscarPorNumeroCredito("123456");
    }

    @Test
    void testBuscarPorCredito_SemResultados() {
        when(creditoService.buscarPorNumeroCredito("000000")).thenReturn(Optional.empty());

        ResponseEntity<Credito> response = creditoController.buscarPorCredito("000000");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(creditoService, times(1)).buscarPorNumeroCredito("000000");
    }

    @Test
    void testBuscarPorNfseComKafka_ComResultados() {
        when(creditoService.buscarPorNfse("7891011")).thenReturn(List.of(credito));

        ResponseEntity<List<Credito>> response = creditoController.buscarPorNfseComKafka("7891011");

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
        assertEquals("123456", response.getBody().get(0).getNumeroCredito());

        verify(creditoService, times(1)).buscarPorNfse("7891011");
        verify(kafkaTemplate, times(1)).send("consulta_credito", "Consulta realizada para NFS-e: 7891011");
    }

    @Test
    void testBuscarPorNfseComKafka_SemResultados() {
        when(creditoService.buscarPorNfse("999999")).thenReturn(List.of());

        ResponseEntity<List<Credito>> response = creditoController.buscarPorNfseComKafka("999999");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(creditoService, times(1)).buscarPorNfse("999999");
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }
}

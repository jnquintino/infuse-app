package br.com.eicon.api.repository;

import br.com.eicon.api.model.Credito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CreditoRepositoryTest {

    @Autowired
    private CreditoRepository creditoRepository;

    private Credito credito1, credito2, credito3;

    @BeforeEach
    void setUp() {
        creditoRepository.deleteAll();
        credito1 = new Credito();
        credito1.setNumeroCredito("123456");
        credito1.setNumeroNfse("7891011");
        credito1.setDataConstituicao(LocalDate.of(2024, 2, 25));
        credito1.setValorIssqn(new BigDecimal("1500.75"));
        credito1.setTipoCredito("ISSQN");
        credito1.setSimplesNacional(true);
        credito1.setAliquota(new BigDecimal("5.0"));
        credito1.setValorFaturado(new BigDecimal("30000.00"));
        credito1.setValorDeducao(new BigDecimal("5000.00"));
        credito1.setBaseCalculo(new BigDecimal("25000.00"));

        creditoRepository.save(credito1);
    }

    @Test
    void testFindByNumeroNfse_ComResultados() {
        List<Credito> creditos = creditoRepository.findByNumeroNfse("7891011");
        assertFalse(creditos.isEmpty());
        assertEquals(1, creditos.size());
        assertEquals("123456", creditos.get(0).getNumeroCredito());
    }

    @Test
    void testFindByNumeroCredito_ComResultado() {
        Optional<Credito> credito = creditoRepository.findByNumeroCredito("123456");
        assertTrue(credito.isPresent());
        assertEquals("123456", credito.get().getNumeroCredito());
    }
}

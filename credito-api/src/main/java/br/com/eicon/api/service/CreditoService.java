package br.com.eicon.api.service;

import br.com.eicon.api.model.Credito;
import br.com.eicon.api.repository.CreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditoService {
    @Autowired
    private CreditoRepository repository;

    public List<Credito> buscarPorNfse(String numeroNfse) {
        return repository.findByNumeroNfse(numeroNfse);
    }

    public Optional<Credito> buscarPorNumeroCredito(String numeroCredito) {
        return repository.findByNumeroCredito(numeroCredito);
    }
}

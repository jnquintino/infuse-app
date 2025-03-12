package br.com.eicon.api.controller;

import br.com.eicon.api.model.Credito;
import br.com.eicon.api.service.CreditoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/creditos")
public class CreditoController {

    @Autowired
    private CreditoService service;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/{numeroNfse}")
    public ResponseEntity<List<Credito>> buscarPorNfse(@PathVariable String numeroNfse) {
        List<Credito> creditos = service.buscarPorNfse(numeroNfse);
        return creditos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(creditos);
    }

    @GetMapping("/credito/{numeroCredito}")
    public ResponseEntity<Credito> buscarPorCredito(@PathVariable String numeroCredito) {
        return service.buscarPorNumeroCredito(numeroCredito)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/kafka/{numeroNfse}")
    public ResponseEntity<List<Credito>> buscarPorNfseComKafka(@PathVariable String numeroNfse) {
        List<Credito> creditos = service.buscarPorNfse(numeroNfse);
        if (!creditos.isEmpty()) {
            kafkaTemplate.send("consulta_credito", "Consulta realizada para NFS-e: " + numeroNfse);
        }
        return creditos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(creditos);
    }
}

package br.edu.ifpb.pweb2.spendwise.service;

import br.edu.ifpb.pweb2.spendwise.model.Correntista;
import br.edu.ifpb.pweb2.spendwise.repository.CorrentistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CorrentistaService {

    private final CorrentistaRepository correntistaRepository;

    public CorrentistaService(CorrentistaRepository correntistaRepository) {
        this.correntistaRepository = correntistaRepository;
    }

    public Correntista salvar(Correntista correntista) {

        if (correntistaRepository.existsByNomeIgnoreCase(correntista.getNome())) {
            throw new IllegalArgumentException("Já existe um correntista com esse nome.");
        }

        return correntistaRepository.save(correntista);
    }

    public Correntista buscarPorId(Long id) {

        var correntista = correntistaRepository.findById(id);

        if (correntista.isEmpty()) {
            throw new IllegalArgumentException("Correntista não encontrado.");
        }

        return correntista.get();
    }

    public List<Correntista> listar() {
        return correntistaRepository.findAll();
    }
}
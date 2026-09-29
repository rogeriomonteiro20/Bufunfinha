package br.edu.ifpb.pweb2.spendwise.service;

import br.edu.ifpb.pweb2.spendwise.model.Transacao;
import br.edu.ifpb.pweb2.spendwise.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;

    public TransacaoService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    public Transacao salvar(Transacao transacao) {
        return transacaoRepository.save(transacao);
    }

    public List<Transacao> listarPorConta(Long contaId) {
        return transacaoRepository.findByContaId(contaId);
    }
    
    public Transacao buscarPorId(Long id) {

        var transacao = transacaoRepository.findById(id);

        if (transacao.isEmpty()) {
            throw new IllegalArgumentException("Transação não encontrada.");
        }

        return transacao.get();
    }
}
package br.edu.ifpb.pweb2.spendwise.service;

import br.edu.ifpb.pweb2.spendwise.model.Comentario;
import br.edu.ifpb.pweb2.spendwise.model.Transacao;
import br.edu.ifpb.pweb2.spendwise.repository.ComentarioRepository;
import br.edu.ifpb.pweb2.spendwise.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final TransacaoRepository transacaoRepository;

    public ComentarioService(ComentarioRepository comentarioRepository, TransacaoRepository transacaoRepository) {
        this.comentarioRepository = comentarioRepository;
        this.transacaoRepository = transacaoRepository;
    }

    // 1. Criar um novo comentário para a transação
    public Comentario salvar(Long transacaoId, String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("O comentário não pode ser vazio.");
        }

        Transacao transacao = transacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada."));

        // Garante que não duplica se já existir um comentário nessa transação
        Optional<Comentario> comentarioExistente = comentarioRepository.findByTransacaoId(transacaoId);
        if (comentarioExistente.isPresent()) {
            throw new IllegalStateException("Esta transação já possui um comentário. Use a edição.");
        }

        Comentario novoComentario = Comentario.builder()
                .texto(texto.trim())
                .transacao(transacao)
                .build();

        return comentarioRepository.save(novoComentario);
    }

    // 2. Editar um comentário existente
    public Comentario atualizar(Long comentarioId, String novoTexto) {
        if (novoTexto == null || novoTexto.isBlank()) {
            throw new IllegalArgumentException("O comentário não pode ser vazio.");
        }

        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new IllegalArgumentException("Comentário não encontrado."));

        comentario.setTexto(novoTexto.trim());

        return comentarioRepository.save(comentario);
    }

    // 3. Deletar comentário por ID
    public void deletar(Long comentarioId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new IllegalArgumentException("Comentário não encontrado."));

        comentarioRepository.delete(comentario);
    }

    // 4. Buscar comentário da transação (para carregar na tela/form)
    public Optional<Comentario> buscarPorTransacao(Long transacaoId) {
        return comentarioRepository.findByTransacaoId(transacaoId);
    }
}
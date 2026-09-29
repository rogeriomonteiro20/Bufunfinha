package br.edu.ifpb.pweb2.spendwise.repository;

import br.edu.ifpb.pweb2.spendwise.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByContaId(Long contaId);
}
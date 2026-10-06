package br.edu.ifpb.pweb2.spendwise.repository;

import br.edu.ifpb.pweb2.spendwise.model.Correntista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorrentistaRepository extends JpaRepository<Correntista, Long> {
        boolean existsByNomeIgnoreCase(String nome);
}
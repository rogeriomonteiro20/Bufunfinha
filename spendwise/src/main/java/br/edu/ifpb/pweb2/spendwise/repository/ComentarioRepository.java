package br.edu.ifpb.pweb2.spendwise.repository;

import br.edu.ifpb.pweb2.spendwise.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    
    Optional<Comentario> findByTransacaoId(Long transacaoId);
}
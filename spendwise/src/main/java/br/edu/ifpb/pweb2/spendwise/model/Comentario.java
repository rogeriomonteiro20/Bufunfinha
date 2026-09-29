package br.edu.ifpb.pweb2.spendwise.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comentarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;

    @ManyToOne
    @JoinColumn(name = "transacao_id")
    private Transacao transacao;
}
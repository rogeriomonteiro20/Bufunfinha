package br.edu.ifpb.pweb2.spendwise.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private boolean ativo;

    private String natureza;

    private int ordem;

    @OneToMany(mappedBy = "categoria")
    @Builder.Default
    private List<Transacao> transacoes = new ArrayList<>();
}
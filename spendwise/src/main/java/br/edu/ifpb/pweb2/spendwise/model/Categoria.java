package br.edu.ifpb.pweb2.spendwise.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotBlank(message = "Nome obrigatório!")
    private String nome;

    @NotNull(message = "Status obrigatório!")
    private boolean ativo;

    @NotBlank (message = "Natureza obrigatória!")
    private String natureza;

    @NotNull (message = "Ordem obrigatória!")
    private int ordem;

    @OneToMany(mappedBy = "categoria")
    @Builder.Default
    private List<Transacao> transacoes = new ArrayList<>();
}
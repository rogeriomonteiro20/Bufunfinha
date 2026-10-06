package br.edu.ifpb.pweb2.spendwise.model;

import br.edu.ifpb.pweb2.spendwise.enums.TipoConta;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private TipoConta tipo;

    private Integer diaFechamento;

    @OneToMany(mappedBy = "conta")
    @Builder.Default
    private List<Transacao> transacoes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "correntista_id")
    private Correntista correntista;
}
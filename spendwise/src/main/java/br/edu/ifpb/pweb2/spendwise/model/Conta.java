package br.edu.ifpb.pweb2.spendwise.model;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pweb2.spendwise.enums.TipoConta;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Pattern (regexp = "\\d{4}-\\d{1}", message = "O número da conta deve estar no formato XXXX-X")
    @NotBlank(message = "O número da conta é obrigatório")
    private String numero;

    @NotBlank (message = "A descrição é obrigatória")
    private String descricao;

    @NotNull(message = "O tipo da conta é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoConta tipo;

    @NotNull(message = "O dia de fechamento é obrigatório")
    private Integer diaFechamento;

    @OneToMany(mappedBy = "conta")
    @Builder.Default
    private List<Transacao> transacoes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "correntista_id")
    private Correntista correntista;
}
package br.edu.ifpb.pweb2.spendwise.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

@Entity
@Table(name = "transacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Valor obrigatório")
    @Positive (message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "Data obrigatória")
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    private LocalDate data;

    @NotEmpty (message = "Movimento obrigatório")
    private String movimento;

    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @Valid 
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToMany(mappedBy = "transacao", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comentario> comentarios = new ArrayList<>();
}
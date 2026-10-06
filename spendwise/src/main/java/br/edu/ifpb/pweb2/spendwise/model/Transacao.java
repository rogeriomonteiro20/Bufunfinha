package br.edu.ifpb.pweb2.spendwise.model;

import jakarta.persistence.*;
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

    private String descricao;

    @NumberFormat (pattern = "#,##0.00")
    private BigDecimal valor;

    @DateTimeFormat (pattern = "yyyy-MM-dd")
    private LocalDate data;

    private String movimento;

    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToMany(mappedBy = "transacao", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comentario> comentarios = new ArrayList<>();
}
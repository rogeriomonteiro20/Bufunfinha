package br.edu.ifpb.pweb2.spendwise.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "correntistas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Correntista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false)
    private String nome;


    @OneToMany(mappedBy = "correntista", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Conta> contas = new ArrayList<>();
}
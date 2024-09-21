package com.klug.loansystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "emprestimo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "usuario_id")
    private Long usuarioId;

    @NotNull
    @Column(name = "livro_id")
    private Long livroId;

    @Column(name = "data_emprestimo")
    private LocalDate dataEmprestimo;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Column(name = "devolvido")
    private boolean devolvido;

    @Column(name = "titulo_livro")
    private String tituloLivro;

    @Column(name = "autor_livro")
    private String autorLivro;

    public Emprestimo(Long usuarioId, Long livroId, LocalDate dataEmprestimo, String tituloLivro, String autorLivro) {
        this.usuarioId = usuarioId;
        this.livroId = livroId;
        this.dataEmprestimo = dataEmprestimo;
        this.devolvido = false;
        this.tituloLivro = tituloLivro;
        this.autorLivro = autorLivro;
    }
}
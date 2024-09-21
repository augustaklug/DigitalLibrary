package com.klug.bookmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "livro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(name = "titulo")
    private String titulo;

    @NotBlank
    @Size(max = 100)
    @Column(name = "autor")
    private String autor;

    @Size(max = 20)
    @Column(name = "isbn")
    private String isbn;

    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    @Size(max = 1000)
    @Column(name = "descricao")
    private String descricao;

    // Constructor without id for creating new books
    public Livro(String titulo, String autor, String isbn, LocalDate dataPublicacao, String descricao) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.dataPublicacao = dataPublicacao;
        this.descricao = descricao;
    }
}
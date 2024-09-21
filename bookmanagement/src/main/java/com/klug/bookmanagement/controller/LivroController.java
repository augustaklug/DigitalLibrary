package com.klug.bookmanagement.controller;

import com.klug.bookmanagement.model.Livro;
import com.klug.bookmanagement.repository.LivroRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private static final Logger logger = LoggerFactory.getLogger(LivroController.class);

    @Autowired
    LivroRepository livroRepository;

    @PostMapping
    public ResponseEntity<Livro> criarLivro(@Valid @RequestBody Livro livro) {
        logger.info("Criando novo livro: {}", livro.getTitulo());
        try {
            Livro _livro = livroRepository.save(new Livro(livro.getTitulo(), livro.getAutor(), livro.getIsbn(),
                    livro.getDataPublicacao(), livro.getDescricao()));
            logger.info("Livro criado com sucesso. ID: {}", _livro.getId());
            return new ResponseEntity<>(_livro, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erro ao criar livro: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Livro>> getAllLivros(@RequestParam(required = false) String titulo) {
        logger.info("Buscando livros. Título: {}", titulo);
        try {
            List<Livro> livros;

            if (titulo == null)
                livros = livroRepository.findAll();
            else
                livros = livroRepository.findByTituloContainingIgnoreCase(titulo);

            if (livros.isEmpty()) {
                logger.info("Nenhum livro encontrado");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            logger.info("Encontrados {} livros", livros.size());
            return new ResponseEntity<>(livros, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erro ao buscar livros: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> getLivroById(@PathVariable("id") long id) {
        var livroData = livroRepository.findById(id);

        return livroData.map(livro -> new ResponseEntity<>(livro, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> updateLivro(@PathVariable("id") long id, @Valid @RequestBody Livro livro) {
        var livroData = livroRepository.findById(id);

        if (livroData.isPresent()) {
            Livro _livro = livroData.get();
            _livro.setTitulo(livro.getTitulo());
            _livro.setAutor(livro.getAutor());
            _livro.setIsbn(livro.getIsbn());
            _livro.setDataPublicacao(livro.getDataPublicacao());
            _livro.setDescricao(livro.getDescricao());
            return new ResponseEntity<>(livroRepository.save(_livro), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteLivro(@PathVariable("id") long id) {
        try {
            livroRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
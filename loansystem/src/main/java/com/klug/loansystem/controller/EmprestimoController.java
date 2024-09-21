package com.klug.loansystem.controller;

import com.klug.loansystem.client.LivroDTO;
import com.klug.loansystem.model.Emprestimo;
import com.klug.loansystem.repository.EmprestimoRepository;
import com.klug.loansystem.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    @Autowired
    EmprestimoRepository emprestimoRepository;

    @Autowired
    BookService bookService;

    @PostMapping
    public ResponseEntity<Emprestimo> realizarEmprestimo(@Valid @RequestBody Emprestimo emprestimo) {
        try {
            // Buscar informações do livro
            LivroDTO livro = bookService.getLivroById(emprestimo.getLivroId());

            // Criar novo empréstimo com informações do livro
            LocalDate dataEmprestimo = LocalDate.now();

            Emprestimo novoEmprestimo = new Emprestimo(
                    emprestimo.getUsuarioId(),
                    emprestimo.getLivroId(),
                    dataEmprestimo,
                    livro.getTitulo(),
                    livro.getAutor()
            );

            Emprestimo _emprestimo = emprestimoRepository.save(novoEmprestimo);
            return new ResponseEntity<>(_emprestimo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/devolucao")
    public ResponseEntity<Emprestimo> registrarDevolucao(@PathVariable("id") long id) {
        var emprestimoData = emprestimoRepository.findById(id);

        if (emprestimoData.isPresent()) {
            Emprestimo _emprestimo = emprestimoData.get();
            _emprestimo.setDevolvido(true);
            _emprestimo.setDataDevolucao(LocalDate.now());
            return new ResponseEntity<>(emprestimoRepository.save(_emprestimo), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Emprestimo>> getAllEmprestimos(@RequestParam(required = false) Boolean devolvido) {
        try {
            List<Emprestimo> emprestimos;

            if (devolvido == null)
                emprestimos = emprestimoRepository.findAll();
            else
                emprestimos = emprestimoRepository.findByDevolvido(devolvido);

            if (emprestimos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(emprestimos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Emprestimo> getEmprestimoById(@PathVariable("id") long id) {
        var emprestimoData = emprestimoRepository.findById(id);

        return emprestimoData.map(emprestimo -> new ResponseEntity<>(emprestimo, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Emprestimo>> getEmprestimosByUsuario(@PathVariable("usuarioId") long usuarioId) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByUsuarioId(usuarioId);

        if (emprestimos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(emprestimos, HttpStatus.OK);
    }

    @GetMapping("/livro/{livroId}")
    public ResponseEntity<List<Emprestimo>> getEmprestimosByLivro(@PathVariable("livroId") long livroId) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByLivroId(livroId);

        if (emprestimos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(emprestimos, HttpStatus.OK);
    }
}
package com.klug.usermanagement.controller;

import java.util.List;
import java.util.Optional;

import brave.spring.rabbit.SpringRabbitTracing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.klug.usermanagement.model.Usuario;
import com.klug.usermanagement.repository.UsuarioRepository;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private Queue userQueue;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpringRabbitTracing springRabbitTracing;

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody Usuario usuario) {
        logger.info("Recebida solicitação para registrar usuário: {}", usuario.getEmail());
        try {
            if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
                logger.warn("Tentativa de registro com e-mail já existente: {}", usuario.getEmail());
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            Usuario novoUsuario = new Usuario(usuario.getNome(), usuario.getEmail(), usuario.getSenha());
            Usuario _usuario = usuarioRepository.save(novoUsuario);

            logger.info("Usuário registrado com sucesso. ID: {}", _usuario.getId());

            // Enviar mensagem para o RabbitMQ com tracing
            String usuarioJson = objectMapper.writeValueAsString(_usuario);
            logger.info("Enviando mensagem para RabbitMQ. Queue: {}", userQueue.getName());

            // Criar um novo RabbitTemplate com tracing
            RabbitTemplate tracedRabbitTemplate = springRabbitTracing.newRabbitTemplate(connectionFactory);
            tracedRabbitTemplate.convertAndSend(userQueue.getName(), usuarioJson);

            logger.info("Mensagem enviada para RabbitMQ com sucesso");

            return new ResponseEntity<>(_usuario, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            logger.error("Erro ao processar JSON do usuário", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarPerfil(@PathVariable("id") long id, @Valid @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioData = usuarioRepository.findById(id);

        if (usuarioData.isPresent()) {
            Usuario _usuario = usuarioData.get();
            _usuario.setNome(usuario.getNome());
            _usuario.setEmail(usuario.getEmail());
            _usuario.setSenha(usuario.getSenha());
            return new ResponseEntity<>(usuarioRepository.save(_usuario), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios(@RequestParam(required = false) String nome) {
        try {
            List<Usuario> usuarios;

            if (nome == null)
                usuarios = usuarioRepository.findAll();
            else
                usuarios = usuarioRepository.findByNomeContainingIgnoreCase(nome);

            if (usuarios.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable("id") long id) {
        Optional<Usuario> usuarioData = usuarioRepository.findById(id);

        return usuarioData.map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUsuario(@PathVariable("id") long id) {
        try {
            usuarioRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
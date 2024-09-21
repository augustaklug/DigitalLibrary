package com.klug.usermanagement.repository;

import java.util.List;
import java.util.Optional;

import com.klug.usermanagement.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    Optional<Usuario> findByEmail(String email);
}

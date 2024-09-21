package com.klug.loansystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_info")
@Data
@NoArgsConstructor
public class UserInfo {

    @Id
    private Long id;

    private String nome;

    private String email;

    public UserInfo(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }
}
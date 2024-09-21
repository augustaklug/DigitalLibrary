package com.klug.loansystem.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bookmanagement", url = "http://localhost:8082")
public interface BookClient {

    @GetMapping("/api/livros/{id}")
    LivroDTO getLivroById(@PathVariable("id") Long id);
}


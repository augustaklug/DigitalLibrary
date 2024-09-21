package com.klug.loansystem.service;

import com.klug.loansystem.client.BookClient;
import com.klug.loansystem.client.LivroDTO;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookClient bookClient;

    public BookService(BookClient bookClient) {
        this.bookClient = bookClient;
    }

    public LivroDTO getLivroById(Long id) {
        return bookClient.getLivroById(id);
    }
}
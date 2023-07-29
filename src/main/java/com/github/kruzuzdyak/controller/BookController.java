package com.github.kruzuzdyak.controller;

import com.github.kruzuzdyak.dto.BookDto;
import com.github.kruzuzdyak.entity.Book;
import com.github.kruzuzdyak.mapper.BookMapper;
import com.github.kruzuzdyak.repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepo repository;
    @Autowired
    private BookMapper mapper;

    @GetMapping("/search")
    @ResponseBody
    public List<BookDto> searchForBooks(@RequestParam String query) {
        List<Book> books = repository.textSearch(query);
        return mapper.toDto(books);
    }
}

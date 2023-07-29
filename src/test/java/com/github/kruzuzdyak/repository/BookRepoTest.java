package com.github.kruzuzdyak.repository;

import com.github.kruzuzdyak.entity.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BookRepoTest {

    @Autowired
    private BookRepo bookRepo;

    @Test
    void test() {
        String query = "1984";

        List<Book> books = bookRepo.textSearch(query);

        Assertions.assertEquals(1, books.size());
    }

}
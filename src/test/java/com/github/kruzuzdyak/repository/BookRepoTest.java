package com.github.kruzuzdyak.repository;

import com.github.kruzuzdyak.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BookRepoTest {

    @Autowired
    private BookRepo bookRepo;

    @DisplayName("Simple case insensitive search")
    @Test
    void simpleSearch() {
        String query = "George orWELL";

        List<Book> books = bookRepo.textSearch(query);

        assertEquals(2, books.size());
    }

    @DisplayName("Ordered search")
    @Test
    void orderedSearch() {
        String queryOrdered = "\"George Orwell\"";
        String queryInverse = "\"Orwell George\"";

        List<Book> result = bookRepo.textSearch(queryOrdered);
        List<Book> resultEmpty = bookRepo.textSearch(queryInverse);

        assertEquals(2, result.size());
        assertTrue(resultEmpty.isEmpty());
    }

    @DisplayName("Search with exclusions")
    @Test
    void searchExclusions() {
        String queryExclude = "George Orwell -1984";

        List<Book> books = bookRepo.textSearch(queryExclude);

        assertEquals(1, books.size());
    }
}
package com.github.kruzuzdyak.repository;

import com.github.kruzuzdyak.entity.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepo extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = "authors")
    @Query("SELECT b FROM Book b WHERE fts(b.textSearch, :query)")
    List<Book> textSearch(@Param("query") String searchQuery);
}

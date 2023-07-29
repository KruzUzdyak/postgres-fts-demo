package com.github.kruzuzdyak.repository;

import com.github.kruzuzdyak.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepo extends JpaRepository<Author, Long> {
}

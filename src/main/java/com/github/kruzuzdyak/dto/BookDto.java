package com.github.kruzuzdyak.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class BookDto {

    private Long id;
    private String title;
    private String publisher;
    private String publishingYear;
    private String review;
    private Set<AuthorDto> authors;
}

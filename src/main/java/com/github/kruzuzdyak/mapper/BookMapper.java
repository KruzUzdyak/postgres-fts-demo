package com.github.kruzuzdyak.mapper;

import com.github.kruzuzdyak.dto.BookDto;
import com.github.kruzuzdyak.entity.Book;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(componentModel = "spring",
        uses = {AuthorMapper.class},
        injectionStrategy = CONSTRUCTOR)
public interface BookMapper {

    BookDto toDto(Book entity);

    List<BookDto> toDto(List<Book> entityList);
}

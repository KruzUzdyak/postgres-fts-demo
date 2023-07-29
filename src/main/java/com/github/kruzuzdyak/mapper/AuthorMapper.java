package com.github.kruzuzdyak.mapper;

import com.github.kruzuzdyak.dto.AuthorDto;
import com.github.kruzuzdyak.entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDto toDto(Author entity);
}

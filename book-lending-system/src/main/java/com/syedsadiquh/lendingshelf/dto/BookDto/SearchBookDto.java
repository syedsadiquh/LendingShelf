package com.syedsadiquh.lendingshelf.dto.BookDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchBookDto {
    private String title;

    private String author;

    private String isbn;

    @JsonProperty("pubYear")
    private Integer publicationYear;
}

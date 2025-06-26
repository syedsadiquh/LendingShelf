package com.syedsadiquh.lendingshelf.dto.BookDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateBookDto {
    private String title;

    private String author;

    private String isbn;

    @JsonProperty("pubYear")
    private Integer publicationYear;

    @Min(0)
    @JsonProperty("quantity")
    private Integer availableQuantity;
}

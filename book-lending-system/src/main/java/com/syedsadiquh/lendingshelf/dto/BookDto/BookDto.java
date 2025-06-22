package com.syedsadiquh.lendingshelf.dto.BookDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookDto {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    private String isbn;

    @JsonProperty("pubYear")
    private int publicationYear;

    @Min(0)
    @JsonProperty("quantity")
    private int availableQuantity = 0;
}

package com.syedsadiquh.lendingshelf.specifications;

import com.syedsadiquh.lendingshelf.dto.BookDto.SearchBookDto;
import com.syedsadiquh.lendingshelf.dto.BookDto.UpdateBookDto;
import com.syedsadiquh.lendingshelf.models.Book;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> matchFields(SearchBookDto dto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), dto.getTitle().toLowerCase() + "%"));
            }
            if (dto.getAuthor() != null && !dto.getAuthor().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), dto.getAuthor().toLowerCase() + "%"));
            }
            if (dto.getIsbn() != null && !dto.getIsbn().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("isbn"), dto.getIsbn()));
            }
            if (dto.getPublicationYear() != null && dto.getPublicationYear() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("publicationYear"), dto.getPublicationYear()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

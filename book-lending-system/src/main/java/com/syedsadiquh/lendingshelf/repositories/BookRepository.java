package com.syedsadiquh.lendingshelf.repositories;

import com.syedsadiquh.lendingshelf.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    Book findBookByIsbn(String isbn);
}

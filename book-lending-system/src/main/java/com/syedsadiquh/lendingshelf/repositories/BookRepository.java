package com.syedsadiquh.lendingshelf.repositories;

import com.syedsadiquh.lendingshelf.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    Book findBookByIsbn(String isbn);
}

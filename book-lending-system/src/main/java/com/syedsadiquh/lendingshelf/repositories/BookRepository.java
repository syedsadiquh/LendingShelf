package com.syedsadiquh.lendingshelf.repositories;

import com.syedsadiquh.lendingshelf.models.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {
    Book getBookByIsbn(String isbn);

    Book getBookById(UUID id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Book b set b.lastUpdatedAt = :updatedAt, b.title = :title, b.author = :author, b.isbn = :isbn, b.publicationYear = :pubYear, b.availableQuantity = :quantity where b.id = :id")
    int updateBookByIsbn(UUID id, LocalDateTime updatedAt, String title, String author, String isbn, int pubYear, int quantity);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Book b set b.availableQuantity = :quantity where b.id = :id")
    int updateBookQuantityById(UUID id, Integer quantity);

}

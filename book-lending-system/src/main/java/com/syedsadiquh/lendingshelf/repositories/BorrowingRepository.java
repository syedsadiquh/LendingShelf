package com.syedsadiquh.lendingshelf.repositories;

import com.syedsadiquh.lendingshelf.models.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, UUID> {

    @Query("select b from Borrowing b where b.user.username = :username and b.book.title = :bookTitle")
    List<Borrowing> getAllByUsernameAndBookTitle(String username, String bookTitle);

    @Query("select b from Borrowing b where b.user.username = :username")
    List<Borrowing> getAllByUsername(String username);

    @Query("select b from Borrowing b where b.book.title = :bookTitle")
    List<Borrowing> getAllByBookTitle(String bookTitle);
}

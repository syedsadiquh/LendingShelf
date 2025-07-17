package com.syedsadiquh.lendingshelf.repositories;

import com.syedsadiquh.lendingshelf.models.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, UUID> {

}

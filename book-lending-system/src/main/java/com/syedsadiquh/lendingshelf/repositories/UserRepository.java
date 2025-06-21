package com.syedsadiquh.lendingshelf.repositories;

import com.syedsadiquh.lendingshelf.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findUsersByUsername(String username);

    User findUsersById(UUID id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User u set u.lastUpdatedAt = :lastUpdated, u.username = :username, u.name = :name, u.email = :email where u.username = :oldUsername ")
    int updateUser(String oldUsername, LocalDateTime lastUpdated, String username, String name, String email);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User u set u.username = :newUsername where u.id = :id ")
    int updateUsername(UUID id, String newUsername);
}

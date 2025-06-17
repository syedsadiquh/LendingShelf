package com.syedsadiquh.lendingshelf.repositories;

import com.syedsadiquh.lendingshelf.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findUsersByusername(String username);

    User findUsersById(UUID id);
}

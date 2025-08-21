package ru.practicum.bankapp.accounts.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.bankapp.accounts.data.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @EntityGraph(value = "user-fetch-accounts")
    Optional<User> findOneByLogin(String login);
}

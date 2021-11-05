package ru.qnocks.oauth2authentication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.qnocks.oauth2authentication.domain.User;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<User, String> {

    Optional<User> findByEmail(String email);
}

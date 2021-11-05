package ru.qnocks.oauth2authentication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.qnocks.oauth2authentication.domain.Message;

import java.util.Optional;

@Repository
public interface MessagesRepository extends CrudRepository<Message, Long> {

    Optional<Message> findFirstByOrderByIdDesc();

    long countByText(String text);
}

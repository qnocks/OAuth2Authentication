package ru.qnocks.oauth2authentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.qnocks.oauth2authentication.domain.User;
import ru.qnocks.oauth2authentication.repository.UsersRepository;
import ru.qnocks.oauth2authentication.security.UserInfo;

import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User save(UserInfo userInfo) {
        Optional<User> userOptional = usersRepository.findByEmail(userInfo.getEmail());

        if (!userOptional.isPresent()) {
            User user = new User();

            user.setId(userInfo.getId());
            user.setEmail(userInfo.getEmail());
            user.setName(userInfo.getName());

            return usersRepository.save(user);
        }

        return userOptional.get();
    }
}

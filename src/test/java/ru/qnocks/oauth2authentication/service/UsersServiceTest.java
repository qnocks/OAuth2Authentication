package ru.qnocks.oauth2authentication.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.qnocks.oauth2authentication.domain.User;
import ru.qnocks.oauth2authentication.repository.UsersRepository;
import ru.qnocks.oauth2authentication.security.UserInfo;
import ru.qnocks.oauth2authentication.security.oauth2.GithubUserInfo;
import ru.qnocks.oauth2authentication.security.oidc.GoogleUserInfo;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;

@SpringBootTest
public class UsersServiceTest {

    private UsersService underTest;

    @MockBean
    private UsersRepository usersRepository;

    @BeforeEach
    void setUp() {
        underTest = new UsersService(usersRepository);
    }

    @Test
    @DisplayName("Should save google user")
    void canSaveGoogleUser() {
        // given
        String id = "3212313123";
        String name = "google-username";
        String email = "email";

        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("sub", id);
        userAttributes.put("name", name);
        userAttributes.put("email", email);
        UserInfo actual = new GoogleUserInfo(userAttributes);

        User expected = new User(id, name, email);

        // when
        underTest.save(actual);

        // then
        verify(usersRepository).save(expected);
    }

    @Test
    @DisplayName("Should save github user")
    void canSaveGithubUser() {
        // given
        long id = 3212313123L;
        String name = "github-username";
        String email = "email";

        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("id", id);
        userAttributes.put("name", name);
        userAttributes.put("email", email);
        UserInfo actual = new GithubUserInfo(userAttributes);

        User expected = new User(String.valueOf(id), name, email);

        // when
        underTest.save(actual);

        // then
        verify(usersRepository).save(expected);
    }
}

package ru.qnocks.oauth2authentication.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import ru.qnocks.oauth2authentication.domain.Message;
import ru.qnocks.oauth2authentication.domain.enums.MessageText;
import ru.qnocks.oauth2authentication.repository.MessagesRepository;
import ru.qnocks.oauth2authentication.service.PrincipalExtractor;

import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MessagesControllerTest {

    private MessagesController underTest;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessagesRepository messagesRepository;

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    private PrincipalExtractor principalExtractor;

    @BeforeEach
    void setUp() {
        underTest = new MessagesController(messagesRepository, principalExtractor);
    }

    @Test
    @DisplayName("Should save message")
    void canSaveMessage() throws Exception {
        // given
        String username = "username";
        OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);

        // when
        when(principalExtractor.extractUser(authentication).get("name")).thenReturn(username);

        mockMvc.perform(post("/messages/{text}", MessageText.BRO.getName())
                .with(SecurityMockMvcRequestPostProcessors.oauth2Login()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/success"));
    }

    @Test
    @DisplayName("Should get message via WebSocket")
    void canGetMessageViaWebSocket() {
        // given
        String text = "test-text";
        String sendBy = "test-username";
        LocalTime time = LocalTime.now();

        Message expected = new Message(text, sendBy, time);

        // when
        underTest.getMessage(expected);

        // then
        var captor = ArgumentCaptor.forClass(Message.class);
        verify(messagesRepository).save(captor.capture());

        Message actual = captor.getValue();
        assertThat(actual.getText()).isEqualTo(expected.getText());
        assertThat(actual.getSendBy()).isEqualTo(expected.getSendBy());
        assertThat(actual.getTime()).isEqualTo(expected.getTime());
        assertThat(actual.getInfo()).isEqualTo(expected.getInfo());
    }
}

package ru.qnocks.oauth2authentication.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import ru.qnocks.oauth2authentication.domain.enums.MessageText;
import ru.qnocks.oauth2authentication.repository.MessagesRepository;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessagesRepository messagesRepository;

    @Test
    @DisplayName("Should return home page with correct messages count and welcoming to authorize")
    void catReturnRootPage() throws Exception {
        // given
        long broCount = 10L;
        long sisCount = 20L;

        // when
        when(messagesRepository.countByText(MessageText.BRO.getName())).thenReturn(broCount);
        when(messagesRepository.countByText(MessageText.SIS.getName())).thenReturn(sisCount);

        // then
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "text/html;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please authenticate")))
                .andExpect(content().string(containsString("Bro: <span>" + broCount + "</span>")))
                .andExpect(content().string(containsString("Sis: <span>" + sisCount + "</span>")));
    }
}

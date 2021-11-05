package ru.qnocks.oauth2authentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PrincipalExtractor {

    private final String TOKEN_SCHEMA = "Bearer ";

    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public PrincipalExtractor(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    public Map<String, Object> extractUser(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());

        String userInfoEndpoint = getUserInfoEndpoint(client);

        if (userInfoEndpoint == null || userInfoEndpoint.isEmpty()) {
            return null;
        }

        return getUserAttributes(client, userInfoEndpoint);
    }

    private String getUserInfoEndpoint(OAuth2AuthorizedClient client) {
        return client.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();
    }

    private Map<String, Object> getUserAttributes(OAuth2AuthorizedClient client, String userInfoEndpoint) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, TOKEN_SCHEMA + client.getAccessToken().getTokenValue());
        ResponseEntity<Map> response = restTemplate
                .exchange(userInfoEndpoint, HttpMethod.GET, new HttpEntity<>("", headers), Map.class);
        return response.getBody();
    }



}

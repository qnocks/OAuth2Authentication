package ru.qnocks.oauth2authentication.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.qnocks.oauth2authentication.security.UserInfo;
import ru.qnocks.oauth2authentication.service.UsersService;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsersService usersService;

    @Autowired
    public CustomOAuth2UserService(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        UserInfo userInfo;

        if (OAuth2Client.GITHUB.getName().equals(registrationId)) {
            userInfo = new GithubUserInfo(oAuth2User.getAttributes());
        } else {
            throw new OAuth2Exception("Invalid registrationId: " + registrationId);
        }

        usersService.save(userInfo);

        return oAuth2User;
    }
}

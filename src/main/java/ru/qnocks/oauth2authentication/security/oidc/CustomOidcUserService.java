package ru.qnocks.oauth2authentication.security.oidc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import ru.qnocks.oauth2authentication.security.UserInfo;
import ru.qnocks.oauth2authentication.service.UsersService;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UsersService usersService;

    @Autowired
    public CustomOidcUserService(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        return processOidcUser(userRequest, oidcUser);
    }

    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        UserInfo userInfo;

        if (OidcClient.GOOGLE.getName().equals(registrationId)) {
            userInfo = new GoogleUserInfo(oidcUser.getAttributes());
        } else {
            throw new OAuth2Exception("Invalid registrationId: " + registrationId);
        }

        usersService.save(userInfo);

        return oidcUser;
    }
}
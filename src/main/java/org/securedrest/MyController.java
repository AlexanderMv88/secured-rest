package org.securedrest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.securedrest.MyController.UserData;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class MyController {

    private final ObjectMapper objectMapper;

    public MyController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public record UserData(String firstName, String secondName, String email) {

    }

    @GetMapping("/")
    public Mono<UserData> get() throws RuntimeException, JsonProcessingException {
        Mono<UserData> map = getUserDataMono();
        return map;
    }

    @GetMapping(path = "/unauthenticated")
    public Mono<UserData> unauthenticatedRequests() {
        Mono<UserData> map = getUserDataMono();
        return map;
    }

    private static Mono<UserData> getUserDataMono() {
        Mono<UserData> map = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .log()
                .map(Authentication::getPrincipal)
                .log()
                .map(principal-> {
                    OidcUserInfo userInfo = ((DefaultOidcUser) principal).getUserInfo();
                    Map claims = userInfo.getClaims();
                    return new UserData(claims.get("given_name").toString(), claims.get("family_name").toString(), claims.get("email").toString());
                });
        return map;
    }

}

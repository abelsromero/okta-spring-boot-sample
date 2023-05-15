package com.example.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * This example controller has endpoints for displaying the user profile info on {code}/{code} and "you have been
     * logged out page" on {code}/post-logout{code}.
     */
    @Controller
    static class ExampleController {

        private final TokenStorage tokenStorage;

        ExampleController(TokenStorage tokenStorage) {
            this.tokenStorage = tokenStorage;
        }

        @GetMapping("/")
        String home() {
            return "home";
        }

        @GetMapping("/profile")
        @PreAuthorize("hasAuthority('SCOPE_profile')")
        ModelAndView userDetails(OAuth2AuthenticationToken authentication) throws IOException {
            final String token = ((OidcUser) authentication.getPrincipal()).getIdToken().getTokenValue();
            tokenStorage.safe(token);

            var attributes = authentication.getPrincipal().getAttributes();
            final var mutableAttributes = new HashMap<>(attributes);

            mutableAttributes.put("token", token);
            return new ModelAndView("userProfile",
                    Collections.singletonMap("details", mutableAttributes));
        }
    }

    @RestController
    static class ExampleRestController {

        @GetMapping("/hello")
        String sayHello(@AuthenticationPrincipal Jwt jwt) {
            return String.format("Hello, %s!", jwt.getSubject());
        }
    }
}

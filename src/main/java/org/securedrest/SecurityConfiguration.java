package org.securedrest;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;


@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrfSpec -> csrfSpec.disable())
                .oauth2Client(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());
                //userInfoEndpoint(); ???


        /*httpSecurity
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
*/
        httpSecurity
                .authorizeExchange((exchangeSpec) ->
                        exchangeSpec
                    .matchers(
                            new PathPatternParserServerWebExchangeMatcher("/unauthenticated"),
                            new PathPatternParserServerWebExchangeMatcher("/oauth2/**"),
                            new PathPatternParserServerWebExchangeMatcher("/login/**")
                    ).permitAll()
                    .anyExchange()
                    .authenticated()
                )
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutUrl("http://localhost:8080/realms/external/protocol/openid-connect/logout?redirect_uri=http://localhost:8081/"));

        return httpSecurity.build();
    }
}

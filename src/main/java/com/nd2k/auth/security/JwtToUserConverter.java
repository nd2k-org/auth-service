package com.nd2k.auth.security;

import com.nd2k.auth.models.domain.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
        User user = new User();
        user.setEmail(jwt.getSubject());
        return new UsernamePasswordAuthenticationToken(user, jwt, Collections.emptyList());
    }
}

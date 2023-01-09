package com.staff.application.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.staff.application.constant.SecurityConstant;
import com.staff.application.model.UserPrincipal;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.staff.application.constant.SecurityConstant.AUTHORITIES;
import static com.staff.application.constant.SecurityConstant.TOKEN_CANNOT_BE_VERIFIED;
import static java.util.Arrays.stream;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret;


    //    generate jwt token
    public String generateJwtToken(UserPrincipal userPrincipal) {
        //        get all claims from user
        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create()
                .withIssuer("GET_MR_NEVER5_LLC")
                .withAudience("GET_MR_NEVER5_ADMINISTRATION")
                .withIssuedAt(new Date())
                .withSubject(userPrincipal.getUsername())
                .withArrayClaim(AUTHORITIES, claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }


    private String[] getClaimsFromUser(UserPrincipal user) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
            authorities.add(grantedAuthority.getAuthority());
        }

        return authorities.toArray(new String[0]);
    }


    //    get authorities from the token
    public List<GrantedAuthority> getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token)
                .getClaim(AUTHORITIES)
                .asArray(String.class);
    }


    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {

            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm)
                    .withIssuer("GET_MR_NEVER5_LLC")
                    .build();

        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }

        return verifier;
    }


    //    get authentication(authenticate the user)
    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities
        );

        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return usernamePasswordAuthenticationToken;

    }


    //    isTokenExpired
    public boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expirationDate = verifier.verify(token).getExpiresAt();
        return expirationDate.before(new Date());
    }


    //    check if the token is valid
    public boolean isTokenValid(String username, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
    }


    //    get subject
    public String getSubject(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }



}

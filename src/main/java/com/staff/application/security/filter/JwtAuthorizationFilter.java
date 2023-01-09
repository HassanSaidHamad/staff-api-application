package com.staff.application.security.filter;

import com.staff.application.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.staff.application.constant.SecurityConstant.OPTIONS_HTTP_METHOD;
import static com.staff.application.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)) {
            response.setStatus(HttpStatus.OK.value());
        } else {

            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authorizationHeader.substring(TOKEN_PREFIX.length());
            String username = jwtTokenProvider.getSubject(token);
            if (jwtTokenProvider.isTokenValid(username, token) && SecurityContextHolder.getContext().getAuthentication() == null) {

                List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
                Authentication authentication = jwtTokenProvider.getAuthentication(username, authorities, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
                SecurityContextHolder.clearContext();
            }

        }

        filterChain.doFilter(request, response);


    }


    //    yake
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        final String authenticationHeader = request.getHeader("Authorization");
//        final String jwtToken;
//        final String userEmail;
//
//        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        jwtToken = authenticationHeader.substring(7);
////         TODO: extract the userEmail from JWT token {jwtService class};
//        userEmail = jwtTokenProvider.getSubject(jwtToken);
//        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
//            if (jwtTokenProvider.isTokenValid(String.valueOf(userDetails), jwtToken)) {
//
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        userDetails.getAuthorities()
//                );
//                authenticationToken.setDetails(
//                        new WebAuthenticationDetailsSource().buildDetails(request)
//                );
////                update SecurityContextHolder
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//
//    }

}

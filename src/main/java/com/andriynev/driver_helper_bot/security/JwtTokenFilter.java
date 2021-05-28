package com.andriynev.driver_helper_bot.security;

import com.andriynev.driver_helper_bot.dao.ModeratorRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final ModeratorRepository moderatorRepository;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil,
                          ModeratorRepository moderatorRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.moderatorRepository = moderatorRepository;
    }

    private boolean isEmpty(String header) {
        return header == null || header.isEmpty();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!jwtTokenUtil.validateToken(token)) {
            chain.doFilter(request, response);
            return;
        }

        // Get user identity
        UserDetails userDetails = moderatorRepository
                .findByUsername(jwtTokenUtil.getUsernameFromToken(token))
                .orElse(null);

        // deny not exist and disabled users
        if (userDetails == null) {
            chain.doFilter(request, response);
            return;
        }

        if (!userDetails.isEnabled()) {
            throw new ServletException("Access Denied", new AccessDeniedException("User is disabled"));
        }

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails.getAuthorities()
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // set user identity on the spring security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

}

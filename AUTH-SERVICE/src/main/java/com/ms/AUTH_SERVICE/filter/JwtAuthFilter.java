package com.ms.AUTH_SERVICE.filter;

import com.ms.AUTH_SERVICE.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

       final String authHeader = request.getHeader("Authorization"); // extract header

       // if the header is null or does not start with Bearer
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        String email;

        try{
            email = jwtUtil.extractEmail(token); // extracting email from token
        }catch(Exception e){
            filterChain.doFilter(request, response); // invalid token
            return;

        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // You don't need DB lookup if you trust the token — create UserDetails manually
            List<SimpleGrantedAuthority> authorities = jwtUtil.extractRoles(token).stream()
                    .map(SimpleGrantedAuthority::new) // Role should be "ROLE_ADMIN", "ROLE_RESTAURANT" etc.
                    .toList();
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);

    }
}

package com.blog.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor

public class JwtAuthFilter extends GenericFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService uds;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var req = (HttpServletRequest) request;
        var header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            var token = header.substring(7);
            try{
                var username = jwtUtil.getUsername(token);
                var userDetails = uds.loadUserByUsername(username);
                var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }catch(Exception ignored){}
        }
        chain.doFilter(request, response);
    }
}

package dev.luchonetvv.userapi.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

public class JwtTokenFilter extends GenericFilterBean {
    private static final String BEARER_PREFIX = "Bearer ";

    private final UserDetailsServiceInt userDetailsServiceInt;

    public JwtTokenFilter(UserDetailsServiceInt userDetailsServiceInt) {
        this.userDetailsServiceInt = userDetailsServiceInt;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String headerValue = ((HttpServletRequest) request).getHeader("Authorization");
        //Pull the Username and Roles from the JWT to construct the user details
        getBearerToken(headerValue).flatMap(userDetailsServiceInt::loadUserByJwtToken).ifPresent(userDetails -> {
            //Add the user details (Permissions) to the Context for just this API invocation
            SecurityContextHolder.getContext().setAuthentication(
                    new PreAuthenticatedAuthenticationToken(userDetails, "", userDetails.getAuthorities()));
        });

        //move on to the next filter in the chains
        chain.doFilter(request, response);
    }

    private Optional<String> getBearerToken(String headerVal) {
        if (headerVal == null) return Optional.empty();

        if (headerVal.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
            return Optional.of(headerVal.substring(BEARER_PREFIX.length()).trim());
        }
        return Optional.empty();
    }
    
}

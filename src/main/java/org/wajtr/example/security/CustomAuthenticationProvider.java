package org.wajtr.example.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

// We still extend AbstractUserDetailsAuthenticationProvider to benefit most of the functionality of user and password checking
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private UserDetailsService userDetailsService;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // just some basic checking first
        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        String presentedPassword = authentication.getCredentials().toString();
        String userName = userDetails.getUsername();

        // !!!!! do your password checking logic here.!!!!
        // You can do whatever you wish, the only requirement is that it's a synchronous (blocking) call. Do not offload to some other thread or something like that
        boolean passwordIsCorrect = presentedPassword.equals(userName); // just a dummy check

        // Terminate the request processing flow by throwing an exception - this will ultimately result to HTTP 401 response
        if (!passwordIsCorrect) {
            this.logger.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }

        // If we reached here, the authentication was successful
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // You can basically use any method to load the user details, the UserDetailsService is kind of standard Spring
        // Security way of doing it. You can also use a custom DAO or whatever you want.

        // Note that the additionalAuthenticationChecks method is called AFTER this method, but it still happens in the
        // same request processing. That means, that the additionalAuthenticationChecks is kind of optional. You can
        // also throw the BadCredentialsException here and it'll have the same effect: the login will be denied.

        // The code in this class is inspired by how the DaoAuthenticationProvider works.

        UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }
}

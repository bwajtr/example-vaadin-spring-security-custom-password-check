# How to implement custom username and password validation using Spring Security and Vaadin

This project can be used as a starting point to create your own Vaadin application with Spring Boot.
It contains all the necessary configuration and some placeholder files to get you started.

# What, where, why?

I used https://start.vaadin.com to create a base for this project. I configured two views that are accessible 
only when the user logs in. This is the [link to the project](https://start.vaadin.com/app/p?id=d44c0733-7335-4e7a-bceb-287ebd20e863&preview=)
That added Spring Security to the project (see `SecurityConfiguration` class) and did the necessary configuration
by extending the `VaadinWebSecurity`. That makes things much easier.

This default configuration uses the `UserDetailsServiceImpl` to load the user details. It is expected that these
details contain also the hashed password. This hashed password is then compared to the password that the user
enters in the login form using the `PasswordEncoder`, which is configured in the `SecurityConfiguration` class.

If, for any reason, you do not like this flow, or you simply need to validate the password using another way,
you have to provide your own implementation of the `AuthenticationProvider`.

This projects shows how to do that. Please take a look at the second commit in this project. It adds
`CustomAuthenticationProvider` and shows how to use it in the `SecurityConfiguration` class.

Also take a look at all the comments in the `CustomAuthenticationProvider` class.

# Useful stuff for debugging

* Place a breakpoint in the `org.springframework.security.authentication.ProviderManager.authenticate` method and see what is going on in there when you press Login button in the login form
* By default, the `DaoAuthenticationProvider` is used - you can take a heavy inspiration from it
* Spring Security uses a set of filters to perform the whole authentication process. Take a look what filters and in which order they are executed by searching for `DefaultSecurityFilterChain` in the application log. The log line starts with `Will securty any request with`

## Running the application

The project is a standard Maven project. To run it from the command line,
type `mvnw` (Windows), or `./mvnw` (Mac & Linux), then open
http://localhost:8080 in your browser.

You can also import the project to your IDE of choice as you would with any
Maven project. Read more on [how to import Vaadin projects to different IDEs](https://vaadin.com/docs/latest/guide/step-by-step/importing) (Eclipse, IntelliJ IDEA, NetBeans, and VS Code).

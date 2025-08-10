# Authentication with Spring Boot

We need 4 main pieces

1. `SecurityConfig` (configures which routes are authenticated)
2. `JwtAuthenticationFilter` (http interceptor)
3. `JwtService` (token creation and validation)
4. `AuthController` (/login, returns a token if credentials match)

### Flow when visiting authenticated routes

Note: In `SecurityConfig`, we set `JwtAuthenticationFilter` to be the first in the filter chain. This will become important later

- We check Authorization headers with each request (`JwtAuthenticationFilter`)
- If there is no header or no token, just pass along the request and response objects to the next filter and return
- Otherwise, extract the token (when user logged successfully (AuthController) and received a token)
- We check the token for validity via the `JwtService::validateToken` method
- If the token is invalid pass along the request and response objects and return
- Finally, if the token was valid we set the user identity in the `SecurityContextHolder`
- Pass along the request and response objects as usual
---
- Next in the filter chain is `AnonymousAuthenticationFilter` (set by Spring Boot). This filter creates an `AnonymousAuthenticationToken` and stores it in `SecurityContextHolder`. This means every request has some Authentication object, but an anonymous one if the user is not logged in.
- Then comes the `FilterSecurityInterceptor`. It compares the `Authentication` object from the `SecurityContextHolder` and the rules in `authorizeHttpRequests`:
    * If route is `.authenticated()` and user is anonymous - rejects with 401
    * If route is `.permitAll()` - allow
    * If route is `.authenticated()` and user from JWT - allow


### Flow during login

- We hit the unauthenticated route `POST /login`
- The controller delegates to `authenticationManager.authenticate(email, password)`
- `AuthenticationManager` calls `DaoAuthenticationProvider`
- `DaoAuthenticationProvider` calls `userDetailsService.loadUserByUsername(email)`
- `UserService` runs `findByEmail(...)` and returns a `UserDetails` object. Note: `UserService` implements `UserDetailsService` and overrides `loadUserByUsername`. This way we can use email, username, phone, id, etc, whatever unique identifier to find the user.
- Password check happens via `BCryptPasswordEncoder.matches(...)`
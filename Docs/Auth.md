# JWT authentication with Spring Boot and Web clients

## The backend

We need 4 main pieces

1. `SecurityConfig` (configures which routes are authenticated, or combines rules from feature configurations)
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


## Working with web clients

Before we dive into code let's have a general overview of the process, from a web client's perspective.

When the web app hits the `/auth/login` endpoint, the backend generates 2 tokens - an access token and a refresh token. The access token is used to authenticate the user when hitting protected endpoints by attaching it to each request as a request header. The backend decodes the token and decides whether the user is authenticated or not.

The authentication token is short-lived, meaning it will expire soon after it has been issued. We do this for security reasons. If a token leaks somehow, this will minimize the damage, because it will expire in few minutes.

This is where the refresh token comes into play. At some point, the access token eventually expires and the next authenticated request will be denied with http error 401. Because we don't want users to log in every 5 minutes we send them a refresh token too, via a cookie.

> **Security note:**
This cookie MUST be `http only`, which makes it unreachable from JavaScript code. This is extremely important, because if the refresh token leaks, an attacker can forge as many access token as they want.

The `http only` cookies are handled by the browser only. The client doesn't need to do anything special to send them to the backend, they are attached to the request automatically.

> **Security note:** You must also set the `secure` attribute. This will prevent cookies from being observed by unauthorized parties due to the transmission of the cookie in clear text. Browsers will only send cookies with the `secure` attribute when the request is going to an HTTPS page.

Now, as soon as an authenticated endpoints responds with 401, our client sends `/auth/refresh` request and the cookie with the refresh token goes with it. In turns, the backend checks the validity of the refresh token and if it is valid it creates a new access token and responds to the client with it. The client receives the new access token and sets it **in-memory** for future requests, then retries the previous request with the new access token. This is how we implement a long "user session" with short-lived access token.

> **Security note:** It's tempting to store the access token in a local storage, and some tutorials even suggest it. What they are trying to avoid is logging out the user when they refresh the page. You should never store any security critical information in local storage, because it can be reached via JavaScript and thus makes it vulnerable to XSS attacks. That is why we store the access token in-memory

When the user refreshes the page the in-memory access token is lost, but that's not a problem in our case. The first authenticated request that gets rejected with 401 will trigger the refresh logic, the client will receive the new access token, attach it to this and any future requests until the access token expires again and everything repeats.

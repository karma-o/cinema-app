package cinema.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/register").permitAll()
                //common rights
                .antMatchers(HttpMethod.GET,
                        "/cinema-halls/**", "/movies/**", "movie-sessions/available/**")
                    .hasAnyRole("ADMIN", "USER")
                //user rights
                .antMatchers(HttpMethod.GET, "/orders/**", "/shopping-carts/by-user/**")
                    .hasRole("USER")
                .antMatchers(HttpMethod.POST, "/orders/complete/**").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/shopping-carts/movie-sessions/**").hasRole("USER")
                //admin rights
                .antMatchers(HttpMethod.GET, "/users/by-email/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,
                        "/cinema-halls/**", "/movies/**", "/movie-sessions/**")
                    .hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/movie-sessions/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/movie-sessions/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }
}

package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // please use this method to be able to access the h2- database
    // Spring security blocks /h2-console (or the path you configured in your application.yaml) path for H2 database. - It throws 403 forbidden
    // https://stackoverflow.com/questions/43794721/spring-boot-h2-console-throws-403-with-spring-security-1-5-2?fbclid=IwAR1welgT-WYX-QPbQOUa1XNjtuBhQjU9HtEkPO9AVSgG3TBCUmkPbV1-nFo
    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll().anyRequest().authenticated();
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.formLogin();
        http.httpBasic();
    }*/
    // this method should be uncommented for In-memory user store configuration, JBDC-based user store configuration & LDAP-backed user store configuration
    // for Customizing user authentication we have another configure(HttpSecurity) method


    // ■■■ In-memory user store configuration
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("buzz").password("{noop}infinity").authorities("ROLE_USER")
                .and()
                .withUser("woody").password("{noop}bullseye").authorities("ROLE_USER");
    }*/
    // when launch the application -> we get java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"
    // Solution: - Prior to Spring Security 5.0 the default PasswordEncoder was NoOpPasswordEncoder which required plain text passwords.
    //              In Spring Security 5, the default is DelegatingPasswordEncoder, which required Password Storage Format.
    //              Add password storage format, for plain text, add {noop}
    // https://mkyong.com/spring-boot/spring-security-there-is-no-passwordencoder-mapped-for-the-id-null/


    // ■■■ JBDC-based user store configuration

    // ■ using default user detail queries
    /*@Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication().dataSource(dataSource);
    }*/
    // Spring Security uses the following queries when we try to login:
    // - public static final String DEF_USERS_BY_USERNAME_QUERY = " select username, password, enabled from Users where username=?";
    // - public static final String DEF_USERS_BY_USERNAME_QUERY = " select username, authority from Authorities where username=?";
    // - public static final String DEF_USERS_BY_USERNAME_QUERY = " select g.id, g.group_name, ga.authority from Groups g, Group_Members gm, Group_Authorities" +
    //                                                              " where username=? and g.id = ga.group_id and g.id = gm.group_id";
    // using this configuration - it expects the above tables to exist in the database
    //      - to define the tables use schema.sql file
    //      - to populate the tables in order to use those values for login use data.sql


    // ■ using overridden user detail queries
    // suppose we have our own tables: Users, UserAuthorities, UserGroups, UserGroup_Members and UserGroup_Authorities
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from Users where username=?")
                .authoritiesByUsernameQuery("select username, authority from UserAuthorities where username=?")
                .groupAuthoritiesByUsername("select g.id, g.group_name, ga.authority from UserGroups g, UserGroup_Members gm, UserGroup_Authorities" +
                        " where username=? and g.id = ga.group_id and g.id = gm.group_id");
    }*/


    // ■ using encoded passwords
    // user passwords are stored in the database - they are stored in plain text => Solution: encoding them
    // but if we encode the passwords in the database, authentication will fail because it won't match the plaintext password submitted by the user
    // Solution: - we need to specify a password encoder by calling passwordEncoder() method

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(new StandardPasswordEncoder("53cr3t"));
    }*/

    // passwordEncoder() accepts any implementation of Spring Security's PasswordEncoder interface
    // Spring Security's cryptography includes implementations like:
    // - BCryptPasswordEncoder  — Applies bcrypt strong hashing encryption
    // - NoOpPasswordEncoder    — Applies no encoding
    // - Pbkdf2PasswordEncoder  — Applies PBKDF2 encryption
    // - SCryptPasswordEncoder  — Applies scrypt hashing encryption
    // - StandardPasswordEncoder — Applies SHA-256 hashing encryption
    // - you can also use your own implementation if none of the above doesn't meet your needs

    // the password in the database is encoded
    // the password that the user enters at login is encoded using the same algorithm, and it’s then compared with the encoded password in the database
    // that comparison is performed in the Password-Encoder’s matches() method

    // In order to test this we need to do the following steps:
    // - we need the encoded values of the 2 passwords and can be obtained like this:
    // print (new StandardPasswordEncoder("53cr3t").encode("password"));

    /*public static void main(String[] args) {
        System.out.println( new StandardPasswordEncoder("53cr3t").encode("Patricitas"));
        System.out.println( new StandardPasswordEncoder("53cr3t").encode("Siminitas"));
    }*/

    // - next step is to change the data.sql with the encoded passwords:
    // insert into Users (username, password, enabled) values ('Simi', 'b3aa4cf93a363e4e4681574e5e70cce54096a923f4659a2c0fa301688353648a5a519412bf8c69d1', true);
    // insert into Users (username, password, enabled) values ('Patri', 'f9b4f5ba72abd081293080b716d3de23d75691233801c314f706c49c81d174907b78b86431eaf06b', true);
    // b3aa4cf93a363e4e4681574e5e70cce54096a923f4659a2c0fa301688353648a5a519412bf8c69d1 is the encoded value for Siminitas

    // and that's it - you can now login with your user and password


    // ■■■ LDAP-backed user store configuration

    /*@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .userSearchBase("ou=people")
                .userSearchFilter("(uid={0}")
                .groupSearchBase("ou=groups")
                .groupSearchFilter("member={0}")
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("passcode");
    }*/
    // LDAP = Lightweight Directory Access Protocol
    // userSearchFilter() and groupSearchFilter() - are used to provide filters for the base LDAP queries - which are used to search for users and groups
    // by default - base queries for both users and groups are empty -> this indicates that the search will be done from the root of the LDAP hierarchy
    // userSearchBase() & groupSearchBase() - provide a base query for finding users and groups (users are searched for where the organizational unit is "people"
    //                                          & groups are searched for where the organizational unit is "groups")
    // - default strategy for authenticating against LDAP is: bind operation (authenticating the user directly to the LDAP server)
    // another strategy for authenticating against LDAP is: comparison operation (sending the entered password to the LDAP directory and asking the server to compare the password against a user's password attribute)
    // the comparison is done with the LDAP server -> the password remains secret
    // passwordCompare() is used for authenticate by doing a password comparison
    // passwordAttribute() - if the password is kept in a different attribute
    // passwordEncoder() - password encoder with an encryption strategy

    // ■ Referring to a remote LDAP server:
    // by default Spring Security's LDAP authentication assumes that the LDAP server is listening on port 33389 on localhost
    // if your LDAP server is on another machine - > you use contextSource() to configure the location:
    /*public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .userSearchBase("ou=people")
                .userSearchFilter("(uid={0}")
                .groupSearchBase("ou=groups")
                .groupSearchFilter("member={0}")
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("passcode")
                .and()
                .contextSource().url("ldap://tacocloud.com:389/dc=tacocloud,dc=com");
    }*/

    // ■ Configuring an embedded LDAP server:
    /*public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .userSearchBase("ou=people")
                .userSearchFilter("(uid={0})")
                .groupSearchBase("ou=groups")
                .groupSearchFilter("member={0}")
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("passcode")
                .and()
                .contextSource().root("dc=tacocloud,dc=com").ldif("classpath:users.ldif");
    }*/
    // using the above method and users.ldif from book it WON'T WORK - there is something wrong with schema

    // https://medium.com/@renquanbo7453/spring-security-with-ldap-based-user-store-6ac947c48f7c
    /*public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .userSearchFilter("(uid={0})")
                .contextSource()
                .url("ldap://localhost:8389/dc=breadcrumbdata,dc=com");
    }*/
    // if you don't have an LDAP server - Spring Security can provide an embedded LDAP server for you
    // instead of setting the URL to a remote LDAP server - you can specify the root suffix for the embedded server via root() method
    // When the LDAP server starts - it will load data from any LDIF files that can find in the classpath
    //      (LDIF = LDAP Data Interchange Format - is a standard way of representing LDAP data in a plain text file)
    // ldif() - loads the files, if you don't want that Spring searches through your classpath looking for any LDIIF file


    // ■■■ Customizing user authentication:
    // we use: User entity: User.java, user repository interface: UserRepository.java, custom UserDetailsService implementation: UserRepositoryUserDetailsService.java
    // now we need to configure our custom user details service with Spring Security
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder() {
//        return new StandardPasswordEncoder("53cr3t");
        return NoOpPasswordEncoder.getInstance();
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }
    // passwordEncoder(encoder())   = because encoder() method is annotated with @Bean -> it will be used to declare a PasswordEncoder bean in the Spring application context
    //                              - any calls to encoder() will be intercepted to return the bean instance from the application context
    // Now we have a custom user details service that reads user information via a JPA repository
    // We need a way to get users from the database => we need to create a registration page for Taco Cloud patrons to register with the application: RegistrationController.java

    // the application has complete user registration & authentication support
    // But if you try to start the application -> you can't get to the registration page without login first => this should be changed
    // homepage, login page & registration page - should be available to unauthenticated users
    // user should be authenticated before designing tacos or placing order

    // to configure these security rules we will use the method: configure(HttpSecurity)
    // configure() method accepts an HttpSecurity object - which can be used to configure how security is handled at the web level

    // ■ Securing requests:
    // - we need to be sure that requests for '/design' & '/orders' are available ony for authenticated users
    // - all other requests should be permitted for all users

    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/design", "/orders")
                .hasRole("ROLE_USER")
                .antMatchers("/", "/**")
                .permitAll();
    }*/
    // authorizeRequests() returns an ExpressionInterceptUrlRegistry object - on which we can specify URL paths & patterns & the security requirements for those paths:
    // - rule 1: requests for /design & /order - should be for users with a granted authority of ROLE_USER
    // - rule 2: all requests should be permitted to all users
    // the order of the rules is important -> rule 1 take precedence over rule 2 - if we switch them - the second rule would have no effect
    // hasRole() & permitAll() = methods for declaring security requirements for request paths
    // hasRole() =
    // permitAll() = allows access unconditionally

    // ================================== we change it to the next method ===========================
    // we refactor de configure() method to use Spring expressions -> it's more flexible
    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/design", "/orders")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/", "/**")
                .access("permitAll")

                .and()
                .formLogin()
                .loginPage("/login")

                // next configurations are optional
                .defaultSuccessUrl("/design")
                *//*.defaultSuccessUrl("/design", true)*//*

                // next configurations are optional
                *//*.loginProcessingUrl("/authenticate")
                .usernameParameter("user")
                .passwordParameter("pass")*//*

                // Make H2-Console non-secured - for debug purposes
                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**")

                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .logout()
                *//*.logoutSuccessUrl("/")*//*
        ;
    }*/
    // and() - signifies that you're finish with some configuration & are ready to apply some additional HTTP configuration
    // formLogin() - used to replace the built-in login page with a custom login page
    // loginPage("/login") - used to choose the path where login will be
    // When Spring Security determines that the user is unauthenticated & needs to login => it will redirect it to this path
    // - optional configurations:
    // loginProcessingUrl("/authenticate") = we specify that Spring Security should listen for requests to '/authenticate' to handle login submissions
    // Spring Security listens for login requests at /login & expects that the username & password fields be named: "username" & "password"
    // usernameParameter("user") = here we change username to be named "user"
    // passwordParameter("pass") = here we change password to be named "pass"
    // By default a successful login will take you to the page that you were navigating to when Spring Security determined that you need to login
    // defaultSuccessUrl("/design") = specifies to which page should bring you after login
    // defaultSuccessUrl("/design", true) = you force the user to go to that page even if he was navigating elsewhere

    // logout() - enables logout
    // When the user clicks the logout button -> their session will be cleared & they will be logged out of the application
    // By default when a user clicks the logout button -> they'll be redirected to the login page
    // logoutSuccessUrl("/") = used if you want to be redirected to a different page


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll() // needed for Angular/CORS
                .antMatchers("/design", "/orders/**")
                .permitAll()
                //.access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.PATCH, "/ingredients").permitAll()
                .antMatchers("/**").access("permitAll")

                .and()
                .formLogin()
                .loginPage("/login")

                .and()
                .httpBasic()
                .realmName("Taco Cloud")

                .and()
                .logout()
                .logoutSuccessUrl("/")

                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**", "/ingredients/**", "/design", "/orders/**")

                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
        ;
    }
}

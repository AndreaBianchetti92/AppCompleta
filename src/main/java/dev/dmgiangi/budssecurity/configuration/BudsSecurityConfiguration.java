package dev.dmgiangi.budssecurity.configuration;

import dev.dmgiangi.budssecurity.authentication.AuthenticationManager;
import dev.dmgiangi.budssecurity.authentication.listeners.AuthenticationEventListener;
import dev.dmgiangi.budssecurity.authentication.service.AuthenticationService;
import dev.dmgiangi.budssecurity.handlerChain.AuthenticationHandler;
import dev.dmgiangi.budssecurity.handlerChain.AuthenticationHeaderWriterHandler;
import dev.dmgiangi.budssecurity.handlerChain.AuthorizationHandler;
import dev.dmgiangi.budssecurity.handlerChain.IsAuthenticationRequiredHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * This Configuration is the main Configuration on BudsSecurity
 * It must be imported in order to use BudsSecurity
 *
 * @author Gianluigi De Marco
 * @version 0.1
 * @since 28 09 2022
 */
@Configuration
@EnableWebMvc
@ComponentScan("dev.dmgiangi.budssecurity")
public class BudsSecurityConfiguration implements WebMvcConfigurer {
    // TODO: 28/09/22 implements this part
    private final List<AuthenticationEventListener> authenticationEventListeners = new ArrayList<>();
    @Autowired
    private List<AuthenticationService> authenticationServices;

    /**
     * <p>authenticationManager.</p>
     *
     * @return a {@link dev.dmgiangi.budssecurity.authentication.AuthenticationManager} object
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new AuthenticationManager(
                authenticationServices,
                authenticationEventListeners
        );
    }

    /**
     * <p>authenticationHandler.</p>
     *
     * @return a {@link dev.dmgiangi.budssecurity.handlerChain.AuthenticationHandler} object
     */
    @Bean
    public AuthenticationHandler authenticationHandler() {
        return new AuthenticationHandler(
                authenticationManager()
        );
    }

    /**
     * <p>authenticationHeaderWriterHandler.</p>
     *
     * @return a {@link dev.dmgiangi.budssecurity.handlerChain.AuthenticationHeaderWriterHandler} object
     */
    @Bean
    public AuthenticationHeaderWriterHandler authenticationHeaderWriterHandler() {
        return new AuthenticationHeaderWriterHandler();
    }

    /**
     * <p>authorizationHandler.</p>
     *
     * @return a {@link dev.dmgiangi.budssecurity.handlerChain.AuthorizationHandler} object
     */
    @Bean
    public AuthorizationHandler authorizationHandler() {
        return new AuthorizationHandler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IsAuthenticationRequiredHandler());
        registry.addInterceptor(authenticationHandler());
        registry.addInterceptor(authenticationHeaderWriterHandler());
        registry.addInterceptor(authorizationHandler());
    }
}

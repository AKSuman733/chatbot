package chatbot;

import chatbot.cache.WolframRepository;
import chatbot.lib.api.SPARQL;
import chatbot.rivescript.RiveScriptBot;
import codeanticode.eliza.ElizaMain;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.send.MessengerSendClient;
import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by ramgathreya on 5/10/17.
 * http://bits-and-kites.blogspot.com/2015/03/spring-and-nodejs.html
 */
@EnableCaching
//@SpringBootApplication(scanBasePackages = {"me.ramswaroop.jbot", "chatbot"})
@SpringBootApplication
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Bean
    public MessengerSendClient initializeFBMessengerSendClient(@Value("${chatbot.fb.pageAccessToken}") String pageAccessToken) {
        logger.info("Initializing MessengerSendClient - pageAccessToken: {}", pageAccessToken);
        return MessengerPlatform.newSendClientBuilder(pageAccessToken).build();
    }

    @Configuration
    static class AssetsConfiguration extends WebMvcConfigurerAdapter {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/assets/**").addResourceLocations("file:node_modules/");
            registry.addResourceHandler("/js/**").addResourceLocations("file:src/main/app/js/");
            registry.addResourceHandler("/css/**").addResourceLocations("file:src/main/app/css/");
            super.addResourceHandlers(registry);
        }
    }

    @Configuration
    @EnableWebSecurity
    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/", "/assets/**/*", "/js/*", "/images/**/*", "/feedback", "/webhook", "/fbwebhook", "/slackwebhook").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .defaultSuccessUrl("/admin")
                    .loginPage("/login")
                    .permitAll()
                    .and()
                    .logout()
                    .permitAll();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth, @Value("${admin.username}") String username, @Value("${admin.password}") String password) throws Exception {
            auth
                    .inMemoryAuthentication()
                    .withUser(username).password(password).roles("ADMIN");
        }
    }

    @Component
    public static class Helper {
        private RiveScriptBot riveScriptBot;
        private Database chatDB, feedbackDB, explorerDB;
        private ElizaMain eliza;
        private WolframRepository wolframRepository;
        private String tmdbApiKey;
        private SPARQL sparql;
        private JLanguageTool languageTool;

        @Autowired
        public Helper(final CloudantClient cloudantClient,
                      WolframRepository wolframRepository,
                      @Value("${cloudant.chatDB}") String chatDBName,
                      @Value("${cloudant.feedbackDB}") String feedbackDBName,
                      @Value("${cloudant.explorerDB}") String explorerDBName,
                      @Value("${tmdb.apiKey}") String tmdbApiKey) {
            riveScriptBot = new RiveScriptBot();

            chatDB = cloudantClient.database(chatDBName, true);
            feedbackDB = cloudantClient.database(feedbackDBName, true);
            explorerDB = cloudantClient.database(explorerDBName, true);

            eliza = new ElizaMain();
            eliza.readScript(true, "src/main/resources/eliza/script");
            this.wolframRepository = wolframRepository;
            this.tmdbApiKey = tmdbApiKey;
            sparql = new SPARQL(explorerDB);

            languageTool = new JLanguageTool(new BritishEnglish());
            for (Rule rule : languageTool.getAllRules()) {
                if (!rule.isDictionaryBasedSpellingRule()) {
                    languageTool.disableRule(rule.getId());
                }
            }
        }

        public RiveScriptBot getRiveScriptBot() {
            return riveScriptBot;
        }

        public Database getChatDB() {
            return chatDB;
        }

        public Database getFeedbackDB() {
            return feedbackDB;
        }

        public ElizaMain getEliza() {
            return eliza;
        }

        public WolframRepository getWolframRepository() {
            return wolframRepository;
        }

        public String getTmdbApiKey() {
            return tmdbApiKey;
        }

        public Database getExplorerDB() {
            return explorerDB;
        }

        public SPARQL getSparql() {
            return sparql;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

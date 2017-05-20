package chatbot.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by ramgathreya on 5/12/17.
 */
@Configuration
public class AssetsConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("file:node_modules/");
        registry.addResourceHandler("/js/**").addResourceLocations("file:src/main/app/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("file:src/main/app/css/");
        super.addResourceHandlers(registry);
    }

}

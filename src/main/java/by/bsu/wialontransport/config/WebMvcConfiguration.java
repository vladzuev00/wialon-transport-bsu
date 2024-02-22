package by.bsu.wialontransport.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private static final String URL_PATH_LOGIN = "/login";
    private static final String VIEW_NAME_LOGIN = "login";

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController(URL_PATH_LOGIN).setViewName(VIEW_NAME_LOGIN);
    }

}

package org.stepaniuk.message;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource messageSource() {
        var source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:messages");
        source.setDefaultEncoding("UTF-8");
        source.setDefaultLocale(Locale.forLanguageTag("ua"));
        source.setCacheSeconds(10); //reload messages every 10 seconds
        return source;
    }

    @Bean
    public MessageResolver telegramMessageResolver(MessageSource messageSource) {
        return new MessageResolver(messageSource, Locale.forLanguageTag("ua"));
    }

}

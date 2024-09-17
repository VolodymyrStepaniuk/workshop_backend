package org.stepaniuk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@SpringBootApplication
public class BotApplication {
    public static void main(String[] args) {
        LocaleContextHolder.setDefaultLocale(Locale.forLanguageTag("en"));
        SpringApplication.run(BotApplication.class, args);
    }
}
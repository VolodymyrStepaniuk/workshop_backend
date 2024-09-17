package org.stepaniuk.message;

import lombok.RequiredArgsConstructor;
import java.util.Locale;
import org.springframework.context.MessageSource;

@RequiredArgsConstructor
public class MessageResolver {

    private final MessageSource messageSource;

    private final Locale defaultLocale;

    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, defaultLocale);
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, defaultLocale);
    }

    public String getMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

    public String getMessage(String code, Locale locale, Object... args) {
        return messageSource.getMessage(code, args, locale);
    }
}
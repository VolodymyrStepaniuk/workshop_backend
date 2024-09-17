package org.stepaniuk.config;

import com.github.darkrymit.telegram.bots.core.api.bot.type.User;
import com.github.darkrymit.telegram.bots.core.bot.Bot;
import com.github.darkrymit.telegram.bots.dispatch.event.UpdateEvent;
import com.github.darkrymit.telegram.bots.dispatch.filter.Filter;
import com.github.darkrymit.telegram.bots.dispatch.filter.FilterChain;
import com.github.darkrymit.telegram.bots.session.userchat.UserChatSession;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class LanguageFilter implements Filter {

    @Override
    public void doFilter(UpdateEvent updateEvent, Bot telegramBot, FilterChain filterChain)
            throws Exception {

        // Try to get the language of the user from the session
        Locale locale = Optional.ofNullable(updateEvent.getUserChatSession())
                .map(session -> session.getAttribute("language")).map(String.class::cast)
                .map(this::parseLocaleIfValid).orElse(null);

        // If the language is not set, try to get the language from the user
        if (locale == null) {
            locale = Optional.ofNullable(updateEvent.getResolvedUser()).map(User::getLanguageCode)
                    .map(this::parseLocaleIfValid).orElse(null);
        }

        // Set the language to context so that it can be used in the message source
        if (locale != null) {
            LocaleContextHolder.setLocale(locale);
            // Set the language to the session so that it can be used in the next request
            UserChatSession userChatSession = updateEvent.getUserChatSession();
            if (userChatSession != null) {
                userChatSession.setAttribute("language", locale.toLanguageTag());
            }
        }

        // Continue the filter chain
        filterChain.doFilter(updateEvent, telegramBot);

        // Clear the language from the context
        LocaleContextHolder.resetLocaleContext();
    }

    private Locale parseLocaleIfValid(String languageCode) {
        try {
            return StringUtils.parseLocale(languageCode);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}

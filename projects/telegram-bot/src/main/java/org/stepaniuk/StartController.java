package org.stepaniuk;

import com.github.darkrymit.telegram.bots.core.api.bot.method.SendMessage;
import com.github.darkrymit.telegram.bots.core.api.bot.type.Chat;
import com.github.darkrymit.telegram.bots.dispatch.event.UpdateEventType;
import com.github.darkrymit.telegram.bots.forward.ForwardRequest;
import com.github.darkrymit.telegram.bots.session.SessionType;
import com.github.darkrymit.telegram.bots.session.user.UserSession;
import com.github.darkrymit.telegram.bots.spring.controller.handler.HandlerMapping;
import com.github.darkrymit.telegram.bots.spring.controller.handler.TelegramController;
import com.github.darkrymit.telegram.bots.spring.controller.handler.condition.IsSpELTrue;
import com.github.darkrymit.telegram.bots.spring.controller.handler.condition.IsUpdateType;
import com.github.darkrymit.telegram.bots.state.IsEmptyState;
import com.github.darkrymit.telegram.bots.state.SessionState;
import com.github.darkrymit.telegram.bots.state.StateManager;
import lombok.RequiredArgsConstructor;
import org.stepaniuk.message.MessageResolver;

@TelegramController
@RequiredArgsConstructor
public class StartController {

    private final MessageResolver messageResolver;

    @HandlerMapping
    @IsUpdateType(UpdateEventType.MESSAGE)
    @IsEmptyState(session = SessionType.USER_CHAT)
    @IsSpELTrue("message.text == '/start'")
    public Object start(Chat chat) {

        return SendMessage
                .builder()
                .chatId(chat.getId())
                .text(messageResolver.getMessage("start"))
                .replyMarkupAsInlineKeyboardMarkup(
                        markup -> markup
                                .addRows(button -> button
                                        .callbackData("start.login")
                                        .text(messageResolver.getMessage("start.buttons.login")),
                                        button -> button
                                                .callbackData("start.register")
                                                .text(messageResolver.getMessage("start.buttons.register"))
                                )
                )
                .build();
    }
}

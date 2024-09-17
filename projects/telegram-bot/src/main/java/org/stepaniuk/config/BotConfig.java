package org.stepaniuk.config;

import com.github.darkrymit.telegram.bots.core.api.bot.BotApiClient;
import com.github.darkrymit.telegram.bots.core.api.file.FileApiClient;
import com.github.darkrymit.telegram.bots.core.bot.Bot;
import com.github.darkrymit.telegram.bots.core.bot.Bot.Interceptor;
import com.github.darkrymit.telegram.bots.core.bot.SimpleBot;
import com.github.darkrymit.telegram.bots.dispatch.BotToDispatcherAdapter;
import com.github.darkrymit.telegram.bots.dispatch.Dispatcher;
import com.github.darkrymit.telegram.bots.dispatch.event.UpdateEventResolver;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class BotConfig {

    @Bean(destroyMethod = "")
    public Bot bot(@Value("${token}") String token, BotApiClient apiClient,
                   FileApiClient fileApiClient, List<Interceptor> interceptors) {
        return new SimpleBot("file-encoder-bot", token, apiClient, fileApiClient, interceptors);
    }

    @Bean
    public ExecutorService adapterExecutor() {
        return new ThreadPoolExecutor(1, 10, 60L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
    }

    @Bean
    public BotToDispatcherAdapter dispatcherAdapter(Bot bot, Dispatcher dispatcher,
                                                    UpdateEventResolver eventResolver, ExecutorService adapterExecutor) {
        return new BotToDispatcherAdapter(bot, dispatcher, eventResolver, adapterExecutor);
    }
}

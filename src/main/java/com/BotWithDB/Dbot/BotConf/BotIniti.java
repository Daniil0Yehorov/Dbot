package com.BotWithDB.Dbot.BotConf;

import com.BotWithDB.Dbot.Commands.ControllTheBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotIniti {
    private final ControllTheBot controllTheBot;

    @Autowired
    public BotIniti(ControllTheBot controllTheBot) {
        this.controllTheBot = controllTheBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        botsApi.registerBot(controllTheBot);
    }
}

package com.BotWithDB.Dbot.Commands;

import com.BotWithDB.Dbot.BotConf.BotConfiguration;
import com.BotWithDB.Dbot.Models.User;
import com.BotWithDB.Dbot.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ControllTheBot extends TelegramLongPollingBot{
    private final UserService userService;
    private final BotConfiguration botConfiguration;

    @Autowired
    public ControllTheBot(UserService userService, BotConfiguration botConfiguration) {
        super(botConfiguration.getBotToken());
        this.userService = userService;
        this.botConfiguration = botConfiguration;
    }

    @Override
    public String getBotUsername() {
        return botConfiguration.getBotName();
    }
    @Override
    public String getBotToken() {
        return botConfiguration.getBotToken();
    }
    @Async
    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        boolean hasText = update.hasMessage() && update.getMessage().hasText();
        String command = update.getMessage().getText();
        if (hasText && command.equals("/addMe")){
            if (!userService.existsByChatId(chatId)){
               User entity = new User();
                entity.setChatId(chatId);
                userService.saveEntity(entity);
                sendMessage(chatId,"User added to DB");
            }
            else sendMessage(chatId, "U are in table yet!");
        }
        else if (hasText && command.equals("/me")){
            sendMessage(chatId, userService.inTable(chatId).toString());
        }

    }


    private void sendMessage(Long chatid,String text){
        SendMessage message=new SendMessage();
        message.setChatId(chatid);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

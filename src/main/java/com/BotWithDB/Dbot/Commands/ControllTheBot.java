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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            // boolean hasText = update.hasMessage() && update.getMessage().hasText();
            String command = update.getMessage().getText();

            switch (command) {
                case "/start":
                    startCommand(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/addMe":
                    if (!userService.existsByChatId(chatId)) {
                        User entity = new User();
                        entity.setChatId(chatId);
                        userService.saveEntity(entity);
                        sendMessage(chatId, "User added to DB");
                    } else {
                        sendMessage(chatId, "You are already in the table!");
                    }
                    break;

                case "/me":
                    User user = userService.inTable(chatId);
                    if (user != null) {
                        sendMessage(chatId, user.toString());
                    } else {
                        sendMessage(chatId, "You are not in the table!");
                    }
                    break;

                case "/removeMe":
                    if (userService.existsByChatId(chatId)) {
                        userService.deleteByChatId(chatId);
                        sendMessage(chatId, "User removed from DB");
                    } else {
                        sendMessage(chatId, "You are not in the table!");
                    }
                    break;
                //with subcommand
                case "/listUsers":
                    List<User> users = userService.getAllUsers();
                    StringBuilder userList = new StringBuilder("Users in DB:\n");
                    for (User u : users) {
                        userList.append(u.toString()).append("\n");
                    }
                    sendMessage(chatId, userList.toString());
                    break;
                default:
                    sendMessage(chatId, "Unknown command");
            }
        }
        else if (update.hasCallbackQuery()) {
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String data = update.getCallbackQuery().getData();
            handleCallbackQuery(chatId, data);
        }
    }
    private void handleCallbackQuery(Long chatId, String data) {
        switch (data) {
            case "/addMe":
                if (!userService.existsByChatId(chatId)) {
                    User entity = new User();
                    entity.setChatId(chatId);
                    userService.saveEntity(entity);
                    sendMessage(chatId, "User added to DB");
                } else {
                    sendMessage(chatId, "You are already in the table!");
                }
                break;
            case "/me":
                User user = userService.inTable(chatId);
                if (user != null) {
                    sendMessage(chatId, user.toString());
                } else {
                    sendMessage(chatId, "You are not in the table!");
                }
                break;
            case "/removeMe":
                if (userService.existsByChatId(chatId)) {
                    userService.deleteByChatId(chatId);
                    sendMessage(chatId, "User removed from DB");
                } else {
                    sendMessage(chatId, "You are not in the table!");
                }
                break;
            case "/listUsers":
                List<User> users = userService.getAllUsers();
                StringBuilder userList = new StringBuilder("Users in DB:\n");
                for (User u : users) {
                    userList.append(u.toString()).append("\n");
                }
                sendMessage(chatId, userList.toString());
                break;
            default:
                sendMessage(chatId, "Unknown command");
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
    private void sendMessageWithKeyboard(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(createKeyboard());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void startCommand(Long chatId, String username) {
        String text = "Hello my friend " + username;
        sendMessageWithKeyboard(chatId, text);
    }
    public static InlineKeyboardMarkup createKeyboard() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        firstRow.add(createButton("Add Me", "/addMe"));
        firstRow.add(createButton("Me", "/me"));
        firstRow.add(createButton("Remove Me", "/removeMe"));

        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        secondRow.add(createButton("List Users", "/listUsers"));

        rows.add(firstRow);
        rows.add(secondRow);

        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    private static InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }
}

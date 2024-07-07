package com.BotWithDB.Dbot.Service;

import com.BotWithDB.Dbot.Models.User;
import com.BotWithDB.Dbot.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void saveEntity(User user){
        userRepository.save(user);
    }

    public boolean existsByChatId(Long chatId){
        return userRepository.existsByChatId(chatId);
    }

    public User inTable(Long chatId){
        return userRepository.findByChatId(chatId);
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public void deleteByChatId(Long ChatId){
        if(existsByChatId(ChatId)&&ChatId!=null){
            User user=userRepository.findByChatId(ChatId);
            userRepository.delete(user);
        }
    }



}

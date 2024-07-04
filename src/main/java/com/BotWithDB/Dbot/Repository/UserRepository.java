package com.BotWithDB.Dbot.Repository;

import com.BotWithDB.Dbot.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByChatId(Long chatId);
    User findByChatId(Long chatId);
}

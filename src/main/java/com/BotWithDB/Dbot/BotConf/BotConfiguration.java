package com.BotWithDB.Dbot.BotConf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("application.yml")
public class BotConfiguration {
    @Value("${bot.name}")
    private String BotName;
    @Value("${bot.token}")
    private String BotToken;
}

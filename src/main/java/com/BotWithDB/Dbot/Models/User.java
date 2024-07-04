package com.BotWithDB.Dbot.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long chatId;
    @Override
    public String toString(){
        return "id = "+id+"\n" +
                "chatId = "+chatId+"\n";
    }
}

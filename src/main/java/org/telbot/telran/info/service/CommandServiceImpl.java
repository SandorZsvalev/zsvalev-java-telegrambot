package org.telbot.telran.info.service;

import org.springframework.stereotype.Service;
import org.telbot.telran.info.commands.Command;

@Service
public class CommandServiceImpl implements CommandService {
    @Override
    public void executeCommand(String text, Switchable bot) {
        System.out.println("зашли в экзекют");
        if (Command.START.getName().equals(text)) {
            bot.On();
        }
        if (Command.STOP.getName().equals(text)) {
            bot.Off();
        }
    }
}

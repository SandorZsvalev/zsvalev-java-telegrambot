package org.telbot.telran.info.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TelegramBot extends TelegramLongPollingBot implements Switchable {

    private Logger log = LoggerFactory.getLogger(TelegramBot.class);

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private boolean enabled = true;

    @Autowired
    private ChannelPostService channelPostService;

    @Autowired
    private TelegramBotService telegramBotService;

    @Autowired
    private CommandService commandService;

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().isCommand()) {
            commandService.executeCommand(update.getMessage().getEntities().get(0).getText(), this);
            return;
        }
        if (!enabled) {
            return;
        }

        if (update.hasChannelPost()) {
            Long channelTlgId = update.getChannelPost().getChatId();
            String text = update.getChannelPost().getText();
            Integer date = update.getChannelPost().getDate();
            Integer messageId = update.getChannelPost().getMessageId();
            String title = update.getChannelPost().getChat().getTitle();
            if (telegramBotService.checkIfChannelIsPresent(channelTlgId)) {
                channelPostService.createPost(channelTlgId, messageId, text, date);
            } else {
                telegramBotService.addNewChannel(title, channelTlgId);
                channelPostService.createPost(channelTlgId, messageId, text, date);
            }
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            Integer date = update.getMessage().getDate();
            Integer messageId = update.getMessage().getMessageId();
            String title = update.getMessage().getChat().getTitle();
            if (telegramBotService.checkIfChannelIsPresent(chatId)) {
                channelPostService.createPost(chatId, messageId, messageText, date);
            } else {
                telegramBotService.addNewChannel(title, chatId);
                channelPostService.createPost(chatId, messageId, messageText, date);
            }
        }
    }

    @Override
    public void On() {
        enabled = true;
    }

    @Override
    public void Off() {
        enabled = false;
    }
}

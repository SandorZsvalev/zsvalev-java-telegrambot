package org.telbot.telran.info.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telbot.telran.info.model.TelegramChannelPost;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    TelegramChannelPostService telegramChannelPostService;

    @Autowired
    TelegramBotService telegramBotService;

    @Override
    public String getBotUsername() {
        return "sandorspringtestbot";
    }

    @Override
    public String getBotToken() {
        return "5823882816:AAFtAteERzxomlIYFs0j3i0Wz5h2CokmdUo";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasChannelPost()) {
            System.out.println(" Вижу пост");
            Long channelTlgId = update.getChannelPost().getChatId();
            String text = update.getChannelPost().getText();
            Integer date = update.getChannelPost().getDate();
            Integer messageId = update.getChannelPost().getMessageId();
            String title = update.getChannelPost().getChat().getTitle();
            System.out.println("chatTitle: " + title);
            System.out.println("chatId: " + channelTlgId);
            System.out.println("test message: " + text);
            System.out.println("date of publish: " + date);
            System.out.println("post ID: " + messageId);
            if (telegramBotService.checkIfChannelIsPresent(channelTlgId)) {
                telegramChannelPostService.createPost(channelTlgId, messageId, text, date);
            } else {
                telegramBotService.addNewChannel(title, channelTlgId);
                telegramChannelPostService.createPost(channelTlgId, messageId, text, date);
            }

            messageId = messageId - 1;
            date = date - 500;
            List<TelegramChannelPost> foundList = telegramChannelPostService.getPostsByChannelTlgIdAndMessageId(channelTlgId, messageId);
            for (TelegramChannelPost post : foundList) {
                System.out.println("id сообщения: " + post.getMessageId());
                System.out.println("text сообщения: " + post.getText());
            }

            List<TelegramChannelPost> foundSecondList = telegramChannelPostService.getPostsByChannelTlgIdAndTime(channelTlgId, date);
            for (TelegramChannelPost post : foundSecondList) {
                System.out.println("id сообщения: " + post.getMessageId());
                System.out.println("text сообщения: " + post.getText());
            }

        }

        if (update.hasEditedChannelPost()) {
            System.out.println("вижу изменения");
            System.out.println(update.getEditedChannelPost().getText());
            System.out.println(update.getEditedChannelPost().getMessageId());
            System.out.println(update.getEditedChannelPost().getDate());
        }


        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println("вижу сообщение");
            Long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            String userName = update.getMessage().getChat().getUserName();

            switch (messageText) {
                case ("/start"):
                    startCommandRecived(chatId, userName);
                    break;
                default:
                    sendMessage(chatId, "Something has written");
                    break;
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    private void startCommandRecived(long chatId, String userName) {
        String greetings = "Hello, " + userName + "! Nice to meet you!";
        sendMessage(chatId, greetings);
    }

    private void sendMessage(long chatId, String greetings) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), greetings);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

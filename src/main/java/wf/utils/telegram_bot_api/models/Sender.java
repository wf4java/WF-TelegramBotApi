package wf.utils.telegram_bot_api.models;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public abstract class Sender extends TelegramLongPollingBot {


    public Sender() {
    }

    public Sender(DefaultBotOptions options) {
        super(options);
    }

    public Sender(String botToken) {
        super(botToken);
    }

    public Sender(DefaultBotOptions options, String botToken) {
        super(options, botToken);
    }

    public abstract Message sendMessage(Long chatId, String text);

    public abstract CompletableFuture<Message> sendMessageAsync(Long chatId, String text);


    public abstract Message sendMessageReply(Long chatId, int replyId, String text);

    public abstract CompletableFuture<Message> sendMessageReplyAsync(Long chatId, int replyId, String text);


    public abstract Message editMessageText(Long chatId, int messageId, String newText);

    public abstract CompletableFuture<Serializable> editMessageTextAsync(Long chatId, int messageId, String newText);

    public abstract Boolean deleteMessage(Long chatId, int messageId);

    public abstract CompletableFuture<Boolean> deleteMessageAsync(Long chatId, int messageId);

    public abstract Message editMessageReplyMarkup(Long chatId, int messageId, InlineKeyboardMarkup replyKeyboard);

    public abstract CompletableFuture<Serializable> editMessageReplyMarkupAsync(Long chatId, int messageId, InlineKeyboardMarkup replyKeyboard);

    public abstract Message forwardMessage(Long chatId, Long fromChatId, int messageId);

    public abstract CompletableFuture<Message> forwardMessageAsync(Long chatId, Long fromChatId, int messageId);

}

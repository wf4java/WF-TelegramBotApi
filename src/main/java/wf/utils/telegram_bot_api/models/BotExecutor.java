package wf.utils.telegram_bot_api.models;

import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BotExecutor extends TelegramLongPollingBot {

    private final String botUsername;

    private final Consumer<Update> updateHandler;
    private final Runnable closingHandler;


    public BotExecutor(String botUsername, String botToken, Consumer<Update> updateHandler, Runnable closingHandler) {
        this(new DefaultBotOptions(), botUsername, botToken, updateHandler, closingHandler);
    }

    public BotExecutor(DefaultBotOptions defaultBotOptions, String botUsername, String botToken, Consumer<Update> updateHandler, Runnable closingHandler) {
        super(defaultBotOptions, botToken);
        this.botUsername = botUsername;
        this.updateHandler = updateHandler;
        this.closingHandler = closingHandler;
    }



    @Override
    public void onUpdateReceived(Update update) {
        updateHandler.accept(update);
    }

    @Override
    public void onClosing() {
        closingHandler.run();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }



    @SneakyThrows
    public Message sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return execute(message);
    }

    @SneakyThrows
    public CompletableFuture<Message> sendMessageAsync(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return executeAsync(message);
    }

    @SneakyThrows
    public Message sendMessageReply(Long chatId, int replyId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setReplyToMessageId(replyId);
        message.setText(text);
        return execute(message);
    }

    @SneakyThrows
    public CompletableFuture<Message> sendMessageReplyAsync(Long chatId, int replyId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setReplyToMessageId(replyId);
        message.setText(text);
        return executeAsync(message);
    }

    @SneakyThrows
    public Message editMessageText(Long chatId, int messageId, String newText) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setText(newText);
        return (Message) execute(editMessage);
    }

    @SneakyThrows
    public CompletableFuture<Serializable> editMessageTextAsync(Long chatId, int messageId, String newText) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setText(newText);
        return executeAsync(editMessage);
    }

    @SneakyThrows
    public Boolean deleteMessage(Long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        return execute(deleteMessage);
    }

    @SneakyThrows
    public CompletableFuture<Boolean> deleteMessageAsync(Long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        return executeAsync(deleteMessage);
    }

    @SneakyThrows
    public Message editMessageReplyMarkup(Long chatId, int messageId, InlineKeyboardMarkup replyKeyboard) {
        EditMessageReplyMarkup editMessage = new EditMessageReplyMarkup();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setReplyMarkup(replyKeyboard);
        return (Message) execute(editMessage);
    }

    @SneakyThrows
    public CompletableFuture<Serializable> editMessageReplyMarkupAsync(Long chatId, int messageId, InlineKeyboardMarkup replyKeyboard) {
        EditMessageReplyMarkup editMessage = new EditMessageReplyMarkup();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setReplyMarkup(replyKeyboard);
        return executeAsync(editMessage);
    }

    @SneakyThrows
    public Message forwardMessage(Long chatId, Long fromChatId, int messageId) {
        ForwardMessage forwardMessage = new ForwardMessage();
        forwardMessage.setChatId(chatId);
        forwardMessage.setFromChatId(fromChatId);
        forwardMessage.setMessageId(messageId);
        return execute(forwardMessage);
    }

    @SneakyThrows
    public CompletableFuture<Message> forwardMessageAsync(Long chatId, Long fromChatId, int messageId) {
        ForwardMessage forwardMessage = new ForwardMessage();
        forwardMessage.setChatId(chatId);
        forwardMessage.setFromChatId(fromChatId);
        forwardMessage.setMessageId(messageId);
        return executeAsync(forwardMessage);
    }

}

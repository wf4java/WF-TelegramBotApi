package wf.utils.telegram_bot_api.models;

import lombok.SneakyThrows;
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

public class BotDataHandler extends Sender {

    private final String botUsername;

    private final Consumer<Update> updateHandler;
    private final Runnable closingHandler;


    public BotDataHandler(String botUsername, String botToken, Consumer<Update> updateHandler, Runnable closingHandler) {
        super(botToken);
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
    @Override
    public Message sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return execute(message);
    }

    @SneakyThrows
    @Override
    public CompletableFuture<Message> sendMessageAsync(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return executeAsync(message);
    }

    @SneakyThrows
    @Override
    public Message sendMessageReply(Long chatId, int replyId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setReplyToMessageId(replyId);
        message.setText(text);
        return execute(message);
    }

    @SneakyThrows
    @Override
    public CompletableFuture<Message> sendMessageReplyAsync(Long chatId, int replyId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setReplyToMessageId(replyId);
        message.setText(text);
        return executeAsync(message);
    }

    @SneakyThrows
    @Override
    public Message editMessageText(Long chatId, int messageId, String newText) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setText(newText);
        return (Message) execute(editMessage);
    }

    @SneakyThrows
    @Override
    public CompletableFuture<Serializable> editMessageTextAsync(Long chatId, int messageId, String newText) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setText(newText);
        return executeAsync(editMessage);
    }

    @SneakyThrows
    @Override
    public Boolean deleteMessage(Long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        return execute(deleteMessage);
    }

    @SneakyThrows
    @Override
    public CompletableFuture<Boolean> deleteMessageAsync(Long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        return executeAsync(deleteMessage);
    }

    @SneakyThrows
    @Override
    public Message editMessageReplyMarkup(Long chatId, int messageId, InlineKeyboardMarkup replyKeyboard) {
        EditMessageReplyMarkup editMessage = new EditMessageReplyMarkup();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setReplyMarkup(replyKeyboard);
        return (Message) execute(editMessage);
    }

    @SneakyThrows
    @Override
    public CompletableFuture<Serializable> editMessageReplyMarkupAsync(Long chatId, int messageId, InlineKeyboardMarkup replyKeyboard) {
        EditMessageReplyMarkup editMessage = new EditMessageReplyMarkup();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setReplyMarkup(replyKeyboard);
        return executeAsync(editMessage);
    }

}

package wf.utils.telegram_bot_api.models;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;

public interface MessageHandler {


    public default void onTextMessage(String text, Long chatId, Message message, Sender sender, Update update) { }

    public default void onUpdate(Update update, Sender sender) { }

    public default void onCallbackQuery(CallbackQuery callbackQuery, Long chatId, Message message, Sender sender, Update update) { }

    public default void onCallbackInlineQuery(CallbackQuery callbackQuery, Long senderId, String inlineMessageId, Sender sender, Update update) { }

    public default void onInlineQuery(InlineQuery inlineQuery, Long senderId, Sender sender, Update update) { }

}

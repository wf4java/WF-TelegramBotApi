package wf.utils.telegram_bot_api.models;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageHandler {


    public default void onTextMessage(String text, Long chatId, Message message, Sender sender, Update update) { }

    public default void onUpdate(Update update, Sender sender) { }

    public default void onCallbackQuery(CallbackQuery callbackQuery, Long chatId, Message message, Sender sender, Update update) { }


}

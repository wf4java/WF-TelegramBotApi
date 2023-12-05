package wf.utils.telegram_bot_api.models;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageHandler {


    public default void onTextMessage(String text, Long chatId, Sender sender, Update update) { }

    public default void onUpdate(Update update, Sender sender) { }


}

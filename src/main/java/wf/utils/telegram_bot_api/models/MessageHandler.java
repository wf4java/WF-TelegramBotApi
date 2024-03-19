package wf.utils.telegram_bot_api.models;

import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;

public interface MessageHandler {


    public default void onTextMessage(String text, Long chatId, Message message, BotExecutor botExecutor, Update update) { }

    public default void onUpdate(Update update, BotExecutor botExecutor) { }

    public default void onCallbackQuery(CallbackQuery callbackQuery, Long chatId, MaybeInaccessibleMessage message, BotExecutor botExecutor, Update update) { }

    public default void onCallbackInlineQuery(CallbackQuery callbackQuery, Long senderId, String inlineMessageId, BotExecutor botExecutor, Update update) { }

    public default void onInlineQuery(InlineQuery inlineQuery, Long senderId, BotExecutor botExecutor, Update update) { }

}

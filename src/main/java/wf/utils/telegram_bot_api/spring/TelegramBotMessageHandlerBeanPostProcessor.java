package wf.utils.telegram_bot_api.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import wf.utils.telegram_bot_api.TelegramBot;
import wf.utils.telegram_bot_api.models.BotExecutor;
import wf.utils.telegram_bot_api.models.MessageHandler;
import wf.utils.telegram_bot_api.spring.annotation.*;
import wf.utils.telegram_bot_api.spring.annotation.handlers.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


@Getter
public class TelegramBotMessageHandlerBeanPostProcessor implements BeanPostProcessor {


    private final TelegramBot telegramBot;
    private final String group;

    private final List<Handler> messageHandlers = new ArrayList<>();
    private final List<Handler> textMessageHandlers = new ArrayList<>();
    private final List<Handler> callbackQueryHandlers = new ArrayList<>();
    private final List<Handler> callbackInlineQueryHandlers = new ArrayList<>();
    private final List<Handler> inlineQueryHandlers = new ArrayList<>();
    private final Map<String, List<Handler>> commandHandler = new HashMap<>();


    @Lazy
    public TelegramBotMessageHandlerBeanPostProcessor(TelegramBot telegramBot) {
        this(telegramBot, "default");
    }


    @Lazy
    public TelegramBotMessageHandlerBeanPostProcessor(TelegramBot telegramBot, String group) {
        this.telegramBot = telegramBot;
        this.group = group;

        telegramBot.addHandler(new MessageHandler() {
            @Override
            public void onTextMessage(String text, Long chatId, Message message, BotExecutor botExecutor, Update update) {
                for(Handler h : textMessageHandlers)
                    invoke(h, text, chatId, message, botExecutor, update);

                for (Handler h : commandHandler.getOrDefault(text.toLowerCase(), Collections.emptyList()))
                    invoke(h, text, chatId, message, botExecutor, update);
            }

            @Override
            public void onUpdate(Update update, BotExecutor botExecutor) {
                for(Handler h : messageHandlers)
                    invoke(h, update, botExecutor);
            }

            @Override
            public void onCallbackQuery(CallbackQuery callbackQuery, Long chatId, MaybeInaccessibleMessage message, BotExecutor botExecutor, Update update) {
                for(Handler h : callbackQueryHandlers)
                    invoke(h, callbackQuery, chatId, message, botExecutor, update);
            }

            @Override
            public void onCallbackInlineQuery(CallbackQuery callbackQuery, Long senderId, String inlineMessageId, BotExecutor botExecutor, Update update) {
                for(Handler h : callbackInlineQueryHandlers)
                    invoke(h, callbackQuery, senderId, inlineMessageId, botExecutor, update);
            }

            @Override
            public void onInlineQuery(InlineQuery inlineQuery, Long senderId, BotExecutor botExecutor, Update update) {
                for(Handler h : inlineQueryHandlers)
                    invoke(h, inlineQuery, senderId, botExecutor, update);
            }


        });
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(TelegramBotController.class)) {
            Method[] methods = beanClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(TelegramBotMessageHandler.class) && method.getAnnotation(TelegramBotMessageHandler.class).group().equals(group))
                    messageHandlers.add(new Handler(bean, method));

                if (method.isAnnotationPresent(TelegramBotTextMessageHandler.class) && method.getAnnotation(TelegramBotTextMessageHandler.class).group().equals(group))
                    textMessageHandlers.add(new Handler(bean, method));

                if (method.isAnnotationPresent(TelegramBotCallbackQueryHandler.class) && method.getAnnotation(TelegramBotCallbackQueryHandler.class).group().equals(group))
                    callbackQueryHandlers.add(new Handler(bean, method));

                if (method.isAnnotationPresent(TelegramBotCallbackInlineQueryHandler.class) && method.getAnnotation(TelegramBotCallbackInlineQueryHandler.class).group().equals(group))
                    callbackInlineQueryHandlers.add(new Handler(bean, method));

                if (method.isAnnotationPresent(TelegramBotInlineQueryHandler.class) && method.getAnnotation(TelegramBotInlineQueryHandler.class).group().equals(group))
                    inlineQueryHandlers.add(new Handler(bean, method));

                if (method.isAnnotationPresent(TelegramBotCommandHandler.class) && method.getAnnotation(TelegramBotCommandHandler.class).group().equals(group))
                    commandHandler.computeIfAbsent(method.getAnnotation(TelegramBotCommandHandler.class).command().toLowerCase(),
                            (k) -> new ArrayList<>()).add(new Handler(bean, method));

            }
        }
        return bean;
    }



    private static void invoke(Handler handler, Object... objects) {
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
        List<Object> args = new ArrayList<>();

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            for (Object object : objects) { if (parameterType.isInstance(object)) { args.add(object); break; } }
            if (args.size() <= i) { args.add(null); }
        }

        try { handler.getMethod().invoke(handler.getObj(), args.toArray()); }
        catch (IllegalAccessException | InvocationTargetException e) {throw new RuntimeException(e);}
    }



    @Data
    @AllArgsConstructor
    private final static class Handler {
        private Object obj;
        private Method method;
    }

}

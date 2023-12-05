package wf.utils.telegram_bot_api.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import wf.utils.telegram_bot_api.TelegramBot;
import wf.utils.telegram_bot_api.models.MessageHandler;
import wf.utils.telegram_bot_api.models.Sender;
import wf.utils.telegram_bot_api.spring.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

@Getter
public class TelegramBotMessageHandlerBeanPostProcessor implements BeanPostProcessor {


    private final TelegramBot telegramBot;

    private final ArrayList<Handler> messageHandlers = new ArrayList<>();
    private final ArrayList<Handler> textMessageHandlers = new ArrayList<>();
    private final ArrayList<Handler> callbackQueryHandlers = new ArrayList<>();


    @Lazy
    public TelegramBotMessageHandlerBeanPostProcessor(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
        telegramBot.addHandler(new MessageHandler() {
            @Override
            public void onTextMessage(String text, Long chatId, Message message, Sender sender, Update update) {
                for(Handler h : textMessageHandlers)
                    invoke(h, text, chatId, message, sender, update);
            }

            @Override
            public void onUpdate(Update update, Sender sender) {
                for(Handler h : messageHandlers)
                    invoke(h, update, sender);
            }

            @Override
            public void onCallbackQuery(CallbackQuery callbackQuery, Long chatId, Message message, Sender sender, Update update) {
                for(Handler h : callbackQueryHandlers)
                    invoke(h, callbackQuery, chatId, message, sender, update);
            }
        });
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(TelegramBotController.class)) {
            Method[] methods = beanClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(TelegramBotMessageHandler.class))
                    messageHandlers.add(new Handler(bean, method));

                if (method.isAnnotationPresent(TelegramBotTextMessageHandler.class))
                    textMessageHandlers.add(new Handler(bean, method));

                if (method.isAnnotationPresent(TelegramBotCallbackQueryHandler.class))
                    callbackQueryHandlers.add(new Handler(bean, method));

            }
        }
        return bean;
    }



    private static void invoke(Handler handler, Object... objects) {
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            if (i < objects.length && parameterTypes[i].isInstance(objects[i])) {args[i] = objects[i];}
            else {args[i] = null;}
        }

        try {handler.getMethod().invoke(handler.getObj(), args);}
        catch (IllegalAccessException | InvocationTargetException e) {throw new RuntimeException(e);}
    }


    @Data
    @AllArgsConstructor
    private final static class Handler {
        private Object obj;
        private Method method;
    }

}

package wf.utils.telegram_bot_api.spring.annotation.handlers;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TelegramBotMessageHandler {

    String group() default "default";

}

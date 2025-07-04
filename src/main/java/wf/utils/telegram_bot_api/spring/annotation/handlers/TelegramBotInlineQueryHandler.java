package wf.utils.telegram_bot_api.spring.annotation.handlers;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TelegramBotInlineQueryHandler {

    String group() default "default";

}

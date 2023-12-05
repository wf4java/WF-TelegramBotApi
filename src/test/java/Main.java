import org.telegram.telegrambots.meta.api.objects.Update;
import wf.utils.telegram_bot_api.TelegramBot;
import wf.utils.telegram_bot_api.models.MessageHandler;
import wf.utils.telegram_bot_api.models.Sender;

public class Main {

    public static void main(String[] args) {

        TelegramBot telegramBot = new TelegramBot("username", "token");

        telegramBot.addHandler(new MessageHandler() {
            @Override
            public void onTextMessage(String text, Long chatId, Sender sender, Update update) {
                sender.deleteMessage(chatId, update.getMessage().getMessageId());
                sender.sendMessage(chatId, text);
            }
        });

    }

}

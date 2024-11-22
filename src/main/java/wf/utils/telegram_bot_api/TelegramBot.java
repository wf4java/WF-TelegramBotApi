package wf.utils.telegram_bot_api;


import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import wf.utils.telegram_bot_api.models.BotExecutor;
import wf.utils.telegram_bot_api.models.MessageHandler;


import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class TelegramBot {


    private static final TelegramBotsApi api;

    static {
        try {api = new TelegramBotsApi(DefaultBotSession.class);}
        catch (TelegramApiException e) {throw new RuntimeException(e);}
    }


    private final BotSession botSession;
    private final BotExecutor botExecutor;
    private final ExecutorService executorService;

    @Setter
    private boolean autoRestartOnFail;
    @Setter
    private ArrayList<MessageHandler> messageHandlers = new ArrayList<>();



    private boolean stopped = false;



    public TelegramBot(String botUsername, String botToken) {
       this(botUsername, botToken, true);
    }

    @SneakyThrows
    public TelegramBot(String botUsername, String botToken, boolean autoRestartOnFail) {
        this(new DefaultBotOptions(), botUsername, botToken, autoRestartOnFail);
    }

    @SneakyThrows
    public TelegramBot(DefaultBotOptions defaultBotOptions, String botUsername, String botToken, boolean autoRestartOnFail) {
        this.botExecutor = new BotExecutor(botUsername, botToken, this::update, this::closing);
        this.botSession = api.registerBot(botExecutor);
        this.autoRestartOnFail = autoRestartOnFail;
        this.executorService = Executors.newCachedThreadPool();
    }

    public void stop(){
        stopped = true;
        if(botSession.isRunning()) botSession.stop();
    }

    public void start() {
        stopped = false;
        if(!botSession.isRunning()) botSession.start();
    }

    @SneakyThrows
    private void closing() {
        if(!autoRestartOnFail && stopped) return;
        Thread.sleep(5000);
        if (!autoRestartOnFail && stopped && !botSession.isRunning()) botSession.start();
    }


    public boolean isRunning() {
        return botSession.isRunning();
    }


    private void update(Update update) {
        messageHandlers.forEach(h -> {h.onUpdate(update, botExecutor);});

        if(update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            if(callbackQuery.getMessage() != null)
                messageHandlers.forEach(h -> {h.onCallbackQuery(update.getCallbackQuery(),
                        update.getCallbackQuery().getMessage().getChatId(),
                        update.getCallbackQuery().getMessage(), botExecutor, update);});

            else if(callbackQuery.getData()!= null)
                messageHandlers.forEach(h -> {h.onCallbackInlineQuery(update.getCallbackQuery(),
                        update.getCallbackQuery().getFrom().getId(),
                        update.getCallbackQuery().getInlineMessageId(), botExecutor, update);});
        }

        if(update.hasInlineQuery())
            messageHandlers.forEach(h -> {h.onInlineQuery(update.getInlineQuery(), update.getInlineQuery().getFrom().getId(), botExecutor, update);});

        if(!update.hasMessage()) return;

        Message message = update.getMessage();

        if(message.hasText() || (message.getCaption() != null && !message.getCaption().isEmpty()))
            messageHandlers.forEach(h -> {h.onTextMessage((message.hasText() ? message.getText() : message.getCaption()),
                    message.getChatId(), message, botExecutor, update);});

    }

    public boolean addHandler(MessageHandler messageHandler) {
        return messageHandlers.add(messageHandler);
    }

    public boolean removeHandler(MessageHandler messageHandler) {
        return messageHandlers.remove(messageHandler);
    }

}

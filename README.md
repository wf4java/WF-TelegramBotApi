# WF-TelegramBotApi:
## Maven:
`Java(min): 17`
```xml
  <dependency>
    <groupId>io.github.wf4java</groupId>
    <artifactId>WF-TelegramBotApi</artifactId>
    <version>2.1</version>
  </dependency>
```

## Example:

```java
TelegramBot telegramBot = new TelegramBot("username", "token");

telegramBot.addHandler(new MessageHandler() {
    @Override
    public void onTextMessage(String text, Long chatId, Message message, BotExecutor botExecutor, Update update) {
        botExecutor.deleteMessage(chatId, message.getMessageId());
        botExecutor.sendMessage(chatId, text);
    }
});
```
ㅤ
# WF-ChatGptApi:
## Maven:
`Java(min): 17`
```xml
  <dependency>
    <groupId>io.github.wf4java</groupId>
    <artifactId>WF-TelegramBotApi</artifactId>
    <version>1.7</version>
  </dependency>
```

## Example:

```java
TelegramBot telegramBot = new TelegramBot("username", "token");

telegramBot.addHandler(new MessageHandler() {
    @Override
    public void onTextMessage(String text, Long chatId, Message message, Sender sender, Update update) {
        sender.deleteMessage(chatId, message.getMessageId());
        sender.sendMessage(chatId, text);
    }
});
```
ㅤ
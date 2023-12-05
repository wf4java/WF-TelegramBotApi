# WF-ChatGptApi:
## Maven:
`Java(min): 17`
```xml
  <dependency>
    <groupId>io.github.wf4java</groupId>
    <artifactId>WF-ChatGptApi</artifactId>
    <version>1.0</version>
  </dependency>
```

## Examples:

### Create and generate:
```java
ChatGpt gpt = new ChatGpt("api-key");

String result1 = gpt.ask("Say hello in 10 languages"); // 2048 tokens, model: GPT_3_5_TURBO_1106
String result2 = gpt.ask("Say hello in 10 languages", 512); // 512 tokens, model: GPT_3_5_TURBO_1106
```
ㅤ
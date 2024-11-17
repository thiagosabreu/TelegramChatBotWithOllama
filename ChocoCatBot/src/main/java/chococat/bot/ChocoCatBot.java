package chococat.bot;

import java.io.IOException;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import io.github.ollama4j.exceptions.OllamaBaseException;

public class ChocoCatBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final String ollamaHost; 
    private final Ollama ollama;

    public ChocoCatBot(String botToken, String ollamaHost, String analysisUrl, String analysisKey) {
        telegramClient = new OkHttpTelegramClient(botToken);
        this.ollamaHost = ollamaHost;
        ollama = new Ollama(ollamaHost, analysisUrl, analysisKey);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            String response = null;

            try {
                response = ollama.getOllamaResponse(message_text);
            } catch (OllamaBaseException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            SendMessage message = SendMessage
                    .builder()
                    .chatId(chat_id)
                    .text(response)
                    .build();
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
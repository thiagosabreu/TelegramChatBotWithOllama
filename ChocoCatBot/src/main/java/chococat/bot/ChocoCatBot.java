package chococat.bot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    private final Map<Long, Boolean> userStates = new HashMap<>();
    private final Map<Long, String> userMessages = new HashMap<>();
    private final Map<Long, String> lastMessageBeforeAnalysis = new HashMap<>();
    private final Map<Long, String> lastMessageReceived = new HashMap<>();  // Para armazenar as mensagens mais recentes
    private final Map<Long, String> lastMessageForAnalysisRequest = new HashMap<>(); // Para armazenar a última mensagem antes de "Quero analisar"

    public ChocoCatBot(String botToken, String ollamaHost, String analysisUrl, String analysisKey) {
        telegramClient = new OkHttpTelegramClient(botToken);
        this.ollamaHost = ollamaHost;
        ollama = new Ollama(ollamaHost, analysisUrl, analysisKey);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equalsIgnoreCase("Quero analisar")) {
                userStates.put(chatId, true);  // O usuário quer análise de sentimento novamente
                performSentimentAnalysis(chatId);  // Realiza a análise com a última mensagem armazenada antes do "Quero analisar"
            } else if (messageText.equalsIgnoreCase("Sim") || messageText.equalsIgnoreCase("Não")) {
                handleSentimentChoice(messageText, chatId);
            } else {
                userMessages.put(chatId, messageText); // Armazena a nova mensagem do usuário
                lastMessageReceived.put(chatId, messageText);  // Armazena sempre a última mensagem
                if (userStates.getOrDefault(chatId, true) == false) {
                    handleConversation(chatId);  // Conversa normal sem análise
                } else {
                    lastMessageForAnalysisRequest.put(chatId, messageText);  // Armazena mensagem antes de querer análise
                    askForSentimentAnalysis(chatId); // Pergunta se ele quer análise
                }
            }
        }
    }

    private void askForSentimentAnalysis(long chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Você gostaria de analisar o sentimento desta mensagem? (Sim/Não)\n\n" +
                        "Após o 'Não', você só poderá solicitar a análise novamente enviando 'Quero analisar'.")
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleSentimentChoice(String userChoice, long chatId) {
        String response;
        String originalMessage;

        if (userChoice.equalsIgnoreCase("Sim")) {
            userStates.put(chatId, true); // O usuário quer fazer a análise
            originalMessage = lastMessageForAnalysisRequest.getOrDefault(chatId, lastMessageReceived.get(chatId)); // Pega a última mensagem antes do "Quero analisar"
            try {
                response = ollama.getOllamaResponse(originalMessage, true);  // Envia para análise
            } catch (IOException | OllamaBaseException | InterruptedException e) {
                e.printStackTrace();
                response = "Houve um erro ao processar sua mensagem.";
            }
        } else {
            userStates.put(chatId, false);  // O usuário não quer a análise agora
            response = "Ok! Vamos seguir com a conversa normal, sem análise de sentimento.";
        }

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(response)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void performSentimentAnalysis(long chatId) {
        String messageToAnalyze = lastMessageForAnalysisRequest.get(chatId);  // Pega a última mensagem antes de "Quero analisar"
        String response;
        try {
            response = ollama.getOllamaResponse(messageToAnalyze, true);  // Envia para análise de sentimento
        } catch (IOException | OllamaBaseException | InterruptedException e) {
            e.printStackTrace();
            response = "Houve um erro ao processar sua mensagem.";
        }

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(response)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleConversation(long chatId) {
        String originalMessage = userMessages.get(chatId);  // Recupera a última mensagem do usuário
        String response;
        try {
            response = ollama.getOllamaResponse(originalMessage, false);  // Envia a mensagem sem análise
        } catch (IOException | OllamaBaseException | InterruptedException e) {
            e.printStackTrace();
            response = "Houve um erro ao processar sua mensagem.";
        }

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(response)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
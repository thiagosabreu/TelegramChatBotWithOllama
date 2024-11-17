package chococat.bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String botToken = properties.getProperty("bot.token");
        String ollamaHost = properties.getProperty("ollama.host");
        String analysisUrl = properties.getProperty("api.url");
        String analysisKey = properties.getProperty("api.key");

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            ChocoCatBot chococatBot = new ChocoCatBot(botToken, ollamaHost, analysisUrl, analysisKey);
            botsApplication.registerBot(botToken, chococatBot);
            System.out.println("ChococatBot successfully started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package chococat.bot;

import java.io.IOException;

import chococat.bot.sentiment.SentimentAnalysis;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.utils.OptionsBuilder;


public class Ollama {
    private final String host;
    private final SentimentAnalysis sentimentAnalysis;
    
    public Ollama(String host, String analysisUrl, String analysisKey) {
        this.host = host;
        this.sentimentAnalysis = new SentimentAnalysis(analysisUrl, analysisKey);
    }

    public String getOllamaResponse(String prompt) throws IOException, OllamaBaseException, InterruptedException {
        OllamaAPI ollamaAPI = new OllamaAPI(host);
        OllamaResult result;

        ollamaAPI.setRequestTimeoutSeconds(200);

        String analyseSentimentResponse = sentimentAnalysis.analyzeSentiment(prompt);
        if (analyseSentimentResponse != null) {
            String promptComAnalise = "Texto para ser analisado: " + prompt + "\n\nAnalise do texto: " + analyseSentimentResponse;
            result = ollamaAPI.generate("gemma2:2b", promptComAnalise, true, new OptionsBuilder().build());
            return analyseSentimentResponse + "\n\n\n" + result.getResponse();
        }
        result = ollamaAPI.generate("gemma2:2b", prompt, true, new OptionsBuilder().build());
        return result.getResponse();
    }
}
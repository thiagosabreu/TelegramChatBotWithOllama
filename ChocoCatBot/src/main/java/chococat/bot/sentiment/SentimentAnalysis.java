package chococat.bot.sentiment;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SentimentAnalysis {
    private final String API_URL;
    private final String API_KEY;

    public SentimentAnalysis(String apiUrl, String apiKey) {
        API_URL = apiUrl;
        API_KEY = apiKey;
    }

    public String analyzeSentiment(String content) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(content, mediaType);

        Request request = new Request.Builder()
            .url(API_URL)
            .addHeader("apikey", API_KEY)
            .post(body)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code: " + response);
            }

            String jsonResponse = response.body().string();
            Gson gson = new Gson();
            SentimentResult result = gson.fromJson(jsonResponse, SentimentResult.class);

            return result.toFormattedString();
        }
    }
}

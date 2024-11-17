package chococat.bot.sentiment;

public class SentimentResult {
    String sentiment;
    int score;
    String language;

    public String toFormattedString() {
        String translatedSentiment = SentimentTranslate.translateSentiment(sentiment);
        String translatedLanguage = SentimentTranslate.translateLanguage(language);

        return String.format(
                "🔍 Análise de Sentimento\n" +
                "📄 Sentimento: %s\n" +
                "📊 Intensidade do sentimento (de -4 a +4): %d\n" +
                "🌐 Idioma: %s",
                translatedSentiment,
                score,
                translatedLanguage
        );
    }
}

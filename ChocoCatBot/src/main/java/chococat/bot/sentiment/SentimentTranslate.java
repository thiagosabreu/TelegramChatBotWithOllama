package chococat.bot.sentiment;

public class SentimentTranslate {

    public static String translateLanguage(String languageCode) {
        if (languageCode == null) {
            return "Desconhecido";
        }
        if (languageCode.contains("en")) {
            return "Inglês";
        }
        if (languageCode.contains("pt")) {
            return "Português";
        }
        if (languageCode.contains("es")) {
            return "Espanhol";
        }
        if (languageCode.contains("fr")) {
            return "Francês";
        }
        if (languageCode.contains("de")) {
            return "Alemão";
        }
        if (languageCode.contains("it")) {
            return "Italiano";
        }
        if (languageCode.contains("ru")) {
            return "Russo";
        }
        if (languageCode.contains("nl")) {
            return "Holandês";
        }
        if (languageCode.contains("ja")) {
            return "Japonês";
        }
        if (languageCode.contains("ko")) {
            return "Coreano";
        }
        if (languageCode.contains("zh")) {
            return "Chinês";
        }
        if (languageCode.contains("ar")) {
            return "Árabe";
        }
        if (languageCode.contains("hi")) {
            return "Hindi";
        }
        if (languageCode.contains("tr")) {
            return "Turco";
        }
        if (languageCode.contains("pl")) {
            return "Polonês";
        }
        if (languageCode.contains("sv")) {
            return "Sueco";
        }
        if (languageCode.contains("no")) {
            return "Norueguês";
        }
        if (languageCode.contains("da")) {
            return "Dinamarquês";
        }
        if (languageCode.contains("fi")) {
            return "Finlandês";
        }
        if (languageCode.contains("cs")) {
            return "Tcheco";
        }
        if (languageCode.contains("hu")) {
            return "Húngaro";
        }
        if (languageCode.contains("ro")) {
            return "Romeno";
        }
        if (languageCode.contains("th")) {
            return "Tailandês";
        }
        if (languageCode.contains("vi")) {
            return "Vietnamita";
        }
        if (languageCode.contains("el")) {
            return "Grego";
        }
        if (languageCode.contains("he")) {
            return "Hebraico";
        }
        if (languageCode.contains("id")) {
            return "Indonésio";
        }
        return "Desconhecido";
    }

    public static String translateSentiment(String sentiment) {
        if (sentiment == null) {
            return "Desconhecido";
        }
        return switch (sentiment.toLowerCase()) {
            case "positive" -> "Positivo";
            case "negative" -> "Negativo";
            case "neutral" -> "Neutro";
            default -> "Desconhecido";
        };
    }
}

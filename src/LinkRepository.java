import java.util.HashMap;
import java.util.Map;

/**
 * Репозиторий для управления сокращенными URL-адресами.
 */
public class LinkRepository {
    private final Map<String, Map<String, ShortUrl>> urls = new HashMap<>();

    public void saveShortUrl(String userId, ShortUrl shortUrl) {
        urls.computeIfAbsent(userId, k -> new HashMap<>()).put(shortUrl.getUrl(), shortUrl);
    }

    public ShortUrl getShortUrl(String userId, String shortUrl) {
        return urls.getOrDefault(userId, new HashMap<>()).get(shortUrl);
    }

    public boolean removeShortUrl(String userId, String shortUrl) {
        Map<String, ShortUrl> userUrls = urls.get(userId);
        return userUrls != null && userUrls.remove(shortUrl) != null;
    }
}
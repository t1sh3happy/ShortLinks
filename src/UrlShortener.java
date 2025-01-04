import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Управление кодированием и декодированием URL-адресов.
 */
public class UrlShortener {

    private static final Logger LOGGER = Logger.getLogger(UrlShortener.class.getName());
    private final Map<String, Map<String, ShortUrl>> userLinks = new HashMap<>();

    /**
     * Кодирует исходный URL-адрес в сокращенную версию и связывает его с пользователем.
     * Параметр originalUrl Исходный URL-адрес, который нужно сократить.
     * Параметр userId Идентификатор пользователя, который будет связан с сокращенным URL-адресом.
     * Параметр clickLimit Максимальное количество разрешенных кликов.
     * Параметр expiryTimeMs Время в миллисекундах до истечения срока действия ссылки.
     * Возврат сокращенного URL-адрес.
     */
    public String encode(String originalUrl, String userId, int clickLimit, long expiryTimeMs) {
        Date dateCreated = new Date();
        String hash = generateHash(originalUrl + userId + dateCreated.getTime() + clickLimit);
        ShortUrl shortUrl = new ShortUrl(hash, originalUrl, dateCreated, clickLimit, expiryTimeMs);
        saveShortUrl(userId, shortUrl);
        return shortUrl.getUrl();
    }

    /**
     * Декодирует сокращенный URL-адрес в исходную форму, если он действителен.
     * Параметр userId Идентификатор пользователя, связанный с URL-адресом.
     * Параметр shortUrl Сокращенный URL-адрес, который необходимо декодировать.
     * Возврат объекта ShortUrl, если он найден и действителен, в противном случае — значение NULL.
     */
    public ShortUrl decode(String userId, String shortUrl) {
        ShortUrl urlData = userLinks.getOrDefault(userId, new HashMap<>()).get(shortUrl);

        if (urlData == null) {
            LOGGER.info("Ссылка не найдена.");
            return null;
        }

        if (urlData.isExpired()) {
            LOGGER.info("Срок действия ссылки истек и она была удалена.");
            deleteLink(userId, shortUrl);
            return null;
        }

        if (urlData.isLimitReached()) {
            LOGGER.info("Лимит переходов достигнут. Ссылка была удалена.");
            notifyUser(userId, "Ссылка с адресом " + shortUrl + " была удалена, так как лимит переходов был исчерпан.");
            deleteLink(userId, shortUrl);
            return null;
        }

        return urlData;
    }

    /**
     * Удаляет сокращенный URL-адрес, связанный с пользователем.
     * Параметр userId Идентификатор пользователя.
     * Параметр shortUrl Сокращенный URL-адрес.
     * Возврат True, если удалено успешно, в противном случае — false.
     */
    public boolean deleteLink(String userId, String shortUrl) {
        Map<String, ShortUrl> links = userLinks.get(userId);
        return links != null && links.remove(shortUrl) != null;
    }


    /**
     * Проверяет, существует ли сокращенный URL-адрес для пользователя.
     * Параметр userId Идентификатор пользователя.
     * Параметр shortUrl Сокращенный URL-адрес.
     * Возвращает True, если URL-адрес существует, и false в противном случае.
     */
    public boolean containsShortUrl(String userId, String shortUrl) {
        return userLinks.getOrDefault(userId, new HashMap<>()).containsKey(shortUrl);
    }

    /**
     * Сохраняет объект ShortUrl, связанный с пользователем.
     * Параметр userId Идентификатор пользователя.
     * Параметр shortUrl объекта ShortUrl.
     */
    private void saveShortUrl(String userId, ShortUrl shortUrl) {
        userLinks.computeIfAbsent(userId, k -> new HashMap<>()).put(shortUrl.getUrl(), shortUrl);
    }

    /**
     * Генерирует хэш для данной входной строки.
     * Параметр input Входная строка.
     * Возвращает сгенерированное значение хеш-функции в виде строки.
     */
    private String generateHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes());
            return Base64.getUrlEncoder().encodeToString(hashBytes).substring(0, 7);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Ошибка генерации хеша.", e);
            return "";
        }
    }

    /**
     * Уведомляет пользователя о событиях, связанных с его URL-адресами.
     * Параметр userId Идентификатор пользователя для уведомления.
     * Параметр message Уведомительное сообщение.
     */
    private void notifyUser(String userId, String message) {
        System.out.println("Уведомление для пользователя " + userId + ": " + message);
        // В будущем метод может быть расширен для использования других методов уведомления.
    }
}
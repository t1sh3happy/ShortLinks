import java.util.Date;

/**
 * Представление сокращенного URL-адреса и связанного с ним данных.
 */
public class ShortUrl {

    private final String url;
    private final String originalUrl;
    private int clickThroughLimit;
    private int actualNumberClicks;
    private final Date dateCreation;
    private long expiryTimeMs;

    public ShortUrl(String hashValue, String originalUrl, Date dateCreation, int clickThroughLimit, long expiryTimeMs) {
        this.url = "http://short.url/" + hashValue;
        this.originalUrl = originalUrl;
        this.clickThroughLimit = clickThroughLimit;
        this.actualNumberClicks = 0;
        this.dateCreation = dateCreation;
        this.expiryTimeMs = expiryTimeMs;
    }

    public String getUrl() { return url; }

    public String getOriginalUrl() { return originalUrl; }

    public boolean isLimitReached() {
        return actualNumberClicks >= clickThroughLimit;
    }

    public boolean isExpired() {
        return new Date().getTime() - dateCreation.getTime() > expiryTimeMs;
    }

    public void incrementClicks() {
        if (!isLimitReached()) {
            actualNumberClicks++;
            System.out.println("Осталось переходов: " + (clickThroughLimit - actualNumberClicks));
        } else {
            System.out.println("Лимит переходов достигнут.");
        }
    }

    public void resetClickThroughLimit(int newLimit) {
        clickThroughLimit = newLimit;
        actualNumberClicks = 0;
    }

    public void updateExpiryDate(long newExpiryTimeMs) {
        this.expiryTimeMs = newExpiryTimeMs;
        System.out.println("Срок действия ссылки обновлен.");
    }
}
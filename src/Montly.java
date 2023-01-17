import java.time.LocalDateTime;

public class Montly implements Repeatability{
    @Override
    public LocalDateTime nextTime(LocalDateTime currentDateTime) {
        return currentDateTime.plusMonths(1);
    }

    @Override
    public String title() {
        return "ежемесячная";
    }
}

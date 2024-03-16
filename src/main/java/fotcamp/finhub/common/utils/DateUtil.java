package fotcamp.finhub.common.utils;

import java.time.LocalDate;

public class DateUtil {
    public static LocalDate convertToDate(Long year, Long month, Long day) {
        // LocalDate 객체 생성
        return LocalDate.of(year.intValue(), month.intValue(), day.intValue());
    }
}

package ressources;

import java.util.Date;

/**
 * @author Jessy Lachapelle
 * @since 2016/07/15
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class DateParser {
  public static Date dateFromString(String dateString) {
    Date date = new Date();

    String year = dateString.substring(0, 4);
    String month = dateString.substring(5, 7);
    String day = dateString.substring(8, 10);

    date.setYear(Integer.parseInt(year) - 1900);
    date.setMonth(Integer.parseInt(month) - 1);
    date.setDate(Integer.parseInt(day));

    return date;
  }

  public static String dateToString(Date date) {
    return (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
  }
}

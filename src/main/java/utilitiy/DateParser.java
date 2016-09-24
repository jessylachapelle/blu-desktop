package utilitiy;

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
    String YYYY = Integer.toString((date.getYear() + 1900));
    String MM = Integer.toString((date.getMonth() + 1));
    String DD = Integer.toString(date.getDate());

    if ((date.getMonth() + 1) <= 9) {
      MM = "0" + MM;
    }

    if (date.getDate() <= 9) {
      DD = "0" + DD;
    }


    return YYYY + "-" + MM + "-" + DD;
  }

  public static String toLongDate(Date date) {
    String[] months = {"janvier", "févier", "mars", "avril", "mai", "juin", "juillet", "août", "septembre", "octobre", "novembre", "décembre"};
    String YYYY = Integer.toString((date.getYear() + 1900));
    String DD = Integer.toString(date.getDate());

    return DD + " " + months[date.getMonth()] + " " + YYYY;
  }
}

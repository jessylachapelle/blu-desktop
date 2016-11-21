package handler;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import api.APIConnector;
import model.item.Reservation;
import utility.DateParser;

/**
 * @author Jessy Lachapelle
 * @since 2016/11/13
 * @version 1.0
 */
public class AdminHandler {
  private ArrayList<Reservation> reservations;

  public AdminHandler() {
    reservations = new ArrayList<>();
  }

  public boolean deleteReservations() {
    JSONObject req = new JSONObject();

    req.put("object", "reservation");
    req.put("function", "deleteAll");
    req.put("data", new JSONObject());

    JSONObject res = APIConnector.call(req);
    JSONObject data = res.optJSONObject("data");

    if (data != null && data.optInt("code", 0) == 200) {
      reservations.clear();
      return true;
    }

    return false;
  }

  public boolean deleteReservations(Date from, Date to) {
    JSONObject req = new JSONObject();
    JSONObject data = new JSONObject();

    data.put("from", DateParser.dateToString(from));
    data.put("to", DateParser.dateToString(to));

    req.put("object", "reservation");
    req.put("function", "deleteRange");
    req.put("data", data);

    JSONObject res = APIConnector.call(req);
    data = res.optJSONObject("data");

    if (data != null && data.optInt("code", 0) == 200) {
      reservations.stream().filter(r -> from.before(r.getDate()) && to.after(r.getDate())).forEach(r -> reservations.remove(r));
      return true;
    }

    return false;
  }

  public boolean deleteStorage() {
    JSONObject req = new JSONObject();

    req.put("object", "storage");
    req.put("function", "delete");
    req.put("data", new JSONObject());

    JSONObject res = APIConnector.call(req);
    JSONObject data = res.optJSONObject("data");

    return data != null && data.optInt("code", 0) == 200;
  }

  public ArrayList<Reservation> getReservations() {
    return reservations;
  }

  public boolean selectReservations() {
    reservations.clear();
    JSONObject req = new JSONObject();

    req.put("object", "reservation");
    req.put("function", "select");
    req.put("data", new JSONObject());

    JSONObject res = APIConnector.call(req);
    JSONArray data = res.optJSONArray("data");

    if (data != null) {
      for (int i = 0; i < data.length(); i++) {
        JSONObject r = data.optJSONObject(i);
        if (r != null) {
          reservations.add(new Reservation(r));
        }
      }
    }

    return data != null;
  }
}

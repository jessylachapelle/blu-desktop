package api;

import org.json.JSONException;
import org.json.JSONObject;
import utility.Dialog;
import utility.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Calls API
 * Sends and receives JSON
 *
 * @author Jessy Lachapelle
 * @since 12/05/2016
 * @version 1.0
 */
public class APIConnector {
  private static final String API_KEY = Settings.apiKey();
  private static final String API_URL = Settings.apiUrl();

  /**
   * Calls the API and sends it JSON.
   * Receives JSON in response.
   * @param req JSONObject to send to API
   * @return JSONObject containing response from API or error
   */
  public static JSONObject call(JSONObject req) {
    try {
      URL url = new URL(API_URL);
      addAPIKey(req);

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoInput(true);
      connection.setDoOutput(true);

      String jsonData = "req=" + req.toString();
      System.out.println(jsonData);
      PrintWriter vars = new PrintWriter(connection.getOutputStream());
      vars.println(jsonData);
      vars.close();

      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
      String response = bufferedReader.readLine();
      bufferedReader.close();

      return new JSONObject(response);
    } catch (MalformedURLException e) {
      return error(422, "INVALID_DATA");
    } catch (ConnectException e) {
      Dialog.information("La connection au serveur n'a pu être établie, veuillez vérifier votre connection internet");
      return error(404, "NOT_FOUND");
    } catch (IOException | JSONException e) {
      return error(500, "INTERNAL_SERVER_ERROR");
    }
  }

  private static void addAPIKey(JSONObject req) {
    try {
      req.put("apikey", API_KEY);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private static JSONObject error(int code, String message) {
    JSONObject error = new JSONObject();
    JSONObject data = new JSONObject();

    try {
      data.put("code", code);
      data.put("message", message);
      error.put("data", data);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return error;
  }
}

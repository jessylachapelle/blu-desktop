package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Calls API
 * Sends and receives JSON
 *
 * @author Jessy Lachapelle
 * @since 12/05/2016
 * @version 1.0
 */
public class APIConnector {
  private static final String API_KEY = "8ecf71749e3a5a5f02d585943e81849f";
  private static final String API_URL = "http://jessylachapelle.no-ip.biz/blu/desktop/query.php";

  /**
   * Calls the API and sends it JSON.
   * Receives JSON in response.
   * @param json JSONObject to send to API
   * @return JSONObject containing response from API or error
   */
  public static JSONObject call(JSONObject json) {
    try {
      URL url = new URL(API_URL);
      json.append("apikey", API_KEY);

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoInput(true);
      connection.setDoOutput(true);

      String jsonData = "jsonData=" + json.toString();
      PrintWriter vars = new PrintWriter(connection.getOutputStream());
      vars.println(jsonData);
      vars.close();

      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
      String response = bufferedReader.readLine();
      bufferedReader.close();

      JSONObject responseJSON = new JSONObject(response);
      return responseJSON;
    } catch (MalformedURLException | JSONException e) {
      // TODO: return ERROR 422 INVALID_DATA
      return null;
    } catch (IOException e) {
      // TODO: return ERROR 500 INTERNAL_SERVER_ERROR
      return null;
    }
  }
}

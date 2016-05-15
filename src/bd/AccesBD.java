package bd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Envoie de requêtes à un fichier PHP sur le serveur
 *
 * @author Jessy Lachapelle
 * @since 14/11/2015
 * @version 0.1
 */
public class AccesBD {
  static final String API_KEY = "8ecf71749e3a5a5f02d585943e81849f";
  static final String DEFAULT_URL = "http://localhost/blu-api/query.php";
  //static final String DEFAULT_URL = "http://jessylachapelle.no-ip.biz/blu/desktop/query.php";

  /**
   * Recupère des données JSON du serveur
   *
   * @param query Requête à envoyer au serveur
   * @return Objet JSON
   */
  public static ArrayList<JSONObject> executeQuery(String query) {
    return executeQuery(DEFAULT_URL, query, "");
  }

  public static ArrayList<JSONObject> executeQuery(String query, String param) {
    return executeQuery(DEFAULT_URL, query, param);
  }

  /**
   * Recupère des données JSON du serveur
   *
   * @param urlStr URL du fichier PHP
   * @param param Paramètre supplémentaire
   * @param query Requête à envoyer au serveur
   * @return Objet JSON
   */
  public static ArrayList<JSONObject> executeQuery(String urlStr, String query, String param) {
    ArrayList<JSONObject> jsonObjects = new ArrayList<>();

    try {
      URL url = new URL(urlStr);

      HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
      connexion.setRequestMethod("POST");
      connexion.setDoOutput(true);

      PrintWriter vars = new PrintWriter(connexion.getOutputStream());

      if (param.equals(""))
        vars.println("q=" + query);
      else
        vars.println("q=" + query + "&" + param + "=true");

      vars.close();

      BufferedReader resultat = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
      String line = resultat.readLine();
      resultat.close();

      line = line.replace("],[", "#");
      line = line.replace("[", "");
      line = line.replace("]", "");
      line = line.replace("},{", ",");

      String[] results;

      if (!line.matches("")) {
        results = line.split("#");
      } else {
        results = new String[0];
      }

      for (int noResult = 0; noResult < results.length; noResult++) {
        JSONObject json = new JSONObject(results[noResult]);
        jsonObjects.add(json);
      }
      return jsonObjects;
    } catch (JSONException | IOException e) {
      Logger.getLogger(AccesBD.class.getName()).log(Level.SEVERE, null, e);
      return jsonObjects;
    }
  }
  
  public static ArrayList<JSONObject> executeFunction(String function, String[][] params) {
    ArrayList<JSONObject> jsonObjects = new ArrayList<>();

    try {
      URL url = new URL(DEFAULT_URL);

      HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
      connexion.setRequestMethod("POST");
      connexion.setDoOutput(true);

      PrintWriter vars = new PrintWriter(connexion.getOutputStream());
      
      String varsStr = "f=" + function;
      
      for(String[] param : params)
        varsStr += "&" + param[0] + "=" + param[1];
      
      vars.println(varsStr);
      vars.close();

      BufferedReader resultat = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
      String line = resultat.readLine();
      resultat.close();

      line = line.replace("],[", "#");
      line = line.replace("[", "");
      line = line.replace("]", "");
      line = line.replace("},{", ",");

      String[] results;

      if (!line.matches("")) {
        results = line.split("#");
      } else {
        results = new String[0];
      }

      for(String result : results) {
        JSONObject json = new JSONObject(result);
        jsonObjects.add(json);
      }
      return jsonObjects;
    } catch (JSONException | IOException e) {
      Logger.getLogger(AccesBD.class.getName()).log(Level.SEVERE, null, e);
      return jsonObjects;
    }
  }

  /**
   * Modification de la base de données
   *
   * @param query Requête à envoyer au serveur
   * @return Vrai si la mise à jour réussie
   */
  public static boolean executeUpdate(String query) {
    return executeUpdate(DEFAULT_URL, query);
  }

  /**
   * Modification de la base de données
   *
   * @param query Requête à envoyer au serveur
   * @return Vrai si la mise à jour réussie
   */
  public static int executeInsert(String query) {
    return executeInsert(DEFAULT_URL, query);
  }

  /**
   * Modification de la base de données
   *
   * @param urlStr URL du fichier PHP
   * @param query Requête à envoyer au serveur
   * @return Vrai si la mise à jour réussie
   */
  public static boolean executeUpdate(String urlStr, String query) {
    try {
      URL url = new URL(urlStr);

      HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
      connexion.setRequestMethod("POST");
      connexion.setDoOutput(true);

      PrintWriter vars = new PrintWriter(connexion.getOutputStream());
      vars.println("q=" + query);
      vars.close();

      BufferedReader resultat = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
      String line = resultat.readLine();
      resultat.close();
    } catch (MalformedURLException | ProtocolException e) {
      return false;
    } catch (IOException ex) {
      return false;
    }

    return true;
  }
  
  /**
   * Modification de la base de données
   * @param function Requête à envoyer au serveur
   * @param params
   * @return Vrai si la mise à jour réussie
   */
  public static boolean executeUpdate(String function, String[][] params) {
    try {
      URL url = new URL(DEFAULT_URL);

      HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
      connexion.setRequestMethod("POST");
      connexion.setDoOutput(true);

      PrintWriter vars = new PrintWriter(connexion.getOutputStream());
      
      String varsStr = "f=" + function;
      
      for(String[] param : params)
        varsStr += "&" + param[0] + "=" + param[1];
      vars.println(varsStr);
      vars.close();

      BufferedReader resultat = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
      String line = resultat.readLine();
      resultat.close();
    } catch (MalformedURLException | ProtocolException e) {
      return false;
    } catch (IOException ex) {
      return false;
    }

    return true;
  }

  /**
   * Modification de la base de données
   *
   * @param urlStr URL du fichier PHP
   * @param query Requête à envoyer au serveur
   * @return Vrai si la mise à jour réussie
   */
  public static int executeInsert(String urlStr, String query) {
    try {
      URL url = new URL(urlStr);

      HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
      connexion.setRequestMethod("POST");
      connexion.setDoOutput(true);

      PrintWriter vars = new PrintWriter(connexion.getOutputStream());
      vars.println("q=" + query);
      vars.close();

      BufferedReader resultat = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
      String line = resultat.readLine();
      resultat.close();

      return Integer.parseInt(line);
    } catch (MalformedURLException | ProtocolException e) {
      return 0;
    } catch (IOException ex) {
      return 0;
    }
  }
  
  /**
   * Modification de la base de données
   *
   * @param urlStr URL du fichier PHP
   * @param function Requête à envoyer au serveur
   * @param params
   * @return Vrai si la mise à jour réussie
   */
  public static int executeInsert(String urlStr, String function, String[][] params) {
    try {
      URL url = new URL(urlStr);

      HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
      connexion.setRequestMethod("POST");
      connexion.setDoOutput(true);

      PrintWriter vars = new PrintWriter(connexion.getOutputStream());
      
      String varsStr = "f=" + function;
      
      for(String[] param : params)
        varsStr += "&" + param[0] + "=" + param[1];
      vars.println(varsStr);
      vars.close();

      BufferedReader resultat = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
      String line = resultat.readLine();
      resultat.close();

      return Integer.parseInt(line);
    } catch (MalformedURLException | ProtocolException e) {
      return 0;
    } catch (IOException e) {
      return 0;
    }
  }
  
  public static JSONObject executeFunction(JSONObject json) {
    try {
      URL url = new URL(DEFAULT_URL);
      json.append("apikey", API_KEY);

      HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
      connexion.setRequestMethod("POST");
      connexion.setDoInput(true);
      connexion.setDoOutput(true);

      String jsonStr = json.toString()
                      .replace("\\", "")
                      .replace("\"[\"", "")
                      .replace("\"]\"", "")
                      .replace("\",\"", ",")
                      .replace("[\"", "\"")
                      .replace("\"]", "\"")
                      .replace("\"{", "{")
                      .replace("}\"", "}");

      String jsonData = "jsonData=" + jsonStr;
      PrintWriter vars = new PrintWriter(connexion.getOutputStream());
      vars.println(jsonData);
      vars.close();

      BufferedReader resultat = new BufferedReader(new InputStreamReader(connexion.getInputStream(), "utf-8"));
      String str = resultat.readLine();
      JSONObject result = new JSONObject(str);
      resultat.close();

      return result;
    } catch (MalformedURLException | JSONException | ProtocolException e) {
      return null;
    } catch (IOException e) {
      return null;
    }
  }
}
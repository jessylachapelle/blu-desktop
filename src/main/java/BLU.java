import java.io.*;

import api.APIConnector;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.*;

import org.json.JSONObject;
import utility.Dialog;
import utility.I18N;
import utility.Settings;

/**
 * Classe principal du logiciel pour démarrer les interfaces
 * @author Alizée Fournier
 * @since 12/11/2015
 * @version 0.1
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class BLU extends Application {
  private static I18N i18n;
  private static Settings settings;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    settings = new Settings();
    i18n = new I18N();

    if (!hasAPIConnection()) {
      Dialog.information("Veuillez vérifier votre connexion au réseau puis redémarrer l'application");
      Platform.exit();
    }

    double width = Screen.getPrimary().getVisualBounds().getWidth();
    double height = Screen.getPrimary().getVisualBounds().getHeight();

    stage.setTitle(i18n.getString("title"));
    stage.getIcons().add(new Image(getClass().getResourceAsStream("images/icon.png")));
    stage.setWidth(width);
    stage.setHeight(height);
    //stage.setMaximized(true);

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("layout/window.fxml"));
      Pane window = loader.load();

      Scene scene = new Scene(window);
      scene.getStylesheets().addAll("css/window.css");
      stage.setScene(scene);
      stage.show();
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  private boolean hasAPIConnection() {
    JSONObject res = APIConnector.call(new JSONObject());
    JSONObject data = res.optJSONObject("data");
    return data != null && data.optInt("code", 404) != 404;
  }
}

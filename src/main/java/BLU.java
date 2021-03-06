import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.json.JSONObject;

import api.APIConnector;
import layout.FlexVBox;
import utility.Dialog;
import utility.Settings;

/**
 * Classe principal du logiciel pour démarrer les interfaces
 * @author Alizée Fournier
 * @since 12/11/2015
 * @version 0.1
 */
public class BLU extends Application {
  // TODO: i18n
  // private static I18N i18n;
  @SuppressWarnings({"FieldCanBeLocal", "unused"})
  private static Settings settings;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    settings = new Settings();
    // TODO: i18n
    // i18n = new I18N();

    if (!_hasAPIConnection()) {
      Dialog.information("Veuillez vérifier votre connexion au réseau puis redémarrer l'application");
      Platform.exit();
    }

    double width = Screen.getPrimary().getVisualBounds().getWidth();
    double height = Screen.getPrimary().getVisualBounds().getHeight();

    stage.setTitle("Banque de Livres Usagés (BETA)");
    stage.getIcons().add(new Image(getClass().getResourceAsStream("images/icon.png")));
    stage.setWidth(width);
    stage.setHeight(height);
    stage.setMaximized(true);

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("layout/window.fxml"));
      FlexVBox window = loader.load();

      Scene scene = new Scene(window);
      scene.getStylesheets().addAll("css/window.css");
      stage.setScene(scene);
      stage.show();
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  private boolean _hasAPIConnection() {
    JSONObject res = APIConnector.call(new JSONObject());
    JSONObject data = res.optJSONObject("data");
    return data != null && data.optInt("code", 404) != 404;
  }
}

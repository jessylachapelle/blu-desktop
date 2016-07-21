import java.io.*;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import ressources.I18N;

/**
 * Classe principal du logiciel pour démarrer les interfaces
 * @author Alizée Fournier
 * @since 12/11/2015
 * @version 0.1
 */
public class BLU extends Application {
  I18N i18n;

  @Override
  public void start(Stage primaryStage) {
    i18n = new I18N();

    primaryStage.setTitle(i18n.getString("title"));
    primaryStage.setWidth(900);
    primaryStage.setHeight(700);
    primaryStage.setMaximized(false);

    try {
      FXMLLoader loader = new FXMLLoader(BLU.class.getResource("view/layout/window.fxml"));
      BorderPane rootLayout = loader.load();

      Scene scene = new Scene(rootLayout);
      scene.getStylesheets().add("view/css/window.css");
      scene.getStylesheets().add("view/css/memberForm.css");
      scene.getStylesheets().add("view/css/recherche.css");
      scene.getStylesheets().add("view/css/memberView.css");
      scene.getStylesheets().add("view/css/itemForm.css");
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}

import java.io.*;

import javafx.application.*;
import javafx.fxml.FXML;
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

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    i18n = new I18N();

    double width = Screen.getPrimary().getVisualBounds().getWidth();
    double height = Screen.getPrimary().getVisualBounds().getHeight();

    primaryStage.setTitle(i18n.getString("title"));
    primaryStage.setWidth(width);
    primaryStage.setHeight(height);
    //primaryStage.setMaximized(true);

    try {
      FXMLLoader loader = new FXMLLoader(BLU.class.getResource("view/layout/window.fxml"));
      Pane window = loader.load();

      Scene scene = new Scene(window);
      scene.getStylesheets().addAll("view/css/window.css", "view/css/memberForm.css", "view/css/recherche.css", "view/css/memberView.css", "view/css/itemForm.css");
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch(IOException e) {
      e.printStackTrace();
    }
  }
}
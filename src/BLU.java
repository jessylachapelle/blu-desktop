import java.io.IOException;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;

/**
 * Classe principal du logiciel pour démarrer les interfaces
 * @author Alizée Fournier
 * @since 12/11/2015
 * @version 0.1
 */
public class BLU extends Application {
  private Stage primaryStage;
  private BorderPane rootLayout;
  private Pane panel;
  private Scene scene;

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("Banque de livres usagés");
    this.primaryStage.setWidth(900);
    this.primaryStage.setHeight(700);
    this.primaryStage.setMaximized(false);

    try {
      FXMLLoader loader = new FXMLLoader(BLU.class.getResource("view/layout/window.fxml"));
      rootLayout = (BorderPane) loader.load();
      afficheRecherche();

      scene = new Scene(rootLayout);
      scene.getStylesheets().add("view/css/window.css");
      scene.getStylesheets().add("view/css/memberForm.css");
      scene.getStylesheets().add("view/css/recherche.css");
      scene.getStylesheets().add("view/css/memberView.css");
      scene.getStylesheets().add("view/css/itemForm.css");
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch(IOException e) {
      System.out.println(e);
    }
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Methode qui affiche le panel de recherche
   */
  public void afficheRecherche(){
    affichePanel("view/layout/search.fxml");
  }

  /**
   * Methode qui affiche le panel de recherche en cliquant sur membre
   */
  public void afficheRechercheMembre() {
    affichePanel("view/layout/search.fxml");
  }

  /**
   * Methode qui affiche le panel de recherche en cliquant sur ouvrage
   */
  public void afficheRechercheOuvrage() {
    affichePanel("view/layout/search.fxml");
  }

  /**
   * Methode qui affiche le panel d'ajout d'article
   */
  public void ajoutArticle() {
    affichePanel("view/layout/itemForm.fxml");
  }

  private void affichePanel(String panelPath) {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource(panelPath));
      panel = (Pane) loader.load();
      rootLayout.setAlignment(panel, Pos.TOP_LEFT);
      //rootLayout.setCenter(panel);
    } catch(IOException e) {
      System.out.println(e);
    }
  }
}

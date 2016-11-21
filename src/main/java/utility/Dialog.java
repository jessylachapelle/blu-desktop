package utility;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

/**
 * Classe pour gérer les fenêtres de dialogue
 * @author Jessy
 * @since 28/11/2015
 * @version 0.1
 */
public class Dialog {
  /**
   * Ouvre une boîte de dialogue pour que l'utilisateur confirme l'action entreprise
   * @param message Le message à afficher à l'utilisateur
   * @return Vrai si l'utilisateur confirme
   */
  public static boolean confirmation(String message) {
    return confirmation("Confirmation", message);
  }

  public static int confirmation(String message, boolean canCancel) {
    return confirmation("Confirmation", message, canCancel);
  }

  /**
   * Ouvre une boîte de dialogue pour que l'utilisateur confirme l'action entreprise
   * @param title Le title de la fenêtre de dialogue
   * @param message Le message à afficher à l'utilisateur
   * @return Vrai si l'utilisateur confirme
   */
  @SuppressWarnings("WeakerAccess")
  public static boolean confirmation(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);

    Optional<ButtonType> result = alert.showAndWait();
    return result.isPresent() && result.get() == ButtonType.OK;
  }

  @SuppressWarnings("WeakerAccess")
  public static int confirmation(String title, String message, boolean canCancel) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setContentText(message);

    ButtonType accept = new ButtonType("Oui");
    ButtonType decline = new ButtonType("Non");
    alert.getButtonTypes().setAll(accept, decline);

    if (canCancel) {
      ButtonType cancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
      alert.getButtonTypes().add(cancel);
    }

    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == accept) {
      return 1;
    }

    if (result.isPresent() && result.get() == decline) {
      return 0;
    }

    return -1;
  }

  /**
   * Ouvre une boîte de dialogue avec un champs de saisie
   * @param title Le title de la fenêtre de dialogue
   * @param message Le message à afficher à l'utilisateur
   * @return Le texte saisie ou une String vide
   */
  public static String input(String title, String message) {
    return input(title, message, "");
  }

  /**
   * Ouvre une boîte de dialogue avec un champs de saisie
   * @param title Le title de la fenêtre de dialogue
   * @param message Le message à afficher à l'utilisateur
   * @param content Le content pré-rempli du champs
   * @return Le texte saisie ou une String vide
   */
  public static String input(String title, String message, String content) {
    TextInputDialog dialog = new TextInputDialog(content);
    dialog.setTitle(title);
    dialog.setHeaderText(null);
    dialog.setContentText(message);

    Optional<String> result = dialog.showAndWait();

    if (result.isPresent())
      return result.get();
    return "";
  }

  /**
   * Ouvre une boîte de dialogue informative
   * @param message Le message à afficher
   */
  public static void information(String message) {
    information("Information", message);
  }
  
  /**
   * Ouvre une boîte de dialogue informative
   * @param title Le title de la fenêtre de dialogue
   * @param message Le message à afficher
   */
  public static void information(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}

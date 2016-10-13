package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.json.JSONObject;

/**
 * Controller to handle author inputs
 * @author Jessy Lachapelle
 * @since 2016-10-12
 * @version 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class AuthorController extends PanelController {

  @FXML private TextField txtAuthorFirstName;
  @FXML private TextField txtAuthorLastName;

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  /**
   * Set the value of the textfield
   * @param firstName The author's first name
   */
  public void setAuthorFirstName(String firstName) {
    txtAuthorFirstName.setText(firstName);
  }

  /**
   * Set the value of the textfield
   * @param lastName The author's last name
   */
  public void setAuthorLastName(String lastName) {
    txtAuthorLastName.setText(lastName);
  }

  /**
   * Set the value of the textfields
   * @param firstName The author's first name
   * @param lastName The author's last name
   */
  public void setAuthorName(String firstName, String lastName) {
    setAuthorFirstName(firstName);
    setAuthorLastName(lastName);
  }

  /**
   * Get the input value of the first name
   * @return String the first name in the textfield
   */
  public String getAuthorFirstName() {
    return txtAuthorFirstName.getText();
  }

  /**
   * Get the input value of the last name
   * @return String the last name in the textfield
   */
  public String getAuthorLastName() {
    return txtAuthorLastName.getText();
  }

  /**
   * Convert fields to JSON
   * { "first_name": "", "last_name": "" }
   * @return JSONObject
   */
  public JSONObject toJSON() {
    JSONObject author = new JSONObject();

    author.put("first_name", getAuthorFirstName());
    author.put("last_name", getAuthorLastName());

    return author;
  }

  /**
   * Converts JSON to textfield values
   * @param author The JSONObject with the author data
   */
  public void fromJSON(JSONObject author) {
    setAuthorFirstName(author.optString("first_name", ""));
    setAuthorLastName(author.optString("last_name", ""));
  }
}
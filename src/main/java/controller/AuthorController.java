package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.json.JSONObject;

import model.item.Author;

/**
 * Controller to handle author inputs
 * @author Jessy Lachapelle
 * @since 2016-10-12
 * @version 1.0
 */
public class AuthorController implements javafx.fxml.Initializable {

  @FXML private TextField txtAuthorFirstName;
  @FXML private TextField txtAuthorLastName;

  private Author author;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    author = new Author();
  }

  /**
   * Converts JSON to textfield values
   * @param author The JSONObject with the author data
   */
  public void fromJSON(JSONObject author) {
    this.author.fromJSON(author);
    _setAuthorFirstName(author.optString("first_name", ""));
    _setAuthorLastName(author.optString("last_name", ""));
  }

  /**
   * Get the Id of the binded author, 0 if no id
   * @return The id of the author
   */
  public int getId() {
    return author.getId();
  }

  /**
   * Get the input value of the last name
   * @return String the last name in the textfield
   */
  public String getAuthorLastName() {
    return txtAuthorLastName.getText();
  }

  /**
   * Set the author binded to the controller
   * @param author The author passed to the controller
   */
  public void setAuthor(Author author) {
    this.author = author;
    txtAuthorFirstName.setText(author.getFirstName());
    txtAuthorLastName.setText(author.getLastName());
  }

  /**
   * Convert fields to JSON
   * { "id": 0, "first_name": "", "last_name": "" }
   * @return JSONObject
   */
  public JSONObject toJSON() {
    if (!_getAuthorFirstName().equals(this.author.getFirstName()) ||
        !getAuthorLastName().equals(this.author.getLastName())) {
      JSONObject author = new JSONObject();

      if (this.author.getId() != 0) {
        author.put("id", this.author.getId());
      }

      author.put("first_name", _getAuthorFirstName());
      author.put("last_name", getAuthorLastName());

      return author;
    }

    return null;
  }


  /**
   * Get the input value of the first name
   * @return String the first name in the textfield
   */
  private String _getAuthorFirstName() {
    return txtAuthorFirstName.getText();
  }

  /**
   * Set the value of the textfield
   * @param firstName The author's first name
   */
  private void _setAuthorFirstName(String firstName) {
    txtAuthorFirstName.setText(firstName);
  }

  /**
   * Set the value of the textfield
   * @param lastName The author's last name
   */
  private void _setAuthorLastName(String lastName) {
    txtAuthorLastName.setText(lastName);
  }
}
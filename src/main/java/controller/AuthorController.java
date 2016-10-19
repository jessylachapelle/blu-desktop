package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import model.item.Author;
import org.json.JSONObject;

/**
 * Controller to handle author inputs
 * @author Jessy Lachapelle
 * @since 2016-10-12
 * @version 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class AuthorController implements javafx.fxml.Initializable {

  @FXML private TextField txtAuthorFirstName;
  @FXML private TextField txtAuthorLastName;

  private Author author;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    author = new Author();
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
   * Get the Id of the binded author, 0 if no id
   * @return The id of the author
   */
  public int getId() {
    return author.getId();
  }

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
   * { "id": 0, "first_name": "", "last_name": "" }
   * @return JSONObject
   */
  public JSONObject toJSON() {
    if (!getAuthorFirstName().equals(this.author.getFirstName()) ||
        !getAuthorLastName().equals(this.author.getLastName())) {
      JSONObject author = new JSONObject();

      if (this.author.getId() != 0) {
        author.put("id", this.author.getId());
      }

      author.put("first_name", getAuthorFirstName());
      author.put("last_name", getAuthorLastName());

      return author;
    }

    return null;
  }

  /**
   * Converts JSON to textfield values
   * @param author The JSONObject with the author data
   */
  public void fromJSON(JSONObject author) {
    this.author.fromJSON(author);
    setAuthorFirstName(author.optString("first_name", ""));
    setAuthorLastName(author.optString("last_name", ""));
  }
}
package controller;

import handler.ItemHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.item.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Jessy Lachapelle
 * @since 2016-09-05
 * @version 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BookTabController extends PanelController {

  // @FXML private SubjectController subject;

  @FXML private TextField txtTitle;
  @FXML private TextField txtEdition;
  @FXML private TextField txtEditor;
  @FXML private TextField txtPublication;
  @FXML private TextField txtEan13;
  @FXML private CheckBox cbHasEan13;
  @FXML private TextArea txtComment;
  @FXML private Pane subject;

  @FXML private VBox authors;
  @FXML private Button btnRemoveAuthor;
  @FXML private Button btnAddAuthor;

  @FXML private Button btnCancel;
  @FXML private Button btnSave;

  private ItemHandler itemHandler;
  private ArrayList<AuthorController> authorControllers;
  private SubjectController subjectController;

  final private int MIN_AUTHOR = 1;
  final private int MAX_AUTHOR = 5;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    itemHandler = new ItemHandler();
    authorControllers = new ArrayList<>();
    _addAuthor();
    updateAuthorButtons();
    _eventHandlers();
    _initSubjectPane();
  }

  private void _initSubjectPane() {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource("layout/subject.fxml"));

    try {
      subject.getChildren().add(loader.load());
      subjectController = loader.getController();
      subjectController.init(itemHandler);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void _addAuthor() {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource("layout/author.fxml"));

    try {
      authors.getChildren().add(loader.load());
      authorControllers.add(loader.getController());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void _removeAuthor(int index) {
    if (index >= 0 && index < authorControllers.size()) {
      authorControllers.remove(index);
      authors.getChildren().remove(index);
    }
  }

  private void removeLastAuthor() {
    _removeAuthor(authorControllers.size() - 1);
  }

  public Button getBtnSave() {
    return btnSave;
  }

  public Item getItem() {
    return itemHandler.getItem();
  }

  public void loadItem(String ean13) {
    Item item = new Book();
    item.setEan13(ean13);
    itemHandler.setItem(item);
    txtEan13.setText(ean13);
  }

  public void loadItem(Book book) {
    itemHandler.setItem(book);

    txtTitle.setText(book.getName());
    txtComment.setText(book.getDescription());
    txtEan13.setText(book.getEan13());
    txtEdition.setText(Integer.toString(book.getEdition()));
    txtEditor.setText(book.getEditor());
    txtPublication.setText(book.getPublication());

    cbHasEan13.setSelected(txtEan13.getText().isEmpty());

    for (Author author : book.getAuthors()) {
      AuthorController controller = authorControllers.get(authorControllers.size() - 1);
      controller.setAuthorFirstName(author.getFirstName());
      controller.setAuthorLastName(author.getLastName());

      if (authorControllers.size() != book.getAuthors().size()) {
        _addAuthor();
      }
    }

    subjectController.selectCategory();
  }

  public boolean save() {
    return itemHandler.saveItem(_toJSON());
  }

  private boolean _canSave() {
    return !txtTitle.getText().isEmpty() &&
           !txtEditor.getText().isEmpty() &&
           !txtPublication.getText().isEmpty() &&
           (!txtEan13.getText().isEmpty() || cbHasEan13.isSelected());
  }

  private JSONObject _toJSON() {
    JSONObject form = new JSONObject();
    JSONArray authorArray = new JSONArray();

    try {
      for (AuthorController authorController : authorControllers) {
        authorArray.put(authorController.toJSON());
      }

      form.put("name", txtTitle.getText());
      form.put("editor", txtEditor.getText());
      form.put("publication", txtPublication.getText());
      form.put("subject", subjectController.getSubjectId());
      form.put("edition", txtEdition.getText());
      form.put("ean13", txtEan13.getText());
      form.put("author", authorArray);
      form.put("is_book", true);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return form;
  }

  private void _eventHandlers() {
    btnCancel.setOnAction(event -> {
      if (itemHandler.getItem().getId() != 0) {
        ((ItemViewController) loadMainPanel("layout/itemView.fxml")).loadItem(itemHandler.getItem());
      } else {
        loadMainPanel("layout/search.fxml");
      }
    });

    btnSave.setOnAction(event -> {
      if (_canSave()) {
        if (itemHandler.saveItem(_toJSON())) {
          ((ItemViewController) loadMainPanel("layout/itemView.fxml")).loadItem(itemHandler.getItem());
        } else {
          utility.Dialog.information("Une erreur est survenue");
        }
      } else {
        utility.Dialog.information("Veuillez remplir tous les champs obligatoires avant de sauvegarder");
      }
    });

    btnAddAuthor.setOnAction(event -> {
      if (authorControllers.size() < MAX_AUTHOR) {
        _addAuthor();
        updateAuthorButtons();
      }
    });

    btnRemoveAuthor.setOnAction(event -> {
      if (authorControllers.size() > MIN_AUTHOR) {
        removeLastAuthor();
        updateAuthorButtons();
      }
    });
  }

  private void updateAuthorButtons() {
    btnAddAuthor.setVisible(authorControllers.size() < MAX_AUTHOR);
    btnRemoveAuthor.setVisible(authorControllers.size() > MIN_AUTHOR);
  }

  @Override
  protected void handleScan(String code, boolean isItem) {}
}

package controller;

import handler.ItemHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.item.Item;
import org.json.JSONObject;
import utility.Dialog;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Jessy Lachapelle
 * @since 2016-09-04
 * @version 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ItemTabController extends PanelController {
  private ItemHandler itemHandler;
  private SubjectController subjectController;

  @FXML private Pane subject;
  @FXML private TextField txtName;
  @FXML private TextField txtEan13;
  @FXML private TextArea txtDescription;

  @FXML private Button btnCancel;
  @FXML private Button btnSave;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    itemHandler = new ItemHandler();
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

  public Button getBtnSave() {
    return btnSave;
  }

  public Button getBtnCancel() {
    return btnCancel;
  }

  public Item getItem() {
    return itemHandler.getItem();
  }

  public void loadItem(Item item) {
    itemHandler.setItem(item);
    txtName.setText(item.getName());
    txtDescription.setText(item.getDescription());
    subjectController.selectCategory();
  }

  public boolean save() {
    return itemHandler.saveItem(_toJSON());
  }

  private boolean _canSave() {
    return !txtName.getText().isEmpty();
  }

  private JSONObject _toJSON() {
    JSONObject form = new JSONObject();

    if (!txtName.getText().equals(getItem().getName())) {
      form.put("name", txtName.getText());
    }

    if (!txtDescription.getText().equals(getItem().getDescription())) {
      form.put("comment", txtDescription.getText());
    }

    if (!txtEan13.getText().equals(getItem().getEan13())) {
      form.put("ean13", txtEan13.getText());
    }

    if (getItem().getSubject().getId() != subjectController.getSubjectId()) {
      form.put("subject", subjectController.getSubjectId());
    }

    form.put("is_book", false);

    return form;
  }

  private void _eventHandlers() {
    btnCancel.setOnAction(event -> {
      if (getItem().getId() != 0) {
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
          Dialog.information("Une erreur est survenue");
        }
      } else {
        Dialog.information("Veuillez remplir tous les champs obligatoires avant de sauvegarder");
      }
    });
  }

  @Override
  protected void handleScan(String code, boolean isItem) {}
}

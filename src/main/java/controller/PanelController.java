package controller;

import handler.ItemHandler;
import handler.MemberHandler;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import utility.Settings;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author jessy on 27/09/16.
 */
public class PanelController extends Controller {
  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public PanelController() {
    _setScanner();
  }

  private void _setScanner() {
    ListView<String> console = new ListView<>(FXCollections.<String>observableArrayList());
    // on s'assure ici de clearer le buffer dans le cas de trop de changement
    console.getItems().addListener((ListChangeListener.Change<? extends String> change) -> {
      while (change.next()) {
        if (change.getList().size() > 20) {
          change.getList().remove(0);
        }
      }
    });

    window.setOnKeyPressed(key -> {
      final String FIRST_CHAR = Settings.scanFirstChar();
      final String LAST_CHAR = Settings.scanLastChar();
      final int MEMBER_CODE_LENGTH = 13;
      final int ITEM_CODE_LENGTH = 16;

      console.getItems().add(key.getText());

      // Not a barcode
      if (!console.getItems().get(0).equals(FIRST_CHAR)) {
        console.getItems().clear();
        return;
      }

      // Student number
      if (key.getText().equals(LAST_CHAR) && console.getItems().size() == MEMBER_CODE_LENGTH) {
        String code = "2" + console.getItems().toString().replaceAll("[\\D]", "").substring(1, 9);
        console.getItems().clear();
        handleScan(code, false);
        return;
      }

      // Item code
      if (key.getText().equals(LAST_CHAR) && console.getItems().size() == ITEM_CODE_LENGTH) {
        String code = console.getItems().toString().replaceAll("[\\D]", "");
        console.getItems().clear();
        handleScan(code, true);
        return;
      }

      // Invalid code
      if (key.getText().equals(LAST_CHAR)) {
        console.getItems().clear();
        utility.Dialog.information("Erreur de code", "Le code saisie n'est pas pris en charge");
      }
    });
  }

  protected void handleScan(String code, boolean isItem) {
    handleScan(getMainPanel(), code, isItem);
  }

  protected void handleScan(Pane target, String code, boolean isItem) {
    if (isItem) {
      ItemHandler itemHandler = new ItemHandler();

      if (itemHandler.exists(code)) {
        ((ItemViewController) loadPanel(target, "layout/itemView.fxml")).loadItem(code);
      } else {
        ((ItemFormController) loadPanel(target, "layout/itemForm.fxml")).loadItem(code);
      }
    } else {
      MemberHandler memberHandler = new MemberHandler();
      int memberNo = Integer.parseInt(code);

      if(memberHandler.exist(memberNo)) {
        ((MemberViewController) loadPanel(target, "layout/memberView.fxml")).loadMember(memberNo);
      } else {
        ((MemberFormController) loadPanel(target, "layout/memberForm.fxml")).loadMember(memberNo);
      }
    }
  }
}

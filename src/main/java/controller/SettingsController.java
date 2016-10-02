package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import utility.Dialog;
import utility.Settings;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Jessy
 */
public class SettingsController extends PanelController {
  @FXML private ComboBox<String> cbLang;
  @FXML private Button btnCalibrate;
  @FXML private Button btnCancel;
  @FXML private Button btnSave;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    _initLangList();
    _eventHandlers();
  }

  private void _initLangList() {
    cbLang.setItems(FXCollections.observableArrayList(Settings.supportedLang()));
    cbLang.getSelectionModel().select(Settings.lang());
  }

  private void _eventHandlers() {
    btnCalibrate.setOnAction(event -> {
      String title = "Calibrer le scanner";
      String message = "Scanner un code dans le champs text ";
      String code = Dialog.input(title, message);

      if (code.length() > 2) {
        String firstChar = code.substring(0, 1);
        String lastChar= code.substring(code.length() - 1);

        Settings.updateScanChars(firstChar, lastChar);
      }
    });

    btnCancel.setOnAction(event -> {
      // TODO: Close Settings window
    });

    btnSave.setOnAction(event -> {
      if (!Settings.lang().equals(cbLang.getValue())) {
        Settings.updateLang(cbLang.getValue());
      }
      // TODO: Close settings window
    });
  }

  @Override
  protected void handleScan(String code, boolean isItem) {}
}

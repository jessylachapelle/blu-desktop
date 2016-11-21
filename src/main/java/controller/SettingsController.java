package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.Node;

import utility.Dialog;
import utility.Settings;

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

  @Override
  protected void handleScan(String code, boolean isItem) {}


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

    btnCancel.setOnAction(event -> ((Node)(event.getSource())).getScene().getWindow().hide() );

    btnSave.setOnAction(event -> {
      if (!Settings.lang().equals(cbLang.getValue())) {
        Settings.updateLang(cbLang.getValue());
        Dialog.information("Pour que le changement de langue soit complet, veuillez redémarrer l'application");
      }

      ((Node)(event.getSource())).getScene().getWindow().hide();
    });
  }

  private void _initLangList() {
    cbLang.setItems(FXCollections.observableArrayList(Settings.supportedLang()));
    cbLang.getSelectionModel().select(Settings.lang());
  }
}

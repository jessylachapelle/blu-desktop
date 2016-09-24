/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import handler.ItemHandler;
import handler.MemberHandler;
import utility.Dialog;

/**
 * Cette classe controller prend en charge le panneau de gauche.
 * C'est avec celle-ci que l'ont charge chacun des panneau de droite
 *
 * @author Marc
 */
@SuppressWarnings("ConstantConditions")
public class WindowController extends Controller {
  @FXML private VBox sideMenu;
  @FXML private VBox menu;
  @FXML private Pane window;
  @FXML private Pane mainPanel;

  @FXML private Button btnSearch;
  @FXML private Button btnItemForm;
  @FXML private Button btnMemberForm;
  @FXML private Button btnAdmin;

  @FXML private Button btnBack;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

    menu.setPrefWidth(screenWidth);
    sideMenu.setPrefSize(screenWidth * .15, screenHeight);
    mainPanel.setPrefSize(screenWidth * .8, screenHeight);
    setMainPanel(mainPanel);

    _setText();
    _eventHandlers();
    loadMainPanel("layout/search.fxml");
    initText(mainPanel);
  }

  private void _eventHandlers() {
    _setScanner();

    btnSearch.setOnAction(event -> loadMainPanel("layout/search.fxml"));
    btnItemForm.setOnAction(event -> loadMainPanel("layout/itemForm.fxml"));
    btnMemberForm.setOnAction(event -> loadMainPanel("layout/memberForm.fxml"));
    btnAdmin.setOnAction(event -> loadMainPanel("layout/admin.fxml"));

    btnBack.setOnAction(event -> {
      // TODO: Handle back button
//      if (viewStack.size() > 1) {
//        View view = viewStack.pop();
//
//        while (view.getController() == controller) {
//          view = viewStack.pop();
//        }
//
//        controller = view.getController();
//        mainPanel.getChildren().clear();
//        mainPanel.getChildren().add(view.getPane());
//        btnBack.setVisible(viewStack.size() > 1);
//      }
    });
  }

  /**
   * Rajoute le listener global pour le scanner
   */
  @SuppressWarnings({"ConstantIfStatement", "StatementWithEmptyBody", "unused"})
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

    window.setOnKeyPressed(ke -> {
      boolean isItem = true;
      String code = "";
      console.getItems().add(ke.getText());

      // Si le premier caractère n'est pas < ce n'est pas une saisie de code barre
      if (!console.getItems().get(0).equals("à")) {
        console.getItems().clear();
        return;
      } else if (ke.getText().equals("À") && console.getItems().size() == 13) {   // Numéro étudiant
        isItem = false;
        code = console.getItems().toString().replaceAll("[\\D]", "");
        code = "2" + code.substring(1, 9);
        console.getItems().clear();

        MemberHandler memberHandler = new MemberHandler();
        int memberNo = Integer.parseInt(code);

        if(memberHandler.exist(memberNo)) {         // Member existe
          ((MemberViewController) loadMainPanel("layout/memberView.fxml")).loadMember(memberNo);
        } else {                                // Nouveau member
          ((MemberFormController) loadMainPanel("layout/memberForm.fxml")).loadMember(memberNo);
        }

        return;
      } else if (ke.getText().equals("À") && console.getItems().size() == 16) {   // Code EAN13
        code = console.getItems().toString().replaceAll("[\\D]", "");
        console.getItems().clear();
      } else if(ke.getText().equals("À")) {                                       // Code non supporté
        console.getItems().clear();
        Dialog.information("Erreur de code", "Le code saisie n'est pas pris en charge");
        return;
      }

      // TODO complété puis décommenter les fonctions en commentaire
      boolean isForm = false;
      boolean isCopyForm = false;
      if (isForm) { // If member or item form ignore action

      } else if(isCopyForm) {     // Si le panel d'ajout d'exemplaire est ouvert
        ItemHandler itemHandler = new ItemHandler();

        if (isItem) {   // itemHandler.itemExists(code) C'est un item existant
          // TODO Créer un exemplaire de l'item et permettre la saisie du prix
        } else if (isItem) {                      // C'est un nouvel item
          // TODO ouvrir un formulaire d'ajout d'item puis retour à l'ajout d'exemplaire
        } else {                                  // Ce n'est pas un item
          Dialog.information("Erreur de code", "Le code saisie n'est pas pris en charge");
        }
      } else {
        if(isItem) {                             // C'est un item
          ItemHandler itemHandler = new ItemHandler();

          if (true) { //ga.itemExists(code)            // L'item existe
            ((ItemViewController) loadMainPanel("layout/itemView.fxml")).loadItem(code);
          } else {                                // Nouvel item
            ((ItemFormController) loadMainPanel("layout/itemForm.fxml")).loadItem(code);
          }
        } else {                                  // Member
          MemberHandler memberHandler = new MemberHandler();
          int memberNo = Integer.parseInt(code);

          if(memberHandler.exist(memberNo)) {         // Member existe
            ((MemberViewController) loadMainPanel("layout/memberView.fxml")).loadMember(memberNo);
          } else {                                // Nouveau member
            ((MemberFormController) loadMainPanel("layout/memberForm.fxml")).loadMember(memberNo);
          }
        }
      }
    });
  }

  private void _setText() {
    initI18n();

    btnSearch.setText(i18n.getString("menu.search"));
    btnItemForm.setText(i18n.getString("menu.item"));
    btnMemberForm.setText(i18n.getString("menu.member"));
    btnAdmin.setText(i18n.getString("menu.admin"));
  }
}
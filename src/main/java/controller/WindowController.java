/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.scene.control.MenuItem;

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

  @FXML private MenuItem exit;
  @FXML private MenuItem about;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

    menu.setPrefWidth(screenWidth);
    sideMenu.setPrefSize(screenWidth * .15, screenHeight);
    mainPanel.setPrefSize(screenWidth * .8, screenHeight);
    setMainPanel(mainPanel);
    setWindow(window);

    _setText();
    _eventHandlers();
    loadMainPanel("layout/search.fxml");
  }

  private void _eventHandlers() {
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

    exit.setOnAction(event -> Platform.exit());
    about.setOnAction(event -> Dialog.information("About", "https://github.com/katima-g33k/blu-desktop"));
  }

  private void _setText() {
    initI18n();

    btnSearch.setText(i18n.getString("menu.search"));
    btnItemForm.setText(i18n.getString("menu.item"));
    btnMemberForm.setText(i18n.getString("menu.member"));
    btnAdmin.setText(i18n.getString("menu.admin"));
  }
}
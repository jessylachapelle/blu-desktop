package controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import handler.AdminHandler;
import model.item.Reservation;
import utility.DateParser;
import utility.Dialog;
import utility.Settings;

/**
 *
 * @author Jessy Lachapelle
 * @since 2016/11/13
 * @version 1.0
 */
public class AdminController extends PanelController {
  private static final String URL = "http://localhost/blu-web/admin/admin.php";
  private AdminHandler adminHandler;

  @FXML private WebView webView;

  @FXML private Button btnDeleteAllReservations;
  @FXML private Button btnDeleteLastReservations;
  @FXML private Button btnDeleteStorage;

  @FXML private TableView<Reservation> tblReservations;
  @FXML private TableColumn<Reservation, String> colParent;
  @FXML private TableColumn<Reservation, String> colTitle;
  @FXML private TableColumn<Reservation, String> colDateReserved;
  @FXML private TableColumn<Reservation, String> colDateReceived;
  @FXML private TableColumn<Reservation, String> colPrice;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    adminHandler = new AdminHandler();
    _dataBinding();
    _eventHandlers();
    _loadReservations();

    if (webView.isVisible()) {
      _initWebview();
    }
  }

  private void _dataBinding() {
    colParent.setCellValueFactory(new PropertyValueFactory<>("parentName"));
    colTitle.setCellValueFactory(new PropertyValueFactory<>("itemName"));
    colDateReserved.setCellValueFactory(new PropertyValueFactory<>("dateReserved"));
    colDateReceived.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
  }

  private void _displayReservations() {
    tblReservations.setItems(FXCollections.observableArrayList(adminHandler.getReservations()));
    tblReservations.refresh();
  }

  private void _eventHandlers() {
    btnDeleteAllReservations.setOnAction(event -> {
      String message = "Êtes-vous certain de vouloir supprimer toutes les réservations?";
      if (Dialog.confirmation(message)) {
        if (adminHandler.deleteReservations()) {
          _displayReservations();
        } else {
          Dialog.information("Une erreur est survenue");
        }
      }
    });

    btnDeleteLastReservations.setOnAction(event -> {
      String message = "Êtes-vous certain de vouloir supprimer les réservations de la session " + Settings.lastSemester() + "?";
      if (Dialog.confirmation(message)) {
        Date[] range = Settings.lastSemesterDates();
        System.out.println(DateParser.dateToString(range[0]) + " to " + DateParser.dateToString(range[1]));
        if (adminHandler.deleteReservations(range[0], range[1])) {
          _displayReservations();
        } else {
          Dialog.information("Une erreur est survenue");
        }
      }
    });

    btnDeleteStorage.setOnAction(event -> {
      String message = "Êtes-vous certains de vouloir supprimer les caisses de rangement?";
      if (Dialog.confirmation(message)) {
        if (adminHandler.deleteStorage()) {
          Dialog.information("Les caisses ont été supprimées");
        } else {
          Dialog.information("Une erreur est survenue");
        }
      }
    });
  }

  private void _initWebview() {
    WebEngine engine = webView.getEngine();
    engine.load(URL);
  }

  private void _loadReservations() {
    if (adminHandler.selectReservations()) {
      _displayReservations();
    }
  }
}

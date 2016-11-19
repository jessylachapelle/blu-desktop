package controller;

import handler.ItemHandler;
import handler.MemberHandler;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.item.*;
import model.member.Member;
import org.json.JSONObject;
import utility.Dialog;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller de la fenêtre d'une fiche d'item
 *
 * @author Jessy Lachapelle
 * @since 29/11/2015
 * @version 0.1
 */
@SuppressWarnings({"unused", "WeakerAccess", "unchecked", "ConstantConditions"})
public class ItemViewController extends PanelController {
  private ItemHandler itemHandler;
  private WebEngine webEngine;

  @FXML private Label lblTitle;
  @FXML private Label lblDescription;
  @FXML private Label lblAuthor;
  @FXML private Label lblEdition;
  @FXML private Label lblEditor;
  @FXML private Label lblPublication;
  @FXML private Label lblEan13;
  @FXML private Label lblComment;
  @FXML private Button btnAddComment;
  @FXML private Label lblStatus;
  @FXML private Label lblCategory;
  @FXML private Label lblSubject;
  @FXML private Button btnStorage;
  @FXML private Label lblStorage;
  @FXML private Button btnReserve;

  @FXML private WebView statistics;

  @FXML private VBox commentPane;
  @FXML private HBox statusPane;
  @FXML private HBox descriptionPane;

  @FXML private Button btnDisplayReservations;
  @FXML private TableView<Reservation> tblReservations;
  @FXML private TableColumn<Reservation, String> colReservationParent;
  @FXML private TableColumn<Reservation, String> colReservationSeller;
  @FXML private TableColumn<Reservation, String> colReservationAdded;
  @FXML private TableColumn<Reservation, String> colReservationDate;
  @FXML private TableColumn<Reservation, String> colReservationPrice;

  @FXML private Button btnDisplayAvailable;
  @FXML private TableView<Copy> tblAvailable;
  @FXML private TableColumn<Copy, String> colAvailableSeller;
  @FXML private TableColumn<Copy, String> colAvailableAdded;
  @FXML private TableColumn<Copy, String> colAvailablePrice;

  @FXML private Button btnSold;
  @FXML private TableView<Copy> tblSold;
  @FXML private TableColumn<Copy, String> colSoldSeller;
  @FXML private TableColumn<Copy, String> colSoldAdded;
  @FXML private TableColumn<Copy, String> colSoldDateSold;
  @FXML private TableColumn<Copy, String> colSoldPrice;

  @FXML private Button btnPaid;
  @FXML private TableView<Copy> tblPaid;
  @FXML private TableColumn<Copy, String> colPaidSeller;
  @FXML private TableColumn<Copy, String> colPaidAdded;
  @FXML private TableColumn<Copy, String> colPaidDateSold;
  @FXML private TableColumn<Copy, String> colPaidDatePaid;
  @FXML private TableColumn<Copy, String> colPaidPrice;

  @FXML private Button btnUpdate;
  @FXML private Button btnDelete;
  @FXML private Button btnMerge;

  @FXML private Button statusUp;
  @FXML private Button statusDown;

   @FXML private Label lblMaximum;
   @FXML private Label lblAverage;
   @FXML private Label lblMinimum;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    itemHandler = new ItemHandler();
    webEngine = statistics.getEngine();
    URL url = getClass().getClassLoader().getResource("html/stats_grid.html");
    webEngine.load(url.toExternalForm());
    _eventHandlers();
    _dataBinding();
  }

  public void loadItem(Item item) {
    boolean isBook = item instanceof Book;
    itemHandler.setItem(item);
    statusUp.setVisible(isBook);
    statusDown.setVisible(isBook);
    _displayItem(isBook);
  }

  public void loadItem(int id) {
    loadItem(itemHandler.selectItem(id));
  }

  public void loadItem(String ean13) {
    loadItem(itemHandler.selectItem(ean13));
  }

  public Item getItem() {
    return itemHandler.getItem();
  }

  public TableView[] getCopyTables() {
    return new TableView[]{ tblAvailable, tblSold, tblPaid };
  }

  public Button getBtnUpdate() {
    return btnUpdate;
  }

  private void _eventHandlers() {
    btnAddComment.setOnAction(event -> {
      String title = "Modifcation du commentaire",
             message = "Veuillez entrer le commentaire que vous souhaitez inscrire :",
             oldComment = lblComment.getText(),
             newComment = Dialog.input(title, message, oldComment);

      if (!oldComment.equals(newComment)) {
        if (itemHandler.updateComment(getItem().getId(), newComment)) {
          _displayBook();
        } else {
          Dialog.information("Une erreur est survenue");
        }
      }
    });

    statusUp.setOnAction(event -> {
      Book book = (Book) getItem();
      String status = book.getStatus().equals("REMOVED") ? Book.STATUS_OUTDATED : Book.STATUS_VALID;

      if (itemHandler.updateStatus(book.getId(), status)) {
        lblStatus.setText(book.getStatus());
        _displayStatusButtons();
      } else {
        Dialog.information("Une erreur est survenue");
      }
    });

    statusDown.setOnAction(event -> {
      Book book = (Book) getItem();
      String status = book.getStatus().equals(Book.STATUS_VALID) ? Book.STATUS_OUTDATED : Book.STATUS_REMOVED;

      if (itemHandler.updateStatus(book.getId(), status)) {
        lblStatus.setText(book.getStatus());
        _displayStatusButtons();
      } else {
        Dialog.information("Une erreur est survenue");
      }
    });

    btnStorage.setOnAction(event -> {
      String title = "Caisses de rangement",
             message = "Veuillez noter les caisses de rangement séparer par un \";\" :";
      String input = Dialog.input(title, message, lblStorage.getText());
      String[] storageArray = input.replace(" ", "").split(";");

      if (itemHandler.updateStorage(getItem().getId(), storageArray)) {
        ArrayList<Storage> storage = new ArrayList<>();

        for (String unit : storageArray) {
          Storage storageUnit = new Storage();
          storageUnit.setCode(unit);
          storage.add(storageUnit);
        }

        getItem().setStorage(storage);
        lblStorage.setText(getItem().getStorageString());
      } else {
        Dialog.information("Une erreur est survenue");
      }
    });

    btnReserve.setOnAction(event -> {
      JSONObject window = _openSearchWindow();

      if (window != null) {
        SearchController searchController = (SearchController) window.get("controller");
        searchController.setSearchParent();
        Stage stage = (Stage) window.get("stage");

        searchController.getTblMemberResults().setOnMouseClicked(e -> {
          TableRow row = _getTableRow(((Node) e.getTarget()).getParent());
          Member member = (Member) row.getItem();
          stage.hide();

          if (itemHandler.addItemReservation(member.getNo())) {
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue");
          }

          e.consume();
        });
      }
    });

    btnDisplayReservations.setOnAction(event -> tblReservations.setVisible(!tblReservations.isVisible()));

    tblReservations.setOnMouseClicked(event -> {
      TableRow row = _getTableRow(((Node) event.getTarget()).getParent());
      Reservation reservation = (Reservation) row.getItem();

      if (reservation != null && isRightClick(event)) {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem sell = new MenuItem("Vendre");
        MenuItem cancel = new MenuItem("Annuler réservation");

        if (reservation.getCopy().getId() == 0) {
          contextMenu.getItems().addAll(cancel);
        } else {
          contextMenu.getItems().addAll(sell, cancel);
        }

        row.setContextMenu(contextMenu);

        sell.setOnAction(e -> {
          if (itemHandler.addTransaction(reservation.getCopy().getMember().getNo(), reservation.getCopy().getId(), "SELL_PARENT")) {
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue");
          }
        });

        cancel.setOnAction(e -> {
          boolean success;
          if (reservation.getCopy() == null || reservation.getCopy().getId() == 0) {
            success = itemHandler.deleteItemReservation(reservation.getParent().getNo());
          } else {
            success = itemHandler.deleteCopyReservation(reservation.getCopy().getId());
          }

          if (success) {
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue");
          }
        });
      }
    });

    btnDisplayAvailable.setOnAction(event -> tblAvailable.setVisible(!tblAvailable.isVisible()));

    tblAvailable.setOnMouseClicked(event -> {
      TableRow row = _getTableRow(((Node) event.getTarget()).getParent());
      Copy copy = (Copy) row.getItem();

      if (isRightClick(event)) {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem sell = new MenuItem("Vendre");
        MenuItem sellParent = new MenuItem("Vendre à 50%");
        MenuItem reserve = new MenuItem("Réserver");
        MenuItem update = new MenuItem("Modifier le prix");
        MenuItem delete = new MenuItem("Supprimer");

        contextMenu.getItems().addAll(sell, sellParent, reserve, update, delete);
        row.setContextMenu(contextMenu);

        sell.setOnAction(e -> {
          if (itemHandler.addTransaction(copy.getMember().getNo(), copy.getId(), "SELL")) {
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue");
          }
        });

        sellParent.setOnAction(e -> {
          if (itemHandler.addTransaction(copy.getMember().getNo(), copy.getId(), "SELL_PARENT")) {
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue");
          }
        });

        reserve.setOnAction(e -> {
          JSONObject window = _openSearchWindow();

          if (window != null) {
            SearchController searchController = (SearchController) window.get("controller");
            searchController.setSearchParent();
            Stage stage = (Stage) window.get("stage");

            searchController.getTblMemberResults().setOnMouseClicked(ev -> {
              TableRow tableRow = _getTableRow(((Node) ev.getTarget()).getParent());
              Member member = (Member) tableRow.getItem();
              stage.hide();

              if (itemHandler.addTransaction(member.getNo(), copy.getId(), "RESERVE")) {
                _displayCopies();
              } else {
                Dialog.information("Une erreur est survenue");
              }

              e.consume();
            });
          }
        });

        update.setOnAction(e -> _updatePrice(copy));

        delete.setOnAction(e -> {
          String message = "Souhaitez-vous vraiment supprimer cet exemplaire appartenant à " + copy.getSeller() + "?";
          if (Dialog.confirmation(message) && itemHandler.deleteCopy(copy.getId())) {
            _displayCopies();
            Dialog.information("L'exemplaire de " + copy.getItem().getName() + " a été supprimé");
          } else {
            Dialog.information("Une erreur est survenue lors de la suppression de l'exemplaire");
          }
        });
      }
    });

    btnSold.setOnAction(event -> tblSold.setVisible(!tblSold.isVisible()));

    tblSold.setOnMouseClicked(event -> {
      TableRow row = _getTableRow(((Node) event.getTarget()).getParent());
      Copy copy = (Copy) row.getItem();

      if (isRightClick(event)) {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem cancel = new MenuItem("Annuler la vente");

        contextMenu.getItems().addAll(cancel);
        row.setContextMenu(contextMenu);

        cancel.setOnAction(e -> {
          if (itemHandler.cancelSell(copy.getId())) {
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue lors de l'annulation de la vente");
          }
        });
      }
    });

    btnPaid.setOnAction(event -> tblPaid.setVisible(!tblPaid.isVisible()));

    btnUpdate.setOnAction(event -> ((ItemFormController) loadMainPanel("layout/itemForm.fxml")).loadItem(itemHandler.getItem()));

    btnDelete.setOnAction(event -> {
      String message = "ëtes-vous certain de vouloir supprimer cet article ?";
      if (Dialog.confirmation(message)) {
        if (itemHandler.delete()) {
          loadMainPanel("layout/search.fxml");
        } else {
          Dialog.information("Une erreur est survenue");
        }
      }
    });

    for (TableView table : getCopyTables()) {
      table.setOnMousePressed(event -> {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
          TableRow row = _getTableRow(((Node) event.getTarget()).getParent());
          Copy copy = (Copy) row.getItem();

          if (copy != null) {
            window.setCursor(Cursor.WAIT);
            Task<Member> t = new Task<Member>() {
              @Override
              protected Member call() throws Exception {
                MemberHandler memberHandler = new MemberHandler();
                return memberHandler.selectMember(copy.getMember().getNo());
              }
            };
            t.setOnSucceeded(e -> {
              window.setCursor(Cursor.DEFAULT);
              ((MemberViewController) loadMainPanel("layout/memberView.fxml")).loadMember(t.getValue());
            });
            new Thread(t).start();
          }
        }
      });
    }
  }

  private void _dataBinding() {
    btnReserve.managedProperty().bind(btnReserve.visibleProperty());
    statusPane.managedProperty().bind(statusPane.visibleProperty());
    commentPane.managedProperty().bind(commentPane.visibleProperty());
    descriptionPane.managedProperty().bind(descriptionPane.visibleProperty());

    colReservationParent.setCellValueFactory(new PropertyValueFactory<>("parentName"));
    colReservationSeller.setCellValueFactory(new PropertyValueFactory<>("memberName"));
    colReservationAdded.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
    colReservationDate.setCellValueFactory(new PropertyValueFactory<>("dateReserved"));
    colReservationPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    colAvailableSeller.setCellValueFactory(new PropertyValueFactory<>("seller"));
    colAvailableAdded.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
    colAvailablePrice.setCellValueFactory(new PropertyValueFactory<>("priceString"));

    colSoldSeller.setCellValueFactory(new PropertyValueFactory<>("seller"));
    colSoldAdded.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
    colSoldDateSold.setCellValueFactory(new PropertyValueFactory<>("dateSold"));
    colSoldPrice.setCellValueFactory(new PropertyValueFactory<>("priceString"));

    colPaidSeller.setCellValueFactory(new PropertyValueFactory<>("seller"));
    colPaidAdded.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
    colPaidDateSold.setCellValueFactory(new PropertyValueFactory<>("dateSold"));
    colPaidDatePaid.setCellValueFactory(new PropertyValueFactory<>("datePaid"));
    colPaidPrice.setCellValueFactory(new PropertyValueFactory<>("priceString"));

    for (TableView table : getCopyTables()) {
      for (int i = 0; i < table.getColumns().size(); i++) {
        TableColumn tableColumn = (TableColumn) table.getColumns().get(i);
        tableColumn.setCellFactory(column -> new TableCell<Copy, String>() {
          @Override
          protected void updateItem(String data, boolean empty) {
            TableRow<Copy> row = getTableRow();
            boolean deactivated = data != null &&
                                  !row.getItem().isDonated() &&
                                  row.getItem().getMember().getAccount().isDeactivated();

            super.updateItem(data, empty);
            setText(data != null ? data : "");
            setStyle(deactivated ? "-fx-background-color: grey" : "");
            setTextFill(deactivated ? Color.WHITE : Color.BLACK);
          }
        });
      }
    }
  }

  private void _displayItem(boolean isBook) {
    lblTitle.setText(getItem().getName());
    lblSubject.setText(getItem().getSubject().getName());
    lblCategory.setText(getItem().getSubject().getCategory().getName());
    lblEan13.setText(getItem().getEan13());
    lblStorage.setText(getItem().getStorageString());

    descriptionPane.setVisible(!isBook);
    statusPane.setVisible(isBook);
    commentPane.setVisible(isBook);

    if (isBook) {
      _displayBook();
    } else {
      lblDescription.setText(getItem().getDescription());
    }

    _displayCopies();
  }

  private void _updatePrice(Copy copy) {
    String titre = "Modification du price",
           message = "Entrez le nouveau montant :";
    boolean isDouble = false;
    double price = copy.getPrice();

    while (!isDouble) {
      try {

        price = Double.parseDouble(Dialog.input(titre, message, Double.toString(copy.getPrice())));
        isDouble = true;
      } catch (NumberFormatException e) {
        Dialog.information("Vous devez entrer un montant valide");
      }
    }

    if (itemHandler.updateCopyPrice(copy.getId(), price)) {
      _displayCopies();
    } else {
      Dialog.information("Une erreur est survenue");
    }
  }

  private void _displayBook() {
    Book book = (Book) getItem();

    lblComment.setText(book.getDescription());
    lblPublication.setText(book.getPublication());
    lblAuthor.setText(book.getAuthorString());
    lblEditor.setText(book.getEditor());
    lblEdition.setText(Integer.toString(book.getEdition()));
    lblStatus.setText(book.getStatus());
    _displayStatusButtons();
  }

  private void _displayStatusButtons() {
    Book book = (Book) getItem();
    statusUp.setVisible(!book.getStatus().equals(Book.STATUS_VALID));
    statusDown.setVisible(!book.getStatus().equals(Book.STATUS_REMOVED));
  }

  private void _displayCopies() {
    tblReservations.setItems(FXCollections.observableArrayList(getItem().getReserved()));
    tblAvailable.setItems(FXCollections.observableArrayList(getItem().getAvailable()));
    tblSold.setItems(FXCollections.observableArrayList(getItem().getSold()));
    tblPaid.setItems(FXCollections.observableArrayList(getItem().getPaid()));

    tblReservations.refresh();
    tblAvailable.refresh();
    tblSold.refresh();
    tblPaid.refresh();

    tblReservations.setVisible(!tblReservations.getItems().isEmpty());
    tblAvailable.setVisible(!tblAvailable.getItems().isEmpty());
    tblSold.setVisible(!tblSold.getItems().isEmpty());
    tblPaid.setVisible(!tblPaid.getItems().isEmpty());
    btnReserve.setVisible(tblAvailable.getItems().isEmpty());

    _displayStats();
  }

  private void _displayStats() {
     URL url = getClass().getClassLoader().getResource("html/stats_grid.html");
     JSONObject stats = getItem().inventory();
     webEngine.load(url.toExternalForm() + "?data=" + stats.toString());

     String maximum = getItem().maximumPriceTotal() + "$ (" + getItem().maximumPriceStock() + "$ en stock)";
     String average = getItem().averagePriceTotal() + "$ (" + getItem().averagePriceStock() + "$ en stock)";
     String minimum = getItem().minimumPriceTotal() + "$ (" + getItem().minimumPriceStock() + "$ en stock)";
     lblMaximum.setText(maximum);
     lblAverage.setText(average);
     lblMinimum.setText(minimum);
  }

  private JSONObject _openSearchWindow() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/search.fxml"));
      Parent parent = fxmlLoader.load();
      Stage stage = new Stage();
      Scene scene = new Scene(parent);

      scene.getStylesheets().addAll("css/window.css");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setTitle("Sélectionner le parent");
      stage.setWidth(600);
      stage.setHeight(650);
      stage.setScene(scene);
      stage.show();

      JSONObject window = new JSONObject();
      window.put("stage", stage);
      window.put("controller", (SearchController) fxmlLoader.getController());
      return window;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
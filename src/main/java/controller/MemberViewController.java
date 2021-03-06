package controller;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.json.JSONObject;

import handler.ItemHandler;
import handler.MemberHandler;
import model.item.Book;
import model.item.Copy;
import model.item.Item;
import model.item.Reservation;
import model.member.Comment;
import model.member.Member;
import model.member.StudentParent;
import utility.DateParser;
import utility.Dialog;
import utility.Settings;


/**
 *
 * @author Jessy Lachapelle
 * @since 2016/07/24
 * @version 1.0
 */
public class MemberViewController extends PanelController {
  @FXML private Button btnUpdate;
  @FXML private Button btnDelete;

  @FXML private Label lblName;
  @FXML private Label lblNo;
  @FXML private Label lblAddress;
  @FXML private Label lblEmail;
  @FXML private Label lblPhone1;
  @FXML private Label lblPhone2;

  @FXML private Label lblState;
  @FXML private Label lblRegistration;
  @FXML private Label lblLastActivity;
  @FXML private Label lblDeactivation;
  @FXML private Button btnReactivate;

  @FXML private TableView<Comment> tblComments;
  @FXML private TableColumn<Comment, String> colComment;
  @FXML private TableColumn<Comment, String> colCommentDate;

  @FXML private Button btnAddCopy;
  @FXML private Button btnRenew;
  @FXML private Button btnAddComment;
  @FXML private Button btnPay;
  @FXML private Button btnReceipt;

  @FXML private VBox reservation;
  @FXML private TableView tblReservation;
  @FXML private TableColumn<Reservation, String> colReservationTitle;
  @FXML private TableColumn<Reservation, String> colReservationDateReserved;
  @FXML private TableColumn<Reservation, String> colReservationSeller;
  @FXML private TableColumn<Reservation, String> colReservationDateAdded;
  @FXML private TableColumn<Reservation, String> colReservationPrice;

  @FXML private WebView statistics;

  @FXML private TableView<Copy> tblAvailable;
  @FXML private TableColumn<Copy, String> colAvailableTitle;
  @FXML private TableColumn<Copy, String> colAvailableEditor;
  @FXML private TableColumn<Copy, String> colAvailableEdition;
  @FXML private TableColumn<Copy, String> colAvailableDateAdded;
  @FXML private TableColumn<Copy, String> colAvailablePrice;

  @FXML private TableView<Copy> tblSold;
  @FXML private TableColumn<Copy, String> colSoldTitle;
  @FXML private TableColumn<Copy, String> colSoldEditor;
  @FXML private TableColumn<Copy, String> colSoldEdition;
  @FXML private TableColumn<Copy, String> colSoldDateAdded;
  @FXML private TableColumn<Copy, String> colSoldDateSold;
  @FXML private TableColumn<Copy, String> colSoldPrice;

  @FXML private TableView<Copy> tblPaid;
  @FXML private TableColumn<Copy, String> colPaidTitle;
  @FXML private TableColumn<Copy, String> colPaidEditor;
  @FXML private TableColumn<Copy, String> colPaidEdition;
  @FXML private TableColumn<Copy, String> colPaidDateAdded;
  @FXML private TableColumn<Copy, String> colPaidDateSold;
  @FXML private TableColumn<Copy, String> colPaidDatePaid;
  @FXML private TableColumn<Copy, String> colPaidPrice;

  private MemberHandler memberHandler;
  private WebEngine webEngine;
  private static final String STATS_GRID_URL = Settings.statsGridUrl();
  private static final String ACCOUNT_STATEMENT_URL = Settings.accountStatementUrl();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    memberHandler = new MemberHandler();
    initI18n();
    webEngine = statistics.getEngine();
    webEngine.load(STATS_GRID_URL);

    _eventHandlers();
    _dataBinding();
  }

  public Member getMember() {
    return memberHandler.getMember();
  }

  public void loadMember(Member member) {
    memberHandler.setMember(member);
    _displayMember();
  }

  private void _dataBinding() {
    btnReactivate.managedProperty().bind(btnReactivate.visibleProperty());
    btnReactivate.visibleProperty().bind(btnAddCopy.disableProperty());
    reservation.managedProperty().bind(reservation.visibleProperty());

    colComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
    colCommentDate.setCellValueFactory(new PropertyValueFactory<>("date"));

    colReservationTitle.setCellValueFactory(new PropertyValueFactory<>("itemName"));
    colReservationDateReserved.setCellValueFactory(new PropertyValueFactory<>("dateReserved"));
    colReservationSeller.setCellValueFactory(new PropertyValueFactory<>("memberName"));
    colReservationDateAdded.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
    colReservationPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    colAvailableTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
    colAvailableEdition.setCellValueFactory(new PropertyValueFactory<>("edition"));
    colAvailableEditor.setCellValueFactory(new PropertyValueFactory<>("editor"));
    colAvailableDateAdded.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
    colAvailablePrice.setCellValueFactory(new PropertyValueFactory<>("priceString"));

    colSoldTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
    colSoldEdition.setCellValueFactory(new PropertyValueFactory<>("edition"));
    colSoldEditor.setCellValueFactory(new PropertyValueFactory<>("editor"));
    colSoldDateAdded.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
    colSoldDateSold.setCellValueFactory(new PropertyValueFactory<>("dateSold"));
    colSoldPrice.setCellValueFactory(new PropertyValueFactory<>("priceString"));

    colPaidTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
    colPaidEditor.setCellValueFactory(new PropertyValueFactory<>("editor"));
    colPaidEdition.setCellValueFactory(new PropertyValueFactory<>("edition"));
    colPaidDateAdded.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
    colPaidDateSold.setCellValueFactory(new PropertyValueFactory<>("dateSold"));
    colPaidDatePaid.setCellValueFactory(new PropertyValueFactory<>("datePaid"));
    colPaidPrice.setCellValueFactory(new PropertyValueFactory<>("priceString"));

    for (TableView table : _getCopyTables()) {
      for (int i = 0; i < table.getColumns().size(); i++) {
        TableColumn tableColumn = (TableColumn) table.getColumns().get(i);
        //noinspection unchecked
        tableColumn.setCellFactory(column -> new TableCell<Copy, String>() {
          @SuppressWarnings("unchecked")
          @Override
          protected void updateItem(String data, boolean empty) {
            TableRow<Copy> row = getTableRow();
            if (row != null) {
              boolean isBook = row.getItem() != null && row.getItem().getItem() instanceof Book;
              boolean outdated = isBook && ((Book) row.getItem().getItem()).isOutdated();
              boolean removed = isBook && ((Book) row.getItem().getItem()).isRemoved();

              super.updateItem(data, empty);
              setText(data != null ? data : "");

              if (removed) {
                setStyle("-fx-background-color: black");
                setTextFill(Color.WHITE);
              } else if (outdated) {
                setStyle("-fx-background-color: grey");
                setTextFill(Color.WHITE);
              } else {
                setStyle("");
                setTextFill(Color.BLACK);
              }
            }
          }
        });
      }
    }
  }

  private void _displayAccount() {
    lblState.setText(getMember().getAccount().isActive() ? "Compte actif" : "Compte désactivé");
    lblRegistration.setText(getMember().getAccount().getRegistrationString());
    lblLastActivity.setText(getMember().getAccount().getLastActivityString());
    lblDeactivation.setText(getMember().getAccount().getDeactivationString());

    btnAddCopy.setDisable(getMember().getAccount().isDeactivated());
    btnRenew.setDisable(getMember().getAccount().isDeactivated());
    btnPay.setDisable(getMember().getAccount().isDeactivated());
  }

  private void _displayComment() {
    tblComments.setItems(FXCollections.observableArrayList(getMember().getAccount().getComments()));
    tblComments.refresh();
  }

  @SuppressWarnings("unchecked")
  private void _displayCopies() {
    tblReservation.setItems(FXCollections.observableArrayList(getMember().getAccount().getReservation()));
    tblAvailable.setItems(FXCollections.observableArrayList(getMember().getAccount().getAvailable()));
    tblSold.setItems(FXCollections.observableArrayList(getMember().getAccount().getSold()));
    tblPaid.setItems(FXCollections.observableArrayList(getMember().getAccount().getPaid()));

    tblReservation.refresh();
    tblAvailable.refresh();
    tblSold.refresh();
    tblPaid.refresh();

    tblReservation.setVisible(!tblReservation.getItems().isEmpty());
    tblAvailable.setVisible(!tblAvailable.getItems().isEmpty());
    tblSold.setVisible(!tblSold.getItems().isEmpty());
    tblPaid.setVisible(!tblPaid.getItems().isEmpty());

    _displayStats();
  }

  private void _displayMember() {
    reservation.setVisible(getMember() instanceof StudentParent);

    lblName.setText(getMember().getFirstName() + " " + getMember().getLastName());
    lblNo.setText(Integer.toString(getMember().getNo()));
    lblAddress.setText(getMember().getAddressString());
    lblEmail.setText(getMember().getEmail());

    if (getMember().getPhone(0) != null) {
      lblPhone1.setText(getMember().getPhone(0).toString());
    }

    if (getMember().getPhone(1) != null) {
      lblPhone2.setText(getMember().getPhone(1).toString());
    }

    _displayAccount();
    _displayComment();
    _displayCopies();
  }

  private void _displayStats() {
    JSONObject stats = getMember().getAccount().getStats();
    webEngine.load(STATS_GRID_URL + "?data=" + stats.toString());
  }

  private void _eventHandlers() {
    btnReactivate.setOnAction(event -> {
      boolean success = true;
      String message = "Vous êtes sur le point de réactiver ce compte. Voulez-vous transférer ses livres en don à la BLU ?";
      switch (Dialog.confirmation(message, true)) {
        case 1:
          success = memberHandler.donate();
        case 0:
          if (success && memberHandler.renewAccount()) {
            _displayMember();
            Dialog.information("Le compte a été réactivé");
          } else {
            Dialog.information("Une erreur est survenue");
          }
          break;
      }
    });

    tblReservation.setOnMouseClicked(event -> {
      TableRow row = getTableRow(((Node) event.getTarget()).getParent());
      Reservation reservation = (Reservation) row.getItem();

      if (reservation != null && event.getButton().equals(MouseButton.SECONDARY)) {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem sell = new MenuItem("Vendre");
        MenuItem cancel = new MenuItem("Annuler la réservation");

        if (reservation.getCopy().getId() == 0) {
          contextMenu.getItems().addAll(cancel);
        } else {
          contextMenu.getItems().addAll(sell, cancel);
        }

        row.setContextMenu(contextMenu);

        sell.setOnAction(e -> {
          if (memberHandler.addTransaction(reservation.getCopy().getId(), "SELL_PARENT")) {
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue lors de la vente de l'exemplaire");
          }
        });

        cancel.setOnAction(e -> {
          boolean success;
          if (reservation.getCopy() == null || reservation.getCopy().getId() == 0) {
            success = memberHandler.deleteItemReservation(reservation.getItem().getId());
          } else {
            success = memberHandler.deleteCopyReservation(reservation.getCopy().getId());
          }

          if (success) {
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue");
          }
        });
      }
    });

    tblAvailable.setOnMouseClicked(event -> {
      TableRow row = getTableRow(((Node) event.getTarget()).getParent());
      Copy copy = (Copy) row.getItem();

      if (isRightClick(event)) {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem sell = new MenuItem("Vendre");
        MenuItem sellParent = new MenuItem("Vendre à 50%");
        MenuItem reserve = new MenuItem("Réserver");
        MenuItem updatePrice = new MenuItem("Modifier le prix");
        MenuItem delete = new MenuItem("Supprimer");

        contextMenu.getItems().addAll(sell, sellParent, reserve, updatePrice, delete);
        row.setContextMenu(contextMenu);

        sell.setOnAction(e -> {
          if (memberHandler.addTransaction(copy.getId(), "SELL")) {
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue lors de la mise à jour de l'exemplaire");
          }
        });

        sellParent.setOnAction(e -> {
          if (memberHandler.addTransaction(copy.getId(), "SELL_PARENT")) {
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue lors de la mise à jour de l'exemplaire");
          }
        });

        reserve.setOnAction(e -> {
          JSONObject window = _openSearchWindow();

          if (window != null) {
            SearchController searchController = (SearchController) window.get("controller");
            searchController.setSearchParent();
            Stage stage = (Stage) window.get("stage");

            searchController.getTblMemberResults().setOnMouseClicked(ev -> {
              TableRow tableRow = getTableRow(((Node) ev.getTarget()).getParent());
              Member member = (Member) tableRow.getItem();
              stage.hide();

              if (memberHandler.addTransaction(copy.getId(), "RESERVE", member.getNo())) {
                _displayCopies();
              } else {
                Dialog.information("Une erreur est survenue");
              }

              e.consume();
            });
          }
        });

        updatePrice.setOnAction(e -> {
          boolean isDouble = false;
          double price = copy.getPrice();
          String title = "Modification du prix",
              message = "Entrez le nouveau montant :";

          while (!isDouble) {
            try {
              price = Double.parseDouble(Dialog.input(title, message, Double.toString(price)));
              isDouble = true;
            } catch (NumberFormatException ex) {
              Dialog.information("Vous devez entrer un montant valide");
            }
          }

          if (memberHandler.updateCopyPrice(copy.getId(), price)) {
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue lors de la mise à jour de l'exemplaire");
          }
        });

        delete.setOnAction(e -> {
          String message = "Souhaitez-vous vraiment supprimer cet exemplaire de " + copy.getItem().getName() + "?";

          if (Dialog.confirmation(message) && memberHandler.deleteCopy(copy.getId())) {
            _displayCopies();
            Dialog.information("L'exemplaire de " + copy.getItem().getName() + " a été supprimé");
          } else {
            Dialog.information("Une erreur est survenue lors de la suppression de l'exemplaire");
          }
        });
      }
    });

    tblSold.setOnMouseClicked(event -> {
      TableRow row = getTableRow(((Node) event.getTarget()).getParent());
      Copy copy = (Copy) row.getItem();

      if (isRightClick(event)) {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem cancel = new MenuItem("Annuler la vente");

        contextMenu.getItems().addAll(cancel);
        row.setContextMenu(contextMenu);

        cancel.setOnAction(e -> {
          if (memberHandler.cancelSell(copy.getId())) {
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue lors de l'annulation de la vente");
          }
        });
      }
    });

    tblComments.setOnMouseClicked(event -> {
      TableRow row = getTableRow(((Node) event.getTarget()).getParent());

      if (row != null) {
        Comment comment = (Comment) row.getItem();

        if (isRightClick(event)) {
          final ContextMenu contextMenu = new ContextMenu();

          MenuItem update = new MenuItem("Modifier");
          MenuItem delete = new MenuItem("Supprimer");
          MenuItem add = new MenuItem("Ajouter");

          contextMenu.getItems().addAll(update, delete, add);
          row.setContextMenu(contextMenu);

          update.setOnAction(e -> {
            String str = Dialog.input("Comment", "Saisissez votre commentaire :", comment.getComment());

            if (!str.isEmpty()) {
              if (memberHandler.updateComment(comment.getId(), str)) {
                _displayComment();
              } else {
                Dialog.information("Une erreur est survenues lors de la modification du commentaire");
              }
            }
          });

          delete.setOnAction(e -> {
            String message = "Voulez-vous vraiment delete ce commentaire ?";

            if (Dialog.confirmation(message) && memberHandler.deleteComment(comment.getId())) {
              _displayComment();
            } else {
              Dialog.information("Une erreur est survenue lors de la supression du commentaire");
            }
          });

          add.setOnAction(e -> btnAddComment.fire());
        }
      }
    });

    btnPay.setOnAction(event -> {
      int amount = (int) getMember().getAccount().amountSold();

      if (amount != 0) {
        String message = "Veuillez remettre " + amount + "$ à " + getMember().getFirstName();

        if (Dialog.confirmation(message)) {
          if (memberHandler.pay()) {
            _displayCopies();
            btnReceipt.fire();
          } else {
            Dialog.information("Une erreur est survenue");
          }
        }
      } else {
        String message = "Le solde de " + getMember().getFirstName() + " est déjà à 0$";
        Dialog.information(message);
      }
    });

    btnRenew.setOnAction(event -> {
      if(memberHandler.renewAccount()) {
        _displayAccount();
        Dialog.information("Le compte a été renouvelé");
      } else {
        Dialog.information("Une erreur empêche le compte d'être renouvelé");
      }
    });

    btnReceipt.setOnAction(event -> {
      int amount = 0;
      for (Copy copy : getMember().getAccount().getSold()) {
        if (copy.getDateSold().equals(DateParser.dateToString(new Date()))) {
          amount += copy.getPrice();
        }
      }

      String params = "?no=" + getMember().getNo() + "&amount=" + amount;

      try {
        URL url = getClass().getClassLoader().getResource("html/account_statement/index.html");

        if (url != null) {
          new ProcessBuilder("x-www-browser", url.toExternalForm() + params).start();
        }
      } catch (IOException e) {
        e.printStackTrace();

        try {
          Desktop.getDesktop().browse(new URL(ACCOUNT_STATEMENT_URL + params).toURI());
        } catch (IOException | URISyntaxException ex) {
          ex.printStackTrace();
        }
      }
    });

    btnAddComment.setOnAction(event -> {
      String string = Dialog.input("Comment", "Saisissez votre commentaire :");

      if (!string.isEmpty() && memberHandler.addComment(string)) {
        _displayComment();
      } else {
        Dialog.information("Une erreur est survenue lors de la création du commentaire");
      }
    });

    btnDelete.setOnAction(event -> {
      String message = "Êtes-vous certain.e de vouloir supprimer ce membre ?";

      if (Dialog.confirmation(message) && memberHandler.deleteMember()) {
        Dialog.information("Suppression réussie");
        loadMainPanel("layout/search.fxml");
      } else {
        Dialog.information("Erreur lors de la suppression");
      }
    });

    btnUpdate.setOnAction(e -> ((MemberFormController) loadMainPanel("layout/memberForm.fxml")).loadMember(memberHandler.getMember()));

    for (TableView table : _getCopyTables()) {
      table.setOnMousePressed(event -> {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
          TableRow row = getTableRow(((Node) event.getTarget()).getParent());
          Copy copy = (Copy) row.getItem();

          if (copy != null) {
            window.setCursor(Cursor.WAIT);
            Task<Item> t = new Task<Item>() {
              @Override
              protected Item call() throws Exception {
                ItemHandler itemHandler = new ItemHandler();
                return itemHandler.selectItem(copy.getItem().getId());
              }
            };
            t.setOnSucceeded(e -> {
              window.setCursor(Cursor.DEFAULT);
              ((ItemViewController) loadMainPanel("layout/itemView.fxml")).loadItem(t.getValue());
            });
            new Thread(t).start();
          }
        }
      });
    }

    btnAddCopy.setOnAction(event -> ((CopyFormController) loadMainPanel("layout/copyForm.fxml")).loadMembre(memberHandler.getMember()));
  }

  private TableView[] _getCopyTables() {
    return new TableView[]{tblAvailable, tblSold, tblPaid};
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

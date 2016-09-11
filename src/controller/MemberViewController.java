package controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.Node;

import handler.MemberHandler;
import javafx.scene.layout.VBox;
import model.item.Copy;
import model.member.Comment;
import model.member.Member;
import model.member.StudentParent;
import ressources.Dialog;

/**
 *
 * @author Jessy Lachapelle
 * @since 2016/07/24
 * @version 1.0
 */
@SuppressWarnings("WeakerAccess")
public class MemberViewController extends Controller {
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

  @FXML private TableView<Comment> tblComments;
  @FXML private TableColumn<Comment, String> colComment;
  @FXML private TableColumn<Comment, String> colCommentDate;

  @FXML private Button btnAddCopy;
  @FXML private Button btnRenew;
  @FXML private Button btnAddComment;
  @FXML private Button btnPay;

  @FXML private VBox reservation;
  @FXML private Button btnAddReservation;
  @FXML private TableView tblReservation;

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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    memberHandler = new MemberHandler();
    initI18n();
    _eventHandlers();
    _dataBinding();
  }

  public TableView[] getCopyTables() {
    return new TableView[]{tblReservation, tblAvailable, tblSold, tblPaid};
  }

  public Member getMember() {
    return memberHandler.getMember();
  }

  public void loadMember(int memberNo) {
    memberHandler.setMember(memberNo);
    _displayMember();
  }

  public void loadMember(Member member) {
    memberHandler.setMember(member);
    _displayMember();
  }

  private void _eventHandlers() {
    tblAvailable.setOnMouseClicked(event -> {
      TableRow row = _getTableRow(((Node) event.getTarget()).getParent());
      Copy copy = (Copy) row.getItem();

      if (event.getButton() == MouseButton.SECONDARY) {
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
          String input = "";
          int memberNo = 0;
          boolean isMember = false;

          while (!isMember) {
            try {
              input = Dialog.input("Réserver cet item", "Entrez le numéro de l'étudiant qui fait la réservation");
              memberNo = Integer.parseInt(input);
              isMember = memberHandler.exist(memberNo);
            } catch (NumberFormatException ex) {
              if (input.equals("")) {
                return;
              }
            }
          }

          if (memberHandler.addTransaction(copy.getId(), "RESERVE", memberNo)) {
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue lors de la mise à jour de l'exemplaire");
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
      TableRow row = _getTableRow(((Node) event.getTarget()).getParent());
      Copy copy = (Copy) row.getItem();

      if (event.getButton() == MouseButton.SECONDARY) {
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
      TableRow row = _getTableRow(((Node) event.getTarget()).getParent());

      if (row != null) {
        Comment comment = (Comment) row.getItem();

        if (event.getButton() == MouseButton.SECONDARY) {
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
          } else {
            Dialog.information("Error");
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

    btnAddComment.setOnAction(event -> {
      String string = Dialog.input("Comment", "Saisissez votre commentaire :");

      if (!string.isEmpty() && memberHandler.addComment(string)) {
        _displayComment();
      } else {
        Dialog.information("Une erreur est survenue lors de la création du commentaire");
      }
    });

    // TODO: Handle reservations
    btnAddReservation.setOnAction(event -> tblReservation.setVisible(!tblReservation.isVisible()));

    btnDelete.setOnAction(event -> {
      String message = "Êtes-vous certain.e de vouloir supprimer ce membre ?";

      if (Dialog.confirmation(message) && memberHandler.deleteMember()) {
        Dialog.information("Suppression réussie");
      } else {
        Dialog.information("Erreur lors de la suppression");
      }
    });

    btnUpdate.setOnAction(e -> ((MemberFormController) loadMainPanel("view/layout/memberForm.fxml")).loadMember(memberHandler.getMember()));

    for (TableView table : getCopyTables()) {
      table.setOnMousePressed(event -> {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
          TableRow row = _getTableRow(((Node) event.getTarget()).getParent());
          Copy copy = (Copy) row.getItem();

          if (copy != null) {
            ((ItemViewController) loadMainPanel("view/layout/itemView.fxml")).loadItem(copy.getItem().getId());
          }
        }
      });
    }

    btnAddCopy.setOnAction(event -> ((CopyFormController) loadMainPanel("view/layout/copyForm.fxml")).loadMembre(memberHandler.getMember()));
  }

  private void _dataBinding() {
    reservation.managedProperty().bind(reservation.visibleProperty());
    tblAvailable.managedProperty().bind(tblAvailable.visibleProperty());
    tblSold.managedProperty().bind(tblSold.visibleProperty());
    tblPaid.managedProperty().bind(tblPaid.visibleProperty());

    colComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
    colCommentDate.setCellValueFactory(new PropertyValueFactory<>("date"));

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
  }

  private void _displayAccount() {
    // TODO: Add this to account object instead
    String state = "Compte actif";

    if (getMember().getAccount().getDeactivation().before(new Date())) {
      state = "Compte désactivé";
    }

    lblState.setText(state);
    lblRegistration.setText(getMember().getAccount().getRegistrationString());
    lblLastActivity.setText(getMember().getAccount().getLastActivityString());
    lblDeactivation.setText(getMember().getAccount().getDeactivationString());
  }

  private void _displayComment() {
    tblComments.setItems(FXCollections.observableArrayList(getMember().getAccount().getComments()));
    tblComments.refresh();
  }

  private void _displayCopies() {
    tblAvailable.setItems(FXCollections.observableArrayList(getMember().getAccount().getAvailable()));
    tblSold.setItems(FXCollections.observableArrayList(getMember().getAccount().getSold()));
    tblPaid.setItems(FXCollections.observableArrayList(getMember().getAccount().getPaid()));

    tblAvailable.refresh();
    tblSold.refresh();
    tblPaid.refresh();

    tblAvailable.setVisible(!tblAvailable.getItems().isEmpty());
    tblSold.setVisible(!tblSold.getItems().isEmpty());
    tblPaid.setVisible(!tblPaid.getItems().isEmpty());
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
}

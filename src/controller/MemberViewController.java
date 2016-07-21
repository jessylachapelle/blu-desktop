package controller;

import handler.MemberHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import model.item.Copy;
import model.member.Comment;
import model.member.Member;
import model.member.StudentParent;
import model.transaction.Transaction;
import ressources.Dialog;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 *
 * @author Jessy Lachapelle
 * @since 24/11/2015
 * @version 0.1
 */
public class MemberViewController extends Controller {

  private MemberHandler memberHandler;

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

  @FXML private Button btnAddReservation;
  @FXML private TableView tblReservation;

  @FXML private Button btnAvailable;
  @FXML private TableView<Copy> tblAvailable;
  @FXML private TableColumn<Copy, String> colAvailableTitle;
  @FXML private TableColumn<Copy, String> colAvailableEditor;
  @FXML private TableColumn<Copy, String> colAvailableEdition;
  @FXML private TableColumn<Copy, String> colAvailableDateAdded;
  @FXML private TableColumn<Copy, String> colAvailablePrice;

  @FXML private Button btnSold;
  @FXML private TableView<Copy> tblSold;
  @FXML private TableColumn<Copy, String> colSoldTitle;
  @FXML private TableColumn<Copy, String> colSoldEditor;
  @FXML private TableColumn<Copy, String> colSoldEdition;
  @FXML private TableColumn<Copy, String> colSoldDateAdded;
  @FXML private TableColumn<Copy, String> colSoldDateSold;
  @FXML private TableColumn<Copy, String> colSoldPrice;

  @FXML private Button btnPaid;
  @FXML private TableView<Copy> tblPaid;
  @FXML private TableColumn<Copy, String> colPaidTitle;
  @FXML private TableColumn<Copy, String> colPaidEditor;
  @FXML private TableColumn<Copy, String> colPaidEdition;
  @FXML private TableColumn<Copy, String> colPaidDateAdded;
  @FXML private TableColumn<Copy, String> colPaidDateSold;
  @FXML private TableColumn<Copy, String> colPaidDatePaid;
  @FXML private TableColumn<Copy, String> colPaidPrice;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    memberHandler = new MemberHandler();
    _eventHandlers();
    _dataBinding();
  }

  public void loadMember(int memberNo) {
    memberHandler.setMember(memberNo);
    _displayMember();
  }

  public void loadMember(Member member) {
    memberHandler.setMember(member);
    _displayMember();
  }

  public Button getEditButton() {
    return btnUpdate;
  }

  public Button getAddCopyButton() {
    return btnAddCopy;
  }

  public TableView[] getCopyTables() {
    return new TableView[]{tblReservation, tblAvailable, tblSold, tblPaid};
  }

  public Member getMember() {
    return memberHandler.getMember();
  }

  @SuppressWarnings("unchecked")
  private void _eventHandlers() {
    tblAvailable.setOnMouseClicked(event -> {
      Node node = ((Node) event.getTarget()).getParent();
      TableRow<Copy> row;

      if(node instanceof TableRow) {
        row = (TableRow<Copy>) node;
      } else {
        row = (TableRow<Copy>) node.getParent();
      }

      final Copy copy = row.getItem();

      if(event.getButton() == MouseButton.SECONDARY) {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem sell = new MenuItem("Vendre");
        MenuItem sellParent = new MenuItem("Vendre à 50%");
        MenuItem reserve = new MenuItem("Réserver");
        MenuItem updatePrice = new MenuItem("Modifier le prix");
        MenuItem delete = new MenuItem("Supprimer");

        contextMenu.getItems().addAll(sell, sellParent, reserve, updatePrice, delete);
        row.setContextMenu(contextMenu);

        sell.setOnAction(e -> {
          int id = memberHandler.addTransaction(copy.getId(), 2);

          if (id != 0) {
            Transaction t = new Transaction();
            t.setId(id);
            t.setType("SELL");
            t.setDate(new Date());

            getMember().getAccount().getAvailable().remove(copy);
            copy.addTransaction(t);
            getMember().getAccount().addSold(copy);

            _displayCopies();
          }
        });

        sellParent.setOnAction(e -> {
          int id = memberHandler.addTransaction(copy.getId(), 2);

          if (id != 0) {
            Transaction t = new Transaction();
            t.setId(id);
            t.setType("SELL");
            t.setDate(new Date());

            getMember().getAccount().getAvailable().remove(copy);
            copy.addTransaction(t);
            getMember().getAccount().addSold(copy);

            _displayCopies();
          }
        });

        // TODO: Handle reservations
        reserve.setOnAction(e -> {});

        updatePrice.setOnAction(e -> {
          boolean isDouble = false;
          double price = copy.getPrice();

          while(!isDouble) {
            try {
              price = Double.parseDouble(Dialog.input("Modification du prix", "Entrez le nouveau montant :", Double.toString(price)));
              isDouble = true;
            } catch (NumberFormatException ex) {
              Dialog.information("Vous devez entrer un montant valide");
            }
          }

          if (memberHandler.updateCopyPrice(copy.getId(), price)) {
            getMember().getAccount().getAvailable().remove(copy);

            copy.setPrice(price);
            getMember().getAccount().getAvailable().add(copy);
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue lors de la mise à jour de l'exemplaire");
          }
        });

        delete.setOnAction(e -> {
          if(memberHandler.deleteCopy(copy.getId())) {
            getMember().getAccount().getAvailable().remove(copy);
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue lors de la supression de l'exemplaire");
          }
        });
      }
    });

    tblSold.setOnMouseClicked(event -> {
      Node node = ((Node) event.getTarget()).getParent();
      TableRow<Copy> row;

      if (node instanceof TableRow) {
        row = (TableRow<Copy>) node;
      } else {
        row = (TableRow<Copy>) node.getParent();
      }

      final Copy copy = row.getItem();

      if(event.getButton() == MouseButton.SECONDARY) {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem cancel = new MenuItem("Annuler la vente");

        contextMenu.getItems().addAll(cancel);
        row.setContextMenu(contextMenu);

        cancel.setOnAction(e -> {
          if(memberHandler.cancelSell(copy.getId())) {
            getMember().getAccount().getSold().remove(copy);

            for (int i = 0; i < copy.getAllTransactions().size(); i++) {
              if (copy.getAllTransactions().get(i).getType().equals("SELL") || copy.getAllTransactions().get(i).getType().equals("SELL_PARENT")) {
                copy.getAllTransactions().remove(i);
              }
            }

            getMember().getAccount().getAvailable().add(copy);
            _displayCopies();
          } else {
            Dialog.information("Une erreur est survenue lors de l'annulation de la vente");
          }
        });
      }
    });

    tblComments.setOnMouseClicked(event -> {
      Node node = ((Node) event.getTarget()).getParent();
      TableRow<Comment> row = null;

      if (node instanceof TableRow) {
        row = (TableRow<Comment>) node;
      } else if (node.getParent() instanceof TableRow) {
        row = (TableRow<Comment>) node.getParent();
      }

      if (row != null) {
        final Comment comment = row.getItem();

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
              int id = comment.getId();

              if (memberHandler.editComment(id, str)) {
                getMember().getAccount().getComment(id).setDate(new Date());
                getMember().getAccount().getComment(id).setComment(str);
                _displayComment();
              } else {
                Dialog.information("Une erreur est survenues lors de la modification du commentaire");
              }
            }
          });

          delete.setOnAction(e -> {
            if (Dialog.dialogueConfirmation("Voulez-vous vraiment delete ce commentaire ?")) {
              if (memberHandler.deleteComment(comment.getId())) {
                getMember().getAccount().getComments().remove(comment);
                _displayComment();
              } else {
                Dialog.information("Une erreur est survenue lors de la supression du commentaire");
              }
            }
          });

          add.setOnAction(e -> {
            String str = Dialog.input("Comment", "Saisissez votre commentaire :");

            if (!str.isEmpty()) {
              int id = memberHandler.addComment(str);

              if (id != 0) {
                Comment c = new Comment();

                c.setId(id);
                c.setComment(str);
                c.setDate(new Date());

                getMember().getAccount().addComment(c);
                _displayComment();
              } else {
                Dialog.information("Une erreur est survenue lors de la création du commentaire");
              }
            }
          });
        }
      }
    });

    btnPay.setOnAction(event -> {
      int amount = (int) getMember().getAccount().amountSold();

      if(amount != 0) {
        String message = "Veuillez remettre " + amount + "$ à " + getMember().getFirstName();

        if(Dialog.dialogueConfirmation(message)) {
          int[] copyId = new int[getMember().getAccount().getSold().size()];

          for(int i = 0; i < getMember().getAccount().getSold().size(); i++) {
            copyId[i] = getMember().getAccount().getSold().get(i).getId();
          }

          memberHandler.addTransaction(copyId, 4);

          getMember().getAccount().addPayed((ArrayList<Copy>) getMember().getAccount().getSold().clone());
          getMember().getAccount().getSold().clear();
          _displayCopies();
        }
      } else {
        String message = "Le solde de " + getMember().getFirstName() + " est à 0$";
        Dialog.information(message);
      }
    });

    btnRenew.setOnAction(event -> {
      if(memberHandler.renewAccount(getMember().getNo())) {
        Date date = new Date();
        getMember().getAccount().setLastActivity(date);
        _displayAccount();
        Dialog.information("Le compte a été renouvelé");
      } else {
        Dialog.information("Une erreur empêche le compte d'être renouvelé");
      }
    });

    btnAddComment.setOnAction(event -> {
      String string = Dialog.input("Comment", "Saisissez votre commentaire :");

      if(!string.isEmpty()) {
        int id = memberHandler.addComment(string);

        if (id != 0) {
          Comment comment = new Comment();

          comment.setComment(string);
          comment.setDate(new Date());
          comment.setId(id);

          getMember().getAccount().addComment(comment);
          _displayComment();
        } else {
          Dialog.information("Une erreur est survenue lors de la création du commentaire");
        }
      }
    });

    btnAddReservation.setOnAction(event -> tblReservation.setVisible(!tblReservation.isVisible()));

    btnAvailable.setOnAction(event -> tblAvailable.setVisible(!tblAvailable.isVisible()));

    btnSold.setOnAction(event -> tblSold.setVisible(!tblSold.isVisible()));

    btnPaid.setOnAction(event -> tblPaid.setVisible(!tblPaid.isVisible()));

    btnDelete.setOnAction(event -> {
      if (Dialog.dialogueConfirmation("Êtes-vous certain.e de vouloir supprimer ce member ?")) {
        String message = "Erreur lors de la suppression";

        if (memberHandler.deleteMember()) {
          message = "Suppression réussie";
        }

        Dialog.information(message);
      }
    });
  }

  private void _dataBinding() {
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

  @SuppressWarnings("StatementWithEmptyBody")
  private void _displayMember() {
    if (getMember() instanceof StudentParent) {
      // TODO: Handle StudentParent
    } else {
      btnAddReservation.setVisible(false);
      tblReservation.setVisible(false);
    }

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

  @SuppressWarnings("deprecation")
  private void _displayAccount() {
    Date today = new Date();
    Date deactivation = (Date) getMember().getAccount().getLastActivity().clone();
    deactivation.setYear(deactivation.getYear() + 1);
    String state = "Compte actif";

    if (deactivation.before(today)) {
      state = "Compte désactivé";
    }

    // TODO: Inside account class
    String registration = getMember().getAccount().getRegistration().getDate() + "/" + (getMember().getAccount().getRegistration().getMonth() + 1) + "/" + (getMember().getAccount().getRegistration().getYear() + 1900);
    String lastActivity = getMember().getAccount().getLastActivity().getDate() + "/" + (getMember().getAccount().getLastActivity().getMonth() + 1) + "/" + (getMember().getAccount().getLastActivity().getYear() + 1900);
    String deactivationString = deactivation.getDate() + "/" + (deactivation.getMonth() + 1) + "/" + (deactivation.getYear() + 1900);

    lblState.setText(state);
    lblRegistration.setText(registration);
    lblLastActivity.setText(lastActivity);
    lblDeactivation.setText(deactivationString);
  }

  private void _displayCopies() {
    ObservableList<Copy> available = FXCollections.observableArrayList(getMember().getAccount().getAvailable());
    ObservableList<Copy> sold = FXCollections.observableArrayList(getMember().getAccount().getSold());
    ObservableList<Copy> paid = FXCollections.observableArrayList(getMember().getAccount().getPaid());

    tblAvailable.setItems(available);
    tblSold.setItems(sold);
    tblPaid.setItems(paid);
  }

  private void _displayComment() {
    ObservableList<Comment> comment = FXCollections.observableArrayList(getMember().getAccount().getComments());
    tblComments.setItems(comment);
  }
}

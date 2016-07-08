package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.article.Exemplaire;
import handler.MemberHandler;
import model.membre.Commentaire;
import model.membre.Membre;
import model.membre.ParentEtudiant;
import model.transaction.Transaction;
import ressources.Dialogue;

/**
 *
 * @author Jessy Lachapelle
 * @since 24/11/2015
 * @version 0.1
 */
public class MemberViewController extends Controller {

  private MemberHandler memberHandler;

  @FXML private Button btn_modif;
  @FXML private Button btn_suppMem;

  @FXML private Label lbl_nom;
  @FXML private Label lbl_no;
  @FXML private Label lbl_adresse;
  @FXML private Label lbl_courriel;
  @FXML private Label lbl_telephone1;
  @FXML private Label lbl_telephone2;

  @FXML private Label lbl_etat;
  @FXML private Label lbl_inscription;
  @FXML private Label lbl_derniereActivite;
  @FXML private Label lbl_desactivation;

  @FXML private TableView tbl_commentaire;
  @FXML private TableColumn<Commentaire, String> col_commentaire;
  @FXML private TableColumn<Commentaire, String> col_commentaire_date;

  @FXML private Button btn_ajout_exemplaires;
  @FXML private Button btn_renouv;
  @FXML private Button btn_ajoutCom;
  @FXML private Button btn_remboursement;

  @FXML private Button btn_reservation;
  @FXML private TableView tbl_reservation;

  @FXML private Button btn_aVendre;
  @FXML private TableView tbl_aVendre;
  @FXML private TableColumn<Exemplaire, String> col_aVendre_titre;
  @FXML private TableColumn<Exemplaire, String> col_aVendre_editeur;
  @FXML private TableColumn<Exemplaire, String> col_aVendre_edition;
  @FXML private TableColumn<Exemplaire, String> col_aVendre_date;
  @FXML private TableColumn<Exemplaire, String> col_aVendre_prix;

  @FXML private Button btn_vendu;
  @FXML private TableView tbl_vendu;
  @FXML private TableColumn<Exemplaire, String> col_vendu_titre;
  @FXML private TableColumn<Exemplaire, String> col_vendu_editeur;
  @FXML private TableColumn<Exemplaire, String> col_vendu_edition;
  @FXML private TableColumn<Exemplaire, String> col_vendu_dateAjout;
  @FXML private TableColumn<Exemplaire, String> col_vendu_dateVente;
  @FXML private TableColumn<Exemplaire, String> col_vendu_prix;

  @FXML private Button btn_argentRemis;
  @FXML private TableView tbl_argentRemis;
  @FXML private TableColumn<Exemplaire, String> col_argentRemis_titre;
  @FXML private TableColumn<Exemplaire, String> col_argentRemis_editeur;
  @FXML private TableColumn<Exemplaire, String> col_argentRemis_edition;
  @FXML private TableColumn<Exemplaire, String> col_argentRemis_dateAjout;
  @FXML private TableColumn<Exemplaire, String> col_argentRemis_dateVente;
  @FXML private TableColumn<Exemplaire, String> col_argentRemis_dateRemise;
  @FXML private TableColumn<Exemplaire, String> col_argentRemis_prix;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    memberHandler = new MemberHandler();
    eventHandlers();
    dataBinding();
    tbl_vendu.setVisible(false);
    tbl_argentRemis.setVisible(false);
  }

  public void loadMember(int memberNo) {
    memberHandler.setMember(memberNo);
    displayMember();
  }

  public void loadMember(Membre member) {
    memberHandler.setMember(member);
    displayMember();
  }

  public Button getEditButton() {
    return btn_modif;
  }

  public Button getAddCopyButton() {
    return btn_ajout_exemplaires;
  }

  public TableView[] getCopyTables() {
    TableView[] tbl = {tbl_reservation, tbl_aVendre, tbl_vendu, tbl_argentRemis};
    return tbl;
  }

  public Membre getMember() {
    return memberHandler.getMember();
  }

  private void eventHandlers() {
    tbl_aVendre.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        Node node = ((Node) event.getTarget()).getParent();
        TableRow row;

        if(node instanceof TableRow) {
          row = (TableRow) node;
        } else {
          row = (TableRow) node.getParent();
        }

        final Exemplaire e = (Exemplaire) row.getItem();

        if(event.isPrimaryButtonDown()) {
          @SuppressWarnings("rawtypes")
          TablePosition pos = (TablePosition) tbl_aVendre.getSelectionModel().getSelectedCells().get(0);
          int col = pos.getColumn();

          if(col == 1) {
            System.out.println("Exemplaire de " + e.getName() + " vendu à " + e.getStrPrix());
          }
        } else if(event.getButton() == MouseButton.SECONDARY) {
          final ContextMenu contextMenu = new ContextMenu();
          MenuItem vendre = new MenuItem("Vendre");
          MenuItem vendreParent = new MenuItem("Vendre à 50%");
          MenuItem reserver = new MenuItem("Réserver");
          MenuItem modifier = new MenuItem("Modifier le prix");
          MenuItem supprimer = new MenuItem("Supprimer");
          contextMenu.getItems().addAll(vendre, vendreParent, reserver, modifier, supprimer);
          row.setContextMenu(contextMenu);

          vendre.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              int id = memberHandler.addTransaction(e.getId(), 2);

              // TODO: Fix transaction id
              //if (id != 0) {
                Transaction t = new Transaction();
                //t.setId(id);
                t.setType(2);
                t.setDate(new Date());

                getMember().getAccount().getEnVente().remove(e);
                e.ajouterTransaction(t);
                getMember().getAccount().addSold(e);

                displayCopy();
              //}
            }
          });

          vendreParent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              int id = memberHandler.addTransaction(e.getId(), 2);

              // TODO: Fix transaction id
              //if (id != 0) {
              Transaction t = new Transaction();
              //t.setId(id);
              t.setType(2);
              t.setDate(new Date());

              getMember().getAccount().getEnVente().remove(e);
              e.ajouterTransaction(t);
              getMember().getAccount().addSold(e);

              displayCopy();
              //}
            }
          });

          // TODO: Handle reservations
          reserver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {}
          });

          modifier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              boolean estDouble = false;
              double prix = e.getPrice();

              while(!estDouble) {
                try {
                  prix = Double.parseDouble(Dialogue.dialogueSaisie("Modification du prix", "Entrez le nouveau montant :", Double.toString(prix)));
                  estDouble = true;
                } catch (NumberFormatException e) {
                  Dialogue.dialogueInformation("Vous devez entrer un montant valide");
                }
              }

              if (memberHandler.updateCopyPrice(e.getId(), prix)) {
                getMember().getAccount().getEnVente().remove(e);

                e.setPrice(prix);
                getMember().getAccount().getEnVente().add(e);
                displayCopy();
              } else {
                Dialogue.dialogueInformation("Une erreur est survenue lors de la mise à jour de l'exemplaire");
              }
            }
          });

          supprimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              if(memberHandler.deleteCopy(e.getId())) {
                getMember().getAccount().getEnVente().remove(e);
                displayCopy();
              } else {
                Dialogue.dialogueInformation("Une erreur est survenue lors de la supression de l'exemplaire");
              }
            }
          });
        }
      }
    });

    tbl_vendu.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        Node node = ((Node) event.getTarget()).getParent();
        TableRow row;

        if(node instanceof TableRow) {
          row = (TableRow) node;
        } else {
          row = (TableRow) node.getParent();
        }

        final Exemplaire e = (Exemplaire) row.getItem();

        if(event.getButton() == MouseButton.SECONDARY) {
          final ContextMenu contextMenu = new ContextMenu();
          MenuItem annuler = new MenuItem("Annuler la vente");

          contextMenu.getItems().addAll(annuler);
          row.setContextMenu(contextMenu);

          annuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              if(memberHandler.cancelSell(e.getId())) {
                getMember().getAccount().getVendu().remove(e);

                for(int noTransaction = 0; noTransaction < e.getTousTransactions().size(); noTransaction++) {
                  if(e.getTousTransactions().get(noTransaction).getType() == 2 || e.getTousTransactions().get(noTransaction).getType() == 3) {
                    e.getTousTransactions().remove(noTransaction);
                  }
                }

                getMember().getAccount().getEnVente().add(e);
                displayCopy();
              } else {
                Dialogue.dialogueInformation("Une erreur est survenue lors de l'annulation de la vente");
              }
            }
          });
        }
      }
    });

    tbl_commentaire.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        Node node = ((Node) event.getTarget()).getParent();
        TableRow row = null;

        if (node instanceof TableRow) {
          row = (TableRow) node;
        } else if (node.getParent() instanceof TableRow) {
          row = (TableRow) node.getParent();
        }

        if (row != null) {
          final Commentaire c = (Commentaire) row.getItem();

          if (event.getButton() == MouseButton.SECONDARY) {
            final ContextMenu contextMenu = new ContextMenu();
            MenuItem modifier = new MenuItem("Modifier");
            MenuItem supprimer = new MenuItem("Supprimer");
            MenuItem ajouter = new MenuItem("Ajouter");

            contextMenu.getItems().addAll(modifier, supprimer, ajouter);
            row.setContextMenu(contextMenu);

            modifier.setOnAction(new EventHandler<ActionEvent>() {
              @Override
              public void handle(ActionEvent event) {
                String str = Dialogue.dialogueSaisie("Commentaire", "Saisissez votre commentaire :", c.getComment());

                if (!str.isEmpty()) {
                  int id = c.getId();

                  if (memberHandler.editComment(id, str)) {
                    getMember().getAccount().getComment(id).setDate(new Date());
                    getMember().getAccount().getComment(id).setComment(str);
                    displayComment();
                  } else {
                    Dialogue.dialogueInformation("Une erreur est survenues lors de la modification du commentaire");
                  }
                }
              }
            });

            supprimer.setOnAction(new EventHandler<ActionEvent>() {
              @Override
              public void handle(ActionEvent event) {
                if (Dialogue.dialogueConfirmation("Voulez-vous vraiment supprimer ce commentaire ?")) {
                  if (memberHandler.deleteComment(c.getId())) {
                    getMember().getAccount().getComments().remove(c);
                    displayComment();
                  } else {
                    Dialogue.dialogueInformation("Une erreur est survenue lors de la supression du commentaire");
                  }
                }
              }
            });

            ajouter.setOnAction(new EventHandler<ActionEvent>() {
              @Override
              public void handle(ActionEvent event) {
                String str = Dialogue.dialogueSaisie("Commentaire", "Saisissez votre commentaire :");

                if (!str.isEmpty()) {
                  int id = memberHandler.addComment(str);

                  if (id != 0) {
                    Commentaire commentaire = new Commentaire();

                    commentaire.setId(id);
                    commentaire.setComment(str);
                    commentaire.setDate(new Date());

                    getMember().getAccount().ajoutCommentaire(commentaire);
                    displayComment();
                  } else {
                    Dialogue.dialogueInformation("Une erreur est survenue lors de la création du commentaire");
                  }
                }
              }
            });
          }
        }
      }
    });

    btn_remboursement.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        int montant = (int) getMember().getAccount().montantDu();

        if(montant != 0) {
          String message = "Veuillez remettre " + montant + "$ à " + getMember().getFirstName();

          if(Dialogue.dialogueConfirmation(message)) {
            int[] copyId = new int[getMember().getAccount().getVendu().size()];

            for(int i = 0; i < getMember().getAccount().getVendu().size(); i++) {
              copyId[i] = getMember().getAccount().getVendu().get(i).getId();
            }

            memberHandler.addTransaction(copyId, 4);

            getMember().getAccount().addPayed((ArrayList<Exemplaire>) getMember().getAccount().getVendu().clone());
            getMember().getAccount().getVendu().clear();
            displayCopy();
          }
        } else {
          String message = "Le solde de " + getMember().getFirstName() + " est à 0$";
          Dialogue.dialogueInformation(message);
        }
      }
    });

    btn_renouv.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if(memberHandler.renewAccount(getMember().getNo())) {
          Date date = new Date();
          getMember().getAccount().setDateDerniereActivite(date);
          displayAccount();
          Dialogue.dialogueInformation("Le compte a été renouvelé");
        } else {
          Dialogue.dialogueInformation("Une erreur empêche le compte d'être renouvelé");
        }
      }
    });

    btn_ajoutCom.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String str = Dialogue.dialogueSaisie("Commentaire", "Saisissez votre commentaire :");

        if(!str.isEmpty()) {
          int id = memberHandler.addComment(str);

          if (id != 0) {
            Commentaire commentaire = new Commentaire();

            commentaire.setComment(str);
            commentaire.setDate(new Date());
            commentaire.setId(id);

            getMember().getAccount().ajoutCommentaire(commentaire);
            displayComment();
          } else {
            Dialogue.dialogueInformation("Une erreur est survenue lors de la création du commentaire");
          }
        }
      }
    });

    btn_reservation.setOnAction(event -> tbl_reservation.setVisible(!tbl_reservation.isVisible()));

    btn_aVendre.setOnAction(event -> tbl_aVendre.setVisible(!tbl_aVendre.isVisible()));

    btn_vendu.setOnAction(event -> tbl_vendu.setVisible(!tbl_vendu.isVisible()));

    btn_argentRemis.setOnAction(event -> tbl_argentRemis.setVisible(!tbl_argentRemis.isVisible()));

    btn_suppMem.setOnAction(event -> {
      if (Dialogue.dialogueConfirmation("Êtes-vous certain.e de vouloir supprimer ce member ?")) {
        String message = "Erreur lors de la suppression";

        if (memberHandler.deleteMember()) {
          message = "Suppression réussie";
        }

        Dialogue.dialogueInformation(message);
      }
    });
  }

  private void dataBinding() {
    col_commentaire.setCellValueFactory(new PropertyValueFactory<Commentaire, String>("comment"));
    col_commentaire_date.setCellValueFactory(new PropertyValueFactory<Commentaire, String>("date"));

    col_aVendre_titre.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("name"));
    col_aVendre_edition.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("edition"));
    col_aVendre_editeur.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("editor"));
    col_aVendre_date.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateAdded"));
    col_aVendre_prix.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("strPrix"));

    col_vendu_titre.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("name"));
    col_vendu_edition.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("edition"));
    col_vendu_editeur.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("editor"));
    col_vendu_dateAjout.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateAdded"));
    col_vendu_dateVente.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateSold"));
    col_vendu_prix.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("strPrix"));

    col_argentRemis_titre.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("name"));
    col_argentRemis_editeur.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("editor"));
    col_argentRemis_edition.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("edition"));
    col_argentRemis_dateAjout.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateAdded"));
    col_argentRemis_dateVente.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateSold"));
    col_argentRemis_dateRemise.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("datePaid"));
    col_argentRemis_prix.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("strPrix"));
  }

  private void displayMember() {
    if(getMember() instanceof ParentEtudiant) {

    } else {
      btn_reservation.setVisible(false);
      tbl_reservation.setVisible(false);
    }

    lbl_nom.setText(getMember().getFirstName() + " " + getMember().getLastName());
    lbl_no.setText(Integer.toString(getMember().getNo()));
    lbl_adresse.setText(getMember().getAddressStr());
    lbl_courriel.setText(getMember().getEmail());

    if(getMember().getPhone(0) != null) {
      lbl_telephone1.setText(getMember().getPhone(0).toString());
    }
    if(getMember().getPhone(1) != null) {
      lbl_telephone2.setText(getMember().getPhone(1).toString());
    }

    displayAccount();
    displayComment();
    displayCopy();
  }

  private void displayAccount() {
    Date today = new Date();
    Date desactivation = (Date) getMember().getAccount().getDateDerniereActivite().clone();
    desactivation.setYear(desactivation.getYear() + 1);
    String etat = "Compte actif";

    if(desactivation.before(today)) {
      etat = "Compte désactivé";
    }

    // TODO: Inside account class
    String inscription = getMember().getAccount().getDateCreation().getDate() + "/" + (getMember().getAccount().getDateCreation().getMonth() + 1) + "/" + (getMember().getAccount().getDateCreation().getYear() + 1900);
    String derniereActivite = getMember().getAccount().getDateDerniereActivite().getDate() + "/" + (getMember().getAccount().getDateDerniereActivite().getMonth() + 1) + "/" + (getMember().getAccount().getDateDerniereActivite().getYear() + 1900);
    String strDesactivation = desactivation.getDate() + "/" + (desactivation.getMonth() + 1) + "/" + (desactivation.getYear() + 1900);

    lbl_etat.setText(etat);
    lbl_inscription.setText(inscription);
    lbl_derniereActivite.setText(derniereActivite);
    lbl_desactivation.setText(strDesactivation);
  }

  private void displayCopy() {
    ObservableList<Exemplaire> aVendre = FXCollections.observableArrayList(getMember().getAccount().getEnVente());
    ObservableList<Exemplaire> vendu = FXCollections.observableArrayList(getMember().getAccount().getVendu());
    ObservableList<Exemplaire> argentRemis = FXCollections.observableArrayList(getMember().getAccount().getArgentRemis());

    tbl_aVendre.setItems(aVendre);
    tbl_vendu.setItems(vendu);
    tbl_argentRemis.setItems(argentRemis);
  }

  private void displayComment() {
    ObservableList<Commentaire> commentaire = FXCollections.observableArrayList(getMember().getAccount().getComments());
    tbl_commentaire.setItems(commentaire);
  }
}

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
import hanlder.MemberHandler;
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
@SuppressWarnings("Convert2Lambda")
public class MemberViewController extends Controller {

  private Membre membre;
  private MemberHandler gMembre;

  @FXML
  private Button btn_modif;

  @FXML
  private Label lbl_nom;
  @FXML
  private Label lbl_no;
  @FXML
  private Label lbl_adresse;
  @FXML
  private Label lbl_courriel;
  @FXML
  private Label lbl_telephone1;
  @FXML
  private Label lbl_telephone2;

  @FXML
  private Label lbl_etat;
  @FXML
  private Label lbl_inscription;
  @FXML
  private Label lbl_derniereActivite;
  @FXML
  private Label lbl_desactivation;

  @FXML
  private TableView tbl_commentaire;
  @FXML
  private TableColumn<Commentaire, String> col_commentaire;
  @FXML
  private TableColumn<Commentaire, String> col_commentaire_date;

  @FXML
  private Button btn_ajout_exemplaires;
  @FXML
  private Button btn_renouv;
  @FXML
  private Button btn_ajoutCom;
  @FXML
  private Button btn_remboursement;

  @FXML
  private Button btn_reservation;
  @FXML
  private TableView tbl_reservation;

  @FXML
  private Button btn_aVendre;
  @FXML
  private TableView tbl_aVendre;
  @FXML
  private TableColumn<Exemplaire, String> col_aVendre_titre;
  @FXML
  private TableColumn<Exemplaire, String> col_aVendre_editeur;
  @FXML
  private TableColumn<Exemplaire, String> col_aVendre_edition;
  @FXML
  private TableColumn<Exemplaire, String> col_aVendre_date;
  @FXML
  private TableColumn<Exemplaire, String> col_aVendre_prix;
  @FXML
  private TableColumn<Exemplaire, String> col_aVendre_vendre;

  @FXML
  private Button btn_vendu;
  @FXML
  private TableView tbl_vendu;
  @FXML
  private TableColumn<Exemplaire, String> col_vendu_titre;
  @FXML
  private TableColumn<Exemplaire, String> col_vendu_editeur;
  @FXML
  private TableColumn<Exemplaire, String> col_vendu_edition;
  @FXML
  private TableColumn<Exemplaire, String> col_vendu_dateAjout;
  @FXML
  private TableColumn<Exemplaire, String> col_vendu_dateVente;
  @FXML
  private TableColumn<Exemplaire, String> col_vendu_prix;

  @FXML
  private Button btn_argentRemis;
  @FXML
  private TableView tbl_argentRemis;
  @FXML
  private TableColumn<Exemplaire, String> col_argentRemis_titre;
  @FXML
  private TableColumn<Exemplaire, String> col_argentRemis_editeur;
  @FXML
  private TableColumn<Exemplaire, String> col_argentRemis_edition;
  @FXML
  private TableColumn<Exemplaire, String> col_argentRemis_dateAjout;
  @FXML
  private TableColumn<Exemplaire, String> col_argentRemis_dateVente;
  @FXML
  private TableColumn<Exemplaire, String> col_argentRemis_dateRemise;
  @FXML
  private TableColumn<Exemplaire, String> col_argentRemis_prix;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    gMembre = new MemberHandler();
    assertions();
    eventHandlers();
    dataBinding();
    tbl_vendu.setVisible(false);
    tbl_argentRemis.setVisible(false);
  }

  public void loadMembre(int noMembre) {
    membre = gMembre.consulteMembre(noMembre);
    afficheMembre();
  }

  public void loadMembre(Membre m) {
    membre = m;
    afficheMembre();
  }

  public Button getButtonModif() {
    return btn_modif;
  }

  public Button getButtonAjoutExemplaires() {
    return btn_ajout_exemplaires;
  }

  public TableView[] getTableauxExemplaires() {
    TableView[] tbl = {tbl_reservation, tbl_aVendre, tbl_vendu, tbl_argentRemis};
    return tbl;
  }

  public Membre getMembre() {
    return membre;
  }

  private void assertions() {
    assert btn_ajout_exemplaires != null : "fx:id=\"btn_ajout_exemplaires\" was not injected: check your FXML file 'memberView.fxml'.";
    assert btn_modif != null : "fx:id=\"btn_modif\" was not injected: check your FXML file 'memberView.fxml'.";
    assert btn_renouv != null : "fx:id=\"btn_renouv\" was not injected: check your FXML file 'memberView.fxml'.";
    assert btn_remboursement != null : "fx:id=\"btn_remboursement\" was not injected: check your FXML file 'memberView.fxml'.";

    assert lbl_nom != null : "fx:id=\"lbl_nom\" was not injected: check your FXML file 'memberView.fxml'.";
    assert lbl_no != null : "fx:id=\"lbl_no\" was not injected: check your FXML file 'memberView.fxml'.";
    assert lbl_adresse != null : "fx:id=\"lbl_adresse\" was not injected: check your FXML file 'memberView.fxml'.";
    assert lbl_courriel != null : "fx:id=\"lbl_courriel\" was not injected: check your FXML file 'memberView.fxml'.";
    assert lbl_telephone1 != null : "fx:id=\"lbl_telephone1\" was not injected: check your FXML file 'memberView.fxml'.";
    assert lbl_telephone2 != null : "fx:id=\"lbl_telephone2\" was not injected: check your FXML file 'memberView.fxml'.";

    assert lbl_etat != null : "fx:id=\"lbl_etat\" was not injected: check your FXML file 'memberView.fxml'.";
    assert lbl_inscription != null : "fx:id=\"lbl_inscription\" was not injected: check your FXML file 'memberView.fxml'.";
    assert lbl_derniereActivite != null : "fx:id=\"lbl_derniereActivite\" was not injected: check your FXML file 'memberView.fxml'.";
    assert lbl_desactivation != null : "fx:id=\"lbl_desactivation\" was not injected: check your FXML file 'memberView.fxml'.";

    assert tbl_commentaire != null : "fx:id=\"tbl_commentaire\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_commentaire != null : "fx:id=\"col_commentaire\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_commentaire_date != null : "fx:id=\"col_commentaire_date\" was not injected: check your FXML file 'memberView.fxml'.";
    assert btn_ajoutCom != null : "fx:id=\"btn_ajoutCom\" was not injected: check your FXML file 'memberView.fxml'.";

    assert btn_reservation != null : "fx:id=\"btn_reservation\" was not injected: check your FXML file 'memberView.fxml'.";
    assert tbl_reservation != null : "fx:id=\"tbl_reservation\" was not injected: check your FXML file 'memberView.fxml'.";

    assert btn_aVendre != null : "fx:id=\"btn_aVendre\" was not injected: check your FXML file 'memberView.fxml'.";
    assert tbl_aVendre != null : "fx:id=\"tbl_aVendre\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_aVendre_titre != null : "fx:id=\"col_aVendre_titre\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_aVendre_editeur != null : "fx:id=\"col_aVendre_editeur\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_aVendre_edition != null : "fx:id=\"col_aVendre_edition\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_aVendre_date != null : "fx:id=\"col_aVendre_date\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_aVendre_prix != null : "fx:id=\"col_aVendre_prix\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_aVendre_vendre != null : "fx:id=\"col_aVendre_vendre\" was not injected: check your FXML file 'memberView.fxml'.";

    assert btn_vendu != null : "fx:id=\"btn_vendu\" was not injected: check your FXML file 'memberView.fxml'.";
    assert tbl_vendu != null : "fx:id=\"tbl_vendu\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_vendu_titre != null : "fx:id=\"col_vendu_titre\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_vendu_editeur != null : "fx:id=\"col_vendu_editeur\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_vendu_edition != null : "fx:id=\"col_vendu_edition\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_vendu_dateAjout != null : "fx:id=\"col_vendu_dateAjout\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_vendu_dateVente != null : "fx:id=\"col_vendu_dateVente\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_vendu_prix != null : "fx:id=\"col_vendu_prix\" was not injected: check your FXML file 'memberView.fxml'.";

    assert btn_argentRemis != null : "fx:id=\"btn_argentRemis\" was not injected: check your FXML file 'memberView.fxml'.";
    assert tbl_argentRemis != null : "fx:id=\"tbl_argentRemis\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_argentRemis_titre != null : "fx:id=\"col_argentRemis_titre\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_argentRemis_editeur != null : "fx:id=\"col_argentRemis_editeur\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_argentRemis_edition != null : "fx:id=\"col_argentRemis_edition\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_argentRemis_dateAjout != null : "fx:id=\"col_argentRemis_dateAjout\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_argentRemis_dateVente != null : "fx:id=\"col_argentRemis_dateVente\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_argentRemis_dateRemise != null : "fx:id=\"col_argentRemis_dateRemise\" was not injected: check your FXML file 'memberView.fxml'.";
    assert col_argentRemis_prix != null : "fx:id=\"col_argentRemis_prix\" was not injected: check your FXML file 'memberView.fxml'.";
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
            System.out.println("Exemplaire de " + e.getNom() + " vendu à " + e.getStrPrix());
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
              Transaction t = new Transaction();
              t.setType(2);
              t.setDate(new Date());

              gMembre.vendreExemplaire(membre.getNoMembre(), e.getNoExemplaire());
              membre.getCompte().getEnVente().remove(e);

              e.ajouterTransaction(t);
              membre.getCompte().ajoutVendu(e);

              afficheExemplaires();
            }
          });

          vendreParent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              Transaction t = new Transaction();
              t.setType(3);
              t.setDate(new Date());

              gMembre.vendreExemplaire(membre.getNoMembre(), e.getNoExemplaire(), true);
              membre.getCompte().getEnVente().remove(e);

              e.ajouterTransaction(t);
              membre.getCompte().ajoutVendu(e);

              afficheExemplaires();
            }
          });

          reserver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
          });

          modifier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              boolean estDouble = false;
              double prix = e.getPrix();

              while(!estDouble) {
                try {
                  prix = Double.parseDouble(Dialogue.dialogueSaisie("Modification du prix", "Entrez le nouveau montant :", Double.toString(prix)));

                  if(prix == 0) {
                    Dialogue.dialogueInformation("Vous devez entrer un montant valide");
                  } else {
                    estDouble = true;
                  }
                } catch (NumberFormatException e) {
                  Dialogue.dialogueInformation("Vous devez entrer un montant valide");
                }
              }
              gMembre.modifiePrixExemplaire(e.getNoExemplaire(), prix);
              membre.getCompte().getEnVente().remove(e);

              e.setPrix(prix);
              membre.getCompte().getEnVente().add(e);
              afficheExemplaires();
            }
          });

          supprimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              if(gMembre.supprimeExemplaire(e.getNoExemplaire())) {
                membre.getCompte().getEnVente().remove(e);
                afficheExemplaires();
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
              if(gMembre.annuleVente(e.getNoExemplaire())) {
                membre.getCompte().getVendu().remove(e);

                for(int noTransaction = 0; noTransaction < e.getTousTransactions().size(); noTransaction++) {
                  if(e.getTousTransactions().get(noTransaction).getType() == 2 || e.getTousTransactions().get(noTransaction).getType() == 3) {
                    e.getTousTransactions().remove(noTransaction);
                  }
                }

                membre.getCompte().getEnVente().add(e);
                afficheExemplaires();
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
        TableRow row;

        if(node instanceof TableRow) {
          row = (TableRow) node;
        } else {
          row = (TableRow) node.getParent();
        }
        final Commentaire c = (Commentaire) row.getItem();

        if(event.getButton() == MouseButton.SECONDARY) {
          final ContextMenu contextMenu = new ContextMenu();
          MenuItem modifier = new MenuItem("Modifier");
          MenuItem supprimer = new MenuItem("Supprimer");
          MenuItem ajouter = new MenuItem("Ajouter");

          contextMenu.getItems().addAll(modifier, supprimer, ajouter);
          row.setContextMenu(contextMenu);

          modifier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              String str = Dialogue.dialogueSaisie("Commentaire", "Saisissez votre commentaire :", c.getCommentaire());

              if(!str.isEmpty()) {
                int id = c.getId();
                gMembre.modifieCommentaire(id, str);
                membre.getCompte().getCommentaire().remove(c);

                Commentaire commentaire = new Commentaire();
                commentaire.setId(id);
                commentaire.setCommentaire(str);
                commentaire.setDate(new Date());

                membre.getCompte().ajoutCommentaire(commentaire);
                afficheCommentaire();
              }
            }
          });

          supprimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              if(Dialogue.dialogueConfirmation("Voulez-vous vraiment supprimer ce commentaire ?")) {
                membre.getCompte().getCommentaire().remove(c);
                gMembre.supprimeCommentaire(c.getId());
                afficheCommentaire();
              }
            }
          });

          ajouter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              String str = Dialogue.dialogueSaisie("Commentaire", "Saisissez votre commentaire :");

              if(!str.isEmpty()) {
                Commentaire commentaire = new Commentaire();

                commentaire.setId(gMembre.ajoutCommentaire(membre.getNoMembre(), str));
                commentaire.setCommentaire(str);
                commentaire.setDate(new Date());

                membre.getCompte().ajoutCommentaire(commentaire);
                afficheCommentaire();
              }
            }
          });
        }
      }
    });

    btn_remboursement.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        int montant = (int) membre.getCompte().montantDu();

        if(montant != 0) {
          String message = "Veuillez remettre " + montant + "$ à " + membre.getPrenom();

          if(Dialogue.dialogueConfirmation(message)) {
            for(int noVendu = 0; noVendu < membre.getCompte().getVendu().size(); noVendu++) {
              gMembre.remiseArgentExemplaire(membre.getNoMembre(), membre.getCompte().getVendu().get(noVendu).getNoExemplaire());
            }
            membre.getCompte().ajoutArgentRemis((ArrayList<Exemplaire>) membre.getCompte().getVendu().clone());
            membre.getCompte().getVendu().clear();
            afficheExemplaires();
          }
        } else {
          String message = "Le solde de " + membre.getPrenom() + " est à 0$";
          Dialogue.dialogueInformation(message);
        }
      }
    });

    btn_renouv.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if(gMembre.renouveleCompte(membre.getNoMembre())) {
          Date date = new Date();
          membre.getCompte().setDateDerniereActivite(date);
          afficheCompte();
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
          Commentaire commentaire = new Commentaire();

          commentaire.setCommentaire(str);
          commentaire.setDate(new Date());
          commentaire.setId(0); // TODO insert commentaire retourne id
          gMembre.ajoutCommentaire(membre.getNoMembre(), str);

          membre.getCompte().ajoutCommentaire(commentaire);
          afficheCommentaire();
        }
      }
    });

    btn_reservation.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        tbl_reservation.setVisible(!tbl_reservation.isVisible());
      }
    });

    btn_aVendre.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        tbl_aVendre.setVisible(!tbl_aVendre.isVisible());
      }
    });

    btn_vendu.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        tbl_vendu.setVisible(!tbl_vendu.isVisible());
      }
    });

    btn_argentRemis.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        tbl_argentRemis.setVisible(!tbl_argentRemis.isVisible());
      }
    });
  }

  private void dataBinding() {
    col_commentaire.setCellValueFactory(new PropertyValueFactory<Commentaire, String>("commentaire"));
    col_commentaire_date.setCellValueFactory(new PropertyValueFactory<Commentaire, String>("date"));

    col_aVendre_titre.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("nom"));
    col_aVendre_edition.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("edition"));
    col_aVendre_editeur.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("editeur"));
    col_aVendre_date.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateAjout"));
    col_aVendre_prix.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("strPrix"));
    col_aVendre_vendre.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("etiquetteVente"));

    col_vendu_titre.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("nom"));
    col_vendu_edition.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("edition"));
    col_vendu_editeur.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("editeur"));
    col_vendu_dateAjout.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateAjout"));
    col_vendu_dateVente.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateVente"));
    col_vendu_prix.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("strPrix"));

    col_argentRemis_titre.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("nom"));
    col_argentRemis_editeur.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("editeur"));
    col_argentRemis_edition.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("edition"));
    col_argentRemis_dateAjout.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateAjout"));
    col_argentRemis_dateVente.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateVente"));
    col_argentRemis_dateRemise.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateRemise"));
    col_argentRemis_prix.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("strPrix"));
  }

  private void afficheMembre() {
    if(membre instanceof ParentEtudiant) {

    } else {
      btn_reservation.setVisible(false);
      tbl_reservation.setVisible(false);
    }

    lbl_nom.setText(membre.getPrenom() + " " + membre.getNom());
    lbl_no.setText(Integer.toString(membre.getNoMembre()));
    // lbl_adresse.setText(membre.getAdresse());
    lbl_courriel.setText(membre.getCourriel());

    if(membre.getPremierTelephone() != null) {
      lbl_telephone1.setText(membre.getPremierTelephone().toString());
    }
    if(membre.getSecondTelephone() != null) {
      lbl_telephone2.setText(membre.getSecondTelephone().toString());
    }

    afficheCompte();
    afficheCommentaire();
    afficheExemplaires();
  }

  private void afficheCompte() {
    Date today = new Date();
    Date desactivation = (Date) membre.getCompte().getDateDerniereActivite().clone();
    desactivation.setYear(desactivation.getYear() + 1);
    String etat = "Compte actif";

    if(desactivation.before(today)) {
      etat = "Compte désactivé";
    }

    String inscription = membre.getCompte().getDateCreation().getDate() + "/" + (membre.getCompte().getDateCreation().getMonth() + 1) + "/" + (membre.getCompte().getDateCreation().getYear() + 1900);
    String derniereActivite = membre.getCompte().getDateDerniereActivite().getDate() + "/" + (membre.getCompte().getDateDerniereActivite().getMonth() + 1) + "/" + (membre.getCompte().getDateDerniereActivite().getYear() + 1900);
    String strDesactivation = desactivation.getDate() + "/" + (desactivation.getMonth() + 1) + "/" + (desactivation.getYear() + 1900);

    lbl_etat.setText(etat);
    lbl_inscription.setText(inscription);
    lbl_derniereActivite.setText(derniereActivite);
    lbl_desactivation.setText(strDesactivation);
  }

  private void afficheExemplaires() {
    ObservableList<Exemplaire> aVendre = FXCollections.observableArrayList(membre.getCompte().getEnVente());
    ObservableList<Exemplaire> vendu = FXCollections.observableArrayList(membre.getCompte().getVendu());
    ObservableList<Exemplaire> argentRemis = FXCollections.observableArrayList(membre.getCompte().getArgentRemis());

    tbl_aVendre.setItems(aVendre);
    tbl_vendu.setItems(vendu);
    tbl_argentRemis.setItems(argentRemis);
  }

  private void afficheCommentaire() {
    ObservableList<Commentaire> commentaire = FXCollections.observableArrayList(membre.getCompte().getCommentaire());
    tbl_commentaire.setItems(commentaire);
  }
}

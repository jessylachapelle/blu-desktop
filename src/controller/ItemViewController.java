package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import handler.CopyHandler;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.article.*;
import handler.ItemHandler;
import handler.MemberHandler;
import model.transaction.Transaction;
import ressources.Dialogue;

/**
 * Controller de la fenêtre d'une fiche d'article
 *
 * @author Jessy Lachapelle
 * @since 29/11/2015
 * @version 0.1
 */
@SuppressWarnings({"Convert2Lambda", "Convert2Diamond"})
public class ItemViewController extends Controller {

  private Article article;
  private ItemHandler gArticle;

  @FXML
  private Label lbl_titre;
  @FXML
  private Label lbl_description;
  @FXML
  private Label lbl_auteur;
  @FXML
  private Label lbl_edition;
  @FXML
  private Label lbl_editeur;
  @FXML
  private Label lbl_annee;
  @FXML
  private Label lbl_code;
  @FXML
  private Label lbl_commentaire;
  @FXML
  private Button btn_commentaire;
  @FXML
  private Label lbl_statut;
  @FXML
  private Label lbl_categorie;
  @FXML
  private Label lbl_matiere;
  @FXML
  private Button btn_rangement;
  @FXML
  private Label lbl_rangement;
  @FXML
  private Button btn_faire_reservation;
  @FXML
  private TableView<?> tbl_statOuvrage;
  @FXML
  private Button btn_reservation;
  @FXML
  private TableView<Exemplaire> tbl_reservation;
  @FXML
  private Button btn_aVendre;
  @FXML
  private TableView<Exemplaire> tbl_aVendre;
  @FXML
  private Button btn_vendu;
  @FXML
  private TableView<Exemplaire> tbl_vendu;
  @FXML
  private Button btn_remis;
  @FXML
  private TableView<Exemplaire> tbl_remis;
  @FXML
  private Button btn_modif;
  @FXML
  private Button btn_suppOuv;
  @FXML
  private Button btn_fusion;
  @FXML
  private TableColumn<Exemplaire, String> col_reservation_membre;
  @FXML
  private TableColumn<Exemplaire, String> col_reservation_vendeur;
  @FXML
  private TableColumn<Exemplaire, String> col_reservation_ajout;
  @FXML
  private TableColumn<Exemplaire, String> col_reservation_date;
  @FXML
  private TableColumn<Exemplaire, String> col_reservation_prix;
  @FXML
  private TableColumn<Exemplaire, String> col_aVendre_vendeur;
  @FXML
  private TableColumn<Exemplaire, String> col_aVendre_ajout;
  @FXML
  private TableColumn<Exemplaire, String> col_aVendre_prix;
  @FXML
  private TableColumn<Exemplaire, String> col_aVendre_action;
  @FXML
  private TableColumn<Exemplaire, String> col_vendu_vendeur;
  @FXML
  private TableColumn<Exemplaire, String> col_vendu_ajout;
  @FXML
  private TableColumn<Exemplaire, String> col_vendu_vente;
  @FXML
  private TableColumn<Exemplaire, String> col_vendu_prix;
  @FXML
  private TableColumn<Exemplaire, String> col_remis_vendeur;
  @FXML
  private TableColumn<Exemplaire, String> col_remis_ajout;
  @FXML
  private TableColumn<Exemplaire, String> col_remis_vente;
  @FXML
  private TableColumn<Exemplaire, String> col_remis_date;
  @FXML
  private TableColumn<Exemplaire, String> col_remis_prix;
  @FXML
  private Button monte_statut;
  @FXML
  private Button baisse_statut;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    gArticle = new ItemHandler();
    assertions();
    eventHandlers();
    dataBinding();
  }

   public void loadArticle(Article article) {
    monte_statut.setVisible(article instanceof Ouvrage);
    baisse_statut.setVisible(article instanceof Ouvrage);
    afficheArticle();
  }
  public void loadArticle(int idArticle) {
    article = gArticle.consulteArticle(idArticle);
    monte_statut.setVisible(article instanceof Ouvrage);
    baisse_statut.setVisible(article instanceof Ouvrage);
    afficheArticle();
  }

  public void loadArticle(String ean13) {
    article = gArticle.consulteArticle(Integer.parseInt(ean13));
    monte_statut.setVisible(article instanceof Ouvrage);
    baisse_statut.setVisible(article instanceof Ouvrage);
    afficheArticle();
  }

  public Article getArticle() {
    return article;
  }

  public TableView[] getTableauxExemplaires() {
    TableView[] tableView = {tbl_reservation, tbl_aVendre, tbl_vendu, tbl_remis};
    return tableView;
  }

  public Button getButtonModifier() {
    return btn_modif;
  }

  private void assertions() {
    assert lbl_titre != null : "fx:id=\"lbl_titre\" was not injected: check your FXML file 'itemView.fxml'.";
    assert lbl_description != null : "fx:id=\"lbl_description\" was not injected: check your FXML file 'itemView.fxml'.";
    assert lbl_auteur != null : "fx:id=\"lbl_auteur\" was not injected: check your FXML file 'itemView.fxml'.";
    assert lbl_edition != null : "fx:id=\"lbl_edition\" was not injected: check your FXML file 'itemView.fxml'.";
    assert lbl_editeur != null : "fx:id=\"lbl_editeur\" was not injected: check your FXML file 'itemView.fxml'.";
    assert lbl_annee != null : "fx:id=\"lbl_annee\" was not injected: check your FXML file 'itemView.fxml'.";
    assert lbl_code != null : "fx:id=\"lbl_code\" was not injected: check your FXML file 'itemView.fxml'.";
    assert lbl_commentaire != null : "fx:id=\"lbl_commentaire\" was not injected: check your FXML file 'itemView.fxml'.";
    assert lbl_statut != null : "fx:id=\"lbl_statut\" was not injected: check your FXML file 'itemView.fxml'.";
    assert lbl_categorie != null : "fx:id=\"lbl_categorie\" was not injected: check your FXML file 'itemView.fxml'.";
    assert lbl_matiere != null : "fx:id=\"lbl_matiere\" was not injected: check your FXML file 'itemView.fxml'.";
    assert btn_rangement != null : "fx:id=\"btn_rangement\" was not injected: check your FXML file 'itemView.fxml'.";
    assert lbl_rangement != null : "fx:id=\"lbl_rangement\" was not injected: check your FXML file 'itemView.fxml'.";
    assert btn_commentaire != null : "fx:id=\"btn_commentaire\" was not injected: check your FXML file 'itemView.fxml'.";
    assert btn_faire_reservation != null : "fx:id=\"btn_faire_reservation\" was not injected: check your FXML file 'itemView.fxml'.";
    assert tbl_statOuvrage != null : "fx:id=\"tbl_statOuvrage\" was not injected: check your FXML file 'itemView.fxml'.";
    assert btn_reservation != null : "fx:id=\"btn_reservation\" was not injected: check your FXML file 'itemView.fxml'.";
    assert tbl_reservation != null : "fx:id=\"tbl_reservation\" was not injected: check your FXML file 'itemView.fxml'.";
    assert btn_aVendre != null : "fx:id=\"btn_aVendre\" was not injected: check your FXML file 'itemView.fxml'.";
    assert tbl_aVendre != null : "fx:id=\"tbl_aVendre\" was not injected: check your FXML file 'itemView.fxml'.";
    assert btn_vendu != null : "fx:id=\"btn_vendu\" was not injected: check your FXML file 'itemView.fxml'.";
    assert tbl_vendu != null : "fx:id=\"tbl_vendu\" was not injected: check your FXML file 'itemView.fxml'.";
    assert btn_remis != null : "fx:id=\"btn_remis\" was not injected: check your FXML file 'itemView.fxml'.";
    assert tbl_remis != null : "fx:id=\"tbl_remis\" was not injected: check your FXML file 'itemView.fxml'.";
    assert btn_modif != null : "fx:id=\"btn_modif\" was not injected: check your FXML file 'itemView.fxml'.";
    assert btn_suppOuv != null : "fx:id=\"btn_suppOuv\" was not injected: check your FXML file 'itemView.fxml'.";
    assert btn_fusion != null : "fx:id=\"btn_fusion\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_aVendre_action != null : "fx:id=\"col_aVendre_action\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_aVendre_ajout != null : "fx:id=\"col_aVendre_ajout\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_aVendre_prix != null : "fx:id=\"col_aVendre_prix\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_aVendre_vendeur != null : "fx:id=\"col_aVendre_vendeur\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_remis_ajout != null : "fx:id=\"col_remis_ajout\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_remis_date != null : "fx:id=\"col_remis_date\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_remis_prix != null : "fx:id=\"col_remis_prix\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_remis_vendeur != null : "fx:id=\"col_remis_vendeur\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_remis_vente != null : "fx:id=\"col_remis_vente\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_reservation_ajout != null : "fx:id=\"col_reservation_ajout\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_reservation_date != null : "fx:id=\"col_reservation_date\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_reservation_membre != null : "fx:id=\"col_reservation_membre\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_reservation_prix != null : "fx:id=\"col_reservation_prix\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_reservation_vendeur != null : "fx:id=\"col_reservation_vendeur\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_vendu_ajout != null : "fx:id=\"col_vendu_ajout\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_vendu_prix != null : "fx:id=\"col_vendu_prix\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_vendu_vendeur != null : "fx:id=\"col_vendu_vendeur\" was not injected: check your FXML file 'itemView.fxml'.";
    assert col_vendu_vente != null : "fx:id=\"col_vendu_vente\" was not injected: check your FXML file 'itemView.fxml'.";
    assert monte_statut != null : "fx:id=\"monte_statut\" was not injected: check your FXML file 'itemView.fxml'.";
    assert baisse_statut != null : "fx:id=\"baisse_statut\" was not injected: check your FXML file 'itemView.fxml'.";
  }

  private void eventHandlers() {
    btn_commentaire.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String commentaireVieux = lbl_commentaire.getText();
        String commentaireNouveau = Dialogue.dialogueSaisie("Modifcation du commentaire", "Veuillez entrer le commentaire que vous souhaitez inscrire :", lbl_commentaire.getText());

        if(!commentaireVieux.equals(commentaireNouveau)) {
          if(gArticle.updateComment(article.getId(), commentaireNouveau)) {
            lbl_commentaire.setText(commentaireNouveau);
          } else {
            Dialogue.dialogueInformation("Une erreur est survenue");
          }
        }
      }
    });

    monte_statut.setOnAction(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent event) {
        if(((Ouvrage)article).getStatut().equals("Retiré")) {
          if(gArticle.supprimeDateRetire(article.getId())) {
            ((Ouvrage)article).setDateRetire("");
          }
        } else if(((Ouvrage)article).getStatut().equals("Désuet")) {
          if(gArticle.supprimeDateDesuet(article.getId())) {
            ((Ouvrage)article).setDateDesuet("");
          }
        }
        lbl_statut.setText(((Ouvrage)article).getStatut());
      }
    });

    baisse_statut.setOnAction(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent event) {
        if(((Ouvrage)article).getStatut().equals("Valide")) {
          if(gArticle.ajoutDateDesuet(article.getId())) {
            ((Ouvrage)article).setOutdated(new Date());
          }
        } else if(((Ouvrage)article).getStatut().equals("Désuet")) {
          if(gArticle.ajoutDateRetire(article.getId())) {
            ((Ouvrage)article).setRemoved(new Date());
          }
        }
        lbl_statut.setText(((Ouvrage)article).getStatut());
      }
    });

    btn_rangement.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String storage = Dialogue.dialogueSaisie("Caisses de rangement", "Veuillez noter les caisses de rangement séparer par un \";\" :", lbl_rangement.getText());
        String[] storageUnits = storage.replace(" ", "").split(";");

        if(gArticle.updateStorage(article.getId(), storageUnits)) {
          ArrayList<UniteRangement> rangements = new ArrayList<>();

          for(int i = 0; i < storageUnits.length; i++) {
            UniteRangement uniteRangement = new UniteRangement();
            uniteRangement.setCode(storageUnits[i]);
          }

          article.setUniteRangement(rangements);
        } else {
          Dialogue.dialogueInformation("Une erreur est survenue");
        }
      }
    });

    btn_faire_reservation.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String saisie = "";
        int noMembre = 0;
        boolean isMembre = false;

        while(!isMembre) {
          try {
            saisie = Dialogue.dialogueSaisie("Réserver cet article", "Entrez le numéro de l'étudiant qui fait la réservation");
            noMembre = Integer.parseInt(saisie);

            MemberHandler gm = new MemberHandler();
            //isMembre = gm.exist(noMembre);
          } catch(NumberFormatException e) {
            if(saisie.equals(""))
              return;
          }
        }

        if(gArticle.ajoutDemandeReservation(noMembre, article.getId())) {
          Exemplaire e = new Exemplaire();
          Transaction t = new Transaction();

          t.setType(5);
          t.setDate(new Date());
          e.ajouterTransaction(t);

          article.ajoutReservation(e);
          afficheExemplaires();
        }
      }
    });

    btn_reservation.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        tbl_reservation.setVisible(!tbl_reservation.isVisible());
      }
    });

    tbl_reservation.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        Node node = ((Node) event.getTarget()).getParent();
        TableRow row;

        if (node instanceof TableRow) {
          row = (TableRow) node;
        } else {
          row = (TableRow) node.getParent();
        }

        final Exemplaire e = (Exemplaire) row.getItem();

        //TODO clique sur les noms et le prix
        if (event.isPrimaryButtonDown()) {

        } else if (event.getButton() == MouseButton.SECONDARY) {
          final ContextMenu contextMenu = new ContextMenu();

          MenuItem vendre = new MenuItem("Vendre");
          MenuItem annuler = new MenuItem("Annuler réservation");

          if(e.getId() == 0)
            contextMenu.getItems().addAll(annuler);
          else
            contextMenu.getItems().addAll(vendre, annuler);

          row.setContextMenu(contextMenu);

          vendre.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              int id = gArticle.addTransaction(e.getMembre().getNo(), e.getId(), 3);

              // TODO: Fix transaction id
              //if (id != 0) {
              Transaction t = new Transaction();
              //t.setId(id);
              t.setType(3);
              t.setDate(new Date());

              article.getReserve().remove(e);
              e.ajouterTransaction(t);
              article.getVendu().add(e);

              afficheExemplaires();
              //}
            }
          });

          annuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              if(e.getId() == 0) {
                gArticle.supprimeDemandeReservation(e.getParent().getNo(), article.getId());
                article.getReserve().remove(e);
              } else {
                gArticle.supprimeReservation(e.getId());
                article.getReserve().remove(e);

                for(int noTransaction = 0; noTransaction < e.getTousTransactions().size(); noTransaction++)
                  if(e.getTousTransactions().get(noTransaction).getType() == 5)
                    e.getTousTransactions().remove(noTransaction);
                article.getEnVente().add(e);
              }

              afficheExemplaires();
            }
          });
        }
      }
    });

    btn_aVendre.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        tbl_aVendre.setVisible(!tbl_aVendre.isVisible());
      }
    });

    tbl_aVendre.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        Node node = ((Node) event.getTarget()).getParent();
        TableRow row;

        if (node instanceof TableRow) {
          row = (TableRow) node;
        } else {
          row = (TableRow) node.getParent();
        }

        final Exemplaire e = (Exemplaire) row.getItem();

        //TODO clique sur $$$ pour vente
        if (event.isPrimaryButtonDown()) {

        } else if (event.getButton() == MouseButton.SECONDARY) {
          final ContextMenu contextMenu = new ContextMenu();

          MenuItem vendre = new MenuItem("Vendre");
          MenuItem vendreParent = new MenuItem("Vendre à 50%");
          MenuItem modifier = new MenuItem("Modifier le prix");
          MenuItem supprimer = new MenuItem("Supprimer");

          contextMenu.getItems().addAll(vendre, vendreParent, modifier, supprimer);
          row.setContextMenu(contextMenu);

          vendre.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              int id = gArticle.addTransaction(e.getMembre().getNo(), e.getId(), 2);

              // TODO: Fix transaction id
              //if (id != 0) {
              Transaction t = new Transaction();
              //t.setId(id);
              t.setType(2);
              t.setDate(new Date());

              article.getEnVente().remove(e);
              e.ajouterTransaction(t);
              article.getVendu().add(e);

              afficheExemplaires();
              //}
            }
          });

          vendreParent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              int id = gArticle.addTransaction(e.getMembre().getNo(), e.getId(), 3);

              // TODO: Fix transaction id
              //if (id != 0) {
              Transaction t = new Transaction();
              //t.setId(id);
              t.setType(3);
              t.setDate(new Date());

              article.getEnVente().remove(e);
              e.ajouterTransaction(t);
              article.getVendu().add(e);

              afficheExemplaires();
              //}
            }
          });

          modifier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              boolean estDouble = false;
              double prix = e.getPrice();

              while (!estDouble) {
                try {
                  prix = Double.parseDouble(Dialogue.dialogueSaisie("Modification du prix", "Entrez le nouveau montant :", Double.toString(e.getPrice())));
                  estDouble = true;
                } catch (NumberFormatException e) {
                  Dialogue.dialogueInformation("Vous devez entrer un montant valide");
                }
              }

              CopyHandler ch = new CopyHandler();
              if(ch.updateCopyPrice(e.getId(), prix)) {
                article.getEnVente().remove(e);
                e.setPrice(prix);
                article.getEnVente().add(e);

                afficheExemplaires();
              } else {
                Dialogue.dialogueInformation("Une erreur est survenue");
              }
            }
          });

          supprimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              CopyHandler ch = new CopyHandler();
              if(ch.deleteCopy(e.getId())) {
                article.getEnVente().remove(e);
                afficheExemplaires();
              } else {
                Dialogue.dialogueInformation("Une erreur est survnue");
              }
            }
          });
        }
      }
    });

    btn_vendu.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        tbl_vendu.setVisible(!tbl_vendu.isVisible());
      }
    });

    tbl_vendu.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        Node node = ((Node) event.getTarget()).getParent();
        TableRow row;

        if (node instanceof TableRow) {
          row = (TableRow) node;
        } else {
          row = (TableRow) node.getParent();
        }

        final Exemplaire e = (Exemplaire) row.getItem();

        if (event.getButton() == MouseButton.SECONDARY) {
          final ContextMenu contextMenu = new ContextMenu();
          MenuItem annuler = new MenuItem("Annuler la vente");

          contextMenu.getItems().addAll(annuler);
          row.setContextMenu(contextMenu);

          annuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              if (gArticle.cancelSell(e.getId())) {
                article.getVendu().remove(e);

                for(int noTransaction = 0; noTransaction < e.getTousTransactions().size(); noTransaction++) {
                  if(e.getTousTransactions().get(noTransaction).getType() == 2 || e.getTousTransactions().get(noTransaction).getType() == 3) {
                    e.getTousTransactions().remove(noTransaction);
                  }
                }

                article.getEnVente().add(e);
                afficheExemplaires();
              }
            }
          });
        }
      }
    });

    btn_remis.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        tbl_remis.setVisible(!tbl_remis.isVisible());
      }
    });
  }

  private void dataBinding() {
    col_reservation_membre.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("reservant"));
    col_reservation_vendeur.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("vendeur"));
    col_reservation_ajout.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateAjout"));
    col_reservation_date.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateReservation"));
    col_reservation_prix.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("strPrix"));

    col_aVendre_vendeur.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("vendeur"));
    col_aVendre_ajout.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateAjout"));
    col_aVendre_prix.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("strPrix"));
    col_aVendre_action.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("etiquetteVente"));

    col_vendu_vendeur.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("vendeur"));
    col_vendu_ajout.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateAjout"));
    col_vendu_vente.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateVente"));
    col_vendu_prix.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("strPrix"));

    col_remis_vendeur.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("vendeur"));
    col_remis_ajout.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateAjout"));
    col_remis_vente.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateVente"));
    col_remis_date.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("dateRemise"));
    col_remis_prix.setCellValueFactory(new PropertyValueFactory<Exemplaire, String>("strPrix"));
  }

  private void afficheArticle() {
    lbl_titre.setText(article.getName());
    lbl_matiere.setText(article.getMatiere().getNom());
    lbl_categorie.setText(article.getMatiere().getCategorie());
    lbl_code.setText(article.getCodeBar());
    lbl_rangement.setText(article.rangementStr());
    lbl_commentaire.setText(article.getCommentaire());

    if(article instanceof Ouvrage) {
      afficheOuvrage();
    } else if(article instanceof Objet) {
      afficheObjet();
    }

    afficheExemplaires();
  }

  private void afficheOuvrage() {
    lbl_annee.setText(Integer.toString(((Ouvrage)article).getPublication()));
    lbl_auteur.setText(((Ouvrage)article).getAuthorString());
    lbl_editeur.setText(((Ouvrage)article).getEditor());
    lbl_edition.setText(Integer.toString(((Ouvrage)article).getEdition()));
    lbl_statut.setText(((Ouvrage)article).getStatut());
  }

  private void afficheObjet() {
    lbl_description.setText(((Objet)article).getDescription());
  }

  private void afficheExemplaires() {
    ObservableList<Exemplaire> reserve = FXCollections.observableArrayList(article.getReserve());
    ObservableList<Exemplaire> aVendre = FXCollections.observableArrayList(article.getEnVente());
    ObservableList<Exemplaire> vendu = FXCollections.observableArrayList(article.getVendu());
    ObservableList<Exemplaire> argentRemis = FXCollections.observableArrayList(article.getArgentRemis());

    tbl_reservation.setItems(reserve);
    tbl_aVendre.setItems(aVendre);
    tbl_vendu.setItems(vendu);
    tbl_remis.setItems(argentRemis);
  }
}
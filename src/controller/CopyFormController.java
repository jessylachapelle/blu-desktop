/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import model.article.Article;
import model.article.Exemplaire;
import handler.CopyHandler;
import model.membre.Membre;

import ressources.Dialogue;

/**
 * Controller de l'interface d'ajout
 * d'exemplaires dans un compte de membre
 * gère aussi la recherche et l'ajout d'articles
 * @author Jessy
 * @since 28/03/2016
 * @verison 1.0
 */
public class CopyFormController extends Controller {
  @FXML private AnchorPane ressources;
  @FXML private AnchorPane setPrix;
  @FXML private Label nomMembre;
  @FXML private Label nomArticle;
  @FXML private Button btn_cancel;
  @FXML private Button btn_add;
  @FXML private TextField txt_prix;
  @FXML private TableView listeExemplaires;
  @FXML private TableColumn<Exemplaire, String> col_article;
  @FXML private TableColumn<Exemplaire, Double> col_prix;

  private Controller controller;
  private Pane panel;

  private CopyHandler ge;
  private Membre membre;
  private ArrayList<Exemplaire> exemplaires;
  private Exemplaire exemplaireCourrant;

  /**
   * Initialisation
   * @param location
   * @param resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ge = new CopyHandler();
    exemplaires = new ArrayList<>();
    dataBinding();

    setPrix.setVisible(false);
    affichePanelRecherche();
    eventHandlers();
  }

  /**
   * Ajout des données au tableau des exemplaires
   */
  private void dataBinding() {
    col_article.setCellValueFactory(new PropertyValueFactory<>("name"));
    col_prix.setCellValueFactory(new PropertyValueFactory<>("price"));
  }

  /**
   * Gestion des évènements
   */
  private void eventHandlers() {
    // Tape sur "Enter" dans le champs de prix
    txt_prix.setOnAction((ActionEvent event) -> { btn_add.fire(); });

    // Click sur le boutton annuler
    btn_cancel.setOnAction((ActionEvent event) -> { toggleView(true, false); });

    // Click sur le bouton ajouter
    btn_add.setOnAction((ActionEvent event) -> {
      try {
        double prix = Double.parseDouble(txt_prix.getText());
        exemplaireCourrant.setPrice(prix);
      } catch (NumberFormatException e) {
        Dialogue.dialogueInformation("Vous devez entrer un montant valide");
        return;
      }

      exemplaireCourrant.setMembre(membre);
      exemplaireCourrant.setId(ge.addCopy(exemplaireCourrant));
      exemplaires.add(exemplaireCourrant);
      exemplaireCourrant = null;

      afficheExemplaires();
      toggleView(true, true);
    });

    // Click droit dans la liste d'exemplaires
    listeExemplaires.setOnMouseClicked((MouseEvent event) -> {
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
        MenuItem supprimer = new MenuItem("Supprimer");
        contextMenu.getItems().addAll(supprimer);
        row.setContextMenu(contextMenu);

        // Clique sur le choix supprimer
        supprimer.setOnAction((ActionEvent event1) -> {
          if (ge.deleteCopy(e)){
            exemplaires.remove(e);
            afficheExemplaires();
          }
        });
      }
    });
  }

  private void rechercheEventHandlers() {
    // Double click sur un item de la liste d'articles
    ((SearchController) controller).getItemResults().setOnMousePressed((MouseEvent event) -> {
      if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
        Node node = ((Node) event.getTarget()).getParent();
        TableRow row;

        if (node instanceof TableRow) {
          row = (TableRow) node;
        } else {
          row = (TableRow) node.getParent();
        }

        Article a = (Article) row.getItem();
        exemplaireCourrant = new Exemplaire();
        exemplaireCourrant.setArticle(a);
        nomArticle.setText(a.getName());

        toggleView(true, false);
      }
    });

    // Ouvrir l'interface pour ajouter un nouvel article
    ((SearchController) controller).getBtnAdd().setOnAction((ActionEvent event) -> {
      affichePanelAjoutArticle();
    });
  }

  private void ajoutArticleEventHandlers() {
    ((ItemFormController) controller).getBtnAjoutOuvrage().setOnAction((ActionEvent event) -> {
      Article a = ((ItemFormController) controller).ajoutArticle();
      exemplaireCourrant = new Exemplaire();
      exemplaireCourrant.setArticle(a);
      nomArticle.setText(a.getName());

      affichePanelRecherche();
      toggleView(true, true);
    });

    ((ItemFormController) controller).getBtnAjoutObjet().setOnAction((ActionEvent event) -> {
      Article a = ((ItemFormController) controller).ajoutArticle();
      exemplaireCourrant = new Exemplaire();
      exemplaireCourrant.setArticle(a);
      nomArticle.setText(a.getName());

      affichePanelRecherche();
      toggleView(true, true);
    });
  }

  /**
   * Ajouter les informations du membre
   * auquel ont veux ajouter des exemplaires
   * @param m Le membre actif
   */
  public void loadMembre(Membre m) {
    membre = m;
    nomMembre.setText(membre.getFirstName() + " " + membre.getLastName());
  }

  /**
   * Rendre le Label du nom du membre publique
   * @return Le label du nom du membre
   */
  public Label getNomMembre() {
    return nomMembre;
  }

  /**
   * Rendre le membre actif publique
   * @return Le membre actif
   */
  public Membre getMembre() {
    return membre;
  }

  /**
   * Affiche le panneau de recherche
   * @return Controller de recherche
   */
  private SearchController affichePanelRecherche() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/search.fxml"));
      panel = (Pane) loader.load();
      controller = (SearchController) loader.getController();
      ressources.getChildren().clear();
      ressources.getChildren().add(panel);

      rechercheEventHandlers();

      ((SearchController) controller).setSearchItemOnly();
      return (SearchController) controller;
    } catch (IOException e) {
      System.out.println(e);
      return null;
    }
  }

  /**
   * Recherche le panneau d'ajout d'article
   * @return Controller d'ajout d'articles
   */
  private ItemFormController affichePanelAjoutArticle() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/itemForm.fxml"));
      panel = (Pane) loader.load();
      controller = (ItemFormController) loader.getController();
      ressources.getChildren().clear();
      ressources.getChildren().add(panel);

      ajoutArticleEventHandlers();
      return (ItemFormController) controller;
    } catch (IOException e) {
      System.out.println(e);
      return null;
    }
  }

  /**
   * Refraichir l'affichage du tableau d'exemplaires ajoutés
   */
  private void afficheExemplaires() {
    ObservableList<Exemplaire> ol_exemplaires = FXCollections.observableArrayList(exemplaires);
    listeExemplaires.setItems(ol_exemplaires);

    listeExemplaires.setPrefHeight(50 * (exemplaires.size() + 1));
    ressources.setLayoutY(150 + 50 * exemplaires.size());
    setPrix.setLayoutY(150 + 50 * exemplaires.size());
  }

  /**
   * Toggle entre la view de recherche et d'ajout de prix
   * @param resetPrix S'il faut effacer le champs de prix
   * @param resetRecherche S'il faut effacer les données de recherche
   */
  private void toggleView(boolean resetPrix, boolean resetRecherche) {
    if (resetPrix) {
     txt_prix.setText("");
    }

    if (resetRecherche && controller instanceof SearchController) {
      ((SearchController) controller).resetSearch(true);
    }

    ressources.setVisible(!ressources.isVisible());
    setPrix.setVisible(!setPrix.isVisible());
  }
}

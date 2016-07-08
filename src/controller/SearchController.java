package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import model.article.Article;
import model.article.Ouvrage;
import handler.SearchHandler;
import model.membre.Membre;

/**
 *
 * @author Jessy Lachapelle
 * @since 19/11/2015
 * @version 0.1
 */
public class SearchController extends Controller {

  private SearchHandler searchHandler;

  @FXML
  private Label titre_recherche;
  @FXML
  private ToggleGroup type_recherche;
  @FXML
  private TextField txtf_recherche;
  @FXML
  private Button btn_recherche;
  @FXML
  private RadioButton rb_membre;
  @FXML
  private RadioButton rb_article;
  @FXML
  private CheckBox cb_desactive;
  @FXML
  private Button btn_add;
  @FXML
  private Label lb_message;
  @FXML
  private TableView resultat_membre;
  @FXML
  private TableColumn<Membre, Integer> col_no;
  @FXML
  private TableColumn<Membre, String> col_prenom;
  @FXML
  private TableColumn<Membre, String> col_nom;
  @FXML
  private TableView resultat_article;
  @FXML
  private TableColumn<Article, String> col_titre;
  @FXML
  private TableColumn<Ouvrage, Integer> col_edition;
  @FXML
  private TableColumn<Ouvrage, String> col_editeur;
  @FXML
  private TableColumn<Ouvrage, Integer> col_annee;
  @FXML
  private TableColumn<Ouvrage, String> col_auteur;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    searchHandler = new SearchHandler();
    dataBinding();
    eventHandlers();

    rb_membre.setSelected(true);
    resetSearch(false);
  }

  private void dataBinding() {
    col_no.setCellValueFactory(new PropertyValueFactory<Membre, Integer>("no"));
    col_prenom.setCellValueFactory(new PropertyValueFactory<Membre, String>("firstName"));
    col_nom.setCellValueFactory(new PropertyValueFactory<Membre, String>("lastName"));

    col_titre.setCellValueFactory(new PropertyValueFactory<Article, String>("name"));
    col_edition.setCellValueFactory(new PropertyValueFactory<Ouvrage, Integer>("edition"));
    col_editeur.setCellValueFactory(new PropertyValueFactory<Ouvrage, String>("editor"));
    col_annee.setCellValueFactory(new PropertyValueFactory<Ouvrage, Integer>("publication"));
    col_auteur.setCellValueFactory(new PropertyValueFactory<Ouvrage, String>("authorString"));
  }

  private void eventHandlers() {
    txtf_recherche.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        recherche();
      }
    });

    btn_recherche.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        recherche();
      }
    });

    type_recherche.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      @Override
      public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
        if (type_recherche.getSelectedToggle() != null) {
          String data = type_recherche.getSelectedToggle().getUserData().toString();

          if (data.matches("membre")) {
            searchHandler.setMemberSearch();
            cb_desactive.setDisable(false);
            resetSearch(true);
          } else {
            searchHandler.setItemSearch();
            cb_desactive.setSelected(false);
            cb_desactive.setDisable(true);
            resetSearch(true);
          }
        }
      }
    });

    cb_desactive.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (cb_desactive.isSelected()) {
          searchHandler.setSearchArchives();
        } else {
          searchHandler.setSearchActive();
        }
        recherche();
      }
    });
  }

  private void recherche() {
    searchHandler.setSearchQuery(txtf_recherche.getText());

    if (type_recherche.getSelectedToggle().getUserData().toString().matches("membre"))
      rechercheMembre();
    else
      rechercheArticle();
  }

  private void rechercheMembre() {
    ObservableList<Membre> membres = FXCollections.observableArrayList(searchHandler.searchMembers());

    if (!membres.isEmpty()) {
      resultat_membre.setItems(membres);
      lb_message.setVisible(false);
      resultat_membre.setVisible(true);
    } else {
      lb_message.setVisible(true);
      resultat_membre.setVisible(false);
    }
  }

  private void rechercheArticle() {
    ObservableList<Article> articles = FXCollections.observableArrayList(searchHandler.searchItems());

    if (!articles.isEmpty()) {
      resultat_article.setItems(articles);
      lb_message.setVisible(false);
      resultat_article.setVisible(true);
    } else {
      lb_message.setVisible(true);
      resultat_article.setVisible(false);
    }
  }

  public TableView getMemberResults() {
    return resultat_membre;
  }

  public TableView getItemResults() {
    return resultat_article;
  }

  public void resetSearch(boolean effaceRecherche) {
    lb_message.setVisible(true);
    resultat_membre.setVisible(false);
    resultat_article.setVisible(false);

    if (effaceRecherche) {
      txtf_recherche.setText("");
    }
  }

  public void setSearchBoth() {
    rb_membre.setSelected(true);
    titre_recherche.setText("Recherche dans le système");
    toggleFilters(true);
  }

  public void setSearchItemOnly() {
    rb_article.setSelected(true);
    titre_recherche.setText("Recherche d'articles");
    toggleFilters(false);
  }

  public void setSearchMemberOnly() {
    rb_membre.setSelected(true);
    titre_recherche.setText("Recherche de membres");
    toggleFilters(false);
  }

  public Button getBtnAdd() {
    return btn_add;
  }

  public boolean isMemberSearch() {
    return rb_membre.isSelected();
  }

  public boolean isItemSearch() {
    return rb_article.isSelected();
  }

  private void toggleFilters(boolean visible) {
    rb_article.setVisible(visible);
    rb_membre.setVisible(visible);
    cb_desactive.setVisible(visible);
  }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import model.article.Article;
import model.article.Exemplaire;
import handler.ItemHandler;
import handler.MemberHandler;
import model.membre.Membre;
import ressources.Dialogue;

/**
 * Cette classe controller prend en charge le panneau de gauche.
 * C'est avec celle-ci que l'ont charge chacun des panneau de droite
 *
 * @author Marc
 */
public class WindowController extends Controller {

  @FXML
  private AnchorPane centreDroit;
  @FXML
  private Button btn_recherche;
  @FXML
  private Button btn_articles;
  @FXML
  private Button btn_membres;
  @FXML
  private Button btn_admin;
  @FXML
  private BorderPane rootLayout;

  private Pane panel;
  private Controller controller;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    assertInjection();
    eventHandlers();
    affichePanelRecherche();
  }

  /**
   * Affiche le panel dans la fenetre de droite
   */
  private SearchController affichePanelRecherche() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/search.fxml"));
      panel = (Pane) loader.load();
      controller = (SearchController) loader.getController();
      centreDroit.getChildren().clear();
      centreDroit.getChildren().add(panel);
      setEventHandlersRecherche();

      return (SearchController) controller;
    } catch (IOException e) {
      System.out.println(e);
      return null;
    }
  }

  private CopyFormController affichePanelAjoutExemplaire() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/copyForm.fxml"));
      panel = (Pane) loader.load();
      controller = (CopyFormController) loader.getController();
      centreDroit.getChildren().clear();
      centreDroit.getChildren().add(panel);

      ((CopyFormController) controller).getNomMembre().setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          Membre m = ((CopyFormController) controller).getMembre();
          affichePanelFicheMembre().loadMember(m);
        }
      });


      return (CopyFormController) controller;
    } catch (IOException e) {
      System.out.println(e);
      return null;
    }
  }

  /**
   * Affiche le panel dans la fenetre de droite
   */
  private MemberViewController affichePanelFicheMembre() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/memberView.fxml"));
      panel = (Pane) loader.load();
      controller = (MemberViewController) loader.getController();
      centreDroit.getChildren().clear();
      centreDroit.getChildren().add(panel);

      ((MemberViewController) controller).getEditButton().setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          Membre m = ((MemberViewController) controller).getMember();
          displayMemberView().loadMember(m);
        }
      });

      ((MemberViewController) controller).getAddCopyButton().setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          Membre m = ((MemberViewController) controller).getMember();
          affichePanelAjoutExemplaire().loadMembre(m);
        }
      });

      for (int noTbl = 0; noTbl < ((MemberViewController) controller).getCopyTables().length; noTbl++) {
        ((MemberViewController) controller).getCopyTables()[noTbl].setOnMousePressed(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
              Node node = ((Node) event.getTarget()).getParent();
              TableRow row;

              if (node instanceof TableRow) {
                row = (TableRow) node;
              } else {
                row = (TableRow) node.getParent();
              }

              Exemplaire e = (Exemplaire) row.getItem();
              affichePanelFicheArticle().loadArticle(e.getArticle().getId());
            }
          }
        });
      }

      return ((MemberViewController) controller);
    } catch (IOException e) {
      System.out.println(e);
      return null;
    }
  }

  /**
   * Affiche le panel dans la fenetre de droite selon un numMembre
   *
   * @param noMembre le numéro du membre a afficher;
   */
  private void affichePanelFicheMembre(int noMembre) {
    affichePanelFicheMembre().loadMember(noMembre);
  }

  private MemberFormController displayMemberView() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/memberForm.fxml"));
      panel = (Pane) loader.load();
      controller = (MemberFormController) loader.getController();
      centreDroit.getChildren().clear();
      centreDroit.getChildren().add(panel);
      setEventHandlersAjoutMembre();

      return (MemberFormController) controller;
    } catch (IOException e) {
      System.out.println(e);
      return null;
    }
  }

  private ItemFormController affichePanelAjoutArticle() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/itemForm.fxml"));
      panel = (Pane) loader.load();
      controller = (ItemFormController) loader.getController();
      centreDroit.getChildren().clear();
      centreDroit.getChildren().add(panel);
      setEventHandlersAjoutArticle();

      return (ItemFormController) controller;
    } catch (IOException e) {
      System.out.println(e);
      return null;
    }
  }

  private ItemViewController affichePanelFicheArticle() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/itemView.fxml"));
      panel = (Pane) loader.load();
      controller = (ItemViewController)loader.getController();
      centreDroit.getChildren().clear();
      centreDroit.getChildren().add(panel);

      ((ItemViewController) controller).getButtonModifier().setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          Article a = ((ItemViewController) controller).getArticle();
          affichePanelAjoutArticle().loadArticle(a);
        }
      });

      for(int noTbl = 0; noTbl < ((ItemViewController) controller).getTableauxExemplaires().length; noTbl++) {
        ((ItemViewController) controller).getTableauxExemplaires()[noTbl].setOnMousePressed(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
              Node node = ((Node) event.getTarget()).getParent();
              TableRow row;

              if (node instanceof TableRow)
                row = (TableRow) node;
              else
                row = (TableRow) node.getParent();

              Exemplaire e = (Exemplaire) row.getItem();
              affichePanelFicheMembre().loadMember(e.getMembre().getNo());
            }
          }
        });
      }

      return (ItemViewController) controller;
    } catch (IOException e) {
      System.out.println(e);
      return null;
    }
  }

  private AdminController affichePanelAdmin() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/admin.fxml"));
      panel = (Pane) loader.load();
      controller = (AdminController) loader.getController();
      centreDroit.getChildren().clear();
      centreDroit.getChildren().add(panel);

      return (AdminController) controller;
    } catch (IOException e) {
      System.out.println(e);
      return null;
    }
  }

  /**
   * Affiche le panel dans la fenetre de droite
   *
   * @param panelPath Le chemin de la vue
   */
  private void affichePanel(String panelPath) {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource(panelPath));
      panel = (Pane) loader.load();
      centreDroit.getChildren().clear();
      centreDroit.getChildren().add(panel);
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  /**
   * S'assure que tout nos élément utilisé dans le codes ont été injecté
   */
  private void assertInjection() {
    assert centreDroit != null : "fx:id=\"centreDroit\" was not injected: check your FXML file 'simple.fxml'.";
    assert btn_recherche != null : "fx:id=\"btn_articles\" was not injected: check your FXML file 'simple.fxml'.";
    assert btn_articles != null : "fx:id=\"btn_articles\" was not injected: check your FXML file 'simple.fxml'.";
    assert btn_membres != null : "fx:id=\"btn_membres\" was not injected: check your FXML file 'simple.fxml'.";
    assert btn_admin != null : "fx:id=\"btn_admin\" was not injected: check your FXML file 'simple.fxml'.";
  }

  private void setEventHandlersRecherche() {
    ((SearchController) controller).getMemberResults().setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
          Node node = ((Node) event.getTarget()).getParent();
          TableRow row;

          if (node instanceof TableRow)
            row = (TableRow) node;
          else
            row = (TableRow) node.getParent();

          Membre m = (Membre) row.getItem();
          affichePanelFicheMembre().loadMember(m.getNo());
        }
      }
    });

    ((SearchController) controller).getItemResults().setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
          Node node = ((Node) event.getTarget()).getParent();
          TableRow row;

          if (node instanceof TableRow) {
            row = (TableRow) node;
          } else {
            row = (TableRow) node.getParent();
          }

          Article a = (Article) row.getItem();
          affichePanelFicheArticle().loadArticle(a.getId());
        }
      }
    });
  }

  /**
   * Appelle la fenêtre fiche membre seulement si MemberFormController a
   * finit ses tâches
   */
  private void setEventHandlersAjoutMembre() {
    ((MemberFormController) controller).getSuccess().addListener(new ChangeListener<Boolean>() {

      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue == true) {
          int m = ((MemberFormController) controller).getNoValue();
          affichePanelFicheMembre().loadMember(m);
        }
      }
    });

    ((MemberFormController) controller).getCancelButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Membre m = ((MemberFormController) controller).getMember();
        affichePanelFicheMembre().loadMember(m);
      }

    });

    ((MemberFormController) controller).getAddButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Membre membre = ((MemberFormController) controller).saveMember();
        affichePanelFicheMembre().loadMember(membre);
      }
    });

  }

  private void setEventHandlersAjoutArticle(){
     ((ItemFormController) controller).getSuccess().addListener(new ChangeListener<Boolean>() {

       @Override
       public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
         Article a = ((ItemFormController) controller).getArticle();
         affichePanelFicheArticle().loadArticle(a);
       }
      });
  }

  private void eventHandlers() {
    setScanner();

    btn_recherche.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        affichePanelRecherche();
      }
    });

    btn_articles.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        affichePanelAjoutArticle();
      }
    });

    btn_membres.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        displayMemberView();
      }
    });

    btn_admin.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        affichePanelAdmin();
      }
    });
  }

  /**
   * Rajoute le listener global pour le scanner
   */
  private void setScanner() {
    ListView<String> console = new ListView<String>(FXCollections.<String>observableArrayList());
    // on s'assure ici de clearer le buffer dans le cas de trop de changement
    console.getItems().addListener((ListChangeListener.Change<? extends String> change) -> {
      while (change.next()) {
        if (change.getList().size() > 20) {
          change.getList().remove(0);
        }
      }
    });

    rootLayout.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
      @Override
      public void handle(javafx.scene.input.KeyEvent ke) {
        boolean article = true;
        String code = "";
        console.getItems().add(ke.getText());

        // Si le premier caractère n'est pas < ce n'est pas une saisie de code barre
        if (!console.getItems().get(0).equals("à")) {
          console.getItems().clear();
          return;
        } else if (ke.getText().equals("À") && console.getItems().size() == 13) {   // Numéro étudiant
          article = false;
          code = console.getItems().toString().replaceAll("[\\D]", "");
          code = "2" + code.substring(1, 9);
          console.getItems().clear();

          MemberHandler gm = new MemberHandler();
          int noMembre = Integer.parseInt(code);

          if(gm.exist(noMembre)) {         // Membre existe
            affichePanelFicheMembre().loadMember(noMembre);
          } else {                                // Nouveau membre
            displayMemberView().loadMember(noMembre);
          }

          return;
        } else if (ke.getText().equals("À") && console.getItems().size() == 16) {   // Code EAN13
          code = console.getItems().toString().replaceAll("[\\D]", "");
          console.getItems().clear();
        } else if(ke.getText().equals("À")) {                                       // Code non supporté
          console.getItems().clear();
          Dialogue.dialogueInformation("Erreur de code", "Le code saisie n'est pas pris en charge");
          return;
        }

        // TODO complété puis décommenter les fonctions en commentaire
        if(controller instanceof MemberFormController || controller instanceof ItemFormController) {

        } else if(controller instanceof CopyFormController) {     // Si le panel d'ajout d'exemplaire est ouvert
          ItemHandler ga = new ItemHandler();

          if(article) {   // ga.articleExiste(code) C'est un article existant
            // TODO Créer un exemplaire de l'article et permettre la saisie du prix
          } else if(article) {                      // C'est un nouvel article
            // TODO ouvrir un formulaire d'ajout d'article puis retour à l'ajout d'exemplaire
          } else {                                  // Ce n'est pas un article
            Dialogue.dialogueInformation("Erreur de code", "Le code saisie n'est pas pris en charge");
          }
        } else {
          if(article) {                             // C'est un article
            ItemHandler ga = new ItemHandler();

            if(true) { //ga.articleExiste(code)            // L'article existe
              affichePanelFicheArticle().loadArticle(code);
            } else {                                // Nouvel article
              affichePanelAjoutArticle().loadArticle(code);
            }
          } else {                                  // Membre
            MemberHandler gm = new MemberHandler();
            int noMembre = Integer.parseInt(code);

            if(gm.exist(noMembre)) {         // Membre existe
              affichePanelFicheMembre().loadMember(noMembre);
            } else {                                // Nouveau membre
              displayMemberView().loadMember(noMembre);
            }
          }
        }
      }
    });
  }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableRow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import model.item.Copy;
import model.item.Item;
import handler.ItemHandler;
import handler.MemberHandler;
import model.member.Member;
import ressources.Dialog;

/**
 * Cette classe controller prend en charge le panneau de gauche.
 * C'est avec celle-ci que l'ont charge chacun des panneau de droite
 *
 * @author Marc
 */
@SuppressWarnings({"ConstantConditions", "unused"})
public class WindowController extends Controller {
  private Pane panel;
  private Controller controller;

  @FXML private BorderPane rootLayout;
  @FXML private AnchorPane mainPanel;

  @FXML private Button btnSearch;
  @FXML private Button btnItemForm;
  @FXML private Button btnMemberForm;
  @FXML private Button btnAdmin;


  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    _setText();
    _eventHandlers();
    _displaySearchPanel();
  }

  /**
   * Affiche le panel dans la fenetre de droite
   */
  private SearchController _displaySearchPanel() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/search.fxml"));
      panel = loader.load();
      controller = (SearchController) loader.getController();
      mainPanel.getChildren().clear();
      mainPanel.getChildren().add(panel);
      _setSearchEventHandlers();

      return (SearchController) controller;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private CopyFormController displayCopyFormPanel() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/copyForm.fxml"));
      panel = loader.load();
      controller = (CopyFormController) loader.getController();
      mainPanel.getChildren().clear();
      mainPanel.getChildren().add(panel);

      ((CopyFormController) controller).getMemberName().setOnMouseClicked(event -> displayMemberViewPanel().loadMember(((CopyFormController) controller).getMember()));


      return (CopyFormController) controller;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Affiche le panel dans la fenetre de droite
   */
  private MemberViewController displayMemberViewPanel() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/memberView.fxml"));
      panel = loader.load();
      controller = (MemberViewController) loader.getController();
      mainPanel.getChildren().clear();
      mainPanel.getChildren().add(panel);

      ((MemberViewController) controller).getEditButton().setOnAction(event -> displayMemberFormPanel().loadMember(((MemberViewController) controller).getMember()));

      ((MemberViewController) controller).getAddCopyButton().setOnAction(event -> displayCopyFormPanel().loadMembre(((MemberViewController) controller).getMember()));

      for (int noTbl = 0; noTbl < ((MemberViewController) controller).getCopyTables().length; noTbl++) {
        ((MemberViewController) controller).getCopyTables()[noTbl].setOnMousePressed(event -> {
          if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            Node node = ((Node) event.getTarget()).getParent();
            TableRow row;

            if (node instanceof TableRow) {
              row = (TableRow) node;
            } else {
              row = (TableRow) node.getParent();
            }

            Copy c = (Copy) row.getItem();
            displayItemViewPanel().loadItem(c.getItem().getId());
          }
        });
      }

      return ((MemberViewController) controller);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Affiche le panel dans la fenetre de droite selon un numMembre
   *
   * @param memberNo le numéro du member a afficher;
   */
  private void displayMemberViewPanel(int memberNo) {
    displayMemberViewPanel().loadMember(memberNo);
  }

  private MemberFormController displayMemberFormPanel() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/memberForm.fxml"));
      panel = loader.load();
      controller = (MemberFormController) loader.getController();
      mainPanel.getChildren().clear();
      mainPanel.getChildren().add(panel);
      setEventHandlersMemberForm();

      return (MemberFormController) controller;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private ItemFormController displayItemFormPanel() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/itemForm.fxml"));
      panel = loader.load();
      controller = (ItemFormController) loader.getController();
      mainPanel.getChildren().clear();
      mainPanel.getChildren().add(panel);
      setEventHandlersItemForm();

      return (ItemFormController) controller;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private ItemViewController displayItemViewPanel() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/itemView.fxml"));
      panel = loader.load();
      controller = (ItemViewController)loader.getController();
      mainPanel.getChildren().clear();
      mainPanel.getChildren().add(panel);

      ((ItemViewController) controller).getBtnUpdate().setOnAction(event -> displayItemFormPanel().loadItem(((ItemViewController) controller).getItem()));

      for (int noTbl = 0; noTbl < ((ItemViewController) controller).getCopyTables().length; noTbl++) {
        ((ItemViewController) controller).getCopyTables()[noTbl].setOnMousePressed(event -> {
          if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            Node node = ((Node) event.getTarget()).getParent();
            TableRow row;

            if (node instanceof TableRow)
              row = (TableRow) node;
            else
              row = (TableRow) node.getParent();

            Copy c = (Copy) row.getItem();
            displayMemberViewPanel().loadMember(c.getMember().getNo());
          }
        });
      }

      return (ItemViewController) controller;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private AdminController displayAdminPanel() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(WindowController.class.getClassLoader().getResource("view/layout/admin.fxml"));
      panel = loader.load();
      controller = (AdminController) loader.getController();
      mainPanel.getChildren().clear();
      mainPanel.getChildren().add(panel);

      return (AdminController) controller;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private void _setSearchEventHandlers() {
    ((SearchController) controller).getTblMemberResults().setOnMousePressed(event -> {
      if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
        Node node = ((Node) event.getTarget()).getParent();
        TableRow row;

        if (node instanceof TableRow) {
          row = (TableRow) node;
        } else {
          row = (TableRow) node.getParent();
        }

        Member m = (Member) row.getItem();
        displayMemberViewPanel().loadMember(m.getNo());
      }
    });

    ((SearchController) controller).getTblItemResults().setOnMousePressed(event -> {
      if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
        Node node = ((Node) event.getTarget()).getParent();
        TableRow row;

        if (node instanceof TableRow) {
          row = (TableRow) node;
        } else {
          row = (TableRow) node.getParent();
        }

        Item i = (Item) row.getItem();
        displayItemViewPanel().loadItem(i.getId());
      }
    });
  }

  /**
   * Appelle la fenêtre fiche member seulement si MemberFormController a
   * finit ses tâches
   */
  private void setEventHandlersMemberForm() {
    ((MemberFormController) controller).getSuccess().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        displayMemberViewPanel().loadMember(((MemberFormController) controller).getNo());
      }
    });

    ((MemberFormController) controller).getCancelButton().setOnAction(event -> displayMemberViewPanel().loadMember(((MemberFormController) controller).getMember()));

    ((MemberFormController) controller).getAddButton().setOnAction(event -> displayMemberViewPanel().loadMember(((MemberFormController) controller).saveMember()));

  }

  private void setEventHandlersItemForm(){
     ((ItemFormController) controller).getSuccess().addListener((observable, oldValue, newValue) -> {
       displayItemViewPanel().loadItem(((ItemFormController) controller).getArticle());
     });
  }

  private void _eventHandlers() {
    setScanner();

    btnSearch.setOnAction(event -> _displaySearchPanel());

    btnItemForm.setOnAction(event -> displayItemFormPanel());

    btnMemberForm.setOnAction(event -> displayMemberFormPanel());

    btnAdmin.setOnAction(event -> displayAdminPanel());
  }

  /**
   * Rajoute le listener global pour le scanner
   */
  @SuppressWarnings({"ConstantIfStatement", "StatementWithEmptyBody", "unused"})
  private void setScanner() {
    ListView<String> console = new ListView<>(FXCollections.<String>observableArrayList());
    // on s'assure ici de clearer le buffer dans le cas de trop de changement
    console.getItems().addListener((ListChangeListener.Change<? extends String> change) -> {
      while (change.next()) {
        if (change.getList().size() > 20) {
          change.getList().remove(0);
        }
      }
    });

    rootLayout.setOnKeyPressed(ke -> {
      boolean isItem = true;
      String code = "";
      console.getItems().add(ke.getText());

      // Si le premier caractère n'est pas < ce n'est pas une saisie de code barre
      if (!console.getItems().get(0).equals("à")) {
        console.getItems().clear();
        return;
      } else if (ke.getText().equals("À") && console.getItems().size() == 13) {   // Numéro étudiant
        isItem = false;
        code = console.getItems().toString().replaceAll("[\\D]", "");
        code = "2" + code.substring(1, 9);
        console.getItems().clear();

        MemberHandler gm = new MemberHandler();
        int noMembre = Integer.parseInt(code);

        if(gm.exist(noMembre)) {         // Member existe
          displayMemberViewPanel().loadMember(noMembre);
        } else {                                // Nouveau member
          displayMemberFormPanel().loadMember(noMembre);
        }

        return;
      } else if (ke.getText().equals("À") && console.getItems().size() == 16) {   // Code EAN13
        code = console.getItems().toString().replaceAll("[\\D]", "");
        console.getItems().clear();
      } else if(ke.getText().equals("À")) {                                       // Code non supporté
        console.getItems().clear();
        Dialog.information("Erreur de code", "Le code saisie n'est pas pris en charge");
        return;
      }

      // TODO complété puis décommenter les fonctions en commentaire
      if (controller instanceof MemberFormController || controller instanceof ItemFormController) {

      } else if(controller instanceof CopyFormController) {     // Si le panel d'ajout d'exemplaire est ouvert
        ItemHandler ga = new ItemHandler();

        if (isItem) {   // ga.itemExists(code) C'est un item existant
          // TODO Créer un exemplaire de l'item et permettre la saisie du prix
        } else if (isItem) {                      // C'est un nouvel item
          // TODO ouvrir un formulaire d'ajout d'item puis retour à l'ajout d'exemplaire
        } else {                                  // Ce n'est pas un item
          Dialog.information("Erreur de code", "Le code saisie n'est pas pris en charge");
        }
      } else {
        if(isItem) {                             // C'est un item
          ItemHandler ga = new ItemHandler();

          if (true) { //ga.itemExists(code)            // L'item existe
            displayItemViewPanel().loadItem(code);
          } else {                                // Nouvel item
            displayItemFormPanel().loadItem(code);
          }
        } else {                                  // Member
          MemberHandler gm = new MemberHandler();
          int noMembre = Integer.parseInt(code);

          if(gm.exist(noMembre)) {         // Member existe
            displayMemberViewPanel().loadMember(noMembre);
          } else {                                // Nouveau member
            displayMemberFormPanel().loadMember(noMembre);
          }
        }
      }
    });
  }

  private void _setText() {
    super.initI18n();

    btnSearch.setText(i18n.getString("menu.search"));
    btnItemForm.setText(i18n.getString("menu.item"));
    btnMemberForm.setText(i18n.getString("menu.member"));
    btnAdmin.setText(i18n.getString("menu.admin"));
  }
}
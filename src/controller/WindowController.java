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
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
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

  @FXML private VBox sideMenu;
  @FXML private VBox menu;
  @FXML private Pane window;
  @FXML private Pane mainPanel;

  @FXML private Button btnSearch;
  @FXML private Button btnItemForm;
  @FXML private Button btnMemberForm;
  @FXML private Button btnAdmin;


  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

    menu.setPrefWidth(screenWidth);
    sideMenu.setPrefSize(screenWidth * .15, screenHeight);
    mainPanel.setPrefSize(screenWidth * .8, screenHeight);

    _setText();
    _setWindowEventHandlers();
    _displaySearchPanel();
  }

  /**
   * Affiche le panel dans la fenetre de droite
   */
  private SearchController _displaySearchPanel() {
    controller = _loadPanel("view/layout/search.fxml");
    _setSearchEventHandlers();
    return (SearchController) controller;
  }

  private CopyFormController _displayCopyFormPanel() {
    controller = _loadPanel("view/layout/copyForm.fxml");
    _CopyFormEventHandlers();
    return (CopyFormController) controller;
  }

  private void _CopyFormEventHandlers() {
    CopyFormController copyFormController = (CopyFormController) controller;

    copyFormController.getMemberName().setOnMouseClicked(event -> _displayMemberViewPanel().loadMember(copyFormController.getMember()));
  }

  private Controller _loadPanel(String resource) {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(WindowController.class.getClassLoader().getResource(resource));
    mainPanel.getChildren().clear();

    try {
      mainPanel.getChildren().add(loader.load());
      return loader.getController();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
  /**
   * Affiche le panel dans la fenetre de droite
   */
  private MemberViewController _displayMemberViewPanel() {
    controller = _loadPanel("view/layout/memberView.fxml");
    _memberViewEventHandlers();
    return (MemberViewController) controller;
  }

  private void _memberViewEventHandlers() {
    MemberViewController memberViewController = (MemberViewController) controller;

    memberViewController.getEditButton().setOnAction(event -> _displayMemberFormPanel().loadMember(memberViewController.getMember()));

    memberViewController.getAddCopyButton().setOnAction(event -> _displayCopyFormPanel().loadMembre(memberViewController.getMember()));

    for (TableView table : memberViewController.getCopyTables()) {
      table.setOnMousePressed(event -> {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
          TableRow row = _getTableRow(((Node) event.getTarget()).getParent());
          Copy copy = (Copy) row.getItem();

          if (copy != null) {
            _displayItemViewPanel().loadItem(copy.getItem().getId());
          }
        }
      });
    }
  }

  /**
   * Affiche le panel dans la fenetre de droite selon un numMembre
   *
   * @param memberNo le numéro du member a afficher;
   */
  private void _displayMemberViewPanel(int memberNo) {
    _displayMemberViewPanel().loadMember(memberNo);
  }

  private MemberFormController _displayMemberFormPanel() {
    controller = _loadPanel("view/layout/memberForm.fxml");
    _setMemberFormEventHandlers();
    return (MemberFormController) controller;
  }

  private ItemFormController _displayItemFormPanel() {
    controller = _loadPanel("view/layout/itemForm.fxml");
    _setItemFormEventHandlers();
    return (ItemFormController) controller;
  }

  private ItemViewController _displayItemViewPanel() {
    controller = _loadPanel("view/layout/itemView.fxml");
    _setItemViewEventHandlers();
    return (ItemViewController) controller;
  }

  private void _setItemViewEventHandlers() {
    ItemViewController itemViewController = (ItemViewController) controller;

    itemViewController.getBtnUpdate().setOnAction(event -> _displayItemFormPanel().loadItem(itemViewController.getItem()));

    for (TableView table : itemViewController.getCopyTables()) {
      table.setOnMousePressed(event -> {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
          TableRow row = _getTableRow(((Node) event.getTarget()).getParent());
          Copy copy = (Copy) row.getItem();

          if (copy != null) {
            _displayMemberViewPanel().loadMember(copy.getMember().getNo());
          }
        }
      });
    }
  }

  private AdminController _displayAdminPanel() {
    controller = _loadPanel("view/layout/admin.fxml");
    return (AdminController) controller;
  }

  private void _setSearchEventHandlers() {
    SearchController searchController = (SearchController) controller;

    searchController.getTblMemberResults().setOnMousePressed(event -> {
      if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
        TableRow row = _getTableRow(((Node) event.getTarget()).getParent());
        Member member = (Member) row.getItem();

        if (member != null) {
          _displayMemberViewPanel().loadMember(member.getNo());
        }
      }
    });

    searchController.getTblItemResults().setOnMousePressed(event -> {
      if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
        TableRow row = _getTableRow(((Node) event.getTarget()).getParent());
        Item item = (Item) row.getItem();

        if (item != null) {
          _displayItemViewPanel().loadItem(item.getId());
        }
      }
    });
  }

  /**
   * Appelle la fenêtre fiche member seulement si MemberFormController a
   * finit ses tâches
   */
  private void _setMemberFormEventHandlers() {
    MemberFormController memberFormController = (MemberFormController) controller;
    memberFormController.getCancelButton().setOnAction(event -> _displayMemberViewPanel().loadMember(memberFormController.getMember()));

    memberFormController.getSaveButton().setOnAction(e -> {
      if (memberFormController.canSave()) {
        _displayMemberViewPanel().loadMember(memberFormController.saveMember());
      } else {
        Dialog.information("Assurez-vous d'avoir bien rempli tous les champs obligatoires avant d'enregistrer");
      }
    });
  }

  private void _setItemFormEventHandlers() {
    ItemFormController itemFormController = new ItemFormController();

    itemFormController.getBtnAjoutObjet().setOnAction(event -> {

    });

    itemFormController.getBtnAjoutOuvrage().setOnAction(event -> {

    });
  }

  private void _setWindowEventHandlers() {
    _setScanner();

    btnSearch.setOnAction(event -> _displaySearchPanel());
    btnItemForm.setOnAction(event -> _displayItemFormPanel());
    btnMemberForm.setOnAction(event -> _displayMemberFormPanel());
    btnAdmin.setOnAction(event -> _displayAdminPanel());
  }

  /**
   * Rajoute le listener global pour le scanner
   */
  @SuppressWarnings({"ConstantIfStatement", "StatementWithEmptyBody", "unused"})
  private void _setScanner() {
    ListView<String> console = new ListView<>(FXCollections.<String>observableArrayList());
    // on s'assure ici de clearer le buffer dans le cas de trop de changement
    console.getItems().addListener((ListChangeListener.Change<? extends String> change) -> {
      while (change.next()) {
        if (change.getList().size() > 20) {
          change.getList().remove(0);
        }
      }
    });

    window.setOnKeyPressed(ke -> {
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
          _displayMemberViewPanel().loadMember(noMembre);
        } else {                                // Nouveau member
          _displayMemberFormPanel().loadMember(noMembre);
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
            _displayItemViewPanel().loadItem(code);
          } else {                                // Nouvel item
            _displayItemFormPanel().loadItem(code);
          }
        } else {                                  // Member
          MemberHandler gm = new MemberHandler();
          int noMembre = Integer.parseInt(code);

          if(gm.exist(noMembre)) {         // Member existe
            _displayMemberViewPanel().loadMember(noMembre);
          } else {                                // Nouveau member
            _displayMemberFormPanel().loadMember(noMembre);
          }
        }
      }
    });
  }

  private void _setText() {
    initI18n();

    btnSearch.setText(i18n.getString("menu.search"));
    btnItemForm.setText(i18n.getString("menu.item"));
    btnMemberForm.setText(i18n.getString("menu.member"));
    btnAdmin.setText(i18n.getString("menu.admin"));
  }
}
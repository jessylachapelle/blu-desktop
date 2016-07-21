package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import model.item.Item;
import model.item.Book;
import handler.SearchHandler;
import model.member.Member;

/**
 *
 * @author Jessy Lachapelle
 * @since 19/11/2015
 * @version 0.1
 */
@SuppressWarnings("unused")
public class SearchController extends Controller {

  private SearchHandler searchHandler;

  @FXML private Label lblTitle;
  @FXML private ToggleGroup type;
  @FXML private TextField txtSearch;
  @FXML private Button btnSearch;
  @FXML private RadioButton rbMembers;
  @FXML private RadioButton rbItems;
  @FXML private CheckBox cbDeactivated;
  @FXML private Button btnAdd;
  @FXML private Label lblMessage;

  @FXML private TableView<Member> tblMemberResults;
  @FXML private TableColumn<Member, Integer> colNo;
  @FXML private TableColumn<Member, String> colFirstName;
  @FXML private TableColumn<Member, String> colLastName;

  @FXML private TableView<Item> tblItemResults;
  @FXML private TableColumn<Item, String> colTitle;
  @FXML private TableColumn<Book, Integer> colEdition;
  @FXML private TableColumn<Book, String> colEditor;
  @FXML private TableColumn<Book, String> colPublication;
  @FXML private TableColumn<Book, String> colAuthors;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    searchHandler = new SearchHandler();
    dataBinding();
    eventHandlers();

    rbMembers.setSelected(true);
    resetSearch(false);
  }

  private void dataBinding() {
    // Member results
    colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
    colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

    // Item results
    colTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
    colEdition.setCellValueFactory(new PropertyValueFactory<>("edition"));
    colEditor.setCellValueFactory(new PropertyValueFactory<>("editor"));
    colPublication.setCellValueFactory(new PropertyValueFactory<>("publication"));
    colAuthors.setCellValueFactory(new PropertyValueFactory<>("authorString"));
  }

  private void eventHandlers() {
    txtSearch.setOnAction(event -> _search());

    btnSearch.setOnAction(event -> _search());

    type.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
      if (type.getSelectedToggle() != null) {
        String data = type.getSelectedToggle().getUserData().toString();

        if (data.matches("member")) {
          searchHandler.setMemberSearch();
          cbDeactivated.setDisable(false);
          resetSearch(true);
        } else {
          searchHandler.setItemSearch();
          cbDeactivated.setSelected(false);
          cbDeactivated.setDisable(true);
          resetSearch(true);
        }
      }
    });

    cbDeactivated.setOnAction(event -> {
      searchHandler.setSearchArchives(cbDeactivated.isSelected());

      if (!txtSearch.getText().isEmpty()) {
        _search();
      }
    });
  }

  private void _search() {
    searchHandler.setSearchQuery(txtSearch.getText());

    if (searchHandler.isItemSearch()) {
      _searchItems();
    } else {
      _searchMembers();
    }
  }

  private void _searchMembers() {
    ObservableList<Member> members = FXCollections.observableArrayList(searchHandler.searchMembers());

    if (members.isEmpty()) {
      lblMessage.setVisible(true);
      tblMemberResults.setVisible(false);
    } else {
      tblMemberResults.setItems(members);
      lblMessage.setVisible(false);
      tblMemberResults.setVisible(true);
    }
  }

  private void _searchItems() {
    ObservableList<Item> items = FXCollections.observableArrayList(searchHandler.searchItems());

    if (items.isEmpty()) {
      lblMessage.setVisible(true);
      tblItemResults.setVisible(false);
    } else {
      tblItemResults.setItems(items);
      lblMessage.setVisible(false);
      tblItemResults.setVisible(true);
    }
  }

  public TableView<Member> getTblMemberResults() {
    return tblMemberResults;
  }

  public TableView<Item> getTblItemResults() {
    return tblItemResults;
  }

  public void resetSearch(boolean eraseTxtSearch) {
    lblMessage.setVisible(true);
    tblMemberResults.setVisible(false);
    tblItemResults.setVisible(false);

    if (eraseTxtSearch) {
      txtSearch.setText("");
    }
  }

  public void setSearchAll() {
    rbMembers.setSelected(true);
    lblTitle.setText("Recherche dans le système");
    toggleFilters(true);
  }

  public void setSearchItems() {
    rbItems.setSelected(true);
    lblTitle.setText("Recherche d'articles");
    toggleFilters(false);
  }

  public void setSearchMembers() {
    rbMembers.setSelected(true);
    lblTitle.setText("Recherche de membres");
    toggleFilters(false);
  }

  public Button getBtnAdd() {
    return btnAdd;
  }

  private void toggleFilters(boolean visible) {
    rbItems.setVisible(visible);
    rbMembers.setVisible(visible);
    cbDeactivated.setVisible(visible);
  }
}

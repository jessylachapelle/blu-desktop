package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.Node;

import handler.ItemHandler;
import handler.MemberHandler;
import handler.SearchHandler;
import model.item.Book;
import model.item.Item;
import model.member.Member;

/**
 *
 * @author Jessy Lachapelle
 * @since 19/11/2015
 * @version 0.1
 */
public class SearchController extends PanelController {

  private SearchHandler searchHandler;
  private PanelController parentController;

  @FXML private Label lblTitle;
  @FXML private ToggleGroup type;
  @FXML private TextField txtSearch;
  @FXML private Button btnSearch;
  @FXML private RadioButton rbMembers;
  @FXML private RadioButton rbItems;
  @FXML private CheckBox cbArchive;
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
    _dataBinding();
    _eventHandlers();

    rbMembers.setSelected(true);
    resetSearch(false);
  }

  public Button getBtnAdd() {
    return btnAdd;
  }

  public TableView<Item> getTblItemResults() {
    return tblItemResults;
  }

  public TableView<Member> getTblMemberResults() {
    return tblMemberResults;
  }

  public void resetSearch(boolean eraseTxtSearch) {
    lblMessage.setVisible(true);
    tblMemberResults.setVisible(false);
    tblItemResults.setVisible(false);

    if (eraseTxtSearch) {
      txtSearch.setText("");
    }
  }

  public void setParentController(PanelController controller) {
    parentController = controller;
  }

  public void setSearchItems() {
    rbItems.setSelected(true);
    lblTitle.setText("Recherche d'articles");
    _toggleFilters(false);
  }

  public void setSearchParent() {
    rbMembers.setSelected(true);
    cbArchive.setSelected(false);
    lblTitle.setText("Recherche de parents-étudiants");
    _toggleFilters(false);
    btnAdd.setVisible(false);
    searchHandler.setParentSearch();
  }

  @Override
  protected void handleScan(String code, boolean isItem) {
    if (parentController != null) {
      parentController.handleScan(code, isItem);
    } else {
      super.handleScan(code, isItem);
    }
  }

  private void _dataBinding() {
    cbArchive.managedProperty().bind(cbArchive.visibleProperty());
    rbMembers.managedProperty().bind(rbMembers.visibleProperty());
    rbItems.managedProperty().bind(rbItems.visibleProperty());
    btnAdd.managedProperty().bind(btnAdd.visibleProperty());

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

  private void _eventHandlers() {
    txtSearch.setOnAction(event -> _search());

    btnSearch.setOnAction(event -> _search());

    type.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
      if (type.getSelectedToggle() != null) {
        String data = type.getSelectedToggle().getUserData().toString();

        if (data.matches("member")) {
          searchHandler.setMemberSearch();
          cbArchive.setSelected(false);
          resetSearch(true);
        } else {
          searchHandler.setItemSearch();
          cbArchive.setSelected(false);
          resetSearch(true);
        }
      }
    });

    cbArchive.setOnAction(event -> {
      searchHandler.setSearchArchives(cbArchive.isSelected());

      if (!txtSearch.getText().isEmpty()) {
        _search();
      }
    });

    tblMemberResults.setOnMousePressed(event -> {
      if (isDoubleClick(event)) {
        TableRow row = getTableRow(((Node) event.getTarget()).getParent());
        Member member = (Member) row.getItem();

        if (member != null) {
          window.setCursor(Cursor.WAIT);
          Task<Member> t = new Task<Member>() {
            @Override
            protected Member call() throws Exception {
              MemberHandler memberHandler = new MemberHandler();
              return memberHandler.selectMember(member.getNo());
            }
          };
          t.setOnSucceeded(e -> {
            window.setCursor(Cursor.DEFAULT);
            ((MemberViewController) loadMainPanel("layout/memberView.fxml")).loadMember(t.getValue());
          });
          new Thread(t).start();
        }
      }
    });

    tblItemResults.setOnMousePressed(event -> {
      if (isDoubleClick(event)) {
        TableRow row = getTableRow(((Node) event.getTarget()).getParent());
        Item item = (Item) row.getItem();

        if (item != null) {
          window.setCursor(Cursor.WAIT);
          Task<Item> t = new Task<Item>() {
            @Override
            protected Item call() throws Exception {
              ItemHandler itemHandler = new ItemHandler();
              return itemHandler.selectItem(item.getId());
            }
          };
          t.setOnSucceeded(e -> {
            window.setCursor(Cursor.DEFAULT);
            ((ItemViewController) loadMainPanel("layout/itemView.fxml")).loadItem(t.getValue());
          });
          new Thread(t).start();
        }
      }
    });

    tblItemResults.setOnKeyPressed(event -> {
      Item item = tblItemResults.getSelectionModel().getSelectedItem();

      if (event.getCode().equals(KeyCode.ENTER) && item != null) {
        window.setCursor(Cursor.WAIT);
        Task<Item> t = new Task<Item>() {
          @Override
          protected Item call() throws Exception {
            ItemHandler itemHandler = new ItemHandler();
            return itemHandler.selectItem(item.getId());
          }
        };
        t.setOnSucceeded(e -> {
          window.setCursor(Cursor.DEFAULT);
          ((ItemViewController) loadMainPanel("layout/itemView.fxml")).loadItem(t.getValue());
        });
        new Thread(t).start();
      }
    });

    tblMemberResults.setOnKeyPressed(event -> {
      Member member = tblMemberResults.getSelectionModel().getSelectedItem();

      if (event.getCode().equals(KeyCode.ENTER) && member != null) {
        window.setCursor(Cursor.WAIT);
        Task<Member> t = new Task<Member>() {
          @Override
          protected Member call() throws Exception {
            MemberHandler memberHandler = new MemberHandler();
            return memberHandler.selectMember(member.getNo());
          }
        };
        t.setOnSucceeded(e -> {
          window.setCursor(Cursor.DEFAULT);
          ((MemberViewController) loadMainPanel("layout/memberView.fxml")).loadMember(t.getValue());
        });
        new Thread(t).start();
      }
    });

    btnAdd.setOnAction(event -> {
      String panel = "layout/" + (rbMembers.isSelected() ? "member" : "item") + "Form.fxml";
      loadMainPanel(panel);
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

  private void _searchItems() {
    window.setCursor(Cursor.WAIT);
    Task<ArrayList<Item>> t = new Task<ArrayList<Item>>() {
      @Override
      protected ArrayList<Item> call() throws Exception {
        return searchHandler.searchItems();
      }
    };
    t.setOnSucceeded(e -> {
      window.setCursor(Cursor.DEFAULT);
      tblItemResults.setItems(FXCollections.observableArrayList(t.getValue()));
      lblMessage.setText(tblItemResults.getItems().size() + " résultats");
      tblItemResults.setVisible(!tblItemResults.getItems().isEmpty());
    });
    new Thread(t).start();
  }

  private void _searchMembers() {
    window.setCursor(Cursor.WAIT);
    Task<ArrayList<Member>> t = new Task<ArrayList<Member>>() {
      @Override
      protected ArrayList<Member> call() throws Exception {
        return searchHandler.searchMembers();      }
    };
    t.setOnSucceeded(e -> {
      window.setCursor(Cursor.DEFAULT);
      tblMemberResults.setItems(FXCollections.observableArrayList(t.getValue()));
      tblMemberResults.setVisible(!tblMemberResults.getItems().isEmpty());
      lblMessage.setText(tblMemberResults.getItems().size() + " résultats");
    });
    new Thread(t).start();
  }

  private void _toggleFilters(boolean visible) {
    rbItems.setVisible(visible);
    rbMembers.setVisible(visible);
    cbArchive.setVisible(visible);
  }
}

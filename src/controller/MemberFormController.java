package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import handler.MemberHandler;
import model.membre.Membre;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * La classe qui fait le pont entre la vue(ajoutMembre) et le modèle
 * (gestionnaireMembre)
 *
 * @author Marc
 */
public class MemberFormController extends Controller {

  private MemberHandler memberHandler;
  private Membre membre;
  private BooleanProperty success;
  private boolean insertion;

  @FXML private Label lbl_ajout;
  @FXML private Label lbl_commentaire;

  @FXML private TextField no;
  @FXML private TextField nom;
  @FXML private TextField prenom;
  @FXML private TextField address;
  @FXML private TextField code_postal;
  @FXML private TextField ville;
  @FXML private TextField courriel;
  @FXML private TextField numero1;
  @FXML private TextField numero2;
  @FXML private TextField note1;
  @FXML private TextField note2;

  @FXML private TextArea commentaire;

  @FXML private ComboBox province;

  @FXML private CheckBox cb_numDA;

  @FXML private Button btn_ajout;
  @FXML private Button btn_annuler;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    this.success = new SimpleBooleanProperty(false);
    insertion = true;
    btn_annuler.setVisible(false);
    memberHandler = new MemberHandler();
    membre = new Membre();
    populeState();

    btn_ajout.setOnAction(event -> memberHandler.insertMember(new Membre(formToJSON())));
  }

  /**
   * Rajout les choix de province au CbBox
   */
  private void populeState() {
    // TODO: Use database
    ObservableList<String> options = FXCollections.observableArrayList("AB", "BC", "MB", "NB", "NL", "NT", "NU", "ON", "PE", "QC", "SK", "YT");
    province.setItems(options);
  }

  private JSONObject formToJSON() {
    JSONObject json = new JSONObject();
    JSONArray phones = new JSONArray();

    try {
      // TODO: Add auto generator for fake member no
      json.put("no", no.getText());
      json.put("last_name", nom.getText());
      json.put("first_name", prenom.getText());
      json.put("address", address.getText());
      json.put("zip", code_postal.getText());
      json.put("city", ville.getText());
      json.put("state", province.getValue());
      json.put("email", courriel.getText());
      json.put("comment", commentaire.getText());

      // TODO: Optimize
      if (!numero1.getText().isEmpty()) {
        JSONObject phone = new JSONObject();
        phone.put("number", numero1.getText());
        phone.put("note", note1.getText());
        phones.put(phone);
      }

      if (!numero2.getText().isEmpty()) {
        JSONObject phone = new JSONObject();
        phone.put("number", numero2.getText());
        phone.put("note", note2.getText());
        phones.put(phone);
      }

      json.put("phone", phones);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return json;
  }

  public Button getAddButton() {
    return btn_ajout;
  }

  public int getNoValue() {
    return Integer.parseInt(no.getText());
  }

  public BooleanProperty getSuccess() {
    return success;
  }

  public void loadMember(Membre m) {
    membre = m;
    insertion = false;

    lbl_ajout.setText("Modifier un membre");
    btn_ajout.setText("Modifier");
    lbl_commentaire.setVisible(false);
    commentaire.setVisible(false);
    cb_numDA.setVisible(false);
    btn_annuler.setVisible(true);

    no.setText(Integer.toString(membre.getNo()));
    prenom.setText(membre.getFirstName());
    nom.setText(membre.getLastName());
    address.setText(membre.getAddress());
    code_postal.setText(membre.getZip());
    ville.setText(membre.getCity());
    courriel.setText(membre.getEmail());

    // TODO: Optimisation
    for (int i = 0; i < 2; i++) {
      if (membre.getPhone(i) != null) {
        if (i == 0) {
          numero1.setText(membre.getPhone(i).getNumber());
          note1.setText(membre.getPhone(i).getNote());
        }

        if (i == 1) {
          numero2.setText(membre.getPhone(i).getNumber());
          note2.setText(membre.getPhone(i).getNote());
        }
      }
    }
  }

  public Membre saveMember() {
    if (isInsert()) {
      return memberHandler.insertMember(new Membre(formToJSON()));
    }

    return memberHandler.updateMember(new Membre(formToJSON()));
  }

  public void loadMember(int memberNo) {
    insertion = true;
    membre.setNo(memberNo);
    no.setText(Integer.toString(membre.getNo()));
  }

  public boolean isInsert() {
    return insertion;
  }

  public Button getCancelButton() {
    return btn_annuler;
  }

  public Membre getMember() {
    return membre;
  }
}

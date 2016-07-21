package controller;

import handler.MemberHandler;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.member.Member;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * La classe qui fait le pont entre la vue(ajoutMembre) et le modèle
 * (gestionnaireMembre)
 *
 * @author Marc
 */
public class MemberFormController extends Controller {

  private MemberHandler memberHandler;
  private Member member;
  private BooleanProperty success;
  private boolean insertion;

  @FXML private Label lblAdd;
  @FXML private Label lblComment;

  @FXML private TextField txtNo;
  @FXML private TextField txtLastName;
  @FXML private TextField txtFirstName;
  @FXML private TextField txtAddress;
  @FXML private TextField txtZip;
  @FXML private TextField txtCity;
  @FXML private TextField txtEmail;
  @FXML private TextField txtPhoneNumber1;
  @FXML private TextField txtPhoneNumber2;
  @FXML private TextField txtPhoneNote1;
  @FXML private TextField txtPhoneNote2;

  @FXML private TextArea txtComment;

  @FXML private ComboBox<String> cbState;

  @FXML private CheckBox cbGenerateMemberNo;

  @FXML private Button btnAdd;
  @FXML private Button btnCancel;

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    this.success = new SimpleBooleanProperty(false);
    insertion = true;
    btnCancel.setVisible(false);
    memberHandler = new MemberHandler();
    member = new Member();
    _populeState();

    btnAdd.setOnAction(event -> memberHandler.insertMember(new Member(_toJSON())));
  }

  /**
   * Rajout les choix de cbState au CbBox
   */
  private void _populeState() {
    // TODO: Use database
    ObservableList<String> options = FXCollections.observableArrayList("AB", "BC", "MB", "NB", "NL", "NT", "NU", "ON", "PE", "QC", "SK", "YT");
    cbState.setItems(options);
  }

  private JSONObject _toJSON() {
    JSONObject json = new JSONObject();
    JSONArray phones = new JSONArray();

    try {
      // TODO: Add auto generator for fake member txtNo
      json.put("no", txtNo.getText());
      json.put("last_name", txtLastName.getText());
      json.put("first_name", txtFirstName.getText());
      json.put("address", txtAddress.getText());
      json.put("zip", txtZip.getText());
      json.put("city", txtCity.getText());
      json.put("state", cbState.getValue());
      json.put("email", txtEmail.getText());
      json.put("comment", txtComment.getText());

      // TODO: Optimize
      if (!txtPhoneNumber1.getText().isEmpty()) {
        JSONObject phone = new JSONObject();
        phone.put("number", txtPhoneNumber1.getText());
        phone.put("note", txtPhoneNote1.getText());
        phones.put(phone);
      }

      if (!txtPhoneNumber2.getText().isEmpty()) {
        JSONObject phone = new JSONObject();
        phone.put("number", txtPhoneNumber2.getText());
        phone.put("note", txtPhoneNote2.getText());
        phones.put(phone);
      }

      json.put("phone", phones);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return json;
  }

  public Button getAddButton() {
    return btnAdd;
  }

  public int getNo() {
    return Integer.parseInt(txtNo.getText());
  }

  public BooleanProperty getSuccess() {
    return success;
  }

  public void loadMember(Member m) {
    member = m;
    insertion = false;

    lblAdd.setText("Modifier un member");
    btnAdd.setText("Modifier");
    lblComment.setVisible(false);
    txtComment.setVisible(false);
    cbGenerateMemberNo.setVisible(false);
    btnCancel.setVisible(true);

    txtNo.setText(Integer.toString(member.getNo()));
    txtFirstName.setText(member.getFirstName());
    txtLastName.setText(member.getLastName());
    txtAddress.setText(member.getAddress());
    txtZip.setText(member.getZip());
    txtCity.setText(member.getCity());
    txtEmail.setText(member.getEmail());

    // TODO: Optimisation
    for (int i = 0; i < 2; i++) {
      if (member.getPhone(i) != null) {
        if (i == 0) {
          txtPhoneNumber1.setText(member.getPhone(i).getNumber());
          txtPhoneNote1.setText(member.getPhone(i).getNote());
        }

        if (i == 1) {
          txtPhoneNumber2.setText(member.getPhone(i).getNumber());
          txtPhoneNote2.setText(member.getPhone(i).getNote());
        }
      }
    }
  }

  public Member saveMember() {
    if (isInsert()) {
      return memberHandler.insertMember(new Member(_toJSON()));
    }

    return memberHandler.updateMember(new Member(_toJSON()));
  }

  public void loadMember(int memberNo) {
    insertion = true;
    member.setNo(memberNo);
    txtNo.setText(Integer.toString(member.getNo()));
  }

  public boolean isInsert() {
    return insertion;
  }

  public Button getCancelButton() {
    return btnCancel;
  }

  public Member getMember() {
    return member;
  }
}

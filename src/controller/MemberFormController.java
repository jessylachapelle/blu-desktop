package controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import hanlder.MemberHandler;
import model.membre.Membre;

/**
 * La classe qui fait le pont entre la vue(ajoutMembre) et le modèle
 * (gestionnaireMembre)
 *
 * @author Marc
 */
public class MemberFormController extends Controller {

  private MemberHandler gMembre;
  private Membre membre;
  private BooleanProperty success;
  private boolean ajout;

  @FXML
  private Label lbl_ajout;
  @FXML
  private Label lbl_commentaire;
  @FXML
  private TextArea commentaire;
  @FXML
  private TextField no;
  @FXML
  private TextField nom;
  @FXML
  private TextField prenom;
  @FXML
  private TextField no_civic;
  @FXML
  private TextField rue;
  @FXML
  private TextField app;
  @FXML
  private TextField code_postal;
  @FXML
  private TextField ville;
  @FXML
  private ComboBox province;
  @FXML
  private TextField courriel;
  @FXML
  private TextField numero1;
  @FXML
  private TextField numero2;
  @FXML
  private CheckBox cb_numDA;
  @FXML
  private TextField note1;
  @FXML
  private TextField note2;
  @FXML
  private Button btn_ajout;
  @FXML
  private Button btn_annuler;

  @Override // This method is called by the FXMLLoader when initialization is complete
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    this.success = new SimpleBooleanProperty(false);
    ajout = true;
    btn_annuler.setVisible(false);
    assertInjection();
    gMembre = new MemberHandler();
    membre = new Membre();
    populeProvince();

    // initialize your logic here: all @FXML variables will have been injected
    btn_ajout.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        HashMap<String, String> infoMembre = new HashMap();
        retrieveValues(infoMembre);
        if (gMembre.ajouteMembre(infoMembre)) {
          success.set(true);
        } else {
          System.out.println("False");
        }
        System.out.println(province.getValue());
      }
    });
  }

  /**
   * Rajout les choix de province au CbBox
   */
  private void populeProvince() {
    // @TODO Softcoder les provinces à la bd
    // Voir matiere dans ajout article

    ObservableList<String> options
            = FXCollections.observableArrayList(
                    "AB",
                    "BC",
                    "MB",
                    "NB",
                    "NL",
                    "NT",
                    "NU",
                    "ON",
                    "PE",
                    "QC",
                    "SK",
                    "YT"
            );
    province.setItems(options);
  }

  private void assertInjection() {
    assert lbl_ajout != null : "fx:id=\"lbl_ajout\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert lbl_commentaire != null : "fx:id=\"lbl_commentaire\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert btn_ajout != null : "fx:id=\"btn_ajout\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert btn_annuler != null : "fx:id=\"btn_annuler\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert no != null : "fx:id=\"no\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert nom != null : "fx:id=\"nom\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert prenom != null : "fx:id=\"prenom\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert no_civic != null : "fx:id=\"no_civic\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert rue != null : "fx:id=\"rue\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert app != null : "fx:id=\"app\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert code_postal != null : "fx:id=\"code_postal\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert ville != null : "fx:id=\"ville\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert province != null : "fx:id=\"province\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert courriel != null : "fx:id=\"courriel\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert numero1 != null : "fx:id=\"numero1\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert numero2 != null : "fx:id=\"numero2\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert note1 != null : "fx:id=\"note1\" was not injected: check your FXML file 'memberForm.fxml'.";
    assert note2 != null : "fx:id=\"note2\" was not injected: check your FXML file 'memberForm.fxml'.";
  }

  private void retrieveValues(HashMap infoMembre) {
    System.out.println(province.getValue());
    // TODO Le auto number generaotr pour membre....
    infoMembre.put("no", no.getText());
    infoMembre.put("nom", nom.getText());
    infoMembre.put("prenom", prenom.getText());
    infoMembre.put("no_civic", no_civic.getText());
    infoMembre.put("rue", rue.getText());
    infoMembre.put("app", app.getText());
    infoMembre.put("code_postal", code_postal.getText());
    infoMembre.put("ville", ville.getText());
    infoMembre.put("province", province.getValue());
    infoMembre.put("courriel", courriel.getText());
    infoMembre.put("numero1", numero1.getText());
    infoMembre.put("numero2", numero2.getText());
    infoMembre.put("note1", note1.getText());
    infoMembre.put("note2", note2.getText());
    infoMembre.put("commentaire", commentaire.getText());
  }

  private HashMap getFormValues() {
    HashMap infoMembre = new HashMap();
     // TODO Le auto number generaotr pour membre....
    infoMembre.put("no", no.getText());
    infoMembre.put("nom", nom.getText());
    infoMembre.put("prenom", prenom.getText());
    infoMembre.put("no_civic", no_civic.getText());
    infoMembre.put("rue", rue.getText());
    infoMembre.put("app", app.getText());
    infoMembre.put("code_postal", code_postal.getText());
    infoMembre.put("ville", ville.getText());
    infoMembre.put("province", province.getValue());
    infoMembre.put("courriel", courriel.getText());
    infoMembre.put("numero1", numero1.getText());
    infoMembre.put("numero2", numero2.getText());
    infoMembre.put("note1", note1.getText());
    infoMembre.put("note2", note2.getText());
    infoMembre.put("commentaire", commentaire.getText());

    return infoMembre;
  }

  public Button getBtn_ajout() {
    return btn_ajout;
  }

  public int getNoValue() {
    return Integer.parseInt(no.getText());
  }

  public BooleanProperty getSuccess() {
    return success;
  }

  public void loadMembre(Membre m) {
    membre = m;
    ajout = false;

    lbl_ajout.setText("Modifier un membre");
    btn_ajout.setText("Modifier");
    lbl_commentaire.setVisible(false);
    commentaire.setVisible(false);
    //cb_numDA.setVisible(false);
    btn_annuler.setVisible(true);

    no.setText(Integer.toString(membre.getNoMembre()));
    prenom.setText(membre.getPrenom());
    nom.setText(membre.getNom());
    no_civic.setText(Integer.toString(membre.getNoCivic()));
    rue.setText(membre.getRue());
    app.setText(String.valueOf(membre.getAppartement()));
    code_postal.setText(String.valueOf(membre.getCodePostal()));
    ville.setText(membre.getVille());
    courriel.setText(membre.getCourriel());

    // TODO arange moé ça :p
    for (int noTel = 0; noTel < 2; noTel++) {
      if (membre.getTelephone(noTel) != null) {
        numero1.setText(membre.getTelephone(noTel).getNumero());
        note1.setText(membre.getTelephone(noTel).getNote());
      }
    }
  }

  public Membre saveMembre() {
    Membre membre = gMembre.construitMembre(getFormValues());

    if (estAjout()) {
      return gMembre.insertMember(membre);
    }

    return gMembre.updateMember(membre);
  }

  public void loadMembre(int noMembre) {
    ajout = true;
    membre.setNoMembre(noMembre);
    no.setText(Integer.toString(membre.getNoMembre()));
  }

  public boolean estAjout() {
    return ajout;
  }

  public Button getButtonAnnule() {
    return btn_annuler;
  }

  public Membre getMembre() {
    return membre;
  }
}

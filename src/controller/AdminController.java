package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author Jessy
 */
public class AdminController extends Controller {
  @SuppressWarnings("FieldCanBeLocal")
  private final String URL = "http://localhost/blu-web/admin/admin.php";

  @FXML
  private WebView webView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    WebEngine engine = webView.getEngine();
    engine.load(URL);
  }
}

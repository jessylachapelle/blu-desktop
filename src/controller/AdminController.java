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
  private final String URL = "http://jessylachapelle.no-ip.biz/blu/htdocs/admin/admin.php";

  @FXML
  private WebView webView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    assertions();

    WebEngine engine = webView.getEngine();
    engine.load(URL);
  }

  private void assertions() {
    assert webView != null : "fx:id=\"webView\" was not injected: check your FXML file 'admin.fxml'.";
  }
}

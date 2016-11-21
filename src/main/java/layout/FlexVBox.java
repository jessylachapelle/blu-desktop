package layout;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import org.json.JSONObject;

/**
 * @author Jessy Lachapelle
 * @since 2016/17/11
 * @version 1.0
 */
public class FlexVBox extends VBox {
  public FlexVBox() {
    super();
    prefHeightProperty().bind(heightProperty());
    heightProperty().addListener((observable, oldValue, newValue) -> {
      double windowHeigth = newValue.doubleValue();
      int flex = 0;

      for(Node node : getChildren()) {
        JSONObject userData = node.getUserData() != null ? new JSONObject(node.getUserData().toString()) : new JSONObject();
        flex += userData.optInt("flex", userData.has("height") ? 0 : 1);
      }

      for(Node node : getChildren()) {
        JSONObject userData = node.getUserData() != null ? new JSONObject(node.getUserData().toString()) : new JSONObject();
        int index = userData.optInt("flex", userData.has("height") ? 0 : 1);
        if (node instanceof Pane) {
          Pane pane = (Pane) node;
          if (index > 0) {
            pane.prefHeight(windowHeigth / flex * index);
          } else {
            pane.setPrefHeight(userData.optDouble("height", 0));
          }
        }
      }
    });
  }
}

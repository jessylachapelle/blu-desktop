package layout;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

/**
 * @author Jessy Lachapelle
 * @since 2016/17/11
 * @version 1.0
 */
public class DynamicVBox extends VBox {
  public DynamicVBox() {
    super();
    JSONObject userData = getUserData() != null ? new JSONObject(getUserData().toString()) : new JSONObject();

    getStyleClass().add("main");
    setSpacing(userData.optDouble("spacing", 10));

    parentProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null && (newValue.getClass().toString().equals("class com.sun.javafx.scene.control.skin.ScrollPaneSkin$4") || newValue.getChildrenUnmodifiable().size() > 1)) {
        Pane parent = (Pane) getParent();
        prefWidthProperty().bind(parent.widthProperty());
      } else if (newValue != null && newValue instanceof Pane) {
        Pane parent = (Pane) newValue;
        prefHeightProperty().bind(parent.heightProperty());
        prefWidthProperty().bind(parent.widthProperty());
      }
    });

    prefWidthProperty().addListener((observable, oldValue, newValue) -> setWidth(newValue.doubleValue()));
    prefHeightProperty().addListener((observable, oldValue, newValue) -> setHeight(newValue.doubleValue()));
  }
}

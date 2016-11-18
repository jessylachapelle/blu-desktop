package layout;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

/**
 * @author Jessy Lachapelle
 * @since 2016/17/11
 * @version 1.0
 */
public class DynamicScrollPane extends ScrollPane {
  public DynamicScrollPane() {
    super();
    getStyleClass().add("main");
    parentProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null && newValue instanceof Pane) {
        Pane parent = (Pane) newValue;
        prefHeightProperty().bind(parent.heightProperty());
        prefWidthProperty().bind(parent.widthProperty());
      }
    });

    prefWidthProperty().addListener((observable, oldValue, newValue) -> setWidth(newValue.doubleValue()));
    prefHeightProperty().addListener((observable, oldValue, newValue) -> setHeight(newValue.doubleValue()));
  }
}

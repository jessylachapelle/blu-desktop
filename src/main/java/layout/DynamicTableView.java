package layout;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

/**
 * @author Jessy Lachapelle
 * @since 2016/30/10
 * @version 1.0
 */
public class DynamicTableView<T> extends TableView<T> {
  public DynamicTableView() {
    super();
    final boolean[] setColumnWidth = {false};
    managedProperty().bind(visibleProperty());
    setFixedCellSize(28);

    parentProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null && newValue instanceof Pane) {
        Pane parent = (Pane) newValue;
        if (parent.getWidth() > 0 || parent.getPrefWidth() > 0) {
          double width = parent.getWidth() != 0 ? parent.getWidth() / 1.1 : parent.getPrefWidth() / 1.1;
          setWidth(width);
          setPrefWidth(width);
        } else {
          setWidth(Screen.getPrimary().getVisualBounds().getWidth() * .745);
          setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() * .745);
        }
      }
    });

    itemsProperty().addListener((observable, oldValue, newValue) -> {
      setPrefHeight(getFixedCellSize() * (newValue.size() + 1));

      if (!setColumnWidth[0]) {
        int flex = 0;
        for (TableColumn column : getColumns()) {
          if (column.getUserData() == null) {
            flex += 1;
          } else {
            flex += Integer.parseInt(column.getUserData().toString());
          }
        }

        for (TableColumn column : getColumns()) {
          int index = column.getUserData() == null ? 1 : Integer.parseInt(column.getUserData().toString());
          column.setPrefWidth(getPrefWidth() / flex * index);
        }

        setColumnWidth[0] = true;
      }
    });
  }
}

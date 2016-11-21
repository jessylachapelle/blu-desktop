package utility;

import javafx.scene.layout.Pane;

import controller.Controller;

/**
 * @author jessy on 06/08/16.
 */
public class View {
  private Pane pane;
  private Controller controller;

  public View(Pane pane, Controller controller) {
    this.pane = pane;
    this.controller = controller;
  }

  public Controller getController() {
    return controller;
  }

  public Pane getPane() {
    return pane;
  }
}

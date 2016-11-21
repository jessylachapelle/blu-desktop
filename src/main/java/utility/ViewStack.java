package utility;

import java.util.ArrayList;

import javafx.scene.layout.Pane;

import controller.Controller;

/**
 * @author Jessy Lachapele
 * @since 2016-08-06
 * @version 1.0
 */
@SuppressWarnings("unused")
public class ViewStack {
  private final int MAX_SIZE = 5;
  private ArrayList<View> stack;

  public ViewStack() {
    stack = new ArrayList<>();
  }

  public View get() {
    return size() > 0 ? stack.get(size() - 1) : null;
  }

  public View pop() {
    return size() > 0 ? stack.remove(size() - 1) : null;
  }

  public void push(Pane pane, Controller controller) {
    if (size() == MAX_SIZE) {
      stack.remove(0);
    }

    stack.add(new View(pane, controller));
  }

  public void push(View view) {
    if (size() == MAX_SIZE) {
      stack.remove(0);
    }

    stack.add(view);
  }

  public int size() {
    return stack.size();
  }
}

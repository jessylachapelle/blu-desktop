package utilitiy;

import java.util.ArrayList;

import controller.Controller;
import javafx.scene.layout.Pane;

/**
 * @author Jessy Lachapele
 * @since 2016-08-06
 * @version 1.0
 */
public class ViewStack {
  private final int MAX_SIZE = 5;
  private ArrayList<View> stack;

  public ViewStack() {
    stack = new ArrayList<>();
  }

  public void push(Pane pane, Controller controller) {
    if (stack.size() == MAX_SIZE) {
      stack.remove(0);
    }

    stack.add(new View(pane, controller));
  }

  public void push(View view) {
    if (stack.size() == MAX_SIZE) {
      stack.remove(0);
    }

    stack.add(view);
  }

  public View pop() {
    if (stack.size() > 0) {
      return stack.remove(stack.size() - 1);
    }

    return null;
  }

  public View get() {
    if (stack.size() > 0) {
      return stack.get(stack.size() - 1);
    }

    return null;
  }

  public int size() {
    return stack.size();
  }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import ressources.I18N;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Jessy Lachapelle
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Controller implements javafx.fxml.Initializable {
  private static Pane mainPanel;
  protected I18N i18n;

  protected void setI18n(I18N i18n) {
    this.i18n = i18n;
  }

  protected void initI18n(String lang) {
    i18n = new I18N(lang);
  }

  protected void initI18n() {
    i18n = new I18N();
  }

  protected TableRow _getTableRow(Node node) {
    if (node instanceof TableRow) {
      return (TableRow) node;
    }

    if (node.getParent() instanceof TableRow) {
      return (TableRow) node.getParent();
    }

    return new TableRow();
  }

  protected void setMainPanel(Pane pane) {
    if (mainPanel == null) {
      mainPanel = pane;
    }
  }

  protected Pane getMainPanel() {
    return mainPanel;
  }

  protected Controller loadMainPanel(String resource) {
    return loadPanel(mainPanel, resource);
  }

  protected Controller loadPanel(Pane parent, String resource) {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(WindowController.class.getClassLoader().getResource(resource));
    parent.getChildren().clear();

    try {
//      Pane pane = loader.load();
//
//      viewStack.push(pane, loader.getController());
//      btnBack.setVisible(viewStack.size() > 1);
//
      parent.getChildren().add(loader.load());
      return loader.getController();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  protected boolean isDoubleClick(MouseEvent event) {
    return event.isPrimaryButtonDown() && event.getClickCount() == 2;
  }

  protected boolean isRightClick(MouseEvent event) {
    return event.isSecondaryButtonDown();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {}
}

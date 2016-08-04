/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.scene.Node;
import javafx.scene.control.TableRow;
import ressources.I18N;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Jessy
 */
@SuppressWarnings("unused")
public class Controller implements javafx.fxml.Initializable {
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

  @Override
  public void initialize(URL location, ResourceBundle resources) {}
}

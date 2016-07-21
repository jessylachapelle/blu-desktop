/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

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

  public void setI18n(I18N i18n) {
    this.i18n = i18n;
  }

  public void initI18n(String lang) {
    i18n = new I18N(lang);
  }

  public void initI18n() {
    i18n = new I18N();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {}
}

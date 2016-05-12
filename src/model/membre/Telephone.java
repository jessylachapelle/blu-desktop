package model.membre;

/**
 * Un numéro de téléphone avec une note
 *
 * @author Jessy Lachapelle
 * @since 26/10/2015
 * @version 0.1
 */
public class Telephone {
  private int id;
  private String numero;
  private String note;

  /**
   * Constructeur par défaut, crée objet à valeur null
   */
  public Telephone() {
    id = 0;
    numero = "";
    note = "";
  }

  /**
   * Constructeur d'un numéro de téléphone sans note
   *
   * @param numero Numéro de téléphone
   */
  public Telephone(String numero) {
    setNumero(numero);
    note = null;
  }

  /**
   * Constructeur d'un numéro de téléphone avec note
   *
   * @param numero Numéro de téléphone
   * @param note Note associée au numéro
   */
  public Telephone(String numero, String note) {
    setNumero(numero);
    setNote(note);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
  /**
   * Accède au numéro de téléphone
   *
   * @return numero Le numéro de téléphone
   */
  public String getNumero() {
    return numero;
  }

  /**
   * Modifie le numéro de téléphone
   *
   * @param numero Le nouveau numéro de téléphone
   */
  public void setNumero(String numero) {
    this.numero = numero;
  }

  /**
   * Accède à la note associée au numéro
   *
   * @return note La note associée au numéro
   */
  public String getNote() {
    if (note == null) {
      return "";
    }
    return note;
  }

  /**
   * Modifie la note associée au numéro
   *
   * @param note Note associé au numéro
   */
  public void setNote(String note) {
    this.note = note;
  }

  @Override
  public String toString() {
    if (numero.isEmpty() || this == null) {
      return "";
    }

    String strTel = numero.substring(0, 3) + " " + numero.substring(3, 6) + "-" + numero.substring(6, 10);

    if (note != null && !note.equals("")) {
      strTel += " (" + note + ")";
    }
    return strTel;
  }
}

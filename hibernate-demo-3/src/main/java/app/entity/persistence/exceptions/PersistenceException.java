
package app.entity.persistence.exceptions;

/** Excepción lanzada por el sistema de persistencia */
public class PersistenceException extends Exception {

  /** SerialVersionUID */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor con mensaje
   * @param message Mensaje de error
   */
  public PersistenceException(String message) {
    super(message);
  }

}

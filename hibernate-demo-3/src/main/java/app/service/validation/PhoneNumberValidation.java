
package app.service.validation;

import app.entity.PhoneNumber;

/**
 * Clase de validaciones de los atributos propios de un número de teléfono. Sólo realizará comprobaciones de atributos no
 * creados en el paquete entity
 */
public class PhoneNumberValidation {

  /** Expresión regular para el número */
  private static final String NUMBER_REGEX = "[6789]\\d{8}";

  /** Constructor privado que evita instanciación de clase */
  private PhoneNumberValidation() {
  }

  /**
   * Verifica que el número de teléfono tenga 9 caracteres numéricos de forma que comience por 6, 7, 8 o 9
   * @param number Número de teléfono
   * @return boolean
   */
  public static boolean isValidNumber(String number) {
    return number != null && number.matches(NUMBER_REGEX);
  }

  /**
   * Verifica que todos los atributos pertenecientes a PhoneNumber no creados como entidad son válidos
   * @param phoneNumber número de teléfono
   * @return boolean
   */
  public static boolean isValidPhoneNumber(PhoneNumber phoneNumber) {
    return isValidNumber(phoneNumber.getNumber());
  }

}

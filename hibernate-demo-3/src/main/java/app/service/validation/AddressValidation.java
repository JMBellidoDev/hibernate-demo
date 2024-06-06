
package app.service.validation;

import app.entity.Address;

/**
 * Clase de validaciones de los atributos propios de una dirección. Sólo realizará comprobaciones de atributos no creados en
 * el paquete entity
 */
public class AddressValidation {

  /** Longitud máxima de la descripción de la dirección */
  private static final int STREET_ADDRESS_MAX_LENGTH = 255;

  /** Longitud máxima del nombre de la ciudad */
  private static final int CITY_MAX_LENGTH = 50;

  /** Expresión regular que debe cumplir un código postal */
  private static final String POSTAL_CODE_REGEX = "([0-4]\\d{4})|([5][012]\\d{3})";

  /** Constructor privado para evitar instanciación de clase */
  private AddressValidation() {
  }

  /**
   * Verifica si la descripción de la dirección es no null de una longitud menor o igual a 255 caracteres, no todos blancos
   * @param streetAddress Descripción de la dirección
   * @return boolean
   */
  public static boolean isValidStreetAddress(String streetAddress) {
    return streetAddress != null && streetAddress.length() <= STREET_ADDRESS_MAX_LENGTH && !streetAddress.isBlank();
  }

  /**
   * Verifica si el nombre de la dirección es no null de una longitud menor o igual a 50 caracteres, no todos blancos
   * @param city Descripción de la dirección
   * @return boolean
   */
  public static boolean isValidCity(String city) {
    return city != null && city.length() <= CITY_MAX_LENGTH && !city.isBlank();
  }

  /**
   * Verifica si el código postal de la dirección es no null y verifica la expresión regular
   * @param postalCode Código postal
   * @return boolean
   */
  public static boolean isValidPostalCode(String postalCode) {
    return postalCode != null && postalCode.matches(POSTAL_CODE_REGEX);
  }

  /**
   * Verifica que todos los atributos pertenecientes a Address no creados como entidad son válidos
   * @param address Dirección
   * @return boolean
   */
  public static boolean isValidAddress(Address address) {
    return isValidStreetAddress(address.getStreetAddress()) && isValidCity(address.getCity())
        && isValidPostalCode(address.getPostalCode());
  }

}

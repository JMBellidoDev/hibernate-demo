
package app.service.validation;

import java.time.LocalDate;

import app.entity.Student;

/**
 * Clase de validaciones de los atributos propios de un estudiante. Sólo realizará comprobaciones de atributos no creados en
 * el paquete entity
 */
public class StudentValidation {

  /** Expresión regular para la comprobación del DNI. 8 dígitos y una letra asociada (letras I, O no son válidas) */
  private static final String REGEX_DNI = "\\d{8}[ABCDEFGHJLMNPQRSTVWXYZ]";

  /** Letras posibles para el DNI, en orden según el resto del número asociado al DNI entre 23 */
  private static final char[] DNI_LETTERS = { 'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S',
      'Q', 'V', 'H', 'L', 'C', 'K', 'E' };

  /**
   * Expresión regular para la comprobación del nombre. Debe ser una cadena de caracteres alfabéticos que puede contener
   * espacios
   */
  private static final String REGEX_NAME = "[A-ZÑa-zñ\\s]*";

  /** Longitud máxima del nombre del estudiante */
  private static final int NAME_MAX_LENGTH = 100;

  /** Constructor privado para evitar instanciación de clase */
  private StudentValidation() {
  }

  /**
   * Verifica que un DNI sea correcto. Para ello, debe ser una cadena de 9 caracteres formados por 8 números y una letra
   * mayúscula, de forma que cumpla las validaciones establecidas en España para un DNI
   * @param dni DNI a verificar
   * @return boolean
   */
  public static boolean isValidDni(String dni) {

    if (dni != null && dni.matches(REGEX_DNI)) {

      // Se comprueba la letra del dni
      int dniNumber = Integer.parseInt(dni.substring(0, dni.length() - 1));
      char associatedLetter = DNI_LETTERS[dniNumber % DNI_LETTERS.length];

      return associatedLetter == dni.charAt(dni.length() - 1);
    }

    // Si se llega aquí, es falso
    return false;
  }

  /**
   * Verifica que un nombre sea correcto. Para ello debe ser una cadena de como máximo 100 caracteres del alfabeto español
   * que puede contener espacios
   * @param name Nombre del estudiante
   * @return boolean
   */
  public static boolean isValidName(String name) {
    return (name != null && name.length() <= NAME_MAX_LENGTH && !name.isBlank() && name.matches(REGEX_NAME));
  }

  /**
   * Verifica que una fecha de nacimiento sea correcta. Para ello, debe ser una fecha anterior a la actual
   * @param birthdate
   * @return
   */
  public static boolean isValidBirthdate(LocalDate birthdate) {
    return birthdate != null && birthdate.isBefore(LocalDate.now());
  }

  /**
   * Verifica que todos los atributos pertenecientes a Student no creados como entidad son válidos
   * @param student Estudiante
   * @return boolean
   */
  public static boolean isValidStudent(Student student) {
    return isValidDni(student.getDni()) && isValidName(student.getName()) && isValidBirthdate(student.getBirthdate());
  }

}

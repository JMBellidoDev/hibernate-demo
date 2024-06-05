
package app.entity.constants;

/** Clase de constantes para usar en los sistemas de persistencia */
public class DbConstants {

  /** Constructor privado para evitar instanciación de clase */
  private DbConstants() {
  }

  // ------------------ Tabla T_ Address ------------------

  /** Nombre de la tabla de direcciones */
  public static final String ADDRESS_TABLE = "T_Address";

  /** Campo ID de la tabla T_Address */
  public static final String ADDRESS_ID = "id";

  /** Campo descripción de la dirección completa de la tabla T_Address */
  public static final String ADDRESS_STREET = "street_address";

  /** Campo nombre de ciudad de la tabla T_Address */
  public static final String ADDRESS_CITY = "city";

  /** Campo código postal de la tabla T_Address */
  public static final String ADDRESS_POSTAL_CODE = "postal_code";

  // ------------------ Tabla T_Phone_Number ------------------

  /** Nombre de la tabla de números de teléfono */
  public static final String PHONE_NUMBER_TABLE = "T_Phone_Number";

  /** Campo ID de la tabla T_Phone_Number */
  public static final String PHONE_NUMBER_ID = "id";

  /** Campo descripción de la dirección completa de la tabla T_Phone_Number */
  public static final String PHONE_NUMBER_NUM = "phone_number";

  /** Campo nombre de ciudad de la tabla T_Address */
  public static final String PHONE_NUMBER_STUDENT_ID = "student_id";

  // ------------------ Tabla T_Student ------------------

  /** Nombre de la tabla de estudiantes */
  public static final String STUDENT_TABLE = "T_Student";

  /** Campo ID de la tabla T_Student */
  public static final String STUDENT_ID = "id";

  /** Campo DNI de la tabla T_Student */
  public static final String STUDENT_DNI = "dni";

  /** Campo nombre de la tabla T_Student */
  public static final String STUDENT_NAME = "name";

  /** Campo fecha de nacimiento de la tabla T_Student */
  public static final String STUDENT_BIRTHDATE = "birthdate";

  /** Campo id de la dirección de la tabla T_Student */
  public static final String STUDENT_ADDRESS_ID = "address_id";

  /** Campo id del número de teléfono asignado de la tabla T_Student */
  public static final String STUDENT_PHONE_NUMBER_ID = "phone_number_id";

  /** Campo ID del curso en el que se encuentra el estudiante */
  public static final String STUDENT_COURSE_ID = "course_id";

  // ------------------ Tabla T_Student_PhoneNumber ------------------

  /** Nombre de la tabla de intermedia entre estudiantes y números de teléfono */
  public static final String STUDENT_PHONE_NUMBER_TABLE = "T_Student_Phone_Number";

  /** Campo ID de la tabla T_Student_PhoneNumber */
  public static final String STUDENT_PHONE_NUMBER_STUDENT_ID = "student_id";

  /** Campo ID de la tabla T_Student_PhoneNumber */
  public static final String STUDENT_PHONE_NUMBER_PHONE_ID = "phone_number_id";

  // ------------------ Tabla T_Course ------------------

  /** Nombre de la tabla de cursos */
  public static final String COURSE_TABLE = "T_Course";

  /** Campo ID de la tabla T_Course */
  public static final String COURSE_ID = "id";

  /** Campo name de la table T_Course */
  public static final String COURSE_NAME = "name";

  /** Campo school de la tabla T_Course */
  public static final String COURSE_SCHOOL = "school";

  /** Campo starting_year de la tabla T_Course */
  public static final String COURSE_STARTING_YEAR = "starting_year";

}

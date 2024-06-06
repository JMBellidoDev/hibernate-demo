
package app.service.validation;

import app.entity.Course;

/**
 * Clase de validaciones de los atributos propios de un curso. Sólo realizará comprobaciones de atributos no creados en el
 * paquete entity
 */
public class CourseValidation {

  /** Longitud máxima del nombre del curso */
  private static final int NAME_MAX_LENGTH = 80;

  /** Longitud máxima del nombre del centro escolar */
  private static final int SCHOOL_MAX_LENGTH = 80;

  /** Constructor privado para evitar instanciación de clase */
  private CourseValidation() {
  }

  /**
   * Verifica que el nombre del curso sea no null de una longitud menor o igual a 80 caracteres, no todos blancos
   * @param name Nombre del curso
   * @return boolean
   */
  public static boolean isValidName(String name) {
    return name != null && name.length() <= NAME_MAX_LENGTH && !name.isBlank();
  }

  /**
   * Verifica que el nombre del centro escolar sea no null de una longitud menor o igual a 100 caracteres, no todos blancos
   * @param school Nombre del centro escolar
   * @return boolean
   */
  public static boolean isValidSchool(String school) {
    return school != null && school.length() <= SCHOOL_MAX_LENGTH && !school.isBlank();
  }

  /**
   * Verifica que todos los atributos pertenecientes a Course no creados como entidad son válidos
   * @param course Curso
   * @return boolean
   */
  public static boolean isValidCourse(Course course) {
    return isValidName(course.getName()) && isValidSchool(course.getSchool());
  }

}

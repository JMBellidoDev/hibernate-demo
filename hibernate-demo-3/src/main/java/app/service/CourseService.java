
package app.service;

import java.util.List;

import app.entity.Course;
import app.entity.persistence.CoursePersistence;
import app.entity.persistence.exceptions.PersistenceException;
import app.service.validation.CourseValidation;

/** Servicio de gestión de cursos */
public class CourseService {

  /** Sistema de persistencia de cursos */
  private CoursePersistence cPersistence;

  /**
   * Constructor
   * @param cPersistence Sistema de persistencia de cursos
   */
  public CourseService(CoursePersistence cPersistence) {
    this.cPersistence = cPersistence;
  }

  /**
   * Almacena o actualiza un curso junto con toda la información relacionada, previa validación de atributos
   * @param name         Nombre del curso
   * @param school       Centro escolar
   * @param startingYear Año de comienzo del curso
   * @return Integer
   * @throws PersistenceException     En caso de que ocurra un error durante el acceso a los datos
   * @throws IllegalArgumentException Si alguno de los atributos de Course no son correctos
   */
  public Integer save(String name, String school, int startingYear) throws PersistenceException {

    // Se verifican los datos del curso
    if (CourseValidation.isValidName(name) && CourseValidation.isValidSchool(school)) {

      // Se comprueba si ya existía previamente y sino se crea
      Course course = cPersistence.findByNameSchoolAndStartingYear(name, school, startingYear);

      // Si no existe, se crea y almacena
      if (course == null) {

        course = new Course();
        course.setName(name);
        course.setSchool(school);
        course.setStartingYear(startingYear);
      }

      // Se persiste la dirección
      return cPersistence.saveOrUpdateCourse(course);

    } else {
      throw new IllegalArgumentException("Los datos de la dirección no son válidos. No se crearán.");
    }
  }

  /**
   * Obtiene una lista que contiene todos los cursos almacenados en el sistema
   * @return List(Course)
   * @throws PersistenceException En caso de que ocurra un error durante el acceso a los datos
   */
  public List<Course> getAll() throws PersistenceException {
    return cPersistence.getAllCourse();
  }

  /**
   * Elimina un curso de la DB
   * @param name         Nombre del curso
   * @param school       Centro escolar
   * @param startingYear Año de comienzo del curso
   * @throws PersistenceException En caso de que ocurra un error durante el acceso a los datos
   */
  public void deleteCourse(String name, String school, int startingYear) throws PersistenceException {
    cPersistence.deleteCourse(findByNameSchoolAndStartingYear(name, school, startingYear).getId());
  }

  /**
   * Busca un curso en la DB
   * @param name         Nombre del curso
   * @param school       Centro escolar
   * @param startingYear Año de comienzo del curso
   * @return Course - Será null si no se encuentra en la DB
   * @throws PersistenceException En caso de que ocurra un error durante el acceso a los datos
   */
  public Course findByNameSchoolAndStartingYear(String name, String school, int startingYear) throws PersistenceException {
    return cPersistence.findByNameSchoolAndStartingYear(name, school, startingYear);
  }

}


package app.entity.persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import app.entity.Course;
import app.entity.constants.DbConstants;
import app.entity.persistence.exceptions.PersistenceException;

/** Sistema de persistencia de Cursos */
public class CoursePersistence {

  /** Factoría de sesiones */
  private SessionFactory factory;

  /**
   * Constructor de la clase. Inicializa la factoría de sesiones
   * @throws ExceptionInInitializerError En caso de que exista un error durante la generación de la factoría de sesiones
   */
  public CoursePersistence() {

    // Se intenta crear la factoría de sesiones añadiendo las clases con anotaciones
    try {
      factory = new Configuration().configure().addAnnotatedClass(app.entity.Course.class).buildSessionFactory();

    } catch (Exception ex) {

      throw new ExceptionInInitializerError("Error al crear un objeto de la clase SessionFactory");
    }
  }

  /**
   * Almacena un curso en la DB
   * @param course Curso a almacenar
   * @return Integer - El ID del curso
   * @throws PersistenceException En caso de que exista un error durante el proceso de almacenamiento de cursos
   */
  public Integer saveCourse(Course course) throws PersistenceException {

    Transaction transaction = null;

    // Se abre la sesión y la transacción
    try (Session session = factory.openSession()) {
      transaction = session.beginTransaction();

      // Se persiste la información y se obtiene el ID del curso
      session.persist(course);
      Integer id = (Integer) session.getIdentifier(course);

      transaction.commit();

      return id;

    } catch (HibernateException e) {

      if (transaction != null) {
        transaction.rollback();
      }

      // Se lanza otra excepción personalizada
      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Obtiene una lista con todos los cursos almacenados en la DB
   * @return List(Course)
   * @throws PersistenceException En caso de que exista un error durante el proceso de obtención de cursos
   */
  public List<Course> getAllCourse() throws PersistenceException {

    // Se crea la sesión y lanza la consulta
    try (Session session = factory.openSession()) {
      return session.createQuery("FROM " + DbConstants.COURSE_TABLE, app.entity.Course.class).list();

    } catch (HibernateException e) {

      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Método que actualiza un curso de la DB
   * @param Course Curso a actualizar
   * @throws PersistenceException En caso de que exista un error durante el proceso de actualización de cursos o de que no
   *                              exista previamente
   */
  public void updateCourse(Course course) throws PersistenceException {

    Transaction transaction = null;

    try (Session session = factory.openSession()) {

      transaction = session.beginTransaction();

      // Obtiene el curso almacenado con el ID pasado por parámetro
      Course extractedCourse = session.get(Course.class, course.getId());

      // Si no es null, actualiza el registro
      if (extractedCourse != null) {
        session.merge(course);
        transaction.commit();

      } else {
        throw new PersistenceException("No existe la dirección que se está intentando actualizar.");
      }

    } catch (HibernateException e) {

      if (transaction != null) {
        transaction.rollback();
      }

      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Elimina un curso dado su ID
   * @param courseId ID del curso
   * @throws PersistenceException En caso de que exista un error durante el proceso de eliminación de cursos
   */
  public void deleteCourse(Integer courseId) throws PersistenceException {

    Transaction transaction = null;

    try (Session session = factory.openSession()) {
      transaction = session.beginTransaction();

      // Se obtiene la dirección dado el ID por parámetro y se elimina
      Course course = session.get(Course.class, courseId);
      session.remove(course);

      transaction.commit();

    } catch (HibernateException e) {

      if (transaction != null) {
        transaction.rollback();
      }

      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Obtiene un curso dado el nombre, instituto y año de inicio
   * @param courseName         Nombre del curso
   * @param courseSchool       Centro escolar que imparte el curso
   * @param courseStartingYear Año de comienzo del curso
   * @return Course - Será null si no se encuentra
   * @throws PersistenceException En caso de que exista un error durante el proceso de obtención del curso
   */
  public Course findByStreetAndCity(String courseName, String courseSchool, String courseStartingYear)
      throws PersistenceException {

    try (Session session = factory.openSession()) {

      // Se crea la consulta y se pasan los parámetros
      Query<Course> query = session.createQuery(String
          .format("FROM %s WHERE name = :nm AND school = :schl AND startingYear = :strtngyr", DbConstants.COURSE_TABLE),
          app.entity.Course.class);

      query.setParameter("nm", courseName);
      query.setParameter("schl", courseSchool);
      query.setParameter("strtngyr", courseStartingYear);

      // Se obtiene la lista de cursos con los parámetros dados
      List<Course> courseList = query.list();

      // Debería existir sólo un resultado como mucho. Si hay más, hay un fallo en el sistema de guardado
      Course resultCourse = null;

      if (courseList.size() == 1) {
        resultCourse = courseList.get(0);

      } else if (!courseList.isEmpty()) {
        throw new PersistenceException(
            "Se ha obtenido más de un curso con el mismo nombre, instituto y año de comienzo. Es necesario revisar el proceso de guardado");
      }

      return resultCourse;

    } catch (HibernateException e) {
      throw new PersistenceException(e.getMessage());
    }
  }

}

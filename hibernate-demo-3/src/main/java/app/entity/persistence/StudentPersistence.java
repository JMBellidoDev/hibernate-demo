
package app.entity.persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import app.entity.Student;
import app.entity.constants.DbConstants;
import app.entity.persistence.exceptions.PersistenceException;

/** Sistema de persistencia de estudiantes */
public class StudentPersistence {

  /** Factoría de sesiones */
  private SessionFactory factory;

  /**
   * Constructor de la clase. Inicializa la factoría de sesiones
   * @throws ExceptionInInitializerError En caso de que exista un error durante la generación de la factoría de sesiones
   */
  public StudentPersistence() {

    try {
      factory = new Configuration().configure().addAnnotatedClass(app.entity.Student.class)
          .addAnnotatedClass(app.entity.Address.class).addAnnotatedClass(app.entity.Course.class)
          .addAnnotatedClass(app.entity.PhoneNumber.class).buildSessionFactory();

    } catch (Exception ex) {
      throw new ExceptionInInitializerError("Error al crear un objeto de la clase SessionFactory");
    }
  }

  /**
   * Almacena un nuevo estudiante en la DB
   * @param student Estudiante
   * @return Integer - ID generado del nuevo estudiante
   * @throws PersistenceException En caso de que exista un error durante el proceso de almacenamiento del estudiante
   */
  public Integer saveStudent(Student student) throws PersistenceException {

    Transaction transaction = null;

    // Se abre sesión y la transacción
    try (Session session = factory.openSession()) {
      transaction = session.beginTransaction();

      // Se almacena el estudiante y se obtiene su ID generado
      session.persist(student);
      Integer studentId = (Integer) session.getIdentifier(student);

      transaction.commit();
      return studentId;

    } catch (HibernateException e) {

      if (transaction != null) {
        transaction.rollback();
      }

      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Obtiene una lista con todos los estudiantes almacenados
   * @return List(Student)
   * @throws PersistenceException En caso de que exista un error durante el proceso de obtención de los estudiantes
   */
  public List<Student> getAllStudent() throws PersistenceException {

    // Se abre la sesión y se lanza la consulta
    try (Session session = factory.openSession()) {

      return session.createQuery("FROM " + DbConstants.STUDENT_TABLE, app.entity.Student.class).list();

    } catch (HibernateException e) {
      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Actualiza un estudiante de la DB
   * @param student Estudiante a actualizar
   * @throws PersistenceException En caso de que exista un error durante el proceso de actualización del estudiante o de que
   *                              no exista previamente
   */
  public void updateStudent(Student student) throws PersistenceException {

    Transaction transaction = null;

    // Se abre la sesión y la transacción
    try (Session session = factory.openSession()) {
      transaction = session.beginTransaction();

      // Se obtiene el número almacenado y, si no es null, se actualiza
      Student extractedStudent = session.get(app.entity.Student.class, student.getId());

      if (extractedStudent != null) {
        session.merge(student);
        transaction.commit();

      } else {
        throw new PersistenceException("No existe el estudiante que se está intentando actualizar.");
      }

    } catch (HibernateException e) {

      if (transaction != null) {
        transaction.rollback();
      }

      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Elimina un estudiante dado su ID
   * @param studentId ID del estudiante
   * @throws PersistenceException En caso de que exista un error durante el proceso de eliminación del estudiante
   */
  public void deleteStudent(Integer studentId) throws PersistenceException {

    Transaction transaction = null;

    // Se abre sesión y transacción
    try (Session session = factory.openSession()) {
      transaction = session.beginTransaction();

      // Se obtiene el número de teléfono dado el ID por parámetro y se elimina
      Student student = session.get(Student.class, studentId);
      session.remove(student);

      transaction.commit();

    } catch (HibernateException e) {

      if (transaction != null) {
        transaction.rollback();
      }

      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Obtiene un estudiante dado su dni, o null si no se encuentra ninguno que coincida con los parámetros aportados
   * @param dni DNI
   * @return Student - Será null si no se encuentra
   * @throws PersistenceException En caso de que exista un error durante el proceso de obtención del estudiante
   */
  public Student findByDni(String dni) throws PersistenceException {

    try (Session session = factory.openSession()) {

      // Se crea la consulta y se pasan los parámetros
      Query<Student> query = session.createQuery(String.format("FROM %s WHERE dni = :num", DbConstants.STUDENT_TABLE),
          app.entity.Student.class);

      query.setParameter("num", dni);

      // Se obtiene la lista de ciudades con la descripción y ciudad dados
      List<Student> studentList = query.list();

      // Debería existir sólo un resultado como mucho. Si hay más, hay un fallo en el sistema de guardado
      Student student = null;

      if (studentList.size() == 1) {
        student = studentList.get(0);

      } else if (!studentList.isEmpty()) {

        throw new PersistenceException(
            "Se ha obtenido más de un dato con el mismo DNI. Es necesario revisar el proceso de guardado");
      }
      return student;

    } catch (HibernateException e) {

      throw new PersistenceException(e.getMessage());
    }
  }

}

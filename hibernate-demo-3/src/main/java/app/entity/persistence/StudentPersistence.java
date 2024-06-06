
package app.entity.persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import app.entity.Student;
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
   * Almacena / Modifica un estudiante en la DB
   * @param student Estudiante del sistema. Será almacenado si no dispone de id, o actualizado en caso contrario
   * @return Integer - ID generado del nuevo estudiante
   * @throws PersistenceException En caso de que exista un error durante el proceso de almacenamiento del estudiante
   */
  public Integer saveOrUpdateStudent(Student student) throws PersistenceException {

    Session session = factory.openSession();
    Transaction transaction = null;

    // Se abre sesión y la transacción
    try {
      transaction = session.beginTransaction();

      // Se almacena el estudiante y se obtiene su ID generado
      Student mergedStudent = session.merge(student);
      Integer studentId = mergedStudent.getId();

      transaction.commit();
      return studentId;

    } catch (Exception e) {

      if (transaction != null) {
        transaction.rollback();
      }
      throw new PersistenceException(e.getMessage());

    } finally {
      session.close();
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

      return session.createQuery("FROM Student", app.entity.Student.class).list();

    } catch (Exception e) {
      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Elimina un estudiante dado su ID
   * @param dni DNI del estudiante
   * @throws PersistenceException En caso de que exista un error durante el proceso de eliminación del estudiante
   */
  public void deleteStudent(String dni) throws PersistenceException {

    // Se abre sesión y transacción
    Session session = factory.openSession();
    Transaction transaction = null;

    try {
      transaction = session.beginTransaction();

      // Se obtiene el número de teléfono dado el ID por parámetro y se elimina
      Student student = findByDni(dni);
      session.remove(student);

      transaction.commit();

    } catch (Exception e) {

      if (transaction != null) {
        transaction.rollback();
      }
      throw new PersistenceException(e.getMessage());

    } finally {
      session.close();
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
      Query<Student> query = session.createQuery("FROM Student WHERE dni = :num", app.entity.Student.class);

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

    } catch (Exception e) {

      throw new PersistenceException(e.getMessage());
    }
  }

}

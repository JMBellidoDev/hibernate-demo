
package app;

import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.entity.Employee;

/** Clase ejemplo que maneja empleados */
public class ManageEmployee {

  /** Logger */
  private static final Logger LOGGER = LoggerFactory.getLogger(app.ManageEmployee.class);

  /** Factoría de sesiones */
  private static SessionFactory factory;

  /**
   * Método main
   * @param args Argumentos
   */
  public static void main(String[] args) {

    // Se intenta crear la factoría de sesiones añadiendo las clases con anotaciones
    try {
      factory = new Configuration().configure().addAnnotatedClass(app.entity.Employee.class).buildSessionFactory();

    } catch (Exception ex) {

      String msg = String.format("Failed to create sessionFactory object.%n%s%n", ex);
      LOGGER.debug(msg);

      throw new ExceptionInInitializerError("Error al crear un objeto de la clase SessionFactory");
    }

    // Se crea un objeto de la clase y varios empleados
    ManageEmployee manageEmployee = new ManageEmployee();

    Integer empID1 = manageEmployee.addEmployee("Zara", "Ali", 1000);
    Integer empID2 = manageEmployee.addEmployee("Daisy", "Das", 5000);

    JOptionPane.showMessageDialog(null,
        String.format("Los ID de los empleados son: empID1: %d, empID2: %d", empID1, empID2));

    // Listado de los empleados
    manageEmployee.listEmployees();

    // Actualización del empleado con ID 1
    manageEmployee.updateEmployee(empID1, 5000);

    // Eliminación del empleado con ID 2
    manageEmployee.deleteEmployee(empID2);

    // Listado de los empleados
    manageEmployee.listEmployees();
  }

  /**
   * Método que crea un nuevo empleado y devuelve su ID
   * @param fname  Nombre de pila
   * @param lname  Apellidos
   * @param salary Salario
   * @return Integer - ID del empleado
   */
  public Integer addEmployee(String fname, String lname, int salary) {

    // Se crea la sesión y las variables necesarias
    Session session = factory.openSession();
    Transaction transaction = null;
    Integer employeeID = null;

    // Se inicia la transacción, se crea el empleado y se persiste la información
    try {
      transaction = session.beginTransaction();

      Employee employee = new Employee(fname, lname, salary);
      session.persist(employee);

      employeeID = (Integer) session.getIdentifier(employee);

      // Commit si se realiza la operación correctamente
      transaction.commit();

    } catch (HibernateException e) {

      // Se realiza el rollback si la transacción no es nula
      if (transaction != null)
        transaction.rollback();

      String msg = String.format("Fallo al añadir un nuevo empleado.%n%s%n", e.getMessage());
      LOGGER.debug(msg);

    } finally {
      // Se cierra la sesión
      session.close();
    }

    return employeeID;
  }

  /** Lee y muestra todos los empleados */
  public void listEmployees() {

    // Sesión y transacción
    Session session = factory.openSession();
    Transaction transaction = null;

    // Se crea y lanza la consulta
    try {

      transaction = session.beginTransaction();
      List<Employee> employees = session.createQuery("FROM Employee", Employee.class).list();

      // Se itera sobre el resultado y se muestra al usuario
      StringBuilder stringBuilder = new StringBuilder("Los empleados son:\n");

      for (Iterator<Employee> iterator = employees.iterator(); iterator.hasNext();) {

        Employee employee = iterator.next();
        stringBuilder.append(String.format("First name: %s, Last name: %s, Salary: %d%n", employee.getFirstName(),
            employee.getLastName(), employee.getSalary()));

      }

      JOptionPane.showMessageDialog(null, stringBuilder.toString());
      transaction.commit();
    } catch (HibernateException e) {

      if (transaction != null)
        transaction.rollback();

      String msg = String.format("Fallo al listar los empleados.%n%s%n", e.getMessage());
      LOGGER.debug(msg);

    } finally {
      session.close();
    }
  }

  /**
   * Método que actualiza el salario de un empleado dado su ID
   * @param employeeID ID del empleado
   * @param salary     Nuevo salario
   */
  public void updateEmployee(Integer employeeID, int salary) {
    Session session = factory.openSession();
    Transaction transaction = null;

    try {
      transaction = session.beginTransaction();
      Employee employee = session.get(Employee.class, employeeID);
      employee.setSalary(salary);

      session.merge(employee);

      transaction.commit();
    } catch (HibernateException e) {

      if (transaction != null)
        transaction.rollback();

      String msg = String.format("Fallo al actualizar el empleado.%n%s%n", e.getMessage());
      LOGGER.debug(msg);

    } finally {
      session.close();
    }
  }

  /**
   * Método para eliminar un empleado dado su ID
   * @param employeeID ID del empleado
   */
  public void deleteEmployee(Integer employeeID) {
    Session session = factory.openSession();
    Transaction transaction = null;

    try {
      transaction = session.beginTransaction();
      Employee employee = session.get(Employee.class, employeeID);

      session.remove(employee);

      transaction.commit();

    } catch (HibernateException e) {

      if (transaction != null)
        transaction.rollback();

      String msg = String.format("Fallo al eliminar al empleado.%n%s%n", e.getMessage());
      LOGGER.debug(msg);

    } finally {
      session.close();
    }
  }
}

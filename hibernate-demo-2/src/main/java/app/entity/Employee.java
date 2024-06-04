
package app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Clase Empleado */
@Entity
@Table(name = "EMPLOYEE")
public class Employee {

  /** ID */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  /** Nombre de pila */
  @Column(name = "first_name")
  private String firstName;

  /** Apellidos */
  @Column(name = "last_name")
  private String lastName;

  /** Salario */
  @Column(name = "salary")
  private int salary;

  /** Constructor por defecto */
  public Employee() {
  }

  /**
   * Constructor con todos los parámetros
   * @param fname  Nombre de pila
   * @param lname  Apellidos
   * @param salary Salario
   */
  public Employee(String fname, String lname, int salary) {
    this.firstName = fname;
    this.lastName = lname;
    this.salary = salary;
  }

  /**
   * Getter id
   * @return int - id
   */
  public int getId() {
    return id;
  }

  /**
   * Setter id
   * @param id ID
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Getter firstName
   * @return String - firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Setter firstName
   * @param firstName firstName
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Getter lastName
   * @return String - lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Setter lastName
   * @param lastName lastName
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Getter salary
   * @return int - salary
   */
  public int getSalary() {
    return salary;
  }

  /**
   * Setter - salary
   * @param salary Salary
   */
  public void setSalary(int salary) {
    this.salary = salary;
  }
}

package app.entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/** Estudiante de un centro educativo */
@Entity
@Table(name = "Student")
public class Student {

  /** ID */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  /** Dirección completa del estudiante */
  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "address_id")
  private Address address;

  /** DNI */
  @Column(name = "dni")
  private String dni;

  /** Nombre completo */
  @Column(name = "name")
  private String name;

  /** Fecha de nacimiento */
  @Column(name = "birthdate")
  private LocalDate birthdate;

  /** Número de teléfono */
  @Column(name = "phoneNumber")
  private String phoneNumber;

  /** Constructor por defecto */
  public Student() {
  }

  /**
   * Constructor con parámetros
   * @param address     Dirección
   * @param dni         DNI
   * @param name        Nombre completo
   * @param birthdate   Fecha de nacimiento
   * @param phoneNumber Número de teléfono
   */
  public Student(Address address, String dni, String name, LocalDate birthdate, String phoneNumber) {
    super();
    this.address = address;
    this.dni = dni;
    this.name = name;
    this.birthdate = birthdate;
    this.phoneNumber = phoneNumber;
  }

  /**
   * Getter - id
   * @return long - id
   */
  public long getId() {
    return id;
  }

  /**
   * Setter - id
   * @param id ID
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Getter - dni
   * @return String - dni
   */
  public String getDni() {
    return dni;
  }

  /**
   * Setter - dni
   * @param dni DNI
   */
  public void setDni(String dni) {
    this.dni = dni;
  }

  /**
   * Getter - name
   * @return String - name
   */
  public String getName() {
    return name;
  }

  /**
   * Setter - name
   * @param name Nombre completo
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getter - birthdate
   * @return LocalDate - birthdate
   */
  public LocalDate getBirthdate() {
    return birthdate;
  }

  /**
   * Setter - birthdate
   * @param birthdate Fecha de nacimiento
   */
  public void setBirthdate(LocalDate birthdate) {
    this.birthdate = birthdate;
  }

  /**
   * Getter - phoneNumber
   * @return String - phoneNumber
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Setter - phoneNumber
   * @param phoneNumber Número de teléfono
   */
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

}

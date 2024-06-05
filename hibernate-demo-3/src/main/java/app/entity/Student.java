
package app.entity;

import java.time.LocalDate;
import java.util.List;

import app.entity.constants.DbConstants;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Estudiante de un centro educativo */
@Entity
@Table(name = DbConstants.STUDENT_TABLE)
@Data
@NoArgsConstructor
public class Student {

  /** ID */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = DbConstants.STUDENT_ID, columnDefinition = "INT")
  private Integer id;

  /** DNI */
  @Column(name = DbConstants.STUDENT_DNI, unique = true, columnDefinition = "CHAR(10)")
  private String dni;

  /** Nombre completo */
  @Column(name = DbConstants.STUDENT_NAME, columnDefinition = "VARCHAR(100)")
  private String name;

  /** Fecha de nacimiento */
  @Column(name = DbConstants.STUDENT_BIRTHDATE, columnDefinition = "DATE")
  private LocalDate birthdate;

  /** Dirección completa del estudiante */
  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = DbConstants.STUDENT_ADDRESS_ID, referencedColumnName = DbConstants.ADDRESS_ID, columnDefinition = "INT")
  private Address address;

  /** Números de teléfono */
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = DbConstants.STUDENT_PHONE_NUMBER_TABLE, joinColumns = @JoinColumn(name = DbConstants.STUDENT_PHONE_NUMBER_STUDENT_ID), inverseJoinColumns = @JoinColumn(name = DbConstants.STUDENT_PHONE_NUMBER_PHONE_ID))
  private List<PhoneNumber> phoneNumbers;

  /** Curso que realiza el estudiante */
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = DbConstants.STUDENT_COURSE_ID, referencedColumnName = DbConstants.COURSE_ID)
  private Course course;

  /**
   * Constructor con parámetros
   * @param dni          DNI
   * @param name         Nombre completo
   * @param birthdate    Fecha de nacimiento
   * @param address      Dirección
   * @param phoneNumbers Números de teléfono
   * @param course       Curso que realiza
   */
  public Student(String dni, String name, LocalDate birthdate, Address address, List<PhoneNumber> phoneNumbers,
      Course course) {

    this.dni = dni;
    this.name = name;
    this.birthdate = birthdate;
    this.address = address;
    this.phoneNumbers = phoneNumbers;
    this.course = course;
  }

}

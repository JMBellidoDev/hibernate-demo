
package app.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
  @Column(name = DbConstants.STUDENT_DNI, unique = true, columnDefinition = "CHAR(9)", nullable = false)
  private String dni;

  /** Nombre completo */
  @Column(name = DbConstants.STUDENT_NAME, columnDefinition = "VARCHAR(100)", nullable = false)
  private String name;

  /** Fecha de nacimiento */
  @Column(name = DbConstants.STUDENT_BIRTHDATE, columnDefinition = "DATE", nullable = false)
  private LocalDate birthdate;

  /** Dirección completa del estudiante */
  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)

  @JoinColumn(name = DbConstants.STUDENT_ADDRESS_ID, referencedColumnName = DbConstants.ADDRESS_ID, columnDefinition = "INT")
  private Address address;

  /** Números de teléfono */
  @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
  @JoinTable(name = DbConstants.STUDENT_PHONE_NUMBER_TABLE, joinColumns = @JoinColumn(name = DbConstants.STUDENT_PHONE_NUMBER_STUDENT_ID), inverseJoinColumns = @JoinColumn(name = DbConstants.STUDENT_PHONE_NUMBER_PHONE_ID))
  private List<PhoneNumber> phoneNumbers;

  /** Curso que realiza el estudiante */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = DbConstants.STUDENT_COURSE_ID, referencedColumnName = DbConstants.COURSE_ID)
  private Course course;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;

    Student other = (Student) obj;
    return Objects.equals(birthdate, other.birthdate) && Objects.equals(dni, other.dni) && Objects.equals(id, other.id)
        && Objects.equals(name, other.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(birthdate, dni, id, name);
  }

  @Override
  public String toString() {
    String birthdateStr = birthdate.toString();

    return String.format("ID: %d, DNI: %s, Name: %s. Birthdate: %s, Address: %s, Phone Numbers: %s, Course: %s", id, dni,
        name, birthdateStr, address, phoneNumbers, course);
  }

}

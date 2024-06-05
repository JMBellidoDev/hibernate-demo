
package app.entity;

import java.util.List;

import app.entity.constants.DbConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Número de teléfono asociado a un estudiante */
@Entity
@Table(name = DbConstants.PHONE_NUMBER_TABLE)
@Data
@NoArgsConstructor
public class PhoneNumber {

  /** ID */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = DbConstants.PHONE_NUMBER_ID)
  private Integer id;

  /** Número de teléfono asociado */
  @Column(name = DbConstants.PHONE_NUMBER_NUM, columnDefinition = "CHAR(9)", unique = true, nullable = false)
  private String number;

  /** Estudiantes que tienen relacionado este número de teléfono */
  @ManyToMany(mappedBy = "phoneNumbers")
  private List<Student> students;

  /**
   * Constructor con parámetros
   * @param number   Número de teléfono
   * @param students Estudiantes al que está asignado
   */
  public PhoneNumber(String number, List<Student> students) {
    this.number = number;
    this.students = students;
  }

  @Override
  public String toString() {
    return String.format("ID: %d, Phone Number: %s, Students: %s", id, number, students);
  }

}

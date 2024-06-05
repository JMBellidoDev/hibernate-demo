
package app.entity;

import app.entity.constants.DbConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Curso que puede cursar un alumno */
@Entity
@Table(name = DbConstants.COURSE_TABLE)
@Data
@NoArgsConstructor
public class Course {

  /** ID del curso */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = DbConstants.COURSE_ID)
  private Integer id;

  /** Nombre del curso */
  @Column(name = DbConstants.COURSE_NAME, columnDefinition = "VARCHAR(80)")
  private String name;

  /** Centro escolar que imparte el curso */
  @Column(name = DbConstants.COURSE_SCHOOL, columnDefinition = "VARCHAR(100)")
  private String school;

  /** Año de comienzo del curso */
  @Column(name = DbConstants.COURSE_STARTING_YEAR)
  private int startingYear;

  /**
   * Constructor con parámetros
   * @param name         Nombre
   * @param school       Centro escolar
   * @param startingYear Año de comienzo del curso
   */
  public Course(String name, String school, int startingYear) {
    this.name = name;
    this.school = school;
    this.startingYear = startingYear;
  }

}

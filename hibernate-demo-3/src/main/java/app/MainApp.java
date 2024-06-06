
package app;

import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.entity.Student;
import app.entity.persistence.AddressPersistence;
import app.entity.persistence.CoursePersistence;
import app.entity.persistence.PhoneNumberPersistence;
import app.entity.persistence.StudentPersistence;
import app.entity.persistence.exceptions.PersistenceException;
import app.service.CourseService;
import app.service.StudentService;

/** Aplicación principal */
public class MainApp {

  /** Logger */
  private static final Logger LOGGER = LoggerFactory.getLogger(app.MainApp.class);

  /** DNI del estudiante 1 */
  private static final String DNI_STUDENT1 = "19024608C";

  /** DNI del estudiante 2 */
  private static final String DNI_STUDENT2 = "52888570R";

  /** Nombre del estudiante 1 */
  private static final String NAME_STUDENT1 = "Alberto Garcia";

  /** Nombre del estudiante 2 */
  private static final String NAME_STUDENT2 = "Juan Jose Sanchez";

  /** Nombre del primer curso */
  private static final String COURSE_NAME1 = "Desarrollo en Aplicaciones Web";

  /** Nombre del segundo curso */
  private static final String COURSE_NAME2 = "Desarrollo en Aplicaciones Multiplataforma";

  /** Nombre del primer centro escolar */
  private static final String SCHOOL1 = "IES Pablo Picasso";

  /** Nombre del segundo centro escolar */
  private static final String SCHOOL2 = "CES San Jose";

  public static void main(String[] args) {

    try {

      // Sistemas de persistencia
      StudentPersistence stPersistence = new StudentPersistence();
      AddressPersistence aPersistence = new AddressPersistence();
      PhoneNumberPersistence phPersistence = new PhoneNumberPersistence();
      CoursePersistence cPersistence = new CoursePersistence();

      // Servicios
      StudentService studentService = new StudentService(stPersistence, cPersistence, phPersistence, aPersistence);
      CourseService courseService = new CourseService(cPersistence);

      // Creación de estudiantes
      studentService.saveOrUpdateStudent(DNI_STUDENT1, NAME_STUDENT1, LocalDate.of(1992, 01, 02));
      studentService.saveOrUpdateStudent(DNI_STUDENT2, NAME_STUDENT2, LocalDate.of(1982, 12, 04));

      // Creación de las direcciones
      studentService.saveOrUpdateAddress(DNI_STUDENT1, "Plaza Vieja nº 123", "Málaga", "29010");
      studentService.saveOrUpdateAddress(DNI_STUDENT2, "Calle inventada nº 21", "Almeria", "04019");

      // Modificación de la segunda dirección
      studentService.saveOrUpdateAddress(DNI_STUDENT2, "Calle imaginacion nº 11", "Almeria", "04019");

      // Números de teléfono del estudiante 1 -> se añaden 2, se elimina 1
      studentService.addPhoneNumber(DNI_STUDENT1, "678419954");
      studentService.addPhoneNumber(DNI_STUDENT1, "798749945");

      studentService.deletePhoneNumber(DNI_STUDENT1, "798749945");

      // Números de teléfono del estudiante 2 -> se reutiliza el primer número de teléfono, se añade uno nuevo
      studentService.addPhoneNumber(DNI_STUDENT2, "678419954");
      studentService.addPhoneNumber(DNI_STUDENT2, "831294821");

      // Se modifica el estudiante 1 (fecha de nacimiento)
      studentService.saveOrUpdateStudent(DNI_STUDENT1, NAME_STUDENT1, LocalDate.of(1999, 01, 02));

      // Se listan los estudiantes en este punto
      showStudents(studentService);

      // Cursos para cada estudiante (1 adicional para eliminar)
      courseService.save(COURSE_NAME1, SCHOOL1, 2026);
      courseService.save(COURSE_NAME2, SCHOOL2, 2025);
      courseService.save("Administración de Sistemas Informáticos y Redes", "IES Belen", 2026);

      courseService.deleteCourse("Administración de Sistemas Informáticos y Redes", "IES Belen", 2026);

      studentService.setCourse(DNI_STUDENT1, COURSE_NAME1, SCHOOL1, 2026);
      studentService.setCourse(DNI_STUDENT2, COURSE_NAME2, SCHOOL2, 2025);

      // Se elimina el estudiante 1
      studentService.deleteStudent(DNI_STUDENT1);

      // Se vuelven a listar los estudiantes
      showStudents(studentService);

    } catch (ExceptionInInitializerError e) {
      JOptionPane.showMessageDialog(null, "Error durante la creación de los sistemas de persistencia" + e.getMessage());
      LOGGER.debug(e.getMessage());

    } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(null,
          "Alguno de los atributos que se ha intentado usar no es válido o no existe en la DB");
      LOGGER.debug(e.getMessage());

    } catch (PersistenceException e) {
      JOptionPane.showMessageDialog(null, "Error durante el almacenamiento");
      LOGGER.debug(e.getMessage());

    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado");
      LOGGER.debug(e.getMessage());
    }

  }

  /**
   * Muestra todos los estudiantes almacenados junto con sus atributos
   * @param studentService Servicio de gestión de estudiantes
   * @throws PersistenceException En caso de problema con el sistema de almacenamiento
   */
  private static void showStudents(StudentService studentService) throws PersistenceException {

    List<Student> students = studentService.getAll();
    StringBuilder sb = new StringBuilder("Estudiantes:\n");
    for (Student st : students) {
      sb.append(st + "\n");
    }

    JOptionPane.showMessageDialog(null, sb.toString());
  }

}

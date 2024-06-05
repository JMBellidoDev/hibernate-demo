
package app;

import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;

import app.entity.Address;
import app.entity.Course;
import app.entity.PhoneNumber;
import app.entity.Student;
import app.entity.persistence.StudentPersistence;
import app.entity.persistence.exceptions.PersistenceException;

/** Aplicación principal */
public class MainApp {

  public static void main(String[] args) {

    Address address = new Address("Plaza Nueva nº 123", "Málaga", "29010");

    PhoneNumber number1 = new PhoneNumber("678987654", null);

    PhoneNumber number2 = new PhoneNumber("698742345", null);

    Course course = new Course("Desarrollo en Aplicaciones Multiplataforma", "IES Pablo Picasso", 2025);

    Student student = new Student("29482182Y", "Juan Alberto García", LocalDate.of(1993, 10, 26), address,
        List.of(number1, number2), course);

    number1.setStudents(List.of(student));
    number2.setStudents(List.of(student));

    try {
      StudentPersistence persistence = new StudentPersistence();

      persistence.saveStudent(student);

    } catch (PersistenceException e) {
      JOptionPane.showMessageDialog(null, "Error durante el almacenamiento de la dirección");
    }

  }

}

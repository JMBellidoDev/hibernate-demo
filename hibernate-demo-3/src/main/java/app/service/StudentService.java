
package app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import app.entity.Address;
import app.entity.Course;
import app.entity.PhoneNumber;
import app.entity.Student;
import app.entity.persistence.AddressPersistence;
import app.entity.persistence.CoursePersistence;
import app.entity.persistence.PhoneNumberPersistence;
import app.entity.persistence.StudentPersistence;
import app.entity.persistence.exceptions.PersistenceException;
import app.service.validation.AddressValidation;
import app.service.validation.PhoneNumberValidation;
import app.service.validation.StudentValidation;

/** Servicio de gestión de estudiantes */
public class StudentService {

  /** Sistema de persistencia de estudiantes */
  private StudentPersistence stPersistence;

  /** Sistema de persistencia de cursos */
  private CoursePersistence cPersistence;

  /** Sistema de persistencia de números de teléfono */
  private PhoneNumberPersistence phPersistence;

  /** Sistema de persistencia de las direcciones */
  private AddressPersistence aPersistence;

  /**
   * Constructor
   * @param stPersistence Sistema de persistencia de estudiantes. No null
   * @param cPersistence  Sistema de persistencia de cursos. No null
   * @param phPersistence Sistema de persistencia de números de teléfono. No null
   * @param aPersistence  Sistema de persistencia de direcciones. No null
   * @throws NullPointerException En caso de que el sistema de persistencia sea null
   */
  public StudentService(StudentPersistence stPersistence, CoursePersistence cPersistence,
      PhoneNumberPersistence phPersistence, AddressPersistence aPersistence) {

    if (stPersistence != null && cPersistence != null && phPersistence != null && aPersistence != null) {
      this.stPersistence = stPersistence;
      this.cPersistence = cPersistence;
      this.phPersistence = phPersistence;
      this.aPersistence = aPersistence;

    } else {
      throw new NullPointerException();
    }
  }

  /**
   * Almacena o actualiza un estudiante junto con toda la información relacionada, previa validación de atributos
   * @param dni       DNI del estudiante
   * @param name      Nombre del estudiante
   * @param birthdate Fecha de nacimiento del estudiante
   * @return Integer
   * @throws PersistenceException     En caso de que ocurra un error durante el acceso a datos
   * @throws IllegalArgumentException Si alguno de los atributos de Student no son correctos
   */
  public Integer saveOrUpdateStudent(String dni, String name, LocalDate birthdate) throws PersistenceException {

    // Se verifican todos los atributos que se asocian con Student
    if (StudentValidation.isValidDni(dni) && StudentValidation.isValidName(name)
        && StudentValidation.isValidBirthdate(birthdate)) {

      // Se intenta buscar el estudiante por su DNI. Si no se encuentra, se crea uno nuevo
      Student student = stPersistence.findByDni(dni);

      if (student == null) {
        student = new Student();
        student.setDni(dni);
      }

      // Se establecen los atributos
      student.setName(name);
      student.setBirthdate(birthdate);

      return stPersistence.saveOrUpdateStudent(student);

    } else {
      throw new IllegalArgumentException("Alguno de los atributos del estudiante no es correcto");
    }
  }

  /**
   * Almacena o actualiza una dirección sobre un estudiante junto con toda la información relacionada, previa validación de
   * atributos
   * @param dni        DNI del estudiante
   * @param street     Descripción completa de la dirección
   * @param city       Ciudad
   * @param postalCode Código postal
   * @return Integer - ID de la dirección
   * @throws PersistenceException     En caso de que ocurra un error durante el acceso a los datos
   * @throws IllegalArgumentException Si alguno de los atributos de Student no son correctos
   */
  public void saveOrUpdateAddress(String dni, String street, String city, String postalCode) throws PersistenceException {

    // Se verifica que existe el estudiante sobre el que relacionar la dirección
    Student student = stPersistence.findByDni(dni);

    if (student != null) {

      // Se verifican los datos de la dirección
      if (AddressValidation.isValidStreetAddress(street) && AddressValidation.isValidCity(city)
          && AddressValidation.isValidPostalCode(postalCode)) {

        // Se comprueba si ya existía previamente y sino se crea
        Address address = aPersistence.findByStreetAndCity(street, city);

        if (address == null) {
          address = new Address();
        }

        // Se modifican los atributos
        address.setCity(city);
        address.setStreetAddress(street);
        address.setPostalCode(postalCode);

        // Se persiste la dirección
        student.setAddress(address);
        stPersistence.saveOrUpdateStudent(student);

      } else {
        throw new IllegalArgumentException("Los datos de la dirección no son válidos. No se crearán.");
      }

    } else {
      throw new IllegalArgumentException("El estudiante no existe, por lo que no se creará la dirección");
    }
  }

  /**
   * Obtiene una lista con todos los estudiantes junto con su información relacionada
   * @return List(Student)
   * @throws PersistenceException En caso de que ocurra un error durante el acceso a los datos
   */
  public List<Student> getAll() throws PersistenceException {
    return stPersistence.getAllStudent();

  }

  /**
   * Añade un nuevo número de teléfono al estudiante
   * @param dni    DNI del estudiante
   * @param number Nuevo número de teléfono
   * @throws PersistenceException     En caso de que ocurra un error durante el acceso a los datos
   * @throws IllegalArgumentException En caso de que los parámetros no sean válidos o no concuerden con valores reales
   */
  public void addPhoneNumber(String dni, String number) throws PersistenceException {

    // Se comprueba que el número de teléfono es válido y que alumno existe
    if (PhoneNumberValidation.isValidNumber(number)) {
      Student student = stPersistence.findByDni(dni);

      if (student != null) {

        // Se comprueba si el número existe y ya está asignado a otro alumno
        PhoneNumber extractedPhoneNumber = phPersistence.findByNumber(number);

        // Sino, se inserta como nuevo teléfono
        if (extractedPhoneNumber == null) {

          List<Student> students = new ArrayList<>();
          students.add(student);

          PhoneNumber phoneNumber = new PhoneNumber(number, students);
          student.getPhoneNumbers().add(phoneNumber);

          // Si ya existe, se inserta el mismo
        } else {
          extractedPhoneNumber.getStudents().add(student);
          student.getPhoneNumbers().add(extractedPhoneNumber);
        }

        stPersistence.saveOrUpdateStudent(student);

      } else {
        throw new IllegalArgumentException(
            "El alumno sobre el que está intentando insertar un nuevo número de teléfono no existe");
      }

    } else {
      throw new IllegalArgumentException("El número de teléfono indicado no es válido");
    }
  }

  /**
   * Elimina un número de teléfono del estudiante. Si no está asociado al mismo estudiante o no existe, no se realiza ninguna
   * acción
   * @param dni    DNI
   * @param number Número de teléfono
   * @throws PersistenceException En caso de que alguno de los parámetros no sea válido o no concuerde con valores reales
   */
  public void deletePhoneNumber(String dni, String number) throws PersistenceException {

    Student student = stPersistence.findByDni(dni);
    PhoneNumber phoneNumber = phPersistence.findByNumber(number);

    // Si las entidades extraídas no son null y el estudiante tiene asignado el teléfono
    if (student != null && phoneNumber != null) {

      student.getPhoneNumbers().remove(phoneNumber);
      phoneNumber.getStudents().remove(student);

      stPersistence.saveOrUpdateStudent(student);
      phPersistence.saveOrUpdatePhoneNumber(phoneNumber);
    }

  }

  /**
   * Establece el curso para un alumno cuyo DNI es el dado
   * @param dni          DNI del estudiante
   * @param courseName   Nombre del curso
   * @param school       Centro escolar donde se imparte el curso
   * @param startingYear Año de comienzo del curso
   * @throws PersistenceException     En caso de que ocurra un error durante el acceso a los datos
   * @throws IllegalArgumentException En caso de que alguno de los parámetros no sea válido o no concuerde con valores reales
   */
  public void setCourse(String dni, String courseName, String school, int startingYear) throws PersistenceException {

    // Se obtiene el curso y el estudiante
    Course course = cPersistence.findByNameSchoolAndStartingYear(courseName, school, startingYear);
    Student student = stPersistence.findByDni(dni);

    // Si el curso existe, se asigna al estudiante. Sino, se lanza un error
    if (course != null && student != null) {

      student.setCourse(course);
      stPersistence.saveOrUpdateStudent(student);

    } else {
      throw new IllegalArgumentException("Los valores del alumno o del curso no corresponden con datos reales");
    }
  }

  /**
   * Elimina un estudiante dado su ID. Si no se encontrase ningún estudiante con el DNI dado, no se lanza ninguna excepción
   * @param dni DNI del estudiante
   * @throws PersistenceException En caso de que ocurra un error durante el acceso a los datos
   */
  public void deleteStudent(String dni) throws PersistenceException {

    Student student = stPersistence.findByDni(dni);

    if (student != null) {

      // Se elimina el estudiante dado su dni
      stPersistence.deleteStudent(dni);

    }
  }

}

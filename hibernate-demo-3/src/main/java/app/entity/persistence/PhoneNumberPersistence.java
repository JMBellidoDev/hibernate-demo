
package app.entity.persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import app.entity.PhoneNumber;
import app.entity.constants.DbConstants;
import app.entity.persistence.exceptions.PersistenceException;

/** Sistema de persistencia de números de teléfono */
public class PhoneNumberPersistence {

  /** Factoría de sesiones */
  private SessionFactory factory;

  /**
   * Constructor de la clase. Inicializa la factoría de sesiones
   * @throws ExceptionInInitializerError En caso de que exista un error durante la generación de la factoría de sesiones
   */
  public PhoneNumberPersistence() {

    try {
      factory = new Configuration().configure().addAnnotatedClass(app.entity.PhoneNumber.class).buildSessionFactory();

    } catch (Exception ex) {
      throw new ExceptionInInitializerError("Error al crear un objeto de la clase SessionFactory");
    }
  }

  /**
   * Almacena un nuevo número de teléfono en la DB
   * @param phoneNumber Número de teléfono
   * @return Integer - ID generado del nuevo número de teléfono
   * @throws PersistenceException En caso de que exista un error durante el proceso de almacenamiento del número de teléfono
   */
  public Integer savePhoneNumber(PhoneNumber phoneNumber) throws PersistenceException {

    Transaction transaction = null;

    // Se abre sesión y la transacción
    try (Session session = factory.openSession()) {
      transaction = session.beginTransaction();

      // Se almacena el número y se obtiene su ID generado
      session.persist(phoneNumber);
      Integer phoneNumberId = (Integer) session.getIdentifier(phoneNumber);

      transaction.commit();
      return phoneNumberId;

    } catch (HibernateException e) {

      if (transaction != null) {
        transaction.rollback();
      }

      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Obtiene una lista con todos los números de teléfono almacenados
   * @return List(PhoneNumber)
   * @throws PersistenceException En caso de que exista un error durante el proceso de obtención de números de teléfono
   */
  public List<PhoneNumber> getAllPhoneNumber() throws PersistenceException {

    // Se abre la sesión y se lanza la consulta
    try (Session session = factory.openSession()) {

      return session.createQuery("FROM " + DbConstants.PHONE_NUMBER_TABLE, app.entity.PhoneNumber.class).list();

    } catch (HibernateException e) {
      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Actualiza un número de teléfono de la DB
   * @param phoneNumber Número de teléfono a actualizar
   * @throws PersistenceException En caso de que exista un error durante el proceso de actualización del número de teléfono o
   *                              de que no exista previamente
   */
  public void updatePhoneNumber(PhoneNumber phoneNumber) throws PersistenceException {

    Transaction transaction = null;

    // Se abre la sesión y la transacción
    try (Session session = factory.openSession()) {
      transaction = session.beginTransaction();

      // Se obtiene el número almacenado y, si no es null, se actualiza
      PhoneNumber extractedPhoneNumber = session.get(app.entity.PhoneNumber.class, phoneNumber.getId());

      if (extractedPhoneNumber != null) {
        session.merge(phoneNumber);
        transaction.commit();

      } else {
        throw new PersistenceException("No existe el número de teléfono que se está intentando actualizar.");
      }

    } catch (HibernateException e) {

      if (transaction != null) {
        transaction.rollback();
      }

      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Elimina un número de teléfono dado su ID
   * @param phoneNumberId ID del número
   * @throws PersistenceException En caso de que exista un error durante el proceso de eliminación del número
   */
  public void deletePhoneNumber(Integer phoneNumberId) throws PersistenceException {

    Transaction transaction = null;

    // Se abre sesión y transacción
    try (Session session = factory.openSession()) {
      transaction = session.beginTransaction();

      // Se obtiene el número de teléfono dado el ID por parámetro y se elimina
      PhoneNumber phoneNumber = session.get(PhoneNumber.class, phoneNumberId);
      session.remove(phoneNumber);

      transaction.commit();

    } catch (HibernateException e) {

      if (transaction != null) {
        transaction.rollback();
      }

      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Obtiene un PhoneNumber dado su número, o null si no se encuentra ningún número de teléfono que coincida con los
   * parámetros aportados
   * @param number Número
   * @return PhoneNumber - Será null si no se encuentra
   * @throws PersistenceException En caso de que exista un error durante el proceso de obtención del número de teléfono
   */
  public PhoneNumber findByNumber(String number) throws PersistenceException {

    try (Session session = factory.openSession()) {

      // Se crea la consulta y se pasan los parámetros
      Query<PhoneNumber> query = session.createQuery(
          String.format("FROM %s WHERE number = :num", DbConstants.PHONE_NUMBER_TABLE), app.entity.PhoneNumber.class);

      query.setParameter("num", number);

      // Se obtiene la lista de ciudades con la descripción y ciudad dados
      List<PhoneNumber> phoneNumberList = query.list();

      // Debería existir sólo un resultado como mucho. Si hay más, hay un fallo en el sistema de guardado
      PhoneNumber phoneNumber = null;

      if (phoneNumberList.size() == 1) {
        phoneNumber = phoneNumberList.get(0);

      } else if (!phoneNumberList.isEmpty()) {

        throw new PersistenceException(
            "Se ha obtenido más de un dato con el mismo número de teléfono. Es necesario revisar el proceso de guardado");
      }
      return phoneNumber;

    } catch (HibernateException e) {

      throw new PersistenceException(e.getMessage());
    }
  }

}

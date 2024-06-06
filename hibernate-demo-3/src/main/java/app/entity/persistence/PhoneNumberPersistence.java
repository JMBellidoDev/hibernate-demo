
package app.entity.persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import app.entity.PhoneNumber;
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
      factory = new Configuration().configure().addAnnotatedClass(app.entity.PhoneNumber.class)
          .addAnnotatedClass(app.entity.Student.class).addAnnotatedClass(app.entity.Address.class)
          .addAnnotatedClass(app.entity.Course.class).buildSessionFactory();

    } catch (Exception ex) {
      throw new ExceptionInInitializerError("Error al crear un objeto de la clase SessionFactory");
    }
  }

  /**
   * Almacena / Modifica número de teléfono en la DB
   * @param phoneNumber Número de teléfono. Será almacenado si no dispone de id, o actualizado en caso contrario
   * @return Integer - ID generado del nuevo número de teléfono
   * @throws PersistenceException En caso de que exista un error durante el proceso de almacenamiento/modificación del número
   *                              de teléfono
   */
  public Integer saveOrUpdatePhoneNumber(PhoneNumber phoneNumber) throws PersistenceException {

    // Se abre sesión y la transacción
    Session session = factory.openSession();
    Transaction transaction = null;

    try {
      transaction = session.beginTransaction();

      // Se almacena el número y se obtiene su ID generado
      PhoneNumber mergedPhoneNumber = session.merge(phoneNumber);
      Integer phoneNumberId = mergedPhoneNumber.getId();

      transaction.commit();
      return phoneNumberId;

    } catch (Exception e) {

      if (transaction != null) {
        transaction.rollback();
      }
      throw new PersistenceException(e.getMessage());

    } finally {
      session.close();
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

      return session.createQuery("FROM PhoneNumber", app.entity.PhoneNumber.class).list();

    } catch (Exception e) {
      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Elimina un número de teléfono dado su ID
   * @param phoneNumberId ID del número
   * @throws PersistenceException En caso de que exista un error durante el proceso de eliminación del número
   */
  public void deletePhoneNumber(Integer phoneNumberId) throws PersistenceException {

    // Se abre sesión y transacción
    Session session = factory.openSession();
    Transaction transaction = null;

    try {
      transaction = session.beginTransaction();

      // Se obtiene el número de teléfono dado el ID por parámetro y se elimina
      PhoneNumber phoneNumber = session.get(PhoneNumber.class, phoneNumberId);
      session.remove(phoneNumber);

      transaction.commit();

    } catch (Exception e) {

      if (transaction != null) {
        transaction.rollback();
      }
      throw new PersistenceException(e.getMessage());

    } finally {
      session.close();
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
      Query<PhoneNumber> query = session.createQuery("FROM PhoneNumber WHERE number = :num", app.entity.PhoneNumber.class);

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

    } catch (Exception e) {

      throw new PersistenceException(e.getMessage());
    }
  }

}

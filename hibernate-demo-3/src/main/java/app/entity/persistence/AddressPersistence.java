
package app.entity.persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import app.entity.Address;
import app.entity.constants.DbConstants;
import app.entity.persistence.exceptions.PersistenceException;

/** Sistema de persistencia de direcciones */
public class AddressPersistence {

  /** Factoría de sesiones */
  private SessionFactory factory;

  /**
   * Constructor de la clase. Inicializa la factoría de sesiones
   * @throws ExceptionInInitializerError En caso de que exista un error durante la generación de la factoría de sesiones
   */
  public AddressPersistence() {

    // Se intenta crear la factoría de sesiones añadiendo las clases con anotaciones
    try {
      factory = new Configuration().configure().addAnnotatedClass(app.entity.Address.class).buildSessionFactory();

    } catch (Exception ex) {

      throw new ExceptionInInitializerError("Error al crear un objeto de la clase SessionFactory");
    }
  }

  /**
   * Almacena una dirección en la DB
   * @param address Dirección a almacenar
   * @return Integer - El ID de la dirección
   * @throws PersistenceException En caso de que exista un error durante el proceso de almacenamiento de direcciones
   */
  public Integer saveAddress(Address address) throws PersistenceException {

    Transaction transaction = null;

    // Se persiste la información y se obtiene el ID de la dirección
    try (Session session = factory.openSession()) {

      transaction = session.beginTransaction();

      session.persist(address);
      Integer id = (Integer) session.getIdentifier(address);

      transaction.commit();

      return id;

    } catch (HibernateException e) {

      if (transaction != null) {
        transaction.rollback();
      }

      // Se lanza otra excepción personalizada
      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Obtiene una lista con todas las direcciones almacenadas en la DB
   * @return List(Address)
   * @throws PersistenceException En caso de que exista un error durante el proceso de obtención de direcciones
   */
  public List<Address> getAllAddress() throws PersistenceException {

    // Se crea la sesión y lanza la consulta
    try (Session session = factory.openSession()) {
      return session.createQuery("FROM " + DbConstants.ADDRESS_TABLE, app.entity.Address.class).list();

    } catch (HibernateException e) {

      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Método que actualiza una dirección de la DB
   * @param Address Dirección a actualizar
   * @throws PersistenceException En caso de que exista un error durante el proceso de actualización de direcciones o
   *                                     de que no exista previamente la dirección
   */
  public void updateAddress(Address address) throws PersistenceException {

    Transaction transaction = null;

    try (Session session = factory.openSession()) {

      transaction = session.beginTransaction();

      // Obtiene la dirección almacenada con el ID pasado por parámetro
      Address extractedAddress = session.get(Address.class, address.getId());

      // Si no es null, actualiza el registro
      if (extractedAddress != null) {
        session.merge(address);
        transaction.commit();

      } else {
        throw new PersistenceException("No existe la dirección que se está intentando actualizar.");
      }

    } catch (HibernateException e) {

      if (transaction != null) {
        transaction.rollback();
      }

      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Elimina una dirección dado su ID
   * @param addressId ID de la dirección
   * @throws PersistenceException En caso de que exista un error durante el proceso de eliminación de direcciones
   */
  public void deleteAddress(Integer addressId) throws PersistenceException {

    Transaction transaction = null;

    try (Session session = factory.openSession()) {
      transaction = session.beginTransaction();

      // Se obtiene la dirección dado el ID por parámetro y se elimina
      Address address = session.get(Address.class, addressId);
      session.remove(address);

      transaction.commit();

    } catch (HibernateException e) {

      if (transaction != null) {
        transaction.rollback();
      }

      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Obtiene una dirección dada su descripción completa (Calle, número, piso, etc) y la ciudad a la que pertenece, o null si
   * no se encuentra ninguna dirección que coincida con los parámetros aportados
   * @param streetAddress Descripción completa de la calle
   * @param city          Ciudad a la que pertenece dicha dirección
   * @return Address - Será null si no se encuentra
   * @throws PersistenceException En caso de que exista un error durante el proceso de obtención de la dirección
   */
  public Address findByStreetAndCity(String streetAddress, String city) throws PersistenceException {

    try (Session session = factory.openSession()) {

      // Se crea la consulta y se pasan los parámetros
      Query<Address> query = session.createQuery(
          String.format("FROM %s WHERE streetAddress = :staddress AND city = :cit", DbConstants.ADDRESS_TABLE),
          app.entity.Address.class);

      query.setParameter("staddress", streetAddress);
      query.setParameter("cit", city);

      // Se obtiene la lista de direcciones con la descripción y ciudad dados
      List<Address> addressList = query.list();

      // Debería existir sólo un resultado como mucho. Si hay más, hay un fallo en el sistema de guardado
      Address resultAddress = null;

      if (addressList.size() == 1) {
        resultAddress = addressList.get(0);

      } else if (!addressList.isEmpty()) {
        throw new PersistenceException(
            "Se ha obtenido más de un dato con la misma descripción y ciudad. Es necesario revisar el proceso de guardado");
      }

      return resultAddress;

    } catch (HibernateException e) {

      throw new PersistenceException(e.getMessage());
    }
  }

}

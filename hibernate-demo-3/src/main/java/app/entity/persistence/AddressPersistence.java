
package app.entity.persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import app.entity.Address;
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
   * Almacena o actualiza una dirección en la DB en función de si existe o no previamente
   * @param address Dirección a almacenar/actualizar. Almacena la dirección si no dispone de un ID asignado, o la actualiza
   *                en caso contrario
   * @return Integer - El ID de la dirección
   * @throws PersistenceException En caso de que exista un error durante el proceso de almacenamiento/actualización de
   *                              direcciones
   */
  public Integer saveOrUpdateAddress(Address address) throws PersistenceException {

    Session session = factory.openSession();
    Transaction transaction = null;

    // Se persiste la información y se obtiene el ID de la dirección
    try {

      transaction = session.beginTransaction();

      Address mergedAddress = session.merge(address);
      Integer id = mergedAddress.getId();

      transaction.commit();

      return id;

    } catch (Exception e) {

      if (transaction != null) {
        transaction.rollback();
      }

      // Se lanza otra excepción personalizada
      throw new PersistenceException(e.getMessage());

    } finally {
      session.close();
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
      return session.createQuery("FROM Address", app.entity.Address.class).list();

    } catch (Exception e) {

      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Elimina una dirección dado su ID
   * @param addressId ID de la dirección
   * @throws PersistenceException En caso de que exista un error durante el proceso de eliminación de direcciones
   */
  public void deleteAddress(Integer addressId) throws PersistenceException {

    Session session = factory.openSession();
    Transaction transaction = null;

    try {
      transaction = session.beginTransaction();

      // Se obtiene la dirección dado el ID por parámetro y se elimina
      Address address = session.get(Address.class, addressId);
      session.remove(address);

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
      Query<Address> query = session.createQuery("FROM Address WHERE streetAddress = :staddress AND city = :cit",
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

    } catch (Exception e) {

      throw new PersistenceException(e.getMessage());
    }
  }

}

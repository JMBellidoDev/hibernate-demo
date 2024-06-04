
package app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Dirección de las distintas personas implicadas en el sistema */
@Entity
@Table(name = "Employee")
public class Address {

  /** ID */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  /** Dirección completa de la persona */
  @Column(name = "street_address")
  private String streetAddress;

  /** Ciudad o población */
  @Column(name = "city")
  private String city;

  /** Código postal */
  @Column(name = "postal_code")
  private String postalCode;

  /** Constructor por defecto */
  public Address() {
  }

  /**
   * Constructor con parámetros
   * @param streetAddress Dirección completa
   * @param city          Ciudad o población
   * @param postalCode    Código postal
   */
  public Address(String streetAddress, String city, String postalCode) {
    super();
    this.streetAddress = streetAddress;
    this.city = city;
    this.postalCode = postalCode;
  }

  /**
   * Getter - id
   * @return long - id
   */
  public long getId() {
    return id;
  }

  /**
   * Setter - id
   * @param id ID
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Getter - streetAddress
   * @return String - streetAddress
   */
  public String getStreetAddress() {
    return streetAddress;
  }

  /**
   * Setter - streetAddress
   * @param streetAddress Dirección completa
   */
  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  /**
   * Getter - city
   * @return String - city
   */
  public String getCity() {
    return city;
  }

  /**
   * Setter - city
   * @param city Ciudad o población
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Getter - postalCode
   * @return String - postalCode
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * Setter - postalCode
   * @param postalCode Código postal
   */
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

}

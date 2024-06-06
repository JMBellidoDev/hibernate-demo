
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

/** Dirección de las distintas personas implicadas en el sistema */
@Entity
@Table(name = DbConstants.ADDRESS_TABLE)
@Data
@NoArgsConstructor
public class Address {

  /** ID */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = DbConstants.ADDRESS_ID)
  private Integer id;

  /** Dirección completa de la persona */
  @Column(name = DbConstants.ADDRESS_STREET, columnDefinition = "VARCHAR(255)", nullable = false)
  private String streetAddress;

  /** Ciudad o población */
  @Column(name = DbConstants.ADDRESS_CITY, columnDefinition = "VARCHAR(50)", nullable = false)
  private String city;

  /** Código postal */
  @Column(name = DbConstants.ADDRESS_POSTAL_CODE, columnDefinition = "CHAR(5)", nullable = false)
  private String postalCode;

  @Override
  public String toString() {
    return String.format("ID: %d, Street Address: %s, City: %s, Postal Code: %s", id, streetAddress, city, postalCode);
  }

}

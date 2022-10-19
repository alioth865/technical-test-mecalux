package com.atsistemas.aafa.warehouses.entity;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.Hibernate;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Warehouse {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String uuid;

  private String client;

  private String family;

  private Integer size;

  @OneToMany(mappedBy = "warehouse", orphanRemoval = true, cascade = CascadeType.REMOVE)
  @Exclude
  private List<Rack> racks;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Warehouse warehouse = (Warehouse) o;
    return id != null && Objects.equals(id, warehouse.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}

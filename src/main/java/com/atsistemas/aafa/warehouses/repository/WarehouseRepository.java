package com.atsistemas.aafa.warehouses.repository;

import com.atsistemas.aafa.warehouses.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {

}

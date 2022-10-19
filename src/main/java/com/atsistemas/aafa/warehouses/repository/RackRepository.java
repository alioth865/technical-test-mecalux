package com.atsistemas.aafa.warehouses.repository;

import com.atsistemas.aafa.warehouses.entity.Rack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RackRepository extends JpaRepository<Rack, Integer> {

}

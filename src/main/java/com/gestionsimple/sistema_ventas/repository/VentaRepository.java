package com.gestionsimple.sistema_ventas.repository;

import com.gestionsimple.sistema_ventas.model.Venta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends CrudRepository<Venta, Long> {
}

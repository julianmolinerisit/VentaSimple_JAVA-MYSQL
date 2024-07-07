package com.gestionsimple.sistema_ventas.repository;

import com.gestionsimple.sistema_ventas.model.Producto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends CrudRepository<Producto, Long> {

    @Query("SELECT DISTINCT p.categoria FROM Producto p")
    List<String> findAllCategories();

    List<Producto> findByCategoria(String categoria);
}

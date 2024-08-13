package com.gestionsimple.sistema_ventas.service;

import java.util.List;
import java.util.Optional;
import com.gestionsimple.sistema_ventas.model.Compra;

public interface CompraService {

    List<Compra> getAllCompras();

    void saveCompra(Compra compra);

    Compra getCompraById(Long id);

    void deleteCompraById(Long id);
    
    Optional<Compra> findLastCompraByProductoId(Long productoId); // Cambiar el tipo de retorno a Optional<Compra>
}

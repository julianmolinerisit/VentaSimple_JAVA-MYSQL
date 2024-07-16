package com.gestionsimple.sistema_ventas.services.impl;

import com.gestionsimple.sistema_ventas.model.DetalleVenta;
import com.gestionsimple.sistema_ventas.repository.DetalleVentaRepository;
import com.gestionsimple.sistema_ventas.services.DetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Override
    public List<DetalleVenta> obtenerDetallesVentaPorProductoId(Long productoId) {
        return detalleVentaRepository.findByProductoId(productoId);
    }
    
    @Override
    public List<DetalleVenta> obtenerDetallesVentaPorIdVenta(Long idVenta) {
        return detalleVentaRepository.findByVentaId(idVenta);
    }

    @Override
    public List<DetalleVenta> obtenerTodosDetallesVenta() {
        return detalleVentaRepository.findAll();
    }
}

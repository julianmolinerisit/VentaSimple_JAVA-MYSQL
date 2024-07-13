package com.gestionsimple.sistema_ventas.services.impl;

import com.gestionsimple.sistema_ventas.model.DetalleVenta;
import com.gestionsimple.sistema_ventas.repository.DetalleVentaRepository;
import com.gestionsimple.sistema_ventas.services.DetalleVentaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {

    private static final Logger logger = LoggerFactory.getLogger(DetalleVentaServiceImpl.class);

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Override
    public List<DetalleVenta> obtenerDetallesVentaPorProductoId(Long productoId) {
        logger.info("Obteniendo detalles de venta para producto con ID: {}", productoId);
        return detalleVentaRepository.findByProductoId(productoId);
    }
    
    @Override
    public List<DetalleVenta> obtenerDetallesVentaPorIdVenta(Long idVenta) {
        return detalleVentaRepository.findByVentaId(idVenta);
    }
}

package com.gestionsimple.sistema_ventas.services;

import com.gestionsimple.sistema_ventas.model.Venta;

import com.gestionsimple.sistema_ventas.model.Venta;
import com.gestionsimple.sistema_ventas.repository.VentaRepository;
import com.gestionsimple.sistema_ventas.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public void guardarVenta(Venta venta) {
        ventaRepository.save(venta);
    }
}
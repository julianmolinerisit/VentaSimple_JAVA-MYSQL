package com.gestionsimple.sistema_ventas.services.impl;

import com.gestionsimple.sistema_ventas.model.Venta;
import com.gestionsimple.sistema_ventas.repository.VentaRepository;
import com.gestionsimple.sistema_ventas.services.VentaService;

import java.util.List;

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
    
    @Override
    public Venta obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

	
	
	   @Override
	    public List<Venta> obtenerTodasVentas() {
	        return (List<Venta>) ventaRepository.findAll();
	    }
}

package com.gestionsimple.sistema_ventas.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestionsimple.sistema_ventas.model.Compra;
import com.gestionsimple.sistema_ventas.repository.CompraRepository;
import com.gestionsimple.sistema_ventas.service.CompraService;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Override
    public List<Compra> getAllCompras() {
        return compraRepository.findAll();
    }

    @Override
    public void saveCompra(Compra compra) {
        compraRepository.save(compra);
    }

    @Override
    public Compra getCompraById(Long id) {
        return compraRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteCompraById(Long id) {
        compraRepository.deleteById(id);
    }
    
    @Override
    public Optional<Compra> findLastCompraByProductoId(Long productoId) {
        return compraRepository.findFirstByProductoIdOrderByFechaDesc(productoId);
    }
    
}

package com.gestionsimple.sistema_ventas.service.impl;

import com.gestionsimple.sistema_ventas.model.PagosProveedores;
import com.gestionsimple.sistema_ventas.repository.PagosProveedoresRepository;
import com.gestionsimple.sistema_ventas.service.PagosProveedoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PagosProveedoresServiceImpl implements PagosProveedoresService {

    @Autowired
    private PagosProveedoresRepository pagosProveedoresRepository;

    @Override
    public List<PagosProveedores> getAllPagosProveedores() {
        return pagosProveedoresRepository.findAll();
    }

    @Override
    public List<PagosProveedores> getPagosByProveedor(Long proveedorId) {
        return pagosProveedoresRepository.findByProveedorId(proveedorId);
    }

    @Override
    public void savePagoProveedores(PagosProveedores pago) {
        pagosProveedoresRepository.save(pago);
    }

    @Override
    public PagosProveedores getPagoProveedoresById(Long id) {
        return pagosProveedoresRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePagoProveedoresById(Long id) {
        pagosProveedoresRepository.deleteById(id);
    }
}

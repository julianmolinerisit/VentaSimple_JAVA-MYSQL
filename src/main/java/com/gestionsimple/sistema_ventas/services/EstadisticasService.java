package com.gestionsimple.sistema_ventas.services;

import java.util.List;

public interface EstadisticasService {
    List<Object[]> obtenerHorariosMasConcurridos();
    List<Object[]> obtenerDiasMasConcurridos();
    List<Object[]> obtenerMediosDePago();
    List<Object[]> obtenerProductosMasVendidos();
    double obtenerTotalRecaudado();
    double obtenerRecaudadoPorDia(String dia);
}

package com.gestionsimple.sistema_ventas.services;

import com.gestionsimple.sistema_ventas.model.Login;

public interface LoginService {
    Login autenticarUsuario(String nombreUsuario, String password);
}



package com.seransaca.intelygenz.securitish.web.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
info = @Info(
        title = "API Caja seguridad",
        version = "1.0.0",
        description = "Esta API permite gestionar caja de seguridad remotamente",
        contact = @Contact(name = "Sergio A. Sánchez Camarero", email = "seransaca@gmail.com")

))
public interface ApiController {
}

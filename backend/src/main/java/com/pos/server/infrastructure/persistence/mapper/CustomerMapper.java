package com.pos.server.infrastructure.persistence.mapper;

import com.pos.server.domain.model.Customer;

import com.pos.server.infrastructure.persistence.entity.Cliente;
import org.mapstruct.*;

import java.util.List;

// ================================================================
// MAPPER PARA CLIENTE -> CUSTOMER
// ================================================================
@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Named("toCustomerFull")
    @Mappings({
            @Mapping(source = "nombre", target = "firstName"),
            @Mapping(source = "apellidos", target = "lastName"),
            @Mapping(source = "celular", target = "phone"),
            @Mapping(source = "direccion", target = "address"),
            @Mapping(source = "correoElectronico", target = "email"),
            @Mapping(source = "username", target = "username"),
            @Mapping(target = "password", ignore = true),  // ✅ SEGURO
            @Mapping(source = "fechaRegistro", target = "registrationDate"),
            @Mapping(source = "fechaActualizacion", target = "lastUpdated"),
            @Mapping(source = "ultimoLogin", target = "lastLogin"),
            @Mapping(source = "intentosLogin", target = "loginAttempts"),
            @Mapping(source = "activo", target = "active"),
            @Mapping(source = "emailVerificado", target = "emailVerified"),
            @Mapping(source = "cuentaBloqueada", target = "accountLocked"),
            @Mapping(source = "fotoPerfil", target = "profilePicture"),
            @Mapping(source = "fechaNacimiento", target = "birthDate"),
            @Mapping(source = "genero", target = "gender")
    })
    Customer toCustomer(Cliente cliente);

    @IterableMapping(qualifiedByName = "toCustomerFull")
    List<Customer> toCustomers(List<Cliente> clientes);
    @InheritInverseConfiguration(name = "toCustomer")
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "tokenVerificacion", ignore = true),
            @Mapping(target = "tokenVerificacionExpira", ignore = true),
            @Mapping(target = "compras", ignore = true),
            @Mapping(source = "password", target = "passwordHash"),  // ✅ SEGURO
            @Mapping(target = "passwordResets", ignore = true),
            @Mapping(target = "sesiones", ignore = true),
            @Mapping(target = "loginAttempts", ignore = true)
    })
    Cliente toCliente(Customer customer);

    // Mapeo específico para registro (sin campos sensibles)
    @Named("toPublicCustomer")
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "firstName", target = "nombre"),
            @Mapping(source = "lastName", target = "apellidos"),
            @Mapping(source = "phone", target = "celular"),
            @Mapping(source = "address", target = "direccion"),
            @Mapping(source = "email", target = "correoElectronico"),
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "birthDate", target = "fechaNacimiento"),
            @Mapping(source = "gender", target = "genero"),
            @Mapping(target = "fechaRegistro", ignore = true),
            @Mapping(target = "passwordHash", ignore = true), // =
            @Mapping(target = "fechaActualizacion", ignore = true),
            @Mapping(target = "ultimoLogin", ignore = true),
            @Mapping(target = "intentosLogin", ignore = true),
            @Mapping(target = "activo", ignore = true),
            @Mapping(target = "emailVerificado", ignore = true),
            @Mapping(target = "cuentaBloqueada", ignore = true),
            @Mapping(target = "tokenVerificacion", ignore = true),
            @Mapping(target = "tokenVerificacionExpira", ignore = true),
            @Mapping(target = "fotoPerfil", ignore = true),
            @Mapping(target = "compras", ignore = true),
            @Mapping(target = "passwordResets", ignore = true),
            @Mapping(target = "sesiones", ignore = true),
            @Mapping(target = "loginAttempts", ignore = true)
    })
    Cliente toClienteFromRegistration(Customer customer);

    // Mapeo para respuesta pública (sin datos sensibles)
    @Mappings({
            @Mapping(source = "nombre", target = "firstName"),
            @Mapping(source = "apellidos", target = "lastName"),
            @Mapping(source = "correoElectronico", target = "email"),
            @Mapping(source = "username", target = "username"),
            @Mapping(target ="password", ignore = true), // ← BUENO
            @Mapping(source = "fechaRegistro", target = "registrationDate"),
            @Mapping(source = "ultimoLogin", target = "lastLogin"),
            @Mapping(source = "activo", target = "active"),
            @Mapping(source = "emailVerificado", target = "emailVerified"),
            @Mapping(source = "fotoPerfil", target = "profilePicture"),
            @Mapping(target = "phone", ignore = true),
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "lastUpdated", ignore = true),
            @Mapping(target = "loginAttempts", ignore = true),
            @Mapping(target = "accountLocked", ignore = true),
            @Mapping(target = "birthDate", ignore = true),
            @Mapping(target = "gender", ignore = true)
    })
    Customer toPublicCustomer(Cliente cliente);
}

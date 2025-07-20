package com.pos.server.infrastructure.persistence.mapper;

import com.pos.server.domain.model.Session;
import com.pos.server.infrastructure.persistence.entity.Sesion;
import org.mapstruct.*;
import java.util.List;

// ================================================================
// MAPPER PARA SESION -> SESSION
// ================================================================
@Mapper(componentModel = "spring")
public interface SessionMapper {

    @Named("toSessionFull")
    @Mappings({
            @Mapping(source = "id", target = "sessionId"),
            @Mapping(source = "clienteId", target = "customerId"),
            @Mapping(source = "tokenSesion", target = "sessionToken"),
            @Mapping(source = "ipAddress", target = "ipAddress"),
            @Mapping(source = "userAgent", target = "userAgent"),
            @Mapping(source = "expiraEn", target = "expiresAt"),
            @Mapping(source = "activa", target = "active"),
            @Mapping(source = "fechaCreacion", target = "createdAt"),
            @Mapping(source = "fechaUltimoUso", target = "lastUsed")
    })
    Session toSession(Sesion sesion);

    @IterableMapping(qualifiedByName = "toSessionFull")
    List<Session> toSessions(List<Sesion> sesiones);

    @InheritInverseConfiguration(name = "toSession")
    @Mappings({
            @Mapping(target = "cliente", ignore = true)
    })
    Sesion toSesion(Session session);

    // Mapeo para respuesta pública (sin token)
    @Named("toSessionPublic")
    @Mappings({
            @Mapping(source = "id", target = "sessionId"),
            @Mapping(source = "clienteId", target = "customerId"),
            @Mapping(source = "ipAddress", target = "ipAddress"),
            @Mapping(source = "userAgent", target = "userAgent"),
            @Mapping(source = "expiraEn", target = "expiresAt"),
            @Mapping(source = "activa", target = "active"),
            @Mapping(source = "fechaCreacion", target = "createdAt"),
            @Mapping(source = "fechaUltimoUso", target = "lastUsed"),
            @Mapping(target = "sessionToken", ignore = true)
    })
    Session toPublicSession(Sesion sesion);

    @IterableMapping(qualifiedByName = "toSessionPublic")
    List<Session> toPublicSessions(List<Sesion> sesiones);
}

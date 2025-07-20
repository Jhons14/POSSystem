package com.pos.server.persistence.mapper;



import com.pos.server.domain.PasswordResetRequest;

import com.pos.server.persistence.entity.PasswordReset;
import org.mapstruct.*;

import java.util.List;
@Mapper(componentModel = "spring")public interface PasswordResetMapper {

    @Named("toPasswordResetRequestFull")
    @Mappings({
            @Mapping(source = "id", target = "requestId"),
            @Mapping(source = "clienteId", target = "customerId"),
            @Mapping(source = "token", target = "resetToken"),
            @Mapping(source = "expiraEn", target = "expiresAt"),
            @Mapping(source = "usado", target = "used"),
            @Mapping(source = "ipSolicitud", target = "requestIp"),
            @Mapping(source = "fechaCreacion", target = "createdAt")
    })
    PasswordResetRequest toPasswordResetRequest(PasswordReset passwordReset);

    @IterableMapping(qualifiedByName = "toPasswordResetRequestFull")
    List<PasswordResetRequest> toPasswordResetRequests(List<PasswordReset> passwordResets);

    @InheritInverseConfiguration(name="toPasswordResetRequest")
    @Mappings({
            @Mapping(target = "cliente", ignore = true)
    })

    PasswordReset toPasswordReset(PasswordResetRequest passwordResetRequest);

    // Mapeo para respuesta pública (sin token)
    @Named("toPasswordResetRequestPublic")
    @Mappings({
            @Mapping(source = "id", target = "requestId"),
            @Mapping(source = "clienteId", target = "customerId"),
            @Mapping(source = "expiraEn", target = "expiresAt"),
            @Mapping(source = "usado", target = "used"),
            @Mapping(source = "fechaCreacion", target = "createdAt"),
            @Mapping(target = "resetToken", ignore = true),
            @Mapping(target = "requestIp", ignore = true)
    })
    PasswordResetRequest toPublicPasswordResetRequest(PasswordReset passwordReset);

    @IterableMapping(qualifiedByName = "toPasswordResetRequestPublic")
    List<PasswordResetRequest> toPublicPasswordResetRequests(List<PasswordReset> passwordResets);
}

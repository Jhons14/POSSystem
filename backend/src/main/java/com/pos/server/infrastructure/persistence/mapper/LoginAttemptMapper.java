package com.pos.server.infrastructure.persistence.mapper;

import com.pos.server.domain.model.LoginAttemptRecord;
import com.pos.server.infrastructure.persistence.entity.LoginAttempt;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

public interface LoginAttemptMapper {
    @Mappings({
            @Mapping(source = "id", target = "attemptId"),
            @Mapping(source = "clienteId", target = "customerId"),
            @Mapping(source = "ipAddress", target = "ipAddress"),
            @Mapping(source = "exitoso", target = "successful"),
            @Mapping(source = "razonFallo", target = "failureReason"),
            @Mapping(source = "userAgent", target = "userAgent"),
            @Mapping(source = "fechaIntento", target = "attemptTime")
    })
    LoginAttemptRecord toLoginAttemptRecord(LoginAttempt loginAttempt);

    List<LoginAttemptRecord> toLoginAttemptRecords(List<LoginAttempt> loginAttempts);

    @InheritInverseConfiguration
    @Mappings({
            @Mapping(target = "cliente", ignore = true)
    })
    LoginAttempt toLoginAttempt(LoginAttemptRecord loginAttemptRecord);

    // Mapeo para respuesta pública (datos limitados)
    @Mappings({
            @Mapping(source = "id", target = "attemptId"),
            @Mapping(source = "exitoso", target = "successful"),
            @Mapping(source = "fechaIntento", target = "attemptTime"),
            @Mapping(target = "customerId", ignore = true),
            @Mapping(target = "ipAddress", ignore = true),
            @Mapping(target = "failureReason", ignore = true),
            @Mapping(target = "userAgent", ignore = true)
    })
    LoginAttemptRecord toPublicLoginAttemptRecord(LoginAttempt loginAttempt);
}

package com.pos.server.web.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;
// Remover este import problemático
// import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ================================================================
    // ERRORES DE PARSING Y FORMATO DE JSON
    // ================================================================

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            org.springframework.http.converter.HttpMessageNotReadableException ex, WebRequest request) {

        String message = "Formato de datos incorrecto";
        String details = ex.getMessage().toLowerCase();


        // Detectar tipos específicos de errores de parsing
        if (details.contains("cannot deserialize") && details.contains("long")) {
            if (details.contains("customerid") || details.contains("id")) {
                message = "El ID debe ser un número válido";
            } else {
                message = "Se esperaba un número pero se recibió texto";
            }
        } else if (details.contains("cannot deserialize") && details.contains("boolean")) {
            message = "Se esperaba true/false pero se recibió otro valor";
        } else if (details.contains("cannot deserialize") && details.contains("date")) {
            message = "Formato de fecha incorrecto. Use: YYYY-MM-DD";
        } else if (details.contains("cannot deserialize") && details.contains("datetime")) {
            message = "Formato de fecha y hora incorrecto. Use: YYYY-MM-DDTHH:mm:ss";
        } else if (details.contains("unexpected character") || details.contains("malformed")) {
            message = "El JSON está mal formado. Verifique llaves, comas y comillas";
        } else if (details.contains("required request body is missing")) {
            message = "El cuerpo de la petición está vacío";
        } else {
            message = "El formato de los datos enviados es incorrecto";
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_JSON_FORMAT",
                message,
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(com.fasterxml.jackson.databind.exc.InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidFormat(
            com.fasterxml.jackson.databind.exc.InvalidFormatException ex, WebRequest request) {

        String fieldName = ex.getPath().isEmpty() ? "campo" : ex.getPath().get(0).getFieldName();
        String targetType = ex.getTargetType().getSimpleName();
        String message = String.format("El campo '%s' tiene un formato incorrecto", fieldName);

        // Personalizar según el tipo esperado
        switch (targetType.toLowerCase()) {
            case "long":
            case "integer":
                message = String.format("El campo '%s' debe ser un número entero", fieldName);
                break;
            case "double":
            case "bigdecimal":
                message = String.format("El campo '%s' debe ser un número decimal", fieldName);
                break;
            case "boolean":
                message = String.format("El campo '%s' debe ser true o false", fieldName);
                break;
            case "localdate":
                message = String.format("El campo '%s' debe tener formato de fecha: YYYY-MM-DD", fieldName);
                break;
            case "localdatetime":
                message = String.format("El campo '%s' debe tener formato: YYYY-MM-DDTHH:mm:ss", fieldName);
                break;
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_FIELD_FORMAT",
                message,
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(com.fasterxml.jackson.core.JsonProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleJsonProcessing(
            com.fasterxml.jackson.core.JsonProcessingException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "JSON_PROCESSING_ERROR",
                "Error al procesar el JSON: " + ex.getOriginalMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // ================================================================
    // ERRORES DE HIBERNATE ESPECÍFICOS
    // ================================================================

    @ExceptionHandler(org.hibernate.PropertyValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handlePropertyValueException(
            org.hibernate.PropertyValueException ex, WebRequest request) {

        String fieldName = ex.getPropertyName();
        String message = "Campo obligatorio faltante: " + fieldName;

        // Personalizar mensaje según el campo
        if ("passwordHash".equals(fieldName)) {
            message = "La contraseña es obligatoria";
        } else if ("correoElectronico".equals(fieldName)) {
            message = "El email es obligatorio";
        } else if ("username".equals(fieldName)) {
            message = "El nombre de usuario es obligatorio";
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "MISSING_REQUIRED_FIELD",
                message,
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // ================================================================
    // ERRORES DE BASE DE DATOS
    // ================================================================

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, WebRequest request) {

        String message = "Error de integridad de datos";
        String details = ex.getMessage().toLowerCase();
        String rootCauseMessage = "";

        // Obtener la causa raíz para mejor detección
        Throwable rootCause = ex.getRootCause();
        if (rootCause != null) {
            rootCauseMessage = rootCause.getMessage().toLowerCase();
        }

        // Combinar ambos mensajes para mejor detección
        String fullMessage = (rootCauseMessage).toLowerCase();


        // Detectar tipos específicos de errores con palabras en español
        if (fullMessage.contains("llave duplicada") ||
                fullMessage.contains("duplicate") ||
                fullMessage.contains("unique") ||
                fullMessage.contains("unicidad") ||
                fullMessage.contains("ya existe")) {

            // Ser MÁS ESPECÍFICO en la detección del campo
            if (fullMessage.contains("correo_electronico") ||
                    fullMessage.contains("email") ||
                    fullMessage.contains("clientes_correo_electronico_key")) {
                message = "El email ya está registrado";

            } else if (fullMessage.contains("username") ||
                    fullMessage.contains("usuario") ||
                    fullMessage.contains("clientes_username_key")) {
                message = "El nombre de usuario ya está en uso";

            } else {
                // Si no podemos determinar el campo específico, mensaje genérico
                message = "Ya existe un registro con estos datos";

                // Debug para ver exactamente qué constraint se violó
            }

        } else if (fullMessage.contains("foreign key") ||
                fullMessage.contains("clave foranea") ||
                fullMessage.contains("clave foránea") ||
                fullMessage.contains("violates") ||
                fullMessage.contains("referencia")) {
            message = "Referencia inválida a otro registro";

        } else if (fullMessage.contains("not-null") ||
                fullMessage.contains("null") ||
                fullMessage.contains("no puede ser null") ||
                fullMessage.contains("cannot be null")) {
            message = "Faltan campos obligatorios";

        } else if (fullMessage.contains("check constraint") ||
                fullMessage.contains("restricción de verificación")) {
            message = "Los datos no cumplen las reglas de validación";
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "CONSTRAINT_VIOLATION",
                message,
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // ================================================================
    // ERRORES DE VALIDACIÓN (Comentado - requiere dependencia adicional)
    // ================================================================

    /*
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR",
            "Datos inválidos: " + ex.getMessage(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    */

    // ================================================================
    // ENTIDADES NO ENCONTRADAS
    // ================================================================

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "ENTITY_NOT_FOUND",
                "Registro no encontrado",
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // ================================================================
    // ARGUMENTOS INVÁLIDOS (VALIDACIONES @Valid)
    // ================================================================

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(
            org.springframework.web.bind.MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Errores de validación en los campos",
                request.getDescription(false),
                errors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // ================================================================
    // ERRORES GENERALES
    // ================================================================

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_ARGUMENT",
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, WebRequest request) {


        // Verificar si es un tipo específico y devolver mensaje apropiado
        String exceptionName = ex.getClass().getSimpleName();

        if (exceptionName.contains("DataIntegrity")) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.CONFLICT.value(),
                    "DATA_INTEGRITY_ERROR",
                    "Error de integridad de datos",
                    request.getDescription(false)
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        if (exceptionName.contains("HttpMessageNotReadable") ||
                exceptionName.contains("JsonProcessing") ||
                exceptionName.contains("InvalidFormat")) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "INVALID_REQUEST_FORMAT",
                    "Formato de petición incorrecto",
                    request.getDescription(false)
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR",
                "Error interno del servidor",
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "UNKNOWN_ERROR",
                "Error inesperado del servidor",
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ================================================================
    // CLASES DE RESPUESTA DE ERROR
    // ================================================================

    public static class ErrorResponse {
        private int status;
        private String error;
        private String message;
        private String path;
        private LocalDateTime timestamp;

        public ErrorResponse(int status, String error, String message, String path) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
            this.timestamp = LocalDateTime.now();
        }

        // Getters y Setters
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }

        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }

    public static class ValidationErrorResponse extends ErrorResponse {
        private Map<String, String> fieldErrors;

        public ValidationErrorResponse(int status, String error, String message, String path, Map<String, String> fieldErrors) {
            super(status, error, message, path);
            this.fieldErrors = fieldErrors;
        }

        public Map<String, String> getFieldErrors() { return fieldErrors; }
        public void setFieldErrors(Map<String, String> fieldErrors) { this.fieldErrors = fieldErrors; }
    }
}
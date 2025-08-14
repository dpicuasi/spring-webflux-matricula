package com.mitocode.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "estudiantes")
public class Estudiante {
    @Id
    private String id;
    @NotBlank(message = "Nombres es obligatorio")
    private String nombres;

    @NotBlank(message = "Apellidos es obligatorio")
    private String apellidos;

    @NotBlank(message = "DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "DNI debe tener 8 dígitos")
    private String dni;

    @Min(value = 1, message = "Edad mínima es 1")
    @Max(value = 120, message = "Edad máxima es 120")
    private int edad;
}

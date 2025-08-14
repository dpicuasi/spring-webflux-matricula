package com.mitocode.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "cursos")
public class Curso {
    @Id
    private String id;
    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Siglas es obligatorio")
    private String siglas;

    private boolean estado;
}

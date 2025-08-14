package com.mitocode.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "matriculas")
public class Matricula {
    @Id
    private String id;
    @NotNull(message = "Fecha de matrícula es obligatoria")
    private LocalDateTime fechaMatricula;

    @NotNull(message = "Estudiante es obligatorio")
    @Valid
    private Estudiante estudiante;

    @NotEmpty(message = "Debe registrar al menos un curso")
    @Valid
    private List<Curso> cursos;

    private boolean estado;
}

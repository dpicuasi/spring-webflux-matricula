package com.mitocode.dto;

import lombok.Data;

@Data
public class MatriculaDTO {
    private String id;
    private EstudianteDTO estudiante;
    private CursoDTO curso;
    private String fechaMatricula;
}

package com.mitocode.dto;

import java.util.List;

import lombok.Data;

@Data
public class MatriculaDTO {
    private String id;
    private EstudianteDTO estudiante;
    private List<CursoDTO> cursos;
    private String fechaMatricula;
}

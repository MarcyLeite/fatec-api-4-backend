package com.fatec.api.backend.DTO;

public record AcuraciaDTO(
    Double concordancia,
    Double falsoNegativo,
    Double falsoPositivo
) {}

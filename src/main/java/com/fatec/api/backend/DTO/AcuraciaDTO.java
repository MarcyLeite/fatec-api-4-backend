package com.fatec.api.backend.DTO;

public class AcuraciaDTO {
    private Double concordancia;
    private Double falsoNegativo;
    private Double falsoPositivo;

    public AcuraciaDTO(Double concordancia, Double falsoNegativo, Double falsoPositivo) {
        this.concordancia = concordancia;
        this.falsoNegativo = falsoNegativo;
        this.falsoPositivo = falsoPositivo;
    }

    public Double getConcordancia() { return concordancia; }
    public Double getFalsoNegativo() { return falsoNegativo; }
    public Double getFalsoPositivo() { return falsoPositivo; }
}

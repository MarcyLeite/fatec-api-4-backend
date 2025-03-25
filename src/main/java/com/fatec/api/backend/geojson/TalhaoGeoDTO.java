package com.fatec.api.backend.geojson;

import org.locationtech.jts.geom.Geometry;

public class TalhaoGeoDTO {
    private Long id;
    private String nome;
    private String cultura;
    private Float area;
    private Geometry shape;
    
    public TalhaoGeoDTO() {}
    
    public TalhaoGeoDTO(Long id, String nome, String cultura, Float area, Geometry shape) {
        this.id = id;
        this.nome = nome;
        this.cultura = cultura;
        this.area = area;
        this.shape = shape;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCultura() { return cultura; }
    public void setCultura(String cultura) { this.cultura = cultura; }
    
    public Float getArea() { return area; }
    public void setArea(Float area) { this.area = area; }
    
    public Geometry getShape() { return shape; }
    public void setShape(Geometry shape) { this.shape = shape; }
}
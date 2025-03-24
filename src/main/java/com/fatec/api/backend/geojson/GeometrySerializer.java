package com.fatec.api.backend.geojson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.*;
import java.io.IOException;

public class GeometrySerializer extends JsonSerializer<Geometry> {
    
    @Override
    public void serialize(Geometry value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        
        gen.writeStartObject();
        gen.writeStringField("type", value.getGeometryType());
        
        switch (value.getGeometryType()) {
            case "Point":
                writePoint((Point) value, gen);
                break;
            case "Polygon":
                writePolygon((Polygon) value, gen);
                break;
            case "LineString":
                writeLineString((LineString) value, gen);
                break;
            case "MultiPolygon":
                writeMultiPolygon((MultiPolygon) value, gen);
                break;
            default:
                gen.writeObjectField("coordinates", value.getCoordinates());
        }
        
        gen.writeEndObject();
    }

    private void writePoint(Point point, JsonGenerator gen) throws IOException {
        gen.writeArrayFieldStart("coordinates");
        gen.writeNumber(point.getX());
        gen.writeNumber(point.getY());
        if (!Double.isNaN(point.getCoordinate().getZ())) {
            gen.writeNumber(point.getCoordinate().getZ());
        }
        gen.writeEndArray();
    }

    private void writeLineString(LineString line, JsonGenerator gen) throws IOException {
        gen.writeArrayFieldStart("coordinates");
        for (Coordinate coord : line.getCoordinates()) {
            gen.writeStartArray();
            gen.writeNumber(coord.x);
            gen.writeNumber(coord.y);
            if (!Double.isNaN(coord.getZ())) {
                gen.writeNumber(coord.getZ());
            }
            gen.writeEndArray();
        }
        gen.writeEndArray();
    }

    private void writePolygon(Polygon polygon, JsonGenerator gen) throws IOException {
        gen.writeArrayFieldStart("coordinates");
        
        // Shell (exterior ring)
        writeLinearRingCoordinates(polygon.getExteriorRing(), gen);
        
        // Holes (interior rings)
        for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
            writeLinearRingCoordinates(polygon.getInteriorRingN(i), gen);
        }
        
        gen.writeEndArray();
    }

    private void writeLinearRingCoordinates(LinearRing ring, JsonGenerator gen) throws IOException {
        gen.writeStartArray();
        for (Coordinate coord : ring.getCoordinates()) {
            gen.writeStartArray();
            gen.writeNumber(coord.x);
            gen.writeNumber(coord.y);
            if (!Double.isNaN(coord.getZ())) {
                gen.writeNumber(coord.getZ());
            }
            gen.writeEndArray();
        }
        gen.writeEndArray();
    }

    private void writeMultiPolygon(MultiPolygon multiPolygon, JsonGenerator gen) throws IOException {
        gen.writeArrayFieldStart("coordinates");
        for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
            Polygon polygon = (Polygon) multiPolygon.getGeometryN(i);
            gen.writeStartArray(); // Inicia um polígono
            writeLinearRingCoordinates(polygon.getExteriorRing(), gen);
            for (int j = 0; j < polygon.getNumInteriorRing(); j++) {
                writeLinearRingCoordinates(polygon.getInteriorRingN(j), gen);
            }
            gen.writeEndArray();
        }
        gen.writeEndArray();
    }
}
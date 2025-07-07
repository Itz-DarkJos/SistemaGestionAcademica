package modelos;

import java.io.Serializable;

public class Materia implements Serializable {
    private String clave;
    private String seriacion;
    private String nombre;
    private String cuatrimestre;

    public Materia(String clave, String seriacion, String nombre, String cuatrimestre) {
        this.clave = clave;
        this.seriacion = seriacion;
        this.nombre = nombre;
        this.cuatrimestre = cuatrimestre;
    }

    public String getClave() { return clave; }
    public String getSeriacion() { return seriacion; }
    public String getNombre() { return nombre; }
    public String getCuatrimestre() { return cuatrimestre; }

    @Override
    public String toString() {
        return clave + " - " + nombre + " (Seriación: " + seriacion + ") - " + cuatrimestre;
    }
}

package modelos;

import java.io.Serializable;

public class Materia implements Serializable {
   private static final long serialVersionUID = 1L;
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

    // Getters
    public String getClave() { return clave; }
    public String getSeriacion() { return seriacion; }
    public String getNombre() { return nombre; }
    public String getCuatrimestre() { return cuatrimestre; }

    // 🔧 Setter que faltaba
    public void setCuatrimestre(String cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    @Override
    public String toString() {
        return clave + " - " + nombre + " (Seriación: " + seriacion + ") - " + cuatrimestre;
    }
}

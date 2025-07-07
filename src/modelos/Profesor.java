package modelos;
import java.io.Serializable;
import java.util.ArrayList; // Import for ArrayList
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Profesor implements Serializable {
    private String id;
    private String nombre;
    private String especialidad;
    private List<Grupo> gruposAsignados; // New field to track assigned groups

    // Mapeo de especialidades (lo moví al inicio como variable de clase)
    private static final Map<String, List<String>> ESPECIALIDAD_MAP = new HashMap<>();
    static {
        ESPECIALIDAD_MAP.put("BASE DE DATOS", Arrays.asList("BASE DE DATOS", "BASES DE DATOS", "DATOS"));
        ESPECIALIDAD_MAP.put("MATEMATICAS", Arrays.asList("MATEMÁTICAS", "ÁLGEBRA", "CÁLCULO", "ESTADÍSTICA"));
        ESPECIALIDAD_MAP.put("PROGRAMACIÓN", Arrays.asList("PROGRAMACIÓN", "SOFTWARE", "WEB"));
        ESPECIALIDAD_MAP.put("TELEMATICA", Arrays.asList("TELEMÁTICA", "REDES"));
        ESPECIALIDAD_MAP.put("HARDWARE", Arrays.asList("HARDWARE", "ARQUITECTURA"));
        ESPECIALIDAD_MAP.put("ADMINISTRACIÓN", Arrays.asList("ADMINISTRACIÓN", "GESTIÓN", "CALIDAD"));
        ESPECIALIDAD_MAP.put("SISTEMAS OPERATIVOS", Arrays.asList("SISTEMAS OPERATIVOS", "ENSAMBLADOR"));
        ESPECIALIDAD_MAP.put("INGENIERIA EN SOFTWARE", Arrays.asList("INGENIERIA EN SOFTWARE", "DISEÑO DE SOFTWARE", "DESARROLLO DE SOFTWARE"));
        ESPECIALIDAD_MAP.put("TECNOLOGÍA MOVIL", Arrays.asList("TECNOLOGÍA MÓVIL", "MULTIMEDIA"));
        ESPECIALIDAD_MAP.put("TEORÍA COMPUTACIONAL", Arrays.asList("TEORÍA COMPUTACIONAL"));
        ESPECIALIDAD_MAP.put("DERECHO", Arrays.asList("DERECHO", "ÉTICA"));
        ESPECIALIDAD_MAP.put("FÍSICA", Arrays.asList("FÍSICA", "CIRCUITOS"));
        ESPECIALIDAD_MAP.put("INVESTIGACIÓN", Arrays.asList("INVESTIGACIÓN", "METODOLOGÍA", "OPERACIONES"));
        ESPECIALIDAD_MAP.put("CREATIVIDAD", Arrays.asList("CREATIVIDAD", "EMPRENDEDORA", "ESTRATEGIAS"));
        ESPECIALIDAD_MAP.put("MANTENIMIENTO", Arrays.asList("MANTENIMIENTO", "EQUIPOS"));
        ESPECIALIDAD_MAP.put("SEMINARIO", Arrays.asList("SEMINARIO", "APLICACIONES", "CALIDAD"));
    }

    public Profesor(String id, String nombre, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.gruposAsignados = new ArrayList<>(); // Initialize the list
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecialidad() { return especialidad; }
    public List<Grupo> getGruposAsignados() { return gruposAsignados; } // Getter for assigned groups

    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    // New methods to manage assigned groups
    public void agregarGrupoAsignado(Grupo grupo) {
        if (!this.gruposAsignados.contains(grupo)) {
            this.gruposAsignados.add(grupo);
        }
    }

    public void eliminarGrupoAsignado(Grupo grupo) {
        this.gruposAsignados.remove(grupo);
    }

    // Method to check if the professor is available at a given time
          public boolean isDisponible(String nuevoHorario) {
          if (nuevoHorario == null || nuevoHorario.trim().isEmpty()) {
              return true; // Si no se proporciona un horario, se asume que está disponible
          }
          // Verificar si el profesor ya tiene un grupo asignado en el mismo horario
          for (Grupo g : gruposAsignados) {
              if (g.getHorario() != null && g.getHorario().equalsIgnoreCase(nuevoHorario)) {
                  return false; // Conflicto encontrado
              }
          }
          return true; // Disponible
      }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n");
        sb.append("Nombre: ").append(nombre).append("\n");
        sb.append("Especialidad: ").append(especialidad).append("\n");
        sb.append("Grupos Asignados: ");
        if (gruposAsignados.isEmpty()) {
            sb.append("Ninguno");
        } else {
            sb.append(gruposAsignados.stream()
                .map(Grupo::getIdGrupo)
                .collect(Collectors.joining(", ")));
        }
        return sb.toString();
    }

    // Método para obtener lista de especialidades (áreas) separadas
    public List<String> getEspecialidadesList() {
        if (especialidad == null || especialidad.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(especialidad.toUpperCase().split("\\s+Y\\s+"))
            .map(String::trim)
            .collect(Collectors.toList());
    }

   public boolean puedeImpartir(Materia materia) {
    if (materia == null || materia.getNombre() == null || this.especialidad == null) {
        return false;
    }

    String nombreMateria = materia.getNombre().toUpperCase();
    String especialidadUpper = this.especialidad.toUpperCase();

    // Relaciones específicas para programación
    if (especialidadUpper.contains("PROGRAM") || 
        especialidadUpper.contains("SOFTWARE") || 
        especialidadUpper.contains("COMPUTACIONAL")) {
        if (nombreMateria.contains("PROGRAM") || 
            nombreMateria.equals("FUNDAMENTOS DE PROGRAMACIÓN")) {
            return true;
        }
    }

    // Verificación con el mapa de especialidades
    for (Map.Entry<String, List<String>> entry : ESPECIALIDAD_MAP.entrySet()) {
        if (especialidadUpper.contains(entry.getKey())) {
            for (String keyword : entry.getValue()) {
                if (nombreMateria.contains(keyword)) {
                    return true;
                }
            }
        }
    }

    return false;
}
}
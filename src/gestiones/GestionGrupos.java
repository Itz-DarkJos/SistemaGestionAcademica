package gestiones;

import modelos.Grupo;
import modelos.Materia;
import modelos.Profesor;
import utilidades.FileManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionGrupos {

    private static final String ARCHIVO_GRUPOS = "data/grupos.txt";
    private static final String ARCHIVO_MATERIAS_PLAN_B = "data/materias_plan_b.txt";

    private static List<Grupo> grupos = new ArrayList<>();
    

    public static void menuGrupos() {
        cargarGrupos();
        inicializarGruposPlanA();

        String[] opciones = {
            "Agregar Grupo (Plan B)",
            "Asignar Materia (Plan B)",
            "Asignar Profesor (Plan B)",
            "Consultar Grupos",
            "Guardar y Regresar"
        };

        int opcion;
        do {
            opcion = JOptionPane.showOptionDialog(
                    null,
                    "Gestión de Grupos\nSeleccione una opción:",
                    "Grupos",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            switch (opcion) {
                case 0 -> agregarGrupoPlanB();
                case 1 -> asignarMateriaPlanB();
                case 2 -> asignarProfesorPlanB();
                case 3 -> consultarGrupos();
                case 4 -> {
                    guardarGrupos();
                    JOptionPane.showMessageDialog(null, "Grupos guardados correctamente.");
                }
                default -> {}
            }
        } while (opcion != 4);
    }

    private static void cargarGrupos() {
        Object obj = FileManager.cargarDatos(ARCHIVO_GRUPOS);
        if (obj != null) {
            grupos = (List<Grupo>) obj;
            // After loading, re-establish professor-group relationships
            for (Grupo g : grupos) {
                if (g.getProfesor() != null) {
                    Profesor p = GestionProfesores.buscarProfesor(g.getProfesor().getId());
                    if (p != null) {
                        p.agregarGrupoAsignado(g);
                    }
                }
            }
        } else {
            grupos = new ArrayList<>();
        }
    }

    private static void guardarGrupos() {
        FileManager.guardarDatos(ARCHIVO_GRUPOS, grupos);
    }

    private static void inicializarGruposPlanA() {
        boolean existePlanA = grupos.stream().anyMatch(Grupo::esPlanA);
        if (existePlanA) return;

        for (int cuatri = 1; cuatri <= 9; cuatri++) {
            for (int numGrupo = 1; numGrupo <= 4; numGrupo++) {
                String idGrupo = cuatri + "19" + numGrupo;
                if (buscarGrupo(idGrupo) == null) {
                    Grupo grupo = new Grupo(idGrupo, true);
                    grupos.add(grupo);
                }
            }
        }
    }

    private static void agregarGrupoPlanB() {
        List<Grupo> disponiblesPlanA = grupos.stream()
                .filter(Grupo::esPlanA)
                .filter(g -> buscarGrupoPlanB(g.getIdGrupo()) == null)
                .collect(Collectors.toList());

        if (disponiblesPlanA.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Todos los grupos Plan A ya están en Plan B.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] opciones = disponiblesPlanA.stream()
                .map(Grupo::getIdGrupo)
                .toArray(String[]::new);

        String seleccionado = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione grupo base Plan A para agregar a Plan B:",
                "Agregar Grupo Plan B",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccionado == null) return;

        Grupo nuevoGrupo = new Grupo(seleccionado, false);
        grupos.add(nuevoGrupo);
        JOptionPane.showMessageDialog(null,
                "Grupo " + seleccionado + " agregado a Plan B. Ahora puede asignarle materias.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static List<Materia> cargarMateriasPlanB() {
        Object obj = FileManager.cargarDatos(ARCHIVO_MATERIAS_PLAN_B);
        if (obj != null) {
            return (List<Materia>) obj;
        }
        return new ArrayList<>();
    }

    private static void asignarMateriaPlanB() {
        // Filtrar grupos Plan B
       List<Grupo> gruposPlanB = grupos.stream()
            .filter(g -> !g.esPlanA())
            .filter(g -> {
                try {
                    int cuatri = Integer.parseInt(g.getIdGrupo().substring(0, 1));
                    return cuatri >= 7 && cuatri <= 9;
                } catch (NumberFormatException e) {
                    return false;
                }
            })
            .collect(Collectors.toList());
        String[] opcionesGrupos = gruposPlanB.stream()
                .map(Grupo::getIdGrupo)
                .toArray(String[]::new);

        String idGrupo = (String) JOptionPane.showInputDialog(null,
                "Seleccione grupo Plan B para asignar materia:",
                "Asignar Materia",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesGrupos,
                opcionesGrupos[0]);

        if (idGrupo == null) return;

        Grupo grupo = grupos.stream()
        .filter(g -> g.getIdGrupo().equalsIgnoreCase(idGrupo))
        .filter(g -> !g.esPlanA()) // Solo Plan B
        .findFirst()
        .orElse(null);
        if (grupo == null) return;

        int cuatrimestre;
        try {
            cuatrimestre = Integer.parseInt(idGrupo.substring(0, 1));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Formato inválido de idGrupo para determinar cuatrimestre.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Materia> materiasPlanB = cargarMateriasPlanB();

        // Obtener claves de materias ya asignadas para no repetir
         List<String> clavesAsignadas = grupo.getMaterias().stream()
             .map(Materia::getClave)
             .collect(Collectors.toList());

         List<Materia> disponibles = materiasPlanB.stream()
          .filter(m -> {
              try {
                  // Normalizar el cuatrimestre de la materia antes de comparar
                  String cuatriMateria = normalizarCuatrimestre(m.getCuatrimestre());
                  return cuatriMateria.equals(String.valueOf(cuatrimestre))
                      && !clavesAsignadas.contains(m.getClave());
              } catch (NumberFormatException e) {
                  return false;
              }
          })
          .collect(Collectors.toList());

        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No hay materias disponibles para el " + cuatrimestre + "° cuatrimestre " +
                            "o todas ya están asignadas en este grupo.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] opcionesMaterias = disponibles.stream()
                .map(m -> m.getClave() + " - " + m.getNombre())
                .toArray(String[]::new);

        String materiaSeleccionada = (String) JOptionPane.showInputDialog(null,
                "Seleccione materia para el grupo " + idGrupo + ":",
                "Seleccionar Materia",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesMaterias,
                opcionesMaterias[0]);

        if (materiaSeleccionada == null) return;

        String claveMateria = materiaSeleccionada.split(" - ")[0];

        Materia materia = disponibles.stream()
                .filter(m -> m.getClave().equals(claveMateria))
                .findFirst()
                .orElse(null);

        if (materia != null) {
            boolean agregado = grupo.agregarMateria(materia);
            if (agregado) {
                JOptionPane.showMessageDialog(null,
                        "Materia asignada correctamente:\n" +
                                "Grupo: " + idGrupo + "\n" +
                                "Materia: " + materia.getNombre(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "La materia ya está asignada en este grupo.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private static void asignarProfesorPlanB() {
    // Filtrar grupos Plan B que tengan materias asignadas
    List<Grupo> gruposConMaterias = grupos.stream()
            .filter(g -> !g.esPlanA())
            .filter(g -> !g.getMaterias().isEmpty())
            .collect(Collectors.toList());

    if (gruposConMaterias.isEmpty()) {
        JOptionPane.showMessageDialog(null,
                "No hay grupos Plan B con materias asignadas.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    // Seleccionar grupo
    String[] opcionesGrupos = gruposConMaterias.stream()
            .map(g -> g.getIdGrupo() + " - " +
                 g.getMaterias().stream().map(Materia::getNombre).collect(Collectors.joining(", ")))
            .toArray(String[]::new);

    String seleccionGrupo = (String) JOptionPane.showInputDialog(null,
            "Seleccione grupo para asignar profesor:",
            "Asignar Profesor",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcionesGrupos,
            opcionesGrupos[0]);

    if (seleccionGrupo == null) return;

    String idGrupo = seleccionGrupo.split(" - ")[0];
    Grupo grupo = buscarGrupoPlanB(idGrupo);
    if (grupo == null) return;

    // Seleccionar materia del grupo
    List<Materia> materiasGrupo = grupo.getMaterias();
    String[] opcionesMaterias = materiasGrupo.stream()
            .map(m -> m.getClave() + " - " + m.getNombre())
            .toArray(String[]::new);

    String seleccionMateria = (String) JOptionPane.showInputDialog(null,
            "Seleccione materia para asignar profesor:",
            "Seleccionar Materia",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcionesMaterias,
            opcionesMaterias[0]);

    if (seleccionMateria == null) return;

    String claveMateria = seleccionMateria.split(" - ")[0];
    Materia materia = materiasGrupo.stream()
            .filter(m -> m.getClave().equals(claveMateria))
            .findFirst()
            .orElse(null);
    if (materia == null) return;

    // Asignar horario
    String horario = JOptionPane.showInputDialog(null,
            "Ingrese horario para la materia " + materia.getNombre() + " en el grupo " + grupo.getIdGrupo() +
            "\n(ejemplo: Lunes 10:00-12:00):",
            "Asignar Horario",
            JOptionPane.QUESTION_MESSAGE);

    if (horario == null || horario.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null,
                "Horario inválido.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Filtrar profesores que pueden impartir la materia y están disponibles en el horario
    List<Profesor> profesoresDisponibles = GestionProfesores.getProfesores().stream()
            .filter(p -> p.puedeImpartir(materia))
            .filter(p -> p.isDisponible(horario)) // Check availability for the proposed time
            .collect(Collectors.toList());

    // Debugging: Mostrar la cantidad de profesores disponibles
    System.out.println("Profesores disponibles: " + profesoresDisponibles.size());

    if (profesoresDisponibles.isEmpty()) {
        JOptionPane.showMessageDialog(null,
                "No hay profesores disponibles con especialidad en: " + materia.getNombre() +
                " y que estén libres en el horario " + horario + ".",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    // Mostrar profesores disponibles
    String[] opcionesProfesores = profesoresDisponibles.stream()
            .map(p -> p.getId() + " - " + p.getNombre() + " (" + p.getEspecialidad() + ")")
            .toArray(String[]::new);

    String seleccionProfesor = (String) JOptionPane.showInputDialog(null,
            "Seleccione profesor para " + materia.getNombre() + " en el horario " + horario + ":",
            "Seleccionar Profesor",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcionesProfesores,
            opcionesProfesores[0]);

    if (seleccionProfesor == null) return;

    String idProfesor = seleccionProfesor.split(" - ")[0];
    Profesor profesor = GestionProfesores.buscarProfesor(idProfesor);

    if (profesor == null) return;

    // If there was a previous professor assigned to this group, remove the group from their assigned list
    if (grupo.getProfesor() != null) {
        Profesor oldProfesor = GestionProfesores.buscarProfesor(grupo.getProfesor().getId());
        if (oldProfesor != null) {
            oldProfesor.eliminarGrupoAsignado(grupo);
        }
    }

    // Assign professor to the group
    grupo.setProfesor(profesor);
    grupo.setHorario(horario);
    profesor.agregarGrupoAsignado(grupo); // Add the group to the professor's assigned list

    JOptionPane.showMessageDialog(null,
            "Profesor asignado correctamente:\n" +
            "Grupo: " + grupo.getIdGrupo() + "\n" +
            "Materia: " + materia.getNombre() + "\n" +
            "Profesor: " + profesor.getNombre() + "\n" +
            "Horario: " + horario,
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
}

    private static void consultarGrupos() {
        if (grupos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay grupos registrados.", "Consulta", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("=== LISTA DE GRUPOS ===\n");

        for (int cuatri = 1; cuatri <= 9; cuatri++) {
          final int cuatriFinal = cuatri;

          sb.append("\n--- CUATRIMESTRE ").append(cuatriFinal).append(" ---\n");

          sb.append("\n[Plan A]\n");
          grupos.stream()
              .filter(g -> g.esPlanA() && g.getIdGrupo().startsWith(String.valueOf(cuatriFinal)))
              .forEach(g -> sb.append(g.toString()).append("\n\n"));

          sb.append("\n[Plan B]\n");
          grupos.stream()
              .filter(g -> !g.esPlanA() && g.getIdGrupo().startsWith(String.valueOf(cuatriFinal)))
              .forEach(g -> sb.append(g.toString()).append("\n\n"));
      }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(600, 400));
        JOptionPane.showMessageDialog(null, scroll, "Grupos Registrados", JOptionPane.INFORMATION_MESSAGE);
    }

    // Cambia la firma del método para que acepte solo el ID
      public static Grupo buscarGrupo(String idGrupo) {
          return grupos.stream()
                  .filter(g -> g.getIdGrupo().equalsIgnoreCase(idGrupo))
                  .findFirst()
                  .orElse(null);
      }

      // Y crea un método separado para buscar solo grupos Plan B
      public static Grupo buscarGrupoPlanB(String idGrupo) {
          return grupos.stream()
                  .filter(g -> g.getIdGrupo().equalsIgnoreCase(idGrupo))
                  .filter(g -> !g.esPlanA()) // Solo Plan B
                  .findFirst()
                  .orElse(null);
   }
       public static String normalizarCuatrimestre(String nombre) {
       return switch (nombre.toUpperCase()) {
           case "PRIMER CUATRIMESTRE" -> "1";
           case "SEGUNDO CUATRIMESTRE" -> "2";
           case "TERCER CUATRIMESTRE" -> "3";
           case "CUARTO CUATRIMESTRE" -> "4";
           case "QUINTO CUATRIMESTRE" -> "5";
           case "SEXTO CUATRIMESTRE" -> "6";
           case "SÉPTIMO CUATRIMESTRE", "SEPTIMO CUATRIMESTRE" -> "7";
           case "OCTAVO CUATRIMESTRE" -> "8";
           case "NOVENO CUATRIMESTRE" -> "9";
           default -> nombre;
       };
   }
   public static List<Grupo> getGrupos() {
      return grupos;
   }
}

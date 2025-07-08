
package gestiones;

import modelos.Alumno;
import modelos.Grupo;
import utilidades.FileManager;
import utilidades.Validaciones;
import java.util.Comparator;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GestionAlumnos {
    private static final String ARCHIVO_ALUMNOS = "data/alumnos.txt";
    private static List<Alumno> alumnos = new ArrayList<>();

    public static void menuAlumnos() {
        cargarAlumnos();

        String[] opciones = {
            "Agregar Alumno",
            "Consultar Alumnos",
            "Modificar Alumno",
            "Borrar Alumno",
            "Regresar"
        };

        int opcion;
        do {
            opcion = JOptionPane.showOptionDialog(
                null,
                "Gestión de Alumnos\nSeleccione una opción:",
                "Alumnos",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );

            switch (opcion) {
                case 0 -> agregarAlumno();
                case 1 -> consultarAlumnos(); // En tu switch del menú de alumnos
                case 2 -> modificarAlumno();
                case 3 -> borrarAlumno();
                case 4 -> guardarAlumnos();
                default -> {}
            }
        } while (opcion != 4);
    }

     private static void agregarAlumno() {
        // Sección para seleccionar plan
        String[] planes = {"Plan A", "Plan B"};
        String planSeleccionado = (String) JOptionPane.showInputDialog(
            null, 
            "Seleccione el Plan del alumno:", 
            "Plan de Estudio", 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            planes, 
            planes[0]
        );

        if (planSeleccionado == null) {
            return;
        }

        // Configurar cuatrimestres disponibles
        Integer[] cuatrimestresDisponibles;
        if (planSeleccionado.equals("Plan A")) {
            cuatrimestresDisponibles = new Integer[]{6, 7, 8, 9};
        } else {
            cuatrimestresDisponibles = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        }

        // Componentes del formulario
        String matricula = generarMatricula();
        JTextField nombreField = new JTextField();
        JTextField apellidoPaternoField = new JTextField();
        JTextField apellidoMaternoField = new JTextField();
        JComboBox<Integer> cuatrimestreCombo = new JComboBox<>(cuatrimestresDisponibles);
        JComboBox<String> grupoCombo = new JComboBox<>();

        // Configurar grupos disponibles
        actualizarGruposCombo(grupoCombo, (int) cuatrimestreCombo.getSelectedItem(), planSeleccionado);
        cuatrimestreCombo.addActionListener(e -> 
            actualizarGruposCombo(grupoCombo, (int) cuatrimestreCombo.getSelectedItem(), planSeleccionado)
        );

        // Mostrar diálogo de entrada
        Object[] message = {
            "Plan:", planSeleccionado,
            "Matrícula:", matricula,
            "Nombre:", nombreField,
            "Apellido Paterno:", apellidoPaternoField,
            "Apellido Materno:", apellidoMaternoField,
            "Cuatrimestre:", cuatrimestreCombo,
            "Grupo:", grupoCombo
        };

        int option = JOptionPane.showConfirmDialog(
            null, 
            message, 
            "Agregar Alumno", 
            JOptionPane.OK_CANCEL_OPTION
        );

        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        // Validar datos
        String nombre = nombreField.getText().trim();
        String apellidoPaterno = apellidoPaternoField.getText().trim();
        String apellidoMaterno = apellidoMaternoField.getText().trim();

        if (nombre.isEmpty() || apellidoPaterno.isEmpty() || 
            !Validaciones.esSoloLetras(nombre) || 
            !Validaciones.esSoloLetras(apellidoPaterno) || 
            (apellidoMaterno.length() > 0 && !Validaciones.esSoloLetras(apellidoMaterno))) {
            JOptionPane.showMessageDialog(
                null, 
                "Datos no válidos. Nombre y apellido paterno son obligatorios y solo pueden contener letras.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Crear y guardar alumno
        Alumno nuevoAlumno = new Alumno(
            matricula,
            nombre,
            apellidoPaterno,
            apellidoMaterno,
            (int) cuatrimestreCombo.getSelectedItem(),
            (String) grupoCombo.getSelectedItem(),
            planSeleccionado.equals("Plan A") ? "A" : "B"
        );

        alumnos.add(nuevoAlumno);
        JOptionPane.showMessageDialog(null, "Alumno registrado exitosamente");
    }

    private static void modificarAlumno() {
    if (alumnos.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay alumnos registrados");
        return;
    }

    String matricula = JOptionPane.showInputDialog("Ingrese la matrícula del alumno:");
    if (matricula == null || matricula.trim().isEmpty()) {
        return;
    }

    Alumno alumno = null;
    for (Alumno a : alumnos) {
        if (a.getMatricula().equalsIgnoreCase(matricula)) {
            alumno = a;
            break;
        }
    }

    if (alumno == null) {
        JOptionPane.showMessageDialog(null, "Alumno no encontrado");
        return;
    }

    // Formulario de modificación
    JTextField nombreField = new JTextField(alumno.getNombre());
    JTextField apellidoPaternoField = new JTextField(alumno.getApellidoPaterno());
    JTextField apellidoMaternoField = new JTextField(alumno.getApellidoMaterno());

    // Configurar cuatrimestres según plan
    Integer[] cuatrimestresDisponibles;
    if (alumno.getPlan().equals("A")) {
        cuatrimestresDisponibles = new Integer[]{6, 7, 8, 9};
    } else {
        cuatrimestresDisponibles = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
    }

    JComboBox<Integer> cuatrimestreCombo = new JComboBox<>(cuatrimestresDisponibles);
    cuatrimestreCombo.setSelectedItem(alumno.getCuatrimestre());
    JComboBox<String> grupoCombo = new JComboBox<>();
    actualizarGruposCombo(grupoCombo, alumno.getCuatrimestre(), alumno.getPlan().equals("A") ? "Plan A" : "Plan B");
    grupoCombo.setSelectedItem(alumno.getGrupo());

    // Almacenar el valor seleccionado en una variable final
    String planActual = alumno.getPlan().equals("A") ? "Plan A" : "Plan B";

    // Listener para cambios de cuatrimestre
    cuatrimestreCombo.addActionListener(e -> {
        int cuatrimestreSeleccionado = (int) cuatrimestreCombo.getSelectedItem();
        actualizarGruposCombo(grupoCombo, cuatrimestreSeleccionado, planActual);
    });

    Object[] message = {
        "Plan:", planActual,
        "Matrícula:", alumno.getMatricula(),
        "Nombre:", nombreField,
        "Apellido Paterno:", apellidoPaternoField,
        "Apellido Materno:", apellidoMaternoField,
        "Cuatrimestre:", cuatrimestreCombo,
        "Grupo:", grupoCombo
    };

    int option = JOptionPane.showConfirmDialog(
        null, 
        message, 
        "Modificar Alumno", 
        JOptionPane.OK_CANCEL_OPTION
    );

    if (option != JOptionPane.OK_OPTION) {
        return;
    }

    // Actualizar datos
    alumno.setNombre(nombreField.getText().trim());
    alumno.setApellidoPaterno(apellidoPaternoField.getText().trim());
    alumno.setApellidoMaterno(apellidoMaternoField.getText().trim());
    alumno.setCuatrimestre((int) cuatrimestreCombo.getSelectedItem());
    alumno.setGrupo((String) grupoCombo.getSelectedItem());

    JOptionPane.showMessageDialog(null, "Alumno modificado exitosamente");
}

  private static void consultarAlumnos() {
    if (alumnos.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay alumnos registrados.");
        return;
    }

    Integer[] cuatrimestres = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    Integer cuatriSeleccionado = (Integer) JOptionPane.showInputDialog(
            null,
            "Seleccione el cuatrimestre:",
            "Consulta por Cuatrimestre",
            JOptionPane.QUESTION_MESSAGE,
            null,
            cuatrimestres,
            cuatrimestres[0]
    );

    if (cuatriSeleccionado == null) return; // Usuario canceló

    List<Alumno> alumnosDelCuatri = alumnos.stream()
            .filter(a -> a.getCuatrimestre() == cuatriSeleccionado)
            .sorted(Comparator.comparing(Alumno::getGrupo).thenComparing(Alumno::getApellidoPaterno))
            .toList();

    if (alumnosDelCuatri.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay alumnos en el cuatrimestre seleccionado.");
        return;
    }

    StringBuilder sb = new StringBuilder("Alumnos del Cuatrimestre ").append(cuatriSeleccionado).append(":\n\n");

    for (Alumno a : alumnosDelCuatri) {
        sb.append("• ").append(a.getMatricula()).append(" - ")
          .append(a.getNombre()).append(" ")
          .append(a.getApellidoPaterno()).append(" ")
          .append(a.getApellidoMaterno()).append(" | Grupo: ")
          .append(a.getGrupo()).append("\n");
    }

    JTextArea textArea = new JTextArea(sb.toString());
    textArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new java.awt.Dimension(550, 450));

    JOptionPane.showMessageDialog(null, scrollPane, "Resultado de la Consulta", JOptionPane.INFORMATION_MESSAGE);
}



    private static void borrarAlumno() {
        if (alumnos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay alumnos registrados");
            return;
        }

        String matricula = JOptionPane.showInputDialog("Ingrese la matrícula del alumno a borrar:");
        if (matricula == null || matricula.trim().isEmpty()) {
            return;
        }

        Alumno alumno = null;
        for (Alumno a : alumnos) {
            if (a.getMatricula().equalsIgnoreCase(matricula)) {
                alumno = a;
                break;
            }
        }

        if (alumno == null) {
            JOptionPane.showMessageDialog(null, "Alumno no encontrado");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            null, 
            "¿Está seguro de borrar al alumno?\n" + alumno.toString(), 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            alumnos.remove(alumno);
            JOptionPane.showMessageDialog(null, "Alumno borrado exitosamente");
        }
    }


    private static void actualizarGruposCombo(JComboBox<String> combo, int cuatrimestre, String plan) {
        combo.removeAllItems();
        String prefijoPlan = plan.equals("Plan A") ? "A" : "B";

        GestionGrupos.getGrupos().stream()
            .filter(g -> g.getCuatrimestre() == cuatrimestre)
            .filter(g -> g.getIdGrupo().startsWith(prefijoPlan))
            .forEach(g -> combo.addItem(g.getIdGrupo()));
    }

    private static String generarMatricula() {
        int consecutivo = alumnos.size() + 1;
        return String.format("A%04d", consecutivo);
    }

    private static void cargarAlumnos() {
        Object obj = FileManager.cargarDatos(ARCHIVO_ALUMNOS);
if (obj != null) {
    alumnos = (List<Alumno>) obj;
} else {
            alumnos = new ArrayList<>();

            // Alumnos del Plan A, 9no cuatrimestre
            alumnos.add(new Alumno("A0001", "Josue", "Gomez", "Solano", 9, "A9A", "A"));
            alumnos.add(new Alumno("A0002", "Ricardo", "Adorno", "Avila", 9, "A9A", "A"));
            alumnos.add(new Alumno("A0003", "Jorge Emilio", "Paz", "Medina", 9, "A9A", "A"));
            alumnos.add(new Alumno("A0004", "Emiliano", "Martinez", "Perez", 9, "A9A", "A"));
            alumnos.add(new Alumno("A0005", "Luis Gerardo", "Sanchez", "Gamez", 9, "A9A", "A"));
            alumnos.add(new Alumno("A0006", "Ricardo", "Suarez", "Barreta", 9, "A9A", "A"));
            alumnos.add(new Alumno("A0007", "Einar Maximiliano", "Puc", "Ramirez", 9, "A9A", "A"));
            alumnos.add(new Alumno("A0008", "Daniel", "Perez", "Soriano", 9, "A9A", "A"));
            alumnos.add(new Alumno("A0009", "Cesar Daniel", "Lucas", "", 9, "A9A", "A"));

            // Alumnos del Plan A, 6to cuatrimestre
            for (int i = 1; i <= 15; i++) {
                alumnos.add(new Alumno(String.format("A%04d", alumnos.size() + 1),
                        "NombreA6_" + i, "ApellidoP" + i, "ApellidoM" + i,
                        6, "A6A", "A"));
            }

            // Alumnos del Plan B, 3er cuatrimestre
            for (int i = 1; i <= 17; i++) {
                alumnos.add(new Alumno(String.format("A%04d", alumnos.size() + 1),
                        "NombreB3_" + i, "ApellidoP" + i, "ApellidoM" + i,
                        3, "B3A", "B"));
            }

            guardarAlumnos();
        }
    }

    private static void guardarAlumnos() {
        FileManager.guardarDatos(ARCHIVO_ALUMNOS, alumnos);
    }

    public static List<Alumno> getAlumnos() {
        return alumnos;
    }
}

package gestiones;
import modelos.Profesor;
import utilidades.FileManager;
import utilidades.Validaciones;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Font;

public class GestionProfesores {
    private static final String ARCHIVO_PROFESORES = "profesores.txt";
    private static final List<Profesor> profesores = new ArrayList<>();

    public static void menuProfesores() {
        cargarProfesores();
        String[] opciones = {"Capturar Profesor", "Consultar Profesores", "Borrar Profesor", "Regresar"};
        int opcion;
        do {
            opcion = JOptionPane.showOptionDialog(null, "Gestión de Profesores\nSeleccione una opción:",
                    "Profesores", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opciones, opciones[0]);

            switch(opcion) {
                case 0:
                    capturarProfesor();
                    guardarProfesores();
                    break;
                case 1:
                    consultarProfesores();
                    break;
                case 2:
                    borrarProfesor();
                    guardarProfesores();
                    break;
                case 3:
                    guardarProfesores();
                    break;
                default:
                    break;
            }
        } while (opcion != 3);
    }

    private static void capturarProfesor() {
        String[] opcionesId = {"Ingresar ID manualmente", "Generar ID automático"};
        int opcionId = JOptionPane.showOptionDialog(
                null,
                "Seleccione cómo desea asignar el ID del profesor:",
                "Asignación de ID",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesId,
                opcionesId[0]);

        String id = null;

        if (opcionId == 0) { // Ingresar manualmente
            id = JOptionPane.showInputDialog("Ingrese el ID del profesor:");
            if (id == null || id.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El ID no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (Profesor p : profesores) {
                if (p.getId().equalsIgnoreCase(id)) {
                    JOptionPane.showMessageDialog(null, "Ya existe un profesor con este ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } else if (opcionId == 1) { // Generar automático
            id = generarIdConsecutivo();
            if (id == null) {
                JOptionPane.showMessageDialog(null, "Error al generar ID automático", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "ID generado automáticamente: " + id, "ID Asignado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            return; // Cancelado
        }

        for (Profesor p : profesores) {
            if (p.getId().equalsIgnoreCase(id)) {
                JOptionPane.showMessageDialog(null, "Ya existe un profesor con este ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        JTextField nombreField = new JTextField();
        JTextField apellidoPaternoField = new JTextField();
        JTextField apellidoMaternoField = new JTextField();

        Object[] message = {
                "Nombre:", nombreField,
                "Apellido Paterno:", apellidoPaternoField,
                "Apellido Materno:", apellidoMaternoField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Ingrese datos del profesor", JOptionPane.OK_CANCEL_OPTION);

        if (option != JOptionPane.OK_OPTION) {
            return; // Usuario canceló
        }

        String nombre = nombreField.getText().trim();
        String apellidoPaterno = apellidoPaternoField.getText().trim();
        String apellidoMaterno = apellidoMaternoField.getText().trim();

        if (nombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() ||
                !Validaciones.esSoloLetras(nombre) ||
                !Validaciones.esSoloLetras(apellidoPaterno) ||
                !Validaciones.esSoloLetras(apellidoMaterno)) {
            JOptionPane.showMessageDialog(null, "Datos no válidos. Asegúrese de llenar todos los campos con solo letras.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

// Concatenar nombre completo en el formato que uses en Profesor
        String nombreCompleto = apellidoPaterno + " " + apellidoMaterno + " " + nombre;

        String[] especialidades = {
                "Programación y Desarrollo de Software",
                "Bases de Datos",
                "Matemáticas y Estadística",
                "Física y Electrónica",
                "Sistemas Operativos y Ensamblador",
                "Redes y Telemática",
                "Ingeniería de Software y Calidad",
                "Administración y Gestión",
                "Derecho y Ética",
                "Hardware y Mantenimiento",
                "Multimedia y Tecnología Móvil",
                "Teoría Computacional",
                "Seminarios Generales",
                "Estrategias y Metodología"
        };

        String especialidad = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la especialidad del profesor:",
                "Especialidad",
                JOptionPane.QUESTION_MESSAGE,
                null,
                especialidades,
                especialidades[0]);

        Profesor nuevoProfesor = new Profesor(id, nombre, especialidad);
        profesores.add(nuevoProfesor);
        JOptionPane.showMessageDialog(null, "Profesor registrado con éxito");
    }

    private static void consultarProfesores() {
        if (profesores.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay profesores registrados", "Consulta", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();

        // Encabezados de columna tipo Excel
        sb.append(String.format("%-10s %-40s %-30s\n", "ID", "Nombre", "Especialidad"));
        sb.append("----------------------------------------------------------------------------\n");

        for (Profesor p : profesores) {
            sb.append(String.format("%-10s %-40s %-30s\n",
                    p.getId(),
                    p.getNombre(),
                    p.getEspecialidad()));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Fuente monoespaciada para alinear
        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(null, scrollPane, "Lista de Profesores", JOptionPane.INFORMATION_MESSAGE);
    }


    private static void borrarProfesor() {
        if (profesores.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay profesores registrados", "Borrar", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String id = JOptionPane.showInputDialog("Ingrese el ID del profesor a borrar:");
        if (id == null || id.trim().isEmpty()) {
            return;
        }

        Profesor profesorABorrar = null;
        for (Profesor p : profesores) {
            if (p.getId().equalsIgnoreCase(id)) {
                profesorABorrar = p;
                break;
            }
        }

        if (profesorABorrar != null) {
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro que desea borrar al profesor?\n" + profesorABorrar.toString(),
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                profesores.remove(profesorABorrar);
                JOptionPane.showMessageDialog(null, "Profesor eliminado con éxito");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró un profesor con ese ID", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void cargarProfesores() {
        List<String> lineas = FileManager.leerArchivo(ARCHIVO_PROFESORES);
        if (lineas.isEmpty()) {
            // Lista predefinida
            profesores.add(new Profesor("01", "BALDERAS GARCÍA JOSÉ ANTONIO", "BASE  DE DATOS"));
            profesores.add(new Profesor("02", "HERNÁNDEZ HERNÁNDEZ SONIA ERANDI", "ADMINISTRACIÓN Y GESTIÓN"));
            profesores.add(new Profesor("03", "GRAJALES OJEDA NORMA VIOLETA", "REDES Y TELEMATICA"));
            profesores.add(new Profesor("04", "LÓPEZ GUTIÉRREZ YOLANDA", "MATEMATICAS"));
            profesores.add(new Profesor("05", "MORALES MANZO ESMERALDA ZULMA", "INGENIERIA EN SOFTWARE"));
            profesores.add(new Profesor("06", "MUÑOZ ALVARADO MIGUEL ÁNGEL", "TECNOLOGÍA MOVIL"));
            profesores.add(new Profesor("07", "QUIJANO PAREDES FRANCISCO GABRIEL", "MATEMATICAS Y ESTADISTICA"));
            profesores.add(new Profesor("08", "RAMÍREZ PÉREZ NORMA ANGÉLICA", "SISTEMAS OPERATIVOS"));
            profesores.add(new Profesor("09", "RAYÓN RAMÍREZ AMADOR", "HARDWARE Y MANTENIMIENTO"));
            profesores.add(new Profesor("10", "SÁNCHEZ SALDÍVAR MIGUEL", "INGENIERIA DE SOTWARE"));
            profesores.add(new Profesor("11", "TORRES RAZO GRACIELA", "TEORÍA COMPUTACIONAL"));
            guardarProfesores();
        } else {
            for (String linea : lineas) {
                String[] partes = linea.split("\\|");
                if (partes.length == 3) {
                    profesores.add(new Profesor(partes[0], partes[1], partes[2]));
                }
            }
        }
    }

    private static void guardarProfesores() {
        List<String> lineas = new ArrayList<>();
        for (Profesor p : profesores) {
            lineas.add(p.getId() + "|" + p.getNombre() + "|" + p.getEspecialidad());
        }
        FileManager.escribirArchivo(ARCHIVO_PROFESORES, lineas);
    }

    public static Profesor buscarProfesor(String id) {
        for (Profesor profesor : profesores) {
            if (profesor.getId().equalsIgnoreCase(id)) {
                return profesor;
            }
        }
        return null;
    }
    
       public static List<Profesor> getProfesores() {
       return profesores;
   }

    private static String generarIdConsecutivo() {
        int id = 1;
        boolean existe;

        do {
            String idString = String.format("%02d", id);
            existe = false;
            for (Profesor p : profesores) {
                if (p.getId().equalsIgnoreCase(idString)) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                return idString;
            }
            id++;
        } while (id <= 99); // Puedes ajustar el rango máximo de IDs permitidos

        return null; // Si no se encuentra un ID disponible
    }


}

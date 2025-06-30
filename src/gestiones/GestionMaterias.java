package gestiones;

import modelos.Materia;
import utilidades.FileManager;
import utilidades.Validaciones;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class GestionMaterias {
    private static final String ARCHIVO_MATERIAS = "data/materias.txt";
    private static List<Materia> materias = new ArrayList<>();

    public static void menuMaterias() {
        cargarMaterias();
        String[] opciones = {"Capturar Materia", "Consultar Materias", "Borrar Materia", "Regresar"};
        int opcion;
        do {
            opcion = JOptionPane.showOptionDialog(null, "Gestión de Materias\nSeleccione una opción:", 
                    "Materias", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
                    null, opciones, opciones[0]);
            
            switch(opcion) {
                case 0:
                    capturarMateria();
                    break;
                case 1:
                    consultarMaterias();
                    break;
                case 2:
                    borrarMateria();
                    break;
                case 3:
                    guardarMaterias();
                    break;
                default:
                    break;
            }
        } while (opcion != 3);
    }

    private static void capturarMateria() {
        String clave = JOptionPane.showInputDialog("Ingrese la clave de la materia (Formato: ABC-123):");
        if (clave == null) return;
        
        if (!Validaciones.validarFormatoClave(clave)) {
            JOptionPane.showMessageDialog(null, "Formato de clave inválido. Debe ser ABC-123", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si la clave ya existe
        for (Materia m : materias) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                JOptionPane.showMessageDialog(null, "Ya existe una materia con esta clave", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String nombre = JOptionPane.showInputDialog("Ingrese el nombre de la materia:");
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String creditosStr = JOptionPane.showInputDialog("Ingrese los créditos (1-10):");
        if (creditosStr == null || !Validaciones.validarCreditos(creditosStr)) {
            JOptionPane.showMessageDialog(null, "Créditos inválidos. Debe ser un número entre 1 y 10", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int creditos = Integer.parseInt(creditosStr);
        Materia nuevaMateria = new Materia(clave.toUpperCase(), nombre, creditos);
        materias.add(nuevaMateria);
        JOptionPane.showMessageDialog(null, "Materia registrada con éxito");
    }

    private static void consultarMaterias() {
        if (materias.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay materias registradas", "Consulta", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("=== LISTA DE MATERIAS ===\n\n");
        for (Materia m : materias) {
            sb.append(m.toString()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Materias Registradas", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void borrarMateria() {
        if (materias.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay materias registradas", "Borrar", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String clave = JOptionPane.showInputDialog("Ingrese la clave de la materia a borrar:");
        if (clave == null || clave.trim().isEmpty()) {
            return;
        }

        Materia materiaABorrar = null;
        for (Materia m : materias) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                materiaABorrar = m;
                break;
            }
        }

        if (materiaABorrar != null) {
            // Verificar si la materia está asignada a algún grupo
            if (GestionGrupos.materiaEstaAsignada(clave)) {
                JOptionPane.showMessageDialog(null, "No se puede borrar. La materia está asignada a un grupo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(null, 
                    "¿Está seguro que desea borrar esta materia?\n" + materiaABorrar.toString(), 
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                materias.remove(materiaABorrar);
                JOptionPane.showMessageDialog(null, "Materia eliminada con éxito");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró una materia con esa clave", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Materia buscarMateria(String clave) {
        for (Materia m : materias) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                return m;
            }
        }
        return null;
    }

    public static boolean existeMateria(String clave) {
        return buscarMateria(clave) != null;
    }

    private static void cargarMaterias() {
        Object obj = FileManager.cargarDatos(ARCHIVO_MATERIAS);
        if (obj != null) {
            materias = (List<Materia>) obj;
        }
    }

    private static void guardarMaterias() {
        FileManager.guardarDatos(ARCHIVO_MATERIAS, materias);
    }
}
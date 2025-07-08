import utilidades.Login; // Import the Login class
import javax.swing.JOptionPane; // Import the JOptionPane class
import gestiones.GestionProfesores; // Import GestionProfesores
import gestiones.GestionMaterias;   // Import GestionMaterias
import gestiones.GestionGrupos;     // Import GestionGrupos
import gestiones.GestionAlumnos;    // Import GestionAlumnos

public class Main {
    public static void main(String[] args) {
        if (Login.autenticar()) {
            mostrarMenuPrincipal();
        } else {
            JOptionPane.showMessageDialog(null, "Acceso denegado. El programa se cerrará.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private static void mostrarMenuPrincipal() {
        String[] opciones = {"Profesores", "Materias", "Grupos", "Alumnos", "Salir"};
        int opcion;
        do {
            opcion = JOptionPane.showOptionDialog(null, "Sistema de Gestión Académica\nSeleccione una opción:", 
                    "Menú Principal", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
                    null, opciones, opciones[0]);
            
            switch(opcion) {
                case 0:
                    GestionProfesores.menuProfesores();
                    break;
                case 1:
                    GestionMaterias.menuMaterias();
                    break;
                case 2:
                    GestionGrupos.menuGrupos();
                    break;
                case 3:
                    GestionAlumnos.menuAlumnos();
                    break;
                case 4:
                    JOptionPane.showMessageDialog(null, "Saliendo del sistema...");
                    System.exit(0);
                    break;
                default:
                    break;
            }
        } while (opcion != 4);
    }
}

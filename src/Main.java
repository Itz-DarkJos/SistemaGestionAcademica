import utilidades.Login;
import gestiones.GestionProfesores;
import gestiones.GestionMaterias;
import gestiones.GestionGrupos;
import javax.swing.JOptionPane;

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
        String[] opciones = {"Profesores", "Materias", "Grupos", "Salir"};
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
                    JOptionPane.showMessageDialog(null, "Saliendo del sistema...");
                    System.exit(0);
                    break;
                default:
                    break;
            }
        } while (opcion != 3);
    }
}
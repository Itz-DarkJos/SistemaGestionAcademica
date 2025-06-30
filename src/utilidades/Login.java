package utilidades;

import java.io.*;
import java.util.List;
import javax.swing.JOptionPane;

public class Login {
    private static final String USUARIO_VALIDO = "AdminUci";
    private static final String PASS_VALIDO = "admin123";
    private static int intentos = 0;
    private static final int MAX_INTENTOS = 3;
    
    public static boolean autenticar() {
        while (intentos < MAX_INTENTOS) {
            String usuario = JOptionPane.showInputDialog(null, "Ingrese su usuario:", "Login", JOptionPane.QUESTION_MESSAGE);
            String password = JOptionPane.showInputDialog(null, "Ingrese su contraseña:", "Login", JOptionPane.QUESTION_MESSAGE);
            
            if (usuario == null || password == null) {
                System.exit(0);
            }
            
            if (usuario.equals(USUARIO_VALIDO) && password.equals(PASS_VALIDO)) {
                return true;
            } else {
                intentos++;
                int restantes = MAX_INTENTOS - intentos;
                JOptionPane.showMessageDialog(null, 
                    "Usuario o contraseña incorrectos.\nIntentos restantes: " + restantes, 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }
}
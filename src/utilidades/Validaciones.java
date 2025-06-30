package utilidades;

import java.io.*;
import java.util.List;
import javax.swing.JOptionPane;

public class Validaciones {
    public static boolean esSoloLetras(String input) {
        return input != null && input.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }
    
    public static boolean esNumero(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean validarFormatoClave(String clave) {
        return clave != null && clave.matches("[A-Z]{3}-\\d{3}");
    }
    
    public static boolean validarCreditos(String creditos) {
        if (!esNumero(creditos)) return false;
        int num = Integer.parseInt(creditos);
        return num >= 1 && num <= 10;
    }
}
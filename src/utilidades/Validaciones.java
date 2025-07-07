package utilidades;

import java.io.*;
import java.util.List;
import javax.swing.JOptionPane;

public class Validaciones {

    public static boolean esSoloLetras(String input) {
        if (input == null || !input.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            return false;
        }

        // Quitar espacios y tildes para evaluación
        String limpio = input.toLowerCase()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ñ", "n")
                .replace(" ", "");

        if (limpio.isEmpty()) {
            return false;
        }

        // Verificar si son puras vocales
        if (limpio.matches("[aeiou]+")) {
            return false;
        }

        // Verificar si son puras consonantes
        if (limpio.matches("[bcdfghjklmnpqrstvwxyz]+")) {
            return false;
        }

        return true;
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

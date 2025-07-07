package utilidades;

import javax.swing.*;
import java.awt.*;

public class Login {
    public static boolean autenticar() {
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        Object[] message = {
                "Usuario:", userField,
                "Contraseña:", passField
        };

        // Configurar colores y fuente
        UIManager.put("Panel.background", new Color(245, 245, 245));
        UIManager.put("OptionPane.background", new Color(245, 245, 245));
        UIManager.put("Button.background", new Color(0, 123, 255)); // azul
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));

        ImageIcon icon = null;

        int option = JOptionPane.showConfirmDialog(
                null,
                message,
                "Iniciar Sesión",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                icon
        );

        if (option == JOptionPane.OK_OPTION) {
            String usuario = userField.getText().trim();
            String contrasena = new String(passField.getPassword());

            if (usuario.equals("admin") && contrasena.equals("123")) {
                JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            return false; // Cancelado
        }
    }
}

package utilidades;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class FileManager {

    /**
     * Guarda una lista de objetos en un archivo usando serialización
     */
    public static void guardarDatos(String archivo, Object datos) {
        File directorio = new File("data");
        if (!directorio.exists()) {
            directorio.mkdir();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(archivo))) {
            oos.writeObject(datos);
        } catch (IOException e) {
            mostrarError("Error al guardar datos en " + archivo + ": " + e.getMessage());
        }
    }

    /**
     * Carga una lista de objetos desde un archivo usando serialización
     */
    public static Object cargarDatos(String archivo) {
        File file = new File(archivo);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(archivo))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            mostrarError("Error al cargar datos de " + archivo + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Lee un archivo de texto línea por línea
     * @param nombreArchivo ruta del archivo
     * @return lista de líneas leídas
     */
    public static List<String> leerArchivo(String nombreArchivo) {
        List<String> lineas = new ArrayList<>();
        File archivo = new File(nombreArchivo);

        if (!archivo.exists()) {
            return lineas; // Retorna vacío si no existe
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            mostrarError("Error al leer archivo " + nombreArchivo + ": " + e.getMessage());
        }

        return lineas;
    }

    /**
     * Escribe una lista de líneas en un archivo de texto (sobrescribe)
     * @param nombreArchivo ruta del archivo
     * @param lineas lista de líneas a escribir
     */
    public static void escribirArchivo(String nombreArchivo, List<String> lineas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (String linea : lineas) {
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            mostrarError("Error al escribir archivo " + nombreArchivo + ": " + e.getMessage());
        }
    }

    /**
     * Verifica si un archivo existe
     */
    public static boolean archivoExiste(String archivo) {
        return new File(archivo).exists();
    }

    /**
     * Elimina un archivo
     */
    public static boolean eliminarArchivo(String archivo) {
        File file = new File(archivo);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * Muestra un mensaje de error en un JOptionPane
     */
    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null,
                mensaje,
                "Error en FileManager",
                JOptionPane.ERROR_MESSAGE);
    }
}

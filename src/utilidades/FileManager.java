package utilidades;

import java.io.*;
import java.util.List;
import javax.swing.JOptionPane;

public class FileManager {
    
    /**
     * Guarda una lista de objetos en un archivo
     * @param archivo Ruta del archivo donde se guardarán los datos
     * @param datos Lista de objetos a serializar
     */
    public static void guardarDatos(String archivo, Object datos) {
        // Crear el directorio si no existe
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
     * Carga una lista de objetos desde un archivo
     * @param archivo Ruta del archivo a cargar
     * @return Lista de objetos o null si hay error
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
     * Verifica si un archivo existe
     * @param archivo Ruta del archivo a verificar
     * @return true si existe, false si no
     */
    public static boolean archivoExiste(String archivo) {
        return new File(archivo).exists();
    }
    
    /**
     * Muestra un mensaje de error en un JOptionPane
     * @param mensaje Mensaje de error a mostrar
     */
    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, 
            mensaje, 
            "Error en FileManager", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Elimina un archivo
     * @param archivo Ruta del archivo a eliminar
     * @return true si se eliminó correctamente, false si no
     */
    public static boolean eliminarArchivo(String archivo) {
        File file = new File(archivo);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
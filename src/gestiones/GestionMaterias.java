
package gestiones;

import modelos.Materia;
import utilidades.FileManager;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GestionMaterias {
    private static final String ARCHIVO_PLAN_A = "data/materias_plan_a.txt";
    private static final String ARCHIVO_PLAN_B = "data/materias_plan_b.txt";

    private static List<Materia> materiasPlanA = new ArrayList<>();
    private static List<Materia> materiasPlanB = new ArrayList<>();

    public static void menuMaterias() {
        cargarMaterias();

        String[] opciones = {"Capturar Materia (Plan B)", "Consultar Materias", "Regresar"};
        int opcion;
        do {
            opcion = JOptionPane.showOptionDialog(null, "Gestión de Materias\nSeleccione una opción:",
                    "Materias", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opciones, opciones[0]);

            switch (opcion) {
                case 0 -> capturarMateriaPlanB();
                case 1 -> consultarMaterias();
                case 2 -> borrarMateriaPlanB();
                case 3 -> guardarMaterias();
                default -> {
                }
            }
        } while (opcion != 3);
    }

    private static void capturarMateriaPlanB() {
        if (materiasPlanA.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Plan A aún no ha sido cargado. Intenta nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> cuatrimestres = new ArrayList<>();
        for (Materia m : materiasPlanA) {
            if (!cuatrimestres.contains(m.getCuatrimestre())) {
                cuatrimestres.add(m.getCuatrimestre());
            }
        }

        String cuatriSeleccionado = (String) JOptionPane.showInputDialog(
                null, "Seleccione el cuatrimestre:", "Plan B - Selección",
                JOptionPane.QUESTION_MESSAGE, null, cuatrimestres.toArray(), cuatrimestres.get(0));

        if (cuatriSeleccionado == null) return;

        List<Materia> opcionesMaterias = new ArrayList<>();
        for (Materia m : materiasPlanA) {
            if (m.getCuatrimestre().equalsIgnoreCase(cuatriSeleccionado)) {
                opcionesMaterias.add(m);
            }
        }

        if (opcionesMaterias.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay materias en ese cuatrimestre del Plan A.");
            return;
        }

        String[] opcionesNombres = new String[opcionesMaterias.size()];
        for (int i = 0; i < opcionesMaterias.size(); i++) {
            Materia m = opcionesMaterias.get(i);
            opcionesNombres[i] = m.getClave() + " - " + m.getNombre();
        }

        String materiaElegida = (String) JOptionPane.showInputDialog(
                null, "Seleccione la materia para el Plan B:", "Materias del " + cuatriSeleccionado,
                JOptionPane.QUESTION_MESSAGE, null, opcionesNombres, opcionesNombres[0]);

        if (materiaElegida == null) return;

        String claveSeleccionada = materiaElegida.split(" - ")[0];
        Materia seleccionada = null;
        for (Materia m : opcionesMaterias) {
            if (m.getClave().equalsIgnoreCase(claveSeleccionada)) {
                seleccionada = m;
                break;
            }
        }

        if (seleccionada == null) return;

        for (Materia m : materiasPlanB) {
            if (m.getClave().equalsIgnoreCase(seleccionada.getClave())) {
                JOptionPane.showMessageDialog(null, "Esa materia ya existe en el Plan B.");
                return;
            }
        }

        Materia nueva = new Materia(
                seleccionada.getClave(),
                seleccionada.getSeriacion(),
                seleccionada.getNombre(),
                seleccionada.getCuatrimestre()
        );

        materiasPlanB.add(nueva);
        JOptionPane.showMessageDialog(null, "Materia agregada exitosamente al Plan B.");
    }

    private static void consultarMaterias() {
        String[] opciones = {"Plan A", "Plan B"};
        int seleccion = JOptionPane.showOptionDialog(null, "¿De qué plan desea consultar las materias?",
                "Consulta", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                opciones, opciones[0]);

        if (seleccion == 0) {
            mostrarListaAgrupada(materiasPlanA, "Materias del Plan A");
        } else if (seleccion == 1) {
            mostrarListaAgrupada(materiasPlanB, "Materias del Plan B");
        }
    }

    private static void mostrarListaAgrupada(List<Materia> lista, String titulo) {
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay materias registradas en este plan.");
            return;
        }

        StringBuilder sb = new StringBuilder(titulo + "\n\n");
        String cuatrimestreActual = "";
        for (Materia m : lista) {
            if (!m.getCuatrimestre().equalsIgnoreCase(cuatrimestreActual)) {
                cuatrimestreActual = m.getCuatrimestre();
                sb.append("\n--- ").append(cuatrimestreActual).append(" ---\n");
            }
            sb.append("Clave: ").append(m.getClave())
              .append(" | Nombre: ").append(m.getNombre())
              .append(" | Seriación: ").append(m.getSeriacion())
              .append("\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        JOptionPane.showMessageDialog(null, scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void cargarMaterias() {
        Object a = FileManager.cargarDatos(ARCHIVO_PLAN_A);
        if (a != null) {
            materiasPlanA = (List<Materia>) a;
        } else {
            cargarMateriasPlanAPredeterminadas();
            FileManager.guardarDatos(ARCHIVO_PLAN_A, materiasPlanA);
        }

        Object b = FileManager.cargarDatos(ARCHIVO_PLAN_B);
        if (b != null) {
            materiasPlanB = (List<Materia>) b;
        } else {
            cargarMateriasPlanBPredeterminadas();
            FileManager.guardarDatos(ARCHIVO_PLAN_B, materiasPlanB);
        }
    }

    private static void guardarMaterias() {
        FileManager.guardarDatos(ARCHIVO_PLAN_A, materiasPlanA);
        FileManager.guardarDatos(ARCHIVO_PLAN_B, materiasPlanB);
    }

    public static List<Materia> getMateriasPlanA() {
        return materiasPlanA;
    }

    public static Materia buscarMateriaPlanA(String clave) {
        for (Materia m : materiasPlanA) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                return m;
            }
        }
        return null;
    }

    public static Materia buscarMateria(String clave) {
        for (Materia m : materiasPlanB) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                return m;
            }
        }
        return buscarMateriaPlanA(clave);
    }

    public static List<Materia> getMateriasPlanB() {
        return materiasPlanB != null ? materiasPlanB : new ArrayList<>();
    }

    public static Materia buscarMateriaPlanB(String clave) {
        return materiasPlanB.stream()
                .filter(m -> m.getClave().equalsIgnoreCase(clave))
                .findFirst()
                .orElse(null);
    }

    private static void cargarMateriasPlanAPredeterminadas() {
        materiasPlanA.clear();
        materiasPlanA.add(new Materia("10801", "0", "Fundamentos de Programación", "PRIMER CUATRIMESTRE"));
        materiasPlanA.add(new Materia("10802", "0", "Matemáticas para la Computación", "PRIMER CUATRIMESTRE"));
        materiasPlanA.add(new Materia("10803", "0", "Temas Selectos de Álgebra", "PRIMER CUATRIMESTRE"));
        materiasPlanA.add(new Materia("10804", "0", "Introducción a la Ingeniería", "PRIMER CUATRIMESTRE"));
        materiasPlanA.add(new Materia("10805", "0", "Estrategias de Aprendizaje", "PRIMER CUATRIMESTRE"));
        materiasPlanA.add(new Materia("10806", "0", "Metodología de la Investigación", "PRIMER CUATRIMESTRE"));

        materiasPlanA.add(new Materia("20807", "0", "Programación Orientada a Objetos", "SEGUNDO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("20808", "0", "Física I", "SEGUNDO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("20809", "0", "Álgebra Lineal", "SEGUNDO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("20810", "0", "Organización de Datos", "SEGUNDO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("20811", "0", "Probabilidad y Estadística", "SEGUNDO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("20812", "0", "Derecho Informático", "SEGUNDO CUATRIMESTRE"));

        materiasPlanA.add(new Materia("30813", "0", "Tópicos Selectos de Programación", "TERCER CUATRIMESTRE"));
        materiasPlanA.add(new Materia("30814", "20808", "Física II", "TERCER CUATRIMESTRE"));
        materiasPlanA.add(new Materia("30815", "0", "Cálculo Diferencial", "TERCER CUATRIMESTRE"));
        materiasPlanA.add(new Materia("30816", "0", "Introducción a Base de Datos", "TERCER CUATRIMESTRE"));
        materiasPlanA.add(new Materia("30817", "0", "Ingeniería de Sistemas I", "TERCER CUATRIMESTRE"));
        materiasPlanA.add(new Materia("30818", "0", "Multimedia", "TERCER CUATRIMESTRE"));

        materiasPlanA.add(new Materia("40819", "0", "Teoría de la Computación", "CUARTO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("40820", "0", "Circuitos Eléctricos y Electrónicos", "CUARTO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("40821", "0", "Cálculo Integral", "CUARTO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("40822", "0", "Bases de Datos", "CUARTO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("40823", "30817", "Ingeniería de Sistemas II", "CUARTO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("40824", "0", "Administración", "CUARTO CUATRIMESTRE"));

        materiasPlanA.add(new Materia("50825", "0", "Análisis de Sistemas de Software", "QUINTO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("50826", "0", "Arquitectura de Computadoras", "QUINTO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("50827", "0", "Ecuaciones Diferenciales", "QUINTO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("50828", "0", "Bases de Datos Distribuidas", "QUINTO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("50829", "0", "Programación en WEB", "QUINTO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("50830", "0", "Administración de Calidad", "QUINTO CUATRIMESTRE"));

        materiasPlanA.add(new Materia("60831", "0", "Diseño y Desarrollo de Sistemas de Software", "SEXTO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("60832", "0", "Lenguaje Ensamblador", "SEXTO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("60833", "0", "Investigación de Operaciones", "SEXTO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("60834", "0", "Tecnología Móvil", "SEXTO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("60835", "50829", "Programación Avanzada en WEB", "SEXTO CUATRIMESTRE"));

        materiasPlanA.add(new Materia("70836", "0", "Instalación y Mantenimiento de Software", "SÉPTIMO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("70837", "0", "Mantenimiento de Equipos de Cómputo", "SÉPTIMO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("70838", "0", "Telemática I", "SÉPTIMO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("70839", "0", "Sistema Operativo Multiusuario I", "SÉPTIMO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("70840", "0", "Creatividad Emprendedora", "SÉPTIMO CUATRIMESTRE"));

        materiasPlanA.add(new Materia("80841", "0", "Administración y Seguridad del Software", "OCTAVO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("80842", "0", "Programación de Software Libre", "OCTAVO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("80843", "70838", "Telemática II", "OCTAVO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("80844", "70839", "Sistema Operativo Multiusuario II", "OCTAVO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("80845", "0", "Ética Profesional", "OCTAVO CUATRIMESTRE"));

        materiasPlanA.add(new Materia("90846", "0", "Seminario de Aplicaciones de Software", "NOVENO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("90847", "0", "Seminario de Bases de Datos", "NOVENO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("90848", "0", "Seminario de Software de Calidad", "NOVENO CUATRIMESTRE"));
        materiasPlanA.add(new Materia("90849", "0", "Seminario de Redes", "NOVENO CUATRIMESTRE"));
    }

    private static void cargarMateriasPlanBPredeterminadas() {
        materiasPlanB.clear();
         // PRIMER CUATRIMESTRE
    materiasPlanB.add(new Materia("101", "0", "Programación I", "PRIMER CUATRIMESTRE"));
    materiasPlanB.add(new Materia("102", "0", "Instalación y Mantenimiento de Hardware y Software", "PRIMER CUATRIMESTRE"));
    materiasPlanB.add(new Materia("103", "0", "Temas Selectos de Álgebra", "PRIMER CUATRIMESTRE"));
    materiasPlanB.add(new Materia("104", "0", "Temas Selectos de Física", "PRIMER CUATRIMESTRE"));
    materiasPlanB.add(new Materia("105", "0", "Saberes Integradores I", "PRIMER CUATRIMESTRE"));
    materiasPlanB.add(new Materia("106", "0", "Proyectos para Nuevos Futuros I", "PRIMER CUATRIMESTRE"));

    // SEGUNDO CUATRIMESTRE
    materiasPlanB.add(new Materia("207", "101", "Programación II", "SEGUNDO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("208", "0", "Probabilidad y Estadística", "SEGUNDO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("210", "0", "Diseño y Gestión de Sistemas Empresariales", "SEGUNDO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("211", "0", "Aprendizajes para la Colaboración", "SEGUNDO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("212", "106", "Proyectos para Nuevos Futuros II", "SEGUNDO CUATRIMESTRE"));

    // TERCER CUATRIMESTRE
    materiasPlanB.add(new Materia("313", "0", "Lógica Matemática", "TERCER CUATRIMESTRE"));
    materiasPlanB.add(new Materia("314", "0", "Legislación y Seguridad Informática", "TERCER CUATRIMESTRE"));
    materiasPlanB.add(new Materia("315", "0", "Bases de Datos I", "TERCER CUATRIMESTRE"));
    materiasPlanB.add(new Materia("316", "0", "Estructura de Datos", "TERCER CUATRIMESTRE"));
    materiasPlanB.add(new Materia("317", "0", "Aprendizajes para la Investigación de lo No Humano", "TERCER CUATRIMESTRE"));
    materiasPlanB.add(new Materia("318", "212", "Proyectos para Nuevos Futuros III", "TERCER CUATRIMESTRE"));

    // CUARTO CUATRIMESTRE
    materiasPlanB.add(new Materia("419", "0", "Investigación de Operaciones", "CUARTO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("420", "0", "Cálculo Diferencial e Integral", "CUARTO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("421", "315", "Bases de Datos II", "CUARTO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("422", "316", "Programación Orientada a Objetos I", "CUARTO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("423", "0", "Aprendizajes para la Creatividad", "CUARTO CUATRIMESTRE"));

    // QUINTO CUATRIMESTRE
    materiasPlanB.add(new Materia("525", "0", "Administración y Seguridad del Software", "QUINTO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("526", "0", "Ecuaciones Diferenciales", "QUINTO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("527", "421", "Minería y Visualización de Datos", "QUINTO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("528", "0", "Análisis y Diseño de Sistemas", "QUINTO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("529", "0", "Programación Lógica y Aprendizaje de Solución de Problemas", "QUINTO CUATRIMESTRE"));

    // SEXTO CUATRIMESTRE
    materiasPlanB.add(new Materia("630", "0", "Redes de Computadoras I", "SEXTO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("631", "422", "Programación Orientada a Objetos II", "SEXTO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("632", "0", "Simulación de Sistemas", "SEXTO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("633", "0", "Desarrollo de Software Multisuario I", "SEXTO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("634", "0", "Cómputo en la Nube", "SEXTO CUATRIMESTRE"));

    // SÉPTIMO CUATRIMESTRE
    materiasPlanB.add(new Materia("735", "631", "Desarrollo de Software Multisuario II", "SÉPTIMO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("736", "0", "Cómputo Paralelo", "SÉPTIMO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("737", "630", "Redes de Computadoras II", "SÉPTIMO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("738", "0", "Inteligencia Artificial", "SÉPTIMO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("742", "0", "Proyectos para Nuevos Futuros", "SÉPTIMO CUATRIMESTRE"));

    // OCTAVO CUATRIMESTRE
    materiasPlanB.add(new Materia("843", "737", "Redes de Computadoras y Comunicaciones", "OCTAVO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("844", "0", "Ciberseguridad", "OCTAVO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("845", "0", "Arquitectura de Nuevas Tecnologías", "OCTAVO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("846", "0", "Optativa 1", "OCTAVO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("847", "0", "Optativa 2", "OCTAVO CUATRIMESTRE"));

    // NOVENO CUATRIMESTRE
    materiasPlanB.add(new Materia("950", "0", "Seminario de Software de Calidad", "NOVENO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("951", "0", "Seminario de Ciencia de Datos", "NOVENO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("952", "0", "Seminario de Habilidades Profesionales", "NOVENO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("953", "0", "Optativa 3", "NOVENO CUATRIMESTRE"));
    materiasPlanB.add(new Materia("954", "0", "Optativa 4", "NOVENO CUATRIMESTRE"));
}
    }

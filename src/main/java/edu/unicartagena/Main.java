package edu.unicartagena;

import edu.unicartagena.arbol.ArbolPlataforma;
import edu.unicartagena.grafo.GrafoDependencias;
import edu.unicartagena.modelo.Curso;
import edu.unicartagena.util.Consola;

/**
 * ============================================================
 *  PLATAFORMA EDUCATIVA — Estructuras de Datos
 *  Estudiante 6: Cursos, Módulos y Dependencias Académicas
 *  Universidad de Cartagena — Ingeniería del Software
 * ============================================================
 *
 * ÁRBOL N-ARIO: Jerarquía de la plataforma
 *   Plataforma → Programas → Semestres → Cursos
 *
 * GRAFO DIRIGIDO: Red de dependencias (prerrequisitos)
 *   A → B significa "A es prerrequisito de B"
 */
public class Main {

    public static void main(String[] args) {

        Consola.titulo("PLATAFORMA EDUCATIVA - ESTRUCTURAS DE DATOS");
        System.out.println("  Universidad de Cartagena | Ingeniería del Software");
        System.out.println("  Estudiante 6: Cursos, Módulos y Dependencias Académicas");

        demostrarArbol();
        demostrarGrafo();

        Consola.titulo("FIN DE LA DEMOSTRACIÓN");
    }

    // ============================================================
    //  PARTE I: ÁRBOL N-ARIO
    // ============================================================
    static void demostrarArbol() {
        Consola.titulo("PARTE I — ÁRBOL N-ARIO: Jerarquía de la Plataforma");

        // Crear raíz
        Curso raiz = new Curso("PLT-00", "Plataforma Educativa UniCartagena",
                "Nodo raíz del sistema educativo", 0, "N/A");
        ArbolPlataforma arbol = new ArbolPlataforma(raiz);

        // ── INSERCIÓN ──
        Consola.seccion("CREATE — Construyendo la jerarquía académica");

        // Programas (nivel 1)
        arbol.insertar("PLT-00", new Curso("IS-00", "Ingeniería del Software", "Programa principal", 0, "N/A"));
        arbol.insertar("PLT-00", new Curso("CS-00", "Ciencias de la Computación", "Programa complementario", 0, "N/A"));

        // Semestres de Ingeniería del Software (nivel 2)
        arbol.insertar("IS-00", new Curso("IS-S1", "Semestre I", "Cursos del primer semestre", 0, "I"));
        arbol.insertar("IS-00", new Curso("IS-S2", "Semestre II", "Cursos del segundo semestre", 0, "II"));
        arbol.insertar("IS-00", new Curso("IS-S3", "Semestre III", "Cursos del tercer semestre", 0, "III"));
        arbol.insertar("IS-00", new Curso("IS-S4", "Semestre IV", "Cursos del cuarto semestre", 0, "IV"));

        // Cursos Semestre I
        arbol.insertar("IS-S1", new Curso("MAT101", "Matemáticas I", "Cálculo diferencial e integral", 4, "I"));
        arbol.insertar("IS-S1", new Curso("ALG101", "Álgebra Lineal", "Vectores, matrices y transformaciones", 3, "I"));
        arbol.insertar("IS-S1", new Curso("INF101", "Introducción a la Programación", "Fundamentos con Python", 3, "I"));

        // Cursos Semestre II
        arbol.insertar("IS-S2", new Curso("MAT201", "Matemáticas II", "Cálculo multivariable", 4, "II"));
        arbol.insertar("IS-S2", new Curso("PRG201", "Programación Orientada a Objetos", "Java y patrones básicos", 4, "II"));
        arbol.insertar("IS-S2", new Curso("EST201", "Estadística", "Probabilidad y estadística descriptiva", 3, "II"));

        // Cursos Semestre III
        arbol.insertar("IS-S3", new Curso("EDD301", "Estructuras de Datos", "Listas, árboles, grafos", 4, "III"));
        arbol.insertar("IS-S3", new Curso("BDD301", "Bases de Datos", "Diseño relacional y SQL", 4, "III"));
        arbol.insertar("IS-S3", new Curso("ALG301", "Análisis de Algoritmos", "Complejidad y eficiencia", 3, "III"));

        // Cursos Semestre IV
        arbol.insertar("IS-S4", new Curso("REQ401", "Ingeniería de Requisitos", "Análisis y especificación", 3, "IV"));
        arbol.insertar("IS-S4", new Curso("ARQ401", "Arquitectura de Software", "Patrones y estilos arquitectónicos", 4, "IV"));
        arbol.insertar("IS-S4", new Curso("PRY401", "Gestión de Proyectos", "PMBOK y metodologías ágiles", 3, "IV"));

        // Semestre de Ciencias de la Computación
        arbol.insertar("CS-00", new Curso("CS-S1", "Semestre I - CS", "Primer semestre CS", 0, "I"));
        arbol.insertar("CS-S1", new Curso("DIS101", "Matemáticas Discretas", "Lógica, conjuntos y grafos", 4, "I"));
        arbol.insertar("CS-S1", new Curso("ARQ101", "Arquitectura de Computadores", "Hardware y organización", 3, "I"));

        System.out.println("\n  Total nodos en el árbol: " + arbol.getTotalNodos());

        // ── CONSULTA ──
        Consola.seccion("READ — Consultar cursos");
        arbol.consultar("EDD301");
        Consola.separador();
        arbol.consultar("IS-S3");
        Consola.separador();
        arbol.consultar("XXX"); // Código inexistente

        // ── ACTUALIZACIÓN ──
        Consola.seccion("UPDATE — Modificar cursos");
        arbol.actualizar("MAT101", "Cálculo Diferencial", "Límites, derivadas e integrales", 4);
        arbol.actualizar("PRG201", "Programación OO con Java", null, 5);
        arbol.actualizar("ZZZ", "Inexistente", null, 0); // No existe

        // Verificar cambio
        System.out.println();
        arbol.consultar("MAT101");

        // ── RECORRIDOS ──
        Consola.seccion("RECORRIDOS del Árbol");
        arbol.recorridoPreOrden();
        arbol.recorridoPorNiveles();
        arbol.recorridoPostOrden();

        // ── ELIMINACIÓN ──
        Consola.seccion("DELETE — Eliminar nodos");
        arbol.eliminar("CS-00");   // Elimina CS y toda su rama
        arbol.eliminar("EST201");  // Elimina solo una hoja
        arbol.eliminar("PLT-00"); // No se puede eliminar la raíz
        arbol.eliminar("XYZ");    // No existe

        System.out.println("\n  Árbol después de eliminaciones:");
        arbol.recorridoPreOrden();
        System.out.println("\n  Total nodos restantes: " + arbol.getTotalNodos());
    }

    // ============================================================
    //  PARTE II: GRAFO DIRIGIDO DE DEPENDENCIAS
    // ============================================================
    static void demostrarGrafo() {
        Consola.titulo("PARTE II — GRAFO DIRIGIDO: Dependencias Académicas");

        GrafoDependencias grafo = new GrafoDependencias();

        // ── INSERCIÓN DE CURSOS (NODOS) ──
        Consola.seccion("CREATE — Agregar cursos al grafo");
        grafo.agregarCurso(new Curso("MAT101", "Cálculo Diferencial", "Base matemática", 4, "I"));
        grafo.agregarCurso(new Curso("ALG101", "Álgebra Lineal", "Vectores y matrices", 3, "I"));
        grafo.agregarCurso(new Curso("INF101", "Intro a la Programación", "Python básico", 3, "I"));
        grafo.agregarCurso(new Curso("DIS101", "Matemáticas Discretas", "Lógica y grafos teóricos", 4, "I"));
        grafo.agregarCurso(new Curso("MAT201", "Cálculo Integral", "Integrales y series", 4, "II"));
        grafo.agregarCurso(new Curso("PRG201", "Programación OO", "Java y OOP", 4, "II"));
        grafo.agregarCurso(new Curso("EST201", "Estadística", "Probabilidad básica", 3, "II"));
        grafo.agregarCurso(new Curso("EDD301", "Estructuras de Datos", "Árboles, grafos, listas", 4, "III"));
        grafo.agregarCurso(new Curso("BDD301", "Bases de Datos", "SQL y diseño relacional", 4, "III"));
        grafo.agregarCurso(new Curso("ALG301", "Análisis de Algoritmos", "Complejidad computacional", 3, "III"));
        grafo.agregarCurso(new Curso("ARQ401", "Arquitectura de Software", "Patrones arquitectónicos", 4, "IV"));
        grafo.agregarCurso(new Curso("REQ401", "Ingeniería de Requisitos", "Análisis y especificación", 3, "IV"));
        grafo.agregarCurso(new Curso("PRY401", "Gestión de Proyectos", "PMBOK y Agile", 3, "IV"));

        // ── INSERCIÓN DE DEPENDENCIAS (ARISTAS) ──
        Consola.seccion("CREATE — Establecer dependencias (prerrequisitos)");
        grafo.agregarDependencia("MAT101", "MAT201");   // Cálculo I → Cálculo II
        grafo.agregarDependencia("MAT101", "EST201");   // Cálculo I → Estadística
        grafo.agregarDependencia("ALG101", "EDD301");   // Álgebra → EDD
        grafo.agregarDependencia("INF101", "PRG201");   // Intro Prog → OOP
        grafo.agregarDependencia("DIS101", "EDD301");   // Discretas → EDD
        grafo.agregarDependencia("DIS101", "ALG301");   // Discretas → Algoritmos
        grafo.agregarDependencia("PRG201", "EDD301");   // OOP → EDD
        grafo.agregarDependencia("PRG201", "BDD301");   // OOP → BD
        grafo.agregarDependencia("MAT201", "EST201");   // Cálculo II → Estadística (también)
        grafo.agregarDependencia("EDD301", "ALG301");   // EDD → Algoritmos
        grafo.agregarDependencia("EDD301", "ARQ401");   // EDD → Arquitectura
        grafo.agregarDependencia("BDD301", "ARQ401");   // BD → Arquitectura
        grafo.agregarDependencia("ALG301", "ARQ401");   // Algoritmos → Arquitectura
        grafo.agregarDependencia("PRG201", "REQ401");   // OOP → Requisitos
        grafo.agregarDependencia("REQ401", "PRY401");   // Requisitos → Proyectos
        grafo.agregarDependencia("ARQ401", "PRY401");   // Arquitectura → Proyectos

        // Intentar ciclo
        System.out.println("\n  Intentando crear ciclo (debe ser rechazado):");
        grafo.agregarDependencia("PRY401", "INF101"); // ciclo prohibido

        // Duplicado
        System.out.println("\n  Intentando duplicar dependencia:");
        grafo.agregarDependencia("MAT101", "MAT201"); // ya existe

        // ── CONSULTA ──
        Consola.seccion("READ — Consultar cursos en el grafo");
        grafo.consultar("EDD301");
        Consola.separador();
        grafo.consultar("MAT101");
        Consola.separador();
        grafo.consultar("PRY401");
        Consola.separador();
        grafo.consultar("NOEXISTE");

        // ── ESTRUCTURA COMPLETA ──
        grafo.mostrarGrafo();

        // ── ACTUALIZACIÓN ──
        Consola.seccion("UPDATE — Modificar cursos en el grafo");
        grafo.actualizar("EDD301", "Estructuras de Datos Avanzadas", "Árboles balanceados, grafos y hashing", 5);
        grafo.actualizar("BDD301", null, "SQL, NoSQL y optimización de consultas", 0);
        grafo.actualizar("ZZZ", "No existe", null, 0);

        System.out.println();
        grafo.consultar("EDD301");

        // ── RECORRIDOS ──
        Consola.seccion("RECORRIDOS del Grafo");
        grafo.dfs("INF101");
        grafo.bfs("MAT101");
        grafo.ordenTopologico();

        // ── ELIMINACIÓN ──
        Consola.seccion("DELETE — Eliminar cursos y dependencias del grafo");
        grafo.eliminarDependencia("MAT101", "EST201");  // Eliminar solo arista
        grafo.eliminarDependencia("ZZZ", "YYY");        // No existe
        grafo.eliminarCurso("EST201");                  // Eliminar nodo
        grafo.eliminarCurso("NOEXISTE");               // No existe

        System.out.println("\n  Grafo después de eliminaciones:");
        grafo.mostrarGrafo();

        System.out.println("\n  Nuevo orden topológico tras los cambios:");
        grafo.ordenTopologico();
    }
}

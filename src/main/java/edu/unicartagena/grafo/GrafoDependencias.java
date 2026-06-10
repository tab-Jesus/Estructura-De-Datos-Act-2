package edu.unicartagena.grafo;

import edu.unicartagena.modelo.Curso;
import java.util.*;

/**
 * Grafo Dirigido que representa las DEPENDENCIAS ACADÉMICAS entre cursos.
 * Una arista A → B significa: "A es prerrequisito de B".
 * 
 * Implementación: Lista de Adyacencia con HashMap.
 */
public class GrafoDependencias {

    // Mapa: código del curso → objeto Curso
    private Map<String, Curso> cursos;

    // Lista de adyacencia: código → lista de códigos de cursos que dependen de él
    // A → [B, C] significa A es prerrequisito de B y C
    private Map<String, List<String>> adyacencia;

    public GrafoDependencias() {
        this.cursos = new LinkedHashMap<>();
        this.adyacencia = new LinkedHashMap<>();
    }

    // ======================== CRUD ========================

    /**
     * CREAR NODO: Agregar un curso al grafo (sin dependencias aún).
     */
    public boolean agregarCurso(Curso curso) {
        if (cursos.containsKey(curso.getCodigo())) {
            System.out.println("  ✗ El curso ya existe: " + curso.getCodigo());
            return false;
        }
        cursos.put(curso.getCodigo(), curso);
        adyacencia.put(curso.getCodigo(), new ArrayList<>());
        System.out.println("  ✓ Curso agregado al grafo: " + curso.getNombre() + " [" + curso.getCodigo() + "]");
        return true;
    }

    /**
     * CREAR ARISTA: Agregar dependencia A → B (A es prerrequisito de B).
     */
    public boolean agregarDependencia(String codigoPrerrequisito, String codigoDependiente) {
        if (!cursos.containsKey(codigoPrerrequisito) || !cursos.containsKey(codigoDependiente)) {
            System.out.println("  ✗ Uno o ambos cursos no existen en el grafo.");
            return false;
        }
        List<String> vecinos = adyacencia.get(codigoPrerrequisito);
        if (vecinos.contains(codigoDependiente)) {
            System.out.println("  ✗ La dependencia ya existe.");
            return false;
        }
        // Verificar que no se cree un ciclo
        if (existeCamino(codigoDependiente, codigoPrerrequisito)) {
            System.out.println("  ✗ No se puede agregar: crearía un ciclo en las dependencias.");
            return false;
        }
        vecinos.add(codigoDependiente);
        System.out.printf("  ✓ Dependencia agregada: [%s] → es prerrequisito de → [%s]%n",
                codigoPrerrequisito, codigoDependiente);
        return true;
    }

    /**
     * CONSULTAR: Mostrar información de un curso y sus relaciones.
     */
    public boolean consultar(String codigo) {
        Curso curso = cursos.get(codigo);
        if (curso == null) {
            System.out.println("  ✗ Curso no encontrado: " + codigo);
            return false;
        }
        System.out.println("  ✓ " + curso);

        // Prerrequisitos (cursos que apuntan hacia este)
        List<String> prerrequisitos = obtenerPrerrequisitos(codigo);
        if (prerrequisitos.isEmpty()) {
            System.out.println("    📋 Prerrequisitos: Ninguno (es curso base)");
        } else {
            System.out.print("    📋 Prerrequisitos: ");
            prerrequisitos.forEach(p -> System.out.print(cursos.get(p).getNombre() + "  "));
            System.out.println();
        }

        // Cursos que desbloquea (adyacencia directa)
        List<String> desbloquea = adyacencia.get(codigo);
        if (desbloquea.isEmpty()) {
            System.out.println("    🔓 Desbloquea: Ninguno (es curso final)");
        } else {
            System.out.print("    🔓 Desbloquea: ");
            desbloquea.forEach(d -> System.out.print(cursos.get(d).getNombre() + "  "));
            System.out.println();
        }
        return true;
    }

    /**
     * ACTUALIZAR: Modificar datos de un curso en el grafo.
     */
    public boolean actualizar(String codigo, String nuevoNombre, String nuevaDesc, int nuevosCreditos) {
        Curso curso = cursos.get(codigo);
        if (curso == null) {
            System.out.println("  ✗ Curso no encontrado: " + codigo);
            return false;
        }
        String anterior = curso.getNombre();
        if (nuevoNombre != null && !nuevoNombre.isEmpty()) curso.setNombre(nuevoNombre);
        if (nuevaDesc != null && !nuevaDesc.isEmpty()) curso.setDescripcion(nuevaDesc);
        if (nuevosCreditos > 0) curso.setCreditos(nuevosCreditos);
        System.out.println("  ✓ Curso actualizado: [" + codigo + "] " + anterior + " → " + curso.getNombre());
        return true;
    }

    /**
     * ELIMINAR NODO: Eliminar un curso y todas sus aristas.
     */
    public boolean eliminarCurso(String codigo) {
        if (!cursos.containsKey(codigo)) {
            System.out.println("  ✗ Curso no encontrado: " + codigo);
            return false;
        }
        String nombre = cursos.get(codigo).getNombre();
        cursos.remove(codigo);
        adyacencia.remove(codigo);
        // Eliminar todas las aristas que apuntan a este nodo
        for (List<String> vecinos : adyacencia.values()) {
            vecinos.remove(codigo);
        }
        System.out.println("  ✓ Curso eliminado del grafo: " + nombre + " [" + codigo + "] y todas sus dependencias.");
        return true;
    }

    /**
     * ELIMINAR ARISTA: Eliminar una dependencia específica.
     */
    public boolean eliminarDependencia(String codigoPrerrequisito, String codigoDependiente) {
        List<String> vecinos = adyacencia.get(codigoPrerrequisito);
        if (vecinos == null || !vecinos.remove(codigoDependiente)) {
            System.out.println("  ✗ La dependencia no existe.");
            return false;
        }
        System.out.printf("  ✓ Dependencia eliminada: [%s] → [%s]%n", codigoPrerrequisito, codigoDependiente);
        return true;
    }

    // ======================== RECORRIDOS ========================

    /**
     * RECORRIDO 1 — DFS (Profundidad) desde un curso dado.
     * Muestra todos los cursos alcanzables (cursos que se pueden tomar después).
     */
    public void dfs(String codigoInicio) {
        System.out.println("\n  ══ RECORRIDO DFS (Profundidad) desde: " + codigoInicio + " ══");
        if (!cursos.containsKey(codigoInicio)) {
            System.out.println("  ✗ Curso no encontrado.");
            return;
        }
        Set<String> visitados = new LinkedHashSet<>();
        Stack<String> pila = new Stack<>();
        pila.push(codigoInicio);

        while (!pila.isEmpty()) {
            String actual = pila.pop();
            if (!visitados.contains(actual)) {
                visitados.add(actual);
                System.out.println("    → " + cursos.get(actual).getNombre() + " [" + actual + "]");
                List<String> vecinos = adyacencia.get(actual);
                if (vecinos != null) {
                    // Agregar en orden inverso para mantener el orden natural
                    for (int i = vecinos.size() - 1; i >= 0; i--) {
                        if (!visitados.contains(vecinos.get(i))) {
                            pila.push(vecinos.get(i));
                        }
                    }
                }
            }
        }
    }

    /**
     * RECORRIDO 2 — BFS (Amplitud) desde un curso dado.
     * Muestra los cursos nivel por nivel (qué se puede tomar primero, luego, etc.)
     */
    public void bfs(String codigoInicio) {
        System.out.println("\n  ══ RECORRIDO BFS (Amplitud) desde: " + codigoInicio + " ══");
        if (!cursos.containsKey(codigoInicio)) {
            System.out.println("  ✗ Curso no encontrado.");
            return;
        }
        Set<String> visitados = new LinkedHashSet<>();
        Queue<String> cola = new LinkedList<>();
        cola.add(codigoInicio);
        visitados.add(codigoInicio);
        int nivel = 0;

        while (!cola.isEmpty()) {
            int tamaño = cola.size();
            System.out.println("  Nivel " + nivel + ":");
            for (int i = 0; i < tamaño; i++) {
                String actual = cola.poll();
                System.out.println("    → " + cursos.get(actual).getNombre() + " [" + actual + "]");
                for (String vecino : adyacencia.get(actual)) {
                    if (!visitados.contains(vecino)) {
                        visitados.add(vecino);
                        cola.add(vecino);
                    }
                }
            }
            nivel++;
        }
    }

    /**
     * RECORRIDO 3 — Orden Topológico (Kahn's Algorithm - BFS).
     * Muestra el orden correcto en que deben tomarse los cursos.
     */
    public void ordenTopologico() {
        System.out.println("\n  ══ ORDEN TOPOLÓGICO (Secuencia académica recomendada) ══");

        // Calcular grado de entrada de cada nodo
        Map<String, Integer> gradoEntrada = new HashMap<>();
        for (String codigo : cursos.keySet()) gradoEntrada.put(codigo, 0);
        for (String desde : adyacencia.keySet()) {
            for (String hasta : adyacencia.get(desde)) {
                gradoEntrada.put(hasta, gradoEntrada.get(hasta) + 1);
            }
        }

        // Iniciar cola con nodos sin prerrequisitos
        Queue<String> cola = new LinkedList<>();
        for (String codigo : gradoEntrada.keySet()) {
            if (gradoEntrada.get(codigo) == 0) cola.add(codigo);
        }

        List<String> resultado = new ArrayList<>();
        while (!cola.isEmpty()) {
            String actual = cola.poll();
            resultado.add(actual);
            for (String vecino : adyacencia.get(actual)) {
                gradoEntrada.put(vecino, gradoEntrada.get(vecino) - 1);
                if (gradoEntrada.get(vecino) == 0) cola.add(vecino);
            }
        }

        if (resultado.size() != cursos.size()) {
            System.out.println("  ⚠ El grafo contiene ciclos, no es posible un orden topológico completo.");
        } else {
            System.out.println("  Orden recomendado de estudio:");
            for (int i = 0; i < resultado.size(); i++) {
                String cod = resultado.get(i);
                System.out.printf("  %2d. %s [%s]%n", (i + 1), cursos.get(cod).getNombre(), cod);
            }
        }
    }

    // ======================== UTILIDADES ========================

    private List<String> obtenerPrerrequisitos(String codigo) {
        List<String> prerrequisitos = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : adyacencia.entrySet()) {
            if (entry.getValue().contains(codigo)) {
                prerrequisitos.add(entry.getKey());
            }
        }
        return prerrequisitos;
    }

    private boolean existeCamino(String origen, String destino) {
        Set<String> visitados = new HashSet<>();
        Queue<String> cola = new LinkedList<>();
        cola.add(origen);
        while (!cola.isEmpty()) {
            String actual = cola.poll();
            if (actual.equals(destino)) return true;
            if (!visitados.contains(actual)) {
                visitados.add(actual);
                List<String> vecinos = adyacencia.get(actual);
                if (vecinos != null) cola.addAll(vecinos);
            }
        }
        return false;
    }

    public void mostrarGrafo() {
        System.out.println("\n  ══ ESTRUCTURA DEL GRAFO DE DEPENDENCIAS ══");
        System.out.println("  Total cursos: " + cursos.size());
        System.out.println("  (A → B significa: A es prerrequisito de B)\n");
        for (String codigo : adyacencia.keySet()) {
            List<String> vecinos = adyacencia.get(codigo);
            String nombreOrigen = cursos.get(codigo).getNombre();
            if (vecinos.isEmpty()) {
                System.out.println("  [" + codigo + "] " + nombreOrigen + " → (sin dependientes)");
            } else {
                for (String v : vecinos) {
                    System.out.printf("  [%s] %-35s  →  [%s] %s%n",
                            codigo, nombreOrigen, v, cursos.get(v).getNombre());
                }
            }
        }
    }

    public Map<String, Curso> getCursos() { return cursos; }
    public Map<String, List<String>> getAdyacencia() { return adyacencia; }
}

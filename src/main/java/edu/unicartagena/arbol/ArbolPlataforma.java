package edu.unicartagena.arbol;

import edu.unicartagena.modelo.Curso;
import java.util.*;

/**
 * Árbol N-ario que representa la jerarquía de la Plataforma Educativa.
 * Estructura: Raíz (Plataforma) → Programas → Semestres → Cursos/Módulos
 */
public class ArbolPlataforma {

    private NodoCurso raiz;

    public ArbolPlataforma(Curso cursoRaiz) {
        this.raiz = new NodoCurso(cursoRaiz);
    }

    // ======================== CRUD ========================

    /**
     * CREAR: Insertar un nuevo curso como hijo de un padre dado (por código).
     */
    public boolean insertar(String codigoPadre, Curso nuevoCurso) {
        NodoCurso padre = buscarNodo(raiz, codigoPadre);
        if (padre == null) {
            System.out.println("  ✗ No se encontró el nodo padre con código: " + codigoPadre);
            return false;
        }
        // Verificar que el código no exista ya
        if (buscarNodo(raiz, nuevoCurso.getCodigo()) != null) {
            System.out.println("  ✗ Ya existe un curso con el código: " + nuevoCurso.getCodigo());
            return false;
        }
        padre.agregarHijo(new NodoCurso(nuevoCurso));
        System.out.println("  ✓ Curso insertado: " + nuevoCurso.getNombre() + " bajo → " + padre.getCurso().getNombre());
        return true;
    }

    /**
     * CONSULTAR: Buscar un curso por código y mostrar su información.
     */
    public NodoCurso consultar(String codigo) {
        NodoCurso nodo = buscarNodo(raiz, codigo);
        if (nodo != null) {
            System.out.println("  ✓ Curso encontrado: " + nodo.getCurso());
            if (nodo.getPadre() != null) {
                System.out.println("    ↑ Padre: " + nodo.getPadre().getCurso().getNombre());
            }
            if (!nodo.getHijos().isEmpty()) {
                System.out.print("    ↓ Hijos: ");
                nodo.getHijos().forEach(h -> System.out.print(h.getCurso().getNombre() + "  "));
                System.out.println();
            }
        } else {
            System.out.println("  ✗ No se encontró curso con código: " + codigo);
        }
        return nodo;
    }

    /**
     * ACTUALIZAR: Modificar los datos de un curso existente.
     */
    public boolean actualizar(String codigo, String nuevoNombre, String nuevaDesc, int nuevosCreditos) {
        NodoCurso nodo = buscarNodo(raiz, codigo);
        if (nodo == null) {
            System.out.println("  ✗ Curso no encontrado: " + codigo);
            return false;
        }
        Curso c = nodo.getCurso();
        String nombreAnterior = c.getNombre();
        if (nuevoNombre != null && !nuevoNombre.isEmpty()) c.setNombre(nuevoNombre);
        if (nuevaDesc != null && !nuevaDesc.isEmpty()) c.setDescripcion(nuevaDesc);
        if (nuevosCreditos > 0) c.setCreditos(nuevosCreditos);
        System.out.println("  ✓ Curso actualizado: [" + codigo + "] " + nombreAnterior + " → " + c.getNombre());
        return true;
    }

    /**
     * ELIMINAR: Eliminar un nodo (y su subárbol) dado su código.
     */
    public boolean eliminar(String codigo) {
        if (raiz.getCurso().getCodigo().equals(codigo)) {
            System.out.println("  ✗ No se puede eliminar la raíz del árbol.");
            return false;
        }
        NodoCurso nodo = buscarNodo(raiz, codigo);
        if (nodo == null) {
            System.out.println("  ✗ Curso no encontrado: " + codigo);
            return false;
        }
        NodoCurso padre = nodo.getPadre();
        padre.eliminarHijo(codigo);
        System.out.println("  ✓ Curso eliminado: " + nodo.getCurso().getNombre()
                + " (y " + (contarNodos(nodo) - 1) + " subnodos)");
        return true;
    }

    // ======================== RECORRIDOS ========================

    /**
     * RECORRIDO 1 — Pre-orden (Profundidad): Raíz → Hijos
     * Útil para imprimir la jerarquía completa.
     */
    public void recorridoPreOrden() {
        System.out.println("\n  ══ RECORRIDO PRE-ORDEN (Profundidad - DFS) ══");
        preOrden(raiz, 0);
    }

    private void preOrden(NodoCurso nodo, int nivel) {
        if (nodo == null) return;
        String indentacion = "  ".repeat(nivel);
        String icono = nodo.esHoja() ? "📘" : (nivel == 0 ? "🏛️" : "📂");
        System.out.printf("  %s%s %s [%s]%n",
                indentacion, icono, nodo.getCurso().getNombre(), nodo.getCurso().getCodigo());
        for (NodoCurso hijo : nodo.getHijos()) {
            preOrden(hijo, nivel + 1);
        }
    }

    /**
     * RECORRIDO 2 — Por Niveles (BFS - Amplitud):
     * Útil para ver cursos por semestre/nivel.
     */
    public void recorridoPorNiveles() {
        System.out.println("\n  ══ RECORRIDO POR NIVELES (Amplitud - BFS) ══");
        Queue<NodoCurso> cola = new LinkedList<>();
        cola.add(raiz);
        int nivelActual = 0;
        Queue<NodoCurso> nextLevel = new LinkedList<>();

        System.out.println("  Nivel 0 (Raíz):");
        while (!cola.isEmpty()) {
            NodoCurso nodo = cola.poll();
            System.out.println("    → " + nodo.getCurso().getNombre() + " [" + nodo.getCurso().getCodigo() + "]");
            nextLevel.addAll(nodo.getHijos());
            if (cola.isEmpty() && !nextLevel.isEmpty()) {
                nivelActual++;
                System.out.println("  Nivel " + nivelActual + ":");
                cola.addAll(nextLevel);
                nextLevel.clear();
            }
        }
    }

    /**
     * RECORRIDO 3 — Post-orden: Hojas primero, luego la raíz.
     * Útil para saber qué cursos ver antes que otros (orden de prerrequisitos inverso).
     */
    public void recorridoPostOrden() {
        System.out.println("\n  ══ RECORRIDO POST-ORDEN ══");
        postOrden(raiz, 0);
    }

    private void postOrden(NodoCurso nodo, int nivel) {
        if (nodo == null) return;
        for (NodoCurso hijo : nodo.getHijos()) {
            postOrden(hijo, nivel + 1);
        }
        String indentacion = "  ".repeat(nivel);
        System.out.printf("  %s📌 %s [%s]%n",
                indentacion, nodo.getCurso().getNombre(), nodo.getCurso().getCodigo());
    }

    // ======================== UTILIDADES ========================

    public NodoCurso buscarNodo(NodoCurso nodo, String codigo) {
        if (nodo == null) return null;
        if (nodo.getCurso().getCodigo().equals(codigo)) return nodo;
        for (NodoCurso hijo : nodo.getHijos()) {
            NodoCurso resultado = buscarNodo(hijo, codigo);
            if (resultado != null) return resultado;
        }
        return null;
    }

    private int contarNodos(NodoCurso nodo) {
        if (nodo == null) return 0;
        int total = 1;
        for (NodoCurso hijo : nodo.getHijos()) total += contarNodos(hijo);
        return total;
    }

    public int getTotalNodos() {
        return contarNodos(raiz);
    }

    public NodoCurso getRaiz() { return raiz; }
}

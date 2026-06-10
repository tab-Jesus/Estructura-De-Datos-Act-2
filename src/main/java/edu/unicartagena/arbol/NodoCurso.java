package edu.unicartagena.arbol;

import edu.unicartagena.modelo.Curso;
import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del Árbol N-ario de la Plataforma Educativa.
 * Cada nodo representa una categoría, módulo o curso.
 */
public class NodoCurso {
    private Curso curso;
    private NodoCurso padre;
    private List<NodoCurso> hijos;

    public NodoCurso(Curso curso) {
        this.curso = curso;
        this.hijos = new ArrayList<>();
        this.padre = null;
    }

    public void agregarHijo(NodoCurso hijo) {
        hijo.setPadre(this);
        this.hijos.add(hijo);
    }

    public boolean eliminarHijo(String codigo) {
        return hijos.removeIf(h -> h.getCurso().getCodigo().equals(codigo));
    }

    // Getters y Setters
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public NodoCurso getPadre() { return padre; }
    public void setPadre(NodoCurso padre) { this.padre = padre; }

    public List<NodoCurso> getHijos() { return hijos; }

    public boolean esHoja() { return hijos.isEmpty(); }
}

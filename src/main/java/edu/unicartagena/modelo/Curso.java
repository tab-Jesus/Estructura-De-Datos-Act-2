package edu.unicartagena.modelo;

/**
 * Modelo que representa un Curso en la Plataforma Educativa.
 */
public class Curso {
    private String codigo;
    private String nombre;
    private String descripcion;
    private int creditos;
    private String semestre; // ej: "I", "II", "III", ...

    public Curso(String codigo, String nombre, String descripcion, int creditos, String semestre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creditos = creditos;
        this.semestre = semestre;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    @Override
    public String toString() {
        return String.format("[%s] %s | %d créditos | Semestre: %s | %s",
                codigo, nombre, creditos, semestre, descripcion);
    }
}

package edu.unicartagena.util;

/**
 * Utilidad para dar formato visual a la salida en consola.
 */
public class Consola {

    public static void titulo(String texto) {
        int ancho = 60;
        String linea = "═".repeat(ancho);
        System.out.println("\n╔" + linea + "╗");
        System.out.printf("║  %-58s║%n", texto);
        System.out.println("╚" + linea + "╝");
    }

    public static void seccion(String texto) {
        System.out.println("\n┌─────────────────────────────────────────────┐");
        System.out.printf("│  %-43s│%n", texto);
        System.out.println("└─────────────────────────────────────────────┘");
    }

    public static void separador() {
        System.out.println("  " + "─".repeat(56));
    }
}

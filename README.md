# Plataforma Educativa — Estructuras de Datos
**Universidad de Cartagena | Ingeniería del Software**
**Actividad Unidad 4 — Estudiante 6**

## Descripción
Gestión de cursos, módulos y dependencias académicas usando **Árboles N-arios** y **Grafos Dirigidos** en Java.

## Estructura del Proyecto
```
src/main/java/edu/unicartagena/
├── Main.java                        ← Punto de entrada, demostración completa
├── modelo/
│   └── Curso.java                   ← Entidad Curso (código, nombre, créditos, semestre)
├── arbol/
│   ├── NodoCurso.java               ← Nodo del árbol N-ario
│   └── ArbolPlataforma.java         ← Árbol con CRUD + 3 recorridos
├── grafo/
│   └── GrafoDependencias.java       ← Grafo dirigido con CRUD + DFS + BFS + Topológico
└── util/
    └── Consola.java                 ← Utilidad de formato en consola
```

## Compilar y Ejecutar
```bash
# Compilar
javac -encoding UTF-8 -d out $(find src -name "*.java")

# Ejecutar
java -cp out edu.unicartagena.Main
```

## Estructuras Implementadas

### 🌳 Árbol N-ario (ArbolPlataforma)
Representa la **jerarquía** de la plataforma:
`Plataforma → Programas → Semestres → Cursos`

**Operaciones CRUD:**
- `insertar(codigoPadre, curso)` — Agregar curso bajo un padre
- `consultar(codigo)` — Buscar y mostrar curso + padre + hijos
- `actualizar(codigo, ...)` — Modificar nombre, descripción, créditos
- `eliminar(codigo)` — Eliminar nodo y todo su subárbol

**Recorridos:**
1. **Pre-orden DFS** — Raíz → Hijos (muestra jerarquía)
2. **BFS por niveles** — Nivel a nivel (muestra profundidad)
3. **Post-orden** — Hojas primero, raíz al final

### 🔗 Grafo Dirigido (GrafoDependencias)
Representa las **dependencias académicas**: A → B significa "A es prerrequisito de B"

**Operaciones CRUD:**
- `agregarCurso(curso)` — Nodo nuevo en el grafo
- `agregarDependencia(desde, hasta)` — Arista dirigida
- `consultar(codigo)` — Información + prerrequisitos + cursos que desbloquea
- `actualizar(codigo, ...)` — Modificar datos del curso
- `eliminarCurso(codigo)` — Elimina nodo y todas sus aristas
- `eliminarDependencia(desde, hasta)` — Elimina solo una arista

**Recorridos:**
1. **DFS** — Profundidad desde un nodo (cursos alcanzables)
2. **BFS** — Amplitud por niveles desde un nodo
3. **Orden Topológico** (Kahn's BFS) — Secuencia académica recomendada

## Diagrama del Grafo de Dependencias
```
MAT101 ──────────────────────────────┐
         \                           ▼
          ──────────────────────► MAT201
DIS101 ─────────────────────────────►EDD301 ──► ALG301 ─┐
ALG101 ─────────────────────────────►                   ├──► ARQ401 ──► PRY401
INF101 ─► PRG201 ───────────────────►                   │
               \                    ►BDD301 ─────────────┘
                └──────────────────► REQ401 ──────────────────────────► PRY401
```

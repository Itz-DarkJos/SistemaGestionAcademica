# SistemaGestionAcademica
PROYECTO DE UCI 
# 📚 Sistema de Gestión de Materias por Plan (A y B)

Este proyecto es una aplicación de escritorio en Java que permite gestionar materias académicas divididas en dos planes de estudio: **Plan A (fijo)** y **Plan B (editable)**. Fue diseñado para facilitar la administración y visualización de asignaturas por cuatrimestres, incluyendo claves, nombres y seriación.

---

## 🛠️ Tecnologías utilizadas

- Java SE
- Swing (interfaz gráfica)
- Serialización de objetos para almacenamiento local
- Git + GitHub (control de versiones)

---

## ✨ Funcionalidades principales

### ✅ Plan A (fijo, no editable)
- Contiene las materias base del plan de estudios.
- Agrupadas por cuatrimestres.
- No se pueden agregar, modificar ni eliminar materias.
- Seriación definida automáticamente.

### 🧪 Plan B (editable)
- El usuario puede:
  - **Agregar materias** del Plan A seleccionando por cuatrimestre.
  - **Eliminar materias** previamente agregadas.
- La seriación se copia automáticamente desde el Plan A.
- Agrupadas por cuatrimestres al consultar.

### 📋 Otras funciones
- Consulta visual de materias con agrupación por cuatrimestre.
- Prevención de duplicados.
- Almacenamiento persistente en archivos `.txt` binarios mediante `FileManager`.

---


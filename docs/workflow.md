# Git Workflow

Este proyecto sigue la convención de **Conventional Commits** y un flujo de trabajo basado en ramas para mantener un historial de cambios claro, consistente y facilitar la colaboración del equipo.

---

# Convención de commits

## Tipos de commits permitidos

| Tipo | Uso |
|------|-----|
| `feat` | Nueva funcionalidad |
| `fix` | Corrección de errores |
| `docs` | Documentación |
| `refactor` | Refactorización sin cambiar el comportamiento |
| `test` | Pruebas |
| `chore` | Configuración, dependencias y tareas de mantenimiento |
| `style` | Cambios de formato (sin afectar la lógica) |

## Formato

```text
<tipo>(<scope opcional>): <descripción>
```

### Recomendaciones

- Escribir la descripción en **inglés**.
- Utilizar verbos en presente (por ejemplo: `add`, `update`, `remove`, `implement`, `fix`).
- Mantener la descripción breve y descriptiva.
- Usar un **scope** solo cuando aporte contexto (por ejemplo: `auth`, `ui`, `database`).

## Ejemplos

```text
feat: add user profile screen
feat(auth): implement login with Google
fix: resolve image upload crash
docs: update architecture documentation
refactor: simplify repository layer
test: add unit tests for login use case
chore: configure ktlint
```

---

# Estrategia de ramas

## Ramas permanentes

| Rama | Propósito |
|------|-----------|
| `main` | Contiene las versiones estables y listas para producción. |
| `develop` | Rama principal de integración para el desarrollo del proyecto. |

## Ramas temporales

| Rama | Uso |
|------|-----|
| `feature/<nombre>` | Desarrollo de nuevas funcionalidades. |
| `fix/<nombre>` | Corrección de errores. |
| `docs/<nombre>` | Cambios relacionados con documentación. |
| `refactor/<nombre>` | Refactorización de código. |
| `test/<nombre>` | Desarrollo o actualización de pruebas. |
| `chore/<nombre>` | Configuración, automatización y mantenimiento. |

## Ejemplos de ramas

```text
feature/login
feature/create-publication
feature/profile

fix/navigation
fix/image-upload

docs/readme
docs/design

refactor/repository

test/auth

chore/github-actions
```

---

# Flujo de trabajo

Cada nueva tarea debe comenzar a partir de un **Issue** y desarrollarse sobre una rama creada desde `develop`.

```text
Issue
   │
   ▼
develop
   │
   ▼
feature/create-publication
   │
   ▼
Pull Request
   │
   ▼
Review
   │
   ▼
Merge a develop
   │
   ▼
Al terminar el Sprint
   │
   ▼
Merge develop → main
```

## Resumen del proceso

1. Crear un **Issue** para la tarea.
2. Crear una rama desde `develop`.
3. Implementar los cambios utilizando la convención de commits.
4. Abrir un **Pull Request** hacia `develop`.
5. Realizar la revisión de código.
6. Una vez aprobado, hacer **merge** a `develop`.
7. Al finalizar el Sprint o una versión estable, hacer **merge** de `develop` hacia `main`.
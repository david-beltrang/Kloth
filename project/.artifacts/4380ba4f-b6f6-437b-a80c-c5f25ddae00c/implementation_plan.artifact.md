# Implementation Plan - Reusable Auth Error Composable

Review the current exception handling in `AuthRepository` and ViewModels, and implement a reusable `AuthErrorMessage` composable to display these errors in the Login and Register screens.

## User Review Required

> [!NOTE]
> The error message will be displayed using `MaterialTheme.colorScheme.error`, matching the "Log out" button style in the profile screen.

## Proposed Changes

### UI Components

#### [NEW] [AuthErrorMessage.kt](file:///C:/Users/USUARIO/StudioProjects/Kloth/project/app/src/main/java/com/example/kloth/ui/utils/AuthErrorMessage.kt)
- Create a new reusable composable that takes a `String` message and displays it with the error theme color.

### Login Screen

#### [MODIFY] [LoginScreenContent.kt](file:///C:/Users/USUARIO/StudioProjects/Kloth/project/app/src/main/java/com/example/kloth/ui/screens/login/LoginScreenContent.kt)
- Add `errorMessage` and `showErrorMessage` parameters.
- Place the `AuthErrorMessage` composable above the login button.

#### [MODIFY] [LoginScreen.kt](file:///C:/Users/USUARIO/StudioProjects/Kloth/project/app/src/main/java/com/example/kloth/ui/screens/login/LoginScreen.kt)
- Pass the appropriate message (either `credentialsMessage` or `errorMessage`) and visibility flag from the `LoginState`.

### Register Screen

#### [MODIFY] [RegisterScreenContent.kt](file:///C:/Users/USUARIO/StudioProjects/Kloth/project/app/src/main/java/com/example/kloth/ui/screens/register/RegisterScreenContent.kt)
- Add `errorMessage` and `showErrorMessage` parameters.
- Place the `AuthErrorMessage` composable above the register button.

#### [MODIFY] [RegisterScreen.kt](file:///C:/Users/USUARIO/StudioProjects/Kloth/project/app/src/main/java/com/example/kloth/ui/screens/register/RegisterScreen.kt)
- Pass the appropriate message and visibility flag from the `RegisterState`.

## Verification Plan

### Manual Verification
- Deploy the app to the emulator/device.
- **Login Screen**:
    - Try to log in with empty fields -> Verify "Por favor ingresa todos los campos" appears in red.
    - Try to log in with invalid credentials -> Verify "Credenciales incorrectas" appears in red.
- **Register Screen**:
    - Try to register with empty fields -> Verify "Todos los campos son obligatorios" appears in red.
    - Try to register with an existing email -> Verify "El correo ya está registrado" appears in red.

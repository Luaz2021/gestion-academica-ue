@echo off
setlocal EnableDelayedExpansion
title Instalador Automatico - Gestion Academica UE (Eclipse)

echo ======================================================================
echo    INSTALADOR AUTOMATICO - GESTION ACADEMICA UNIVERSIDAD EUROPEA
echo ======================================================================
echo.

:: 1. Verificacion de Java
echo [*] Comprobando instalacion de Java...
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [!] AVISO: Java no esta detectado en el PATH de Windows.
    echo     Asegurate de tener instalado JDK 21 (LTS).
) else (
    echo [OK] Java detectado en el sistema.
)

:: 2. Verificacion de Maven
echo [*] Comprobando instalacion de Maven...
mvn -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [!] AVISO: Maven no esta en el PATH del sistema (Eclipse usara el embebido).
) else (
    echo [OK] Maven detectado.
)

:: 3. Deteccion del Workspace de Eclipse
set "REPO_DIR=%~dp0"
:: Quitar la barra final
if "%REPO_DIR:~-1%"=="\" set "REPO_DIR=%REPO_DIR:~0,-1%"

set "WS_DEFAULT=%USERPROFILE%\eclipse-workspace"
set "WS_ALT=%USERPROFILE%\workspace"

echo [*] Buscando workspace de Eclipse...
if exist "%WS_DEFAULT%" (
    set "TARGET_WS=%WS_DEFAULT%"
) else if exist "%WS_ALT%" (
    set "TARGET_WS=%WS_ALT%"
) else (
    set "TARGET_WS=%WS_DEFAULT%"
    echo [*] Creando carpeta de workspace por defecto en: !TARGET_WS!
    mkdir "!TARGET_WS!" >nul 2>&1
)

echo [OK] Workspace detectado: "!TARGET_WS!"

:: 4. Comprobar si el repo ya esta en el workspace
set "DEST_DIR=!TARGET_WS!\gestion-academica-ue"

if /I "%REPO_DIR%"=="!DEST_DIR!" (
    echo.
    echo [OK] El proyecto ya se encuentra ubicado dentro de tu workspace de Eclipse.
    echo      Ruta: "!DEST_DIR!"
) else (
    echo [*] Sincronizando proyecto con el workspace de Eclipse...
    if not exist "!DEST_DIR!" (
        echo [*] Creando enlace de directorio (Junction)...
        mklink /J "!DEST_DIR!" "%REPO_DIR%" >nul 2>&1
        if !ERRORLEVEL! NEQ 0 (
            echo [*] Enlace no disponible, copiando archivos estructurales...
            xcopy /E /I /Q /Y "%REPO_DIR%" "!DEST_DIR!" >nul 2>&1
        )
        echo [OK] Proyecto sincronizado en: "!DEST_DIR!"
    ) else (
        echo [OK] La carpeta ya existe en el workspace.
    )
)

echo.
echo ======================================================================
echo                      INSTALACION COMPLETADA
echo ======================================================================
echo.
echo Pasos para abrir en Eclipse:
echo   1. Abre Eclipse IDE 2026-12 (o superior).
echo   2. Ve a: File -^> Import... -^> Maven -^> Existing Maven Projects.
echo   3. Selecciona la carpeta: "!TARGET_WS!\gestion-academica-ue"
echo   4. Pulsa Finish y espera a que Maven descargue las librerias.
echo.
echo Para abrir el disenador visual:
echo   - Clic derecho en Login.java -^> Open With -^> WindowBuilder Editor.
echo.
pause

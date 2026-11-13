@echo off
setlocal enabledelayedexpansion
cd /d "%~dp0"

echo ========================================================
echo   Lanzador Gestion Academica UE (JDK 21 + .m2)
echo ========================================================
echo.

where javac >nul 2>&1
if errorlevel 1 (
    echo [ERROR] No se encontro 'javac' en el PATH. Instala JDK 21 o superior.
    pause
    exit /b 1
)

set "M2=%USERPROFILE%\.m2\repository"
set "JAR_MARIADB=%M2%\org\mariadb\jdbc\mariadb-java-client\3.3.3\mariadb-java-client-3.3.3.jar"
set "JAR_FLATLAF=%M2%\com\formdev\flatlaf\3.4.1\flatlaf-3.4.1.jar"
set "JAR_LOMBOK=%M2%\org\projectlombok\lombok\1.18.38\lombok-1.18.38.jar"
set "JAR_CODEC=%M2%\commons-codec\commons-codec\1.16.1\commons-codec-1.16.1.jar"
set "JAR_SLF4J_NOP=%M2%\org\slf4j\slf4j-nop\2.0.16\slf4j-nop-2.0.16.jar"
set "JAR_SLF4J_API=%M2%\org\slf4j\slf4j-api\2.0.16\slf4j-api-2.0.16.jar"

for %%J in ("%JAR_MARIADB%" "%JAR_FLATLAF%" "%JAR_LOMBOK%" "%JAR_CODEC%" "%JAR_SLF4J_NOP%" "%JAR_SLF4J_API%") do (
    if not exist %%J (
        echo [ERROR] Falta dependencia Maven local: %%~J
        echo         Abre el proyecto en Eclipse: clic derecho -^> Maven -^> Update Project.
        pause
        exit /b 1
    )
)

set "CP=%JAR_MARIADB%;%JAR_FLATLAF%;%JAR_LOMBOK%;%JAR_CODEC%;%JAR_SLF4J_NOP%;%JAR_SLF4J_API%"

if not exist "target\classes" mkdir "target\classes"
if exist "src\main\resources" (
    xcopy /E /Y /I /Q "src\main\resources\*" "target\classes\" >nul
)

echo [1/2] Compilando fuentes Java...
dir /s /b "src\main\java\*.java" > "target\sources.tmp"
javac -encoding UTF-8 -d "target\classes" -cp "%CP%" @"target\sources.tmp"
set "COMPILE_EXIT=%ERRORLEVEL%"
del "target\sources.tmp" >nul 2>&1

if not "%COMPILE_EXIT%"=="0" (
    echo [ERROR] Fallo la compilacion. Revisa los errores arriba.
    pause
    exit /b %COMPILE_EXIT%
)

echo [2/2] Arrancando view.Login...
echo       (Asegurate de tener MariaDB/Laragon encendido en localhost:3306)
echo.
java -cp "target\classes;%CP%" view.Login
if errorlevel 1 (
    echo.
    echo [AVISO] La aplicacion cerro con codigo de error %ERRORLEVEL%.
    pause
)
endlocal

#!/usr/bin/env bash
# ==============================================================================
# Instalador Automatico Multiplataforma - Gestion Academica UE (Linux / macOS)
# ==============================================================================

set -e

echo "======================================================================"
echo "   INSTALADOR AUTOMATICO - GESTION ACADEMICA UNIVERSIDAD EUROPEA"
echo "======================================================================"
echo ""

# 1. Comprobacion de Java
echo "[*] Comprobando instalacion de Java..."
if command -v java >/dev/null 2>&1; then
    JAVA_VER=$(java -version 2>&1 | head -n 1)
    echo "[OK] Java detectado: $JAVA_VER"
else
    echo "[!] AVISO: Java no encontrado en el PATH. Instala JDK 21 (LTS)."
fi

# 2. Comprobacion de Maven
echo "[*] Comprobando Maven..."
if command -v mvn >/dev/null 2>&1; then
    MVN_VER=$(mvn -version | head -n 1)
    echo "[OK] Maven detectado: $MVN_VER"
else
    echo "[!] AVISO: Maven no encontrado en el PATH (Eclipse usara el embebido)."
fi

# 3. Deteccion de Workspace
REPO_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
WS_DEFAULT="$HOME/eclipse-workspace"
WS_ALT="$HOME/workspace"

if [ -d "$WS_DEFAULT" ]; then
    TARGET_WS="$WS_DEFAULT"
elif [ -d "$WS_ALT" ]; then
    TARGET_WS="$WS_ALT"
else
    TARGET_WS="$WS_DEFAULT"
    mkdir -p "$TARGET_WS"
fi

echo "[OK] Workspace detectado: $TARGET_WS"

DEST_DIR="$TARGET_WS/gestion-academica-ue"

if [ "$REPO_DIR" = "$DEST_DIR" ]; then
    echo "[OK] El proyecto ya esta en el workspace de Eclipse."
else
    if [ ! -d "$DEST_DIR" ]; then
        echo "[*] Creando enlace simbolico al workspace..."
        ln -s "$REPO_DIR" "$DEST_DIR"
        echo "[OK] Enlace creado en: $DEST_DIR"
    else
        echo "[OK] El destino ya existe en el workspace."
    fi
fi

echo ""
echo "======================================================================"
echo "                      INSTALACION COMPLETADA"
echo "======================================================================"
echo "Pasos para abrir en Eclipse:"
echo "  1. Abre Eclipse IDE (2026-09 o superior) con Lombok configurado (-javaagent:lombok.jar)."
echo "  2. File -> Import... -> Maven -> Existing Maven Projects."
echo "  3. Selecciona la carpeta: $DEST_DIR"
echo "  4. Si hace falta, clic derecho en el proyecto -> Maven -> Update Project."
echo "  5. Ejecuta con Ejecutar_Gestion_UE.launch -> Run As -> Ejecutar_Gestion_UE."
echo "  6. Abre las vistas con: Clic derecho en Login.java -> Open With -> WindowBuilder Editor."
echo ""

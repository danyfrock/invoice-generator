@echo off
chcp 65001 >nul
:: Définition des variables pour les chemins
set "WORKSPACE_DIR=C:\Users\daniel.lorenzi\eclipse-workspace\InvoiceGenerator"
set "POM_FILE=%WORKSPACE_DIR%\pom.xml"
set "INSTALL_DIR=C:\Program Files\InvoiceGenerator"
set "JDK_MODS_DIR=C:\Program Files\Java\jdk-22\jmods"
set "JAVAFX_SDK_DIR=C:\JavaFX\javafx-sdk-23.0.2"
set "TARGET_DIR=%WORKSPACE_DIR%\target"
set "JAVADOC_DIR=%TARGET_DIR%\site\apidocs"
set "DEFAULT_PRODUCT_CODE={42cdc10f-6833-356f-b92a-511eee1d12bf}"

:: Lecture de la version depuis le pom.xml
echo Debug : Recherche de la version dans le pom.xml...
for /f "tokens=2 delims=<version></version>" %%i in ('findstr /r "<version>[0-9]*\.[0-9]*\.[0-9]*-SNAPSHOT<\/version>" "%POM_FILE%"') do (
    set "VERSION=%%i"
)
if not defined VERSION (
    echo Erreur : Impossible de lire la version dans le pom.xml. Utilisation de la version par défaut : 0.0.1-SNAPSHOT
    set "VERSION=0.0.1-SNAPSHOT"
) else (
    echo Version lue avec succès : %VERSION%
)
set "EXE_VERSION=%VERSION:-SNAPSHOT=%"

echo Lancement du script pour la version %VERSION%...
echo Debug : Début du script

:: -----------------------------------
:: Étape 0 : Nettoyage préalable (tuer les tâches et désinstaller)
:: -----------------------------------
echo Tentative de fermeture de tous les processus liés à "InvoiceGenerator" ou "msiexec.exe"...

:: Fermeture des processus InvoiceGenerator
for /f "tokens=2" %%i in ('tasklist /FI "IMAGENAME eq InvoiceGenerator*.exe" /FO TABLE /NH 2^>nul') do (
    taskkill /PID %%i /F 2>nul
    if %errorlevel% equ 0 (
        echo Processus InvoiceGenerator avec PID %%i fermé avec succès.
    ) else (
        echo Erreur lors de la fermeture du processus avec PID %%i.
    )
)

:: Fermeture des processus msiexec.exe
for /f "tokens=2" %%i in ('tasklist /FI "IMAGENAME eq msiexec.exe" /FO TABLE /NH 2^>nul') do (
    taskkill /PID %%i /F 2>nul
    if %errorlevel% equ 0 (
        echo Processus msiexec.exe avec PID %%i fermé avec succès.
    ) else (
        echo Erreur lors de la fermeture du processus msiexec.exe avec PID %%i.
    )
)

:: Vérification finale des processus
tasklist | findstr /i "InvoiceGenerator msiexec.exe" >nul
if %errorlevel% equ 0 (
    echo Attention : Certains processus sont encore actifs. Tentative de poursuite...
) else (
    echo Nettoyage des processus terminé avec succès.
)

:: Recherche du ProductCode dans le registre
echo Recherche du ProductCode actuel dans le registre...
for /f "tokens=2 delims==" %%i in ('reg query "HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall" /s ^| findstr /i "InvoiceGenerator" ^| findstr "UninstallString"') do (
    for /f "tokens=3" %%j in ("%%i") do (
        set "PRODUCT_CODE=%%j"
    )
)
if not defined PRODUCT_CODE (
    echo Aucun ProductCode trouvé dans le registre. Utilisation du ProductCode par défaut : %DEFAULT_PRODUCT_CODE%
    set "PRODUCT_CODE=%DEFAULT_PRODUCT_CODE%"
) else (
    echo ProductCode trouvé dans le registre : %PRODUCT_CODE%
)

echo Désinstallation de l'application via MSI avec ProductCode %PRODUCT_CODE%...
msiexec /x %PRODUCT_CODE% /qn /norestart >nul 2>&1 && echo Désinstallation MSI réussie. || echo MSI non trouvé ou erreur ignorée. Poursuite avec suppression physique...

echo Vérification du dossier après désinstallation MSI...
if exist "%INSTALL_DIR%" (
    echo Dossier %INSTALL_DIR% trouvé après désinstallation MSI. Tentative de suppression...
) else (
    echo Dossier %INSTALL_DIR% non trouvé après désinstallation MSI.
)

echo Suppression d'une installation précédente dans %INSTALL_DIR%...
if exist "%INSTALL_DIR%" (
    :: Tentative de suppression normale
    rmdir /s /q "%INSTALL_DIR%" 2>nul
    if exist "%INSTALL_DIR%" (
        echo Échec de la suppression silencieuse. Tentative avec force...
        :: Tentative de suppression forcée après déverrouillage potentiel
        attrib -r -h -s "%INSTALL_DIR%\*.*" /s /d 2>nul
        rmdir /s /q "%INSTALL_DIR%" 2>nul
        if exist "%INSTALL_DIR%" (
            echo Erreur : Impossible de supprimer %INSTALL_DIR%. Vérifiez les permissions ou les fichiers verrouillés.
            echo Exécutez le script en mode administrateur si ce n'est pas déjà fait.
            pause
        ) else (
            echo Ancienne installation supprimée avec succès après tentative forcée.
        )
    ) else (
        echo Ancienne installation supprimée avec succès.
    )
) else (
    echo Aucune installation précédente trouvée dans %INSTALL_DIR%.
)
echo Debug : Nettoyage installation terminé

:: -----------------------------------
:: Étape 1 : Génération de la documentation
:: -----------------------------------
echo Génération de la documentation Javadoc...
cd %WORKSPACE_DIR%
call mvn -B clean install javadoc:javadoc >nul 2>&1 || echo Erreur lors de la génération Javadoc, mais on continue.
echo Debug : Étape 1 terminée

:: -----------------------------------
:: Étape 2 : Préparation et génération de l’installateur
:: -----------------------------------
echo Préparation du packaging...
call mvn -B clean package >nul 2>&1 || echo Erreur lors du packaging, mais on continue.
echo Debug : Packaging terminé

echo Suppression de l'ancien runtime...
rmdir /s /q %TARGET_DIR%\runtime 2>nul || echo Aucun runtime à supprimer, on continue.
echo Debug : Suppression runtime terminée

echo Création du runtime avec jlink...
jlink --module-path "%JDK_MODS_DIR%;%JAVAFX_SDK_DIR%" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,java.logging --output %TARGET_DIR%\runtime || echo Erreur jlink, mais on continue.
echo Debug : jlink terminé

echo Copie des dépendances...
copy %TARGET_DIR%\libs\*.jar %TARGET_DIR%\ 2>nul || echo Erreur lors de la copie, mais on continue.
echo Debug : Copie terminée

echo Génération de l'exécutable avec jpackage...
if not exist "%TARGET_DIR%\InvoiceGenerator-%VERSION%.jar" (
    echo Erreur : Le fichier InvoiceGenerator-%VERSION%.jar n'existe pas. Vérifiez la version dans le pom.xml.
    pause
    exit /b 1
)
jpackage --input %TARGET_DIR% --main-jar InvoiceGenerator-%VERSION%.jar --main-class com.invoicegenerator.Main --name InvoiceGenerator --app-version %EXE_VERSION% --type exe --runtime-image %TARGET_DIR%\runtime --win-dir-chooser --dest %TARGET_DIR% --verbose || echo Erreur jpackage, vérifiez les logs.
echo Debug : jpackage terminé

:: -----------------------------------
:: Étape 3 : Ouverture des résultats
:: -----------------------------------
echo Ouverture du dossier target...
start "" "%TARGET_DIR%"
echo Debug : Dossier target ouvert

echo Ouverture de la documentation Javadoc...
start "" "%JAVADOC_DIR%\index.html"
echo Debug : Javadoc ouvert

echo Lancement de l'exécutable généré...
cd %TARGET_DIR%
if exist "InvoiceGenerator-%EXE_VERSION%.exe" (
    start "" /wait "InvoiceGenerator-%EXE_VERSION%.exe"
    if %errorlevel% equ 0 (
        echo Exécutable lancé avec succès.
    ) else (
        echo Erreur lors du lancement de l'exécutable. Code d'erreur : %errorlevel%.
    )
) else (
    echo Erreur : L'exécutable InvoiceGenerator-%EXE_VERSION%.exe n'a pas été trouvé dans %TARGET_DIR%.
)
echo Debug : Exécutable lancé

pause
echo Debug : Fin du script
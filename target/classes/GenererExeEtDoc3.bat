@echo off
set "PROJECT_PATH=C:\Users\daniel.lorenzi\eclipse-workspace\InvoiceGenerator"
set "JAVA_PATH=C:\Program Files\Java\jdk-22"
set "JAVAFX_PATH=C:\JavaFX\javafx-sdk-23.0.2"
set "INSTALL_PATH=C:\Program Files\InvoiceGenerator"
set "TARGET_PATH=%PROJECT_PATH%\target"

:: Vérification des chemins
if not exist "%PROJECT_PATH%" (
    echo PROJECT_PATH no good : %PROJECT_PATH% n'existe pas.
    pause
    exit /b 1
)
if not exist "%TARGET_PATH%" (
    echo TARGET_PATH no good : %TARGET_PATH% n'existe pas. Peut-etre que le projet n'a pas encore ete compile.
    pause
    exit /b 1
)
if not exist "%JAVA_PATH%\jmods" (
    echo JAVA_PATH no good : %JAVA_PATH%\jmods n'existe pas. Verifiez votre installation JDK.
    pause
    exit /b 1
)
if not exist "%JAVAFX_PATH%" (
    echo JAVAFX_PATH no good : %JAVAFX_PATH% n'existe pas. Verifiez votre installation JavaFX.
    pause
    exit /b 1
)
if not exist "%INSTALL_PATH%" (
    echo INSTALL_PATH no good : %INSTALL_PATH% n'existe pas. Cela peut etre normal si l'application n'est pas encore installee.
)

:: Lecture de la version depuis le pom.xml
echo Debug : Recherche de la version dans le pom.xml...
for /f "tokens=2 delims=<version></version>" %%i in ('findstr /r "<version>[0-9]*\.[0-9]*\.[0-9]*-SNAPSHOT<\/version>" "%PROJECT_PATH%\pom.xml"') do (
    set "VERSION=%%i"
)
if not defined VERSION (
    echo Erreur : Impossible de lire la version dans le pom.xml. Utilisation de la version par defaut : 0.0.1-SNAPSHOT
    set "VERSION=0.0.1-SNAPSHOT"
) else (
    echo Version lue avec succes : %VERSION%
)
set "EXE_VERSION=%VERSION:-SNAPSHOT=%"

echo Lancement du script pour la version %VERSION%...
echo Debug : Debut du script

:: Étape 0 : Nettoyage préalable (tuer les tâches et désinstaller)
echo Tentative de fermeture de tous les processus contenant "InvoiceGenerator"...
for /f "tokens=2" %%i in ('tasklist ^| findstr /i "InvoiceGenerator"') do (
    taskkill /PID %%i /F 2>nul
    if %errorlevel% equ 0 (
        echo Processus avec PID %%i ferme avec succes.
    ) else (
        echo Erreur lors de la fermeture du processus avec PID %%i.
    )
) || echo Aucun processus trouve, on continue.
echo Nettoyage des processus termine.
echo Debug : Etape 0 terminee

echo Suppression d'une installation precedente dans %INSTALL_PATH%...
rmdir /s /q "%INSTALL_PATH%" 2>nul
if %errorlevel% equ 0 (
    echo Ancienne installation supprimee.
) else (
    echo Aucune installation trouvee ou erreur lors de la suppression.
)
echo Debug : Nettoyage installation termine

:: Étape 1 : Génération de la documentation
echo Generation de la documentation Javadoc...
cd "%PROJECT_PATH%"
call mvn -B clean install javadoc:javadoc >"%PROJECT_PATH%\maven-javadoc.log" 2>&1 || echo Erreur lors de la generation Javadoc, mais on continue.
echo Debug : Etape 1 terminee - Logs dans maven-javadoc.log

:: Étape 2 : Préparation et génération de l’installateur
echo Preparation du packaging...
call mvn -B clean package >"%PROJECT_PATH%\maven-package.log" 2>&1 || echo Erreur lors du packaging, mais on continue.
echo Debug : Packaging termine - Logs dans maven-package.log

echo Suppression de l'ancien runtime...
rmdir /s /q "%TARGET_PATH%\runtime" 2>nul || echo Aucun runtime a supprimer, on continue.
echo Debug : Suppression runtime terminee

echo Creation du runtime avec jlink...
jlink --module-path "%JAVA_PATH%\jmods;%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,java.logging --output "%TARGET_PATH%\runtime" || echo Erreur jlink, mais on continue.
echo Debug : jlink termine

echo Copie des dependances...
copy "%TARGET_PATH%\libs\*.jar" "%TARGET_PATH%\" 2>nul || echo Erreur lors de la copie, mais on continue.
echo Debug : Copie terminee

echo Generation de l'executable avec jpackage...
if not exist "%TARGET_PATH%\InvoiceGenerator-%VERSION%.jar" (
    echo Erreur : Le fichier InvoiceGenerator-%VERSION%.jar n'existe pas. Verifiez la version dans le pom.xml.
    pause
    exit /b 1
)
jpackage --input "%TARGET_PATH%" --main-jar "InvoiceGenerator-%VERSION%.jar" --main-class com.invoicegenerator.Main --name InvoiceGenerator --app-version %EXE_VERSION% --type exe --runtime-image "%TARGET_PATH%\runtime" --win-dir-chooser --dest "%TARGET_PATH%" --verbose || echo Erreur jpackage, verifiez les logs.
echo Debug : jpackage termine

:: Étape 3 : Ouverture des résultats
echo Ouverture du dossier target...
start "" "%TARGET_PATH%"
echo Debug : Dossier target ouvert

echo Ouverture de la documentation Javadoc...
start "" "%TARGET_PATH%\site\apidocs\index.html"
echo Debug : Javadoc ouvert

echo Lancement de l'executable genere...
cd "%TARGET_PATH%"
start "" "InvoiceGenerator-%EXE_VERSION%.exe" 2>nul || echo Executable non trouve, verifiez la generation.
echo Debug : Executable lance

pause
REM echo Debug : Fin du script
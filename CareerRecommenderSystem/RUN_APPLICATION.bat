@echo off
echo ========================================
echo  AI-Based Career Recommendation System
echo ========================================
echo.
echo Compiling Java files...
javac -encoding UTF-8 -cp ".;sqlite-jdbc-3.42.0.0.jar" *.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed!
    echo Please make sure Java JDK is installed and added to PATH.
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo.
echo Starting the application...
echo.
java -cp ".;sqlite-jdbc-3.42.0.0.jar" Main

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Failed to run the application!
    pause
    exit /b 1
)

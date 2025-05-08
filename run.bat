@echo off
REM 1) Compile
javac Recipe.java RecipeManager.java RecipeOrganizer.java
if %errorlevel% neq 0 (
  echo *** Compilation failed. ***
  pause
  exit /b 1
)

REM 
java RecipeOrganizer

REM 
pause

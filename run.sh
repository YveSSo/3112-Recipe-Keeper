#!/usr/bin/env bash


javac Recipe.java RecipeManager.java RecipeOrganizer.java
if [ $? -ne 0 ]; then
  echo "*** Compilation failed ***"
  exit 1
fi


java RecipeOrganizer


read -p "Press Enter to exit…"

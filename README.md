# Recipe Keeper

A simple console-based Java application for managing your personal recipe collection. You can add, view, search, delete, and edit recipes, with auto-saving to persist your data.

### Features

- Add a Recipe: Enter recipe name, ingredients, instructions, and tags (all line-by-line).
- View Recipes: List saved recipes and display details with clear formatting.
- Search Recipes: Lookup a recipe by name.
- Delete a Recipe: Remove a recipe by name.
- Edit a Recipe: Modify name, ingredients, instructions (line-by-line edit menu), and tags.
- Auto‑Save: Recipes are saved automatically after add/edit operations.

### Prerequistes

- Java Development Kit (JDK) 8 or newer installed. Ensure `javac` and `java` are on your system `PATH`.

### Project Structure
```
3112-Recipe-Keeper/
├── Recipe.java           # Recipe model class
├── RecipeManager.java    # Manages collection and file I/O
├── RecipeOrganizer.java  # Main application with menu and logic
├── recipes.dat           # Serialized data file (created at runtime)
├── run.bat               # Windows batch script to compile & run
├── run.sh                # macOS/Linux shell script to compile & run
└── README.md             # This file
```

### Running the Application

Before running, open your terminal (or PowerShell) and navigate to the project directory:
```
cd 3112-Recipe-Keeper
```

#### Windows

1. Open PowerShell or Command Prompt in the project directory.
2. Execute:
```
.\run.bat
```
3. Follow the on-screen prompts. Press any key when prompted to exit.

#### macOS/Linux

1. Open Terminal in the project directory
2. Make the script executable (once):
```
chmod +x run.sh
```
3. Run:
```
./run.sh
```
4. Follow the on-screen prompts and press Enter when prompted to exit.

#### Manual Compile & Run

If you prefer not to use scripts, you can compile and run manually:
```
# Compile
javac Recipe.java RecipeManager.java RecipeOrganizer.java

# Run
java RecipeOrganizer
```

### Data Persistence

- Recipes are stored in `recipes.dat` in the working directory.
- On startup, the application attempts to load existing data.
- On exit (or after add/edit), data is saved automatically.
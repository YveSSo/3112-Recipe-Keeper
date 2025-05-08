import java.io.*;
import java.util.*;

public class RecipeOrganizer {
    private static final RecipeManager recipeManager = new RecipeManager();
    private static final String FILENAME = "recipes.dat";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            recipeManager.loadRecipesFromFile(FILENAME);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing recipes found. Starting fresh!");
        }

        boolean running = true;
        while (running) {
            System.out.println("\nWelcome to Recipe Organizer!");
            System.out.println("1. Add a Recipe");
            System.out.println("2. View Recipes");
            System.out.println("3. Search Recipes");
            System.out.println("4. Delete a Recipe");
            System.out.println("5. Edit a Recipe");
            System.out.println("6. Save and Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    addRecipe(scanner);
                    break;
                case 2:
                    viewRecipes(scanner);
                    break;
                case 3:
                    searchRecipes(scanner);
                    break;
                case 4:
                    deleteRecipe(scanner);
                    break;
                case 5:
                    editRecipe(scanner);
                    break;
                case 6:
                    saveAndExit();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again!");
            }
        }

       // scanner.close();
    }

    private static void addRecipe(Scanner scanner) {
        System.out.print("Enter recipe name: ");
        String name = scanner.nextLine();

        //System.out.print("Enter ingredients (comma-separated): ");
        List<String> ingredients = readLinesUntilDone(scanner, "Enter ingredients:");

        List<String> instructionsList = readLinesUntilDone(scanner, "Enter instructions:");
        StringBuilder instrBuilder = new StringBuilder();
        for(int i = 0; i < instructionsList.size(); i++) {
            instrBuilder.append(i+1).append(". ").append(instructionsList.get(i)).append("\n");
        }
        String instructions = instrBuilder.toString();

        List<String> tags = readLinesUntilDone(scanner, "Enter tags:");

        Recipe recipe = new Recipe(name, ingredients, instructions, tags);
        recipeManager.addRecipe(recipe);

        System.out.println("Recipe added successfully!");

        try {
            recipeManager.saveRecipesToFile(FILENAME);
          } catch (IOException e) {
            System.out.println("Error auto-saving: " + e.getMessage());
          }
          
    }

    private static void viewRecipes(Scanner scanner) {
        List<Recipe> recipes = recipeManager.getAllRecipes();
        if (recipes.isEmpty()) {
            System.out.println("No recipes found.");
            return;
        }

        for (int i = 0; i < recipes.size(); i++) {
            System.out.println((i + 1) + ". " + recipes.get(i).getName());
        }

        System.out.print("Enter the number of the recipe to view (or 0 to go back): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= recipes.size()) {
            displayRecipe(recipes.get(choice - 1));
        }        
        //scanner.close();
    }

    private static void searchRecipes(Scanner scanner) {
        System.out.print("Enter recipe name to search: ");
        String name = scanner.nextLine();

        Recipe recipe = recipeManager.searchRecipeByName(name);
        if (recipe != null) {
            displayRecipe(recipe);
        } else {
            System.out.println("Recipe not found.");
        }
    }

    private static void deleteRecipe(Scanner scanner) {
        System.out.print("Enter recipe name to delete: ");
        String name = scanner.nextLine();

        recipeManager.deleteRecipe(name);
        System.out.println("Recipe deleted (if it existed).");
    }

    private static void editRecipe(Scanner scanner) {
        List<Recipe> recipes = recipeManager.getAllRecipes();
        if (recipes.isEmpty()) {
          System.out.println("No recipes to edit.");
          return;
        }
        for (int i = 0; i < recipes.size(); i++) {
          System.out.printf("%d. %s%n", i+1, recipes.get(i).getName());
        }
        System.out.print("Select recipe number to edit: ");
        int idx = scanner.nextInt();
        scanner.nextLine();
        if (idx < 1 || idx > recipes.size()) {
          System.out.println("Invalid choice.");
          return;
        }
        Recipe old = recipes.get(idx - 1);
        
        // Name
        System.out.printf("Current name: %s%nEnter new name (or Enter to keep): ", old.getName());
        String newName = scanner.nextLine();
        if (newName.isEmpty()) newName = old.getName();
        
        // Ingredients
        System.out.printf("Current ingredients: %s%n",
                          String.join(", ", old.getIngredients()));
        //String ingLine = scanner.nextLine();
        List<String> ingInput = readLinesUntilDone(scanner, "Enter new ingredients or press 'Enter' to keep:");
        List<String> newIngredients = ingInput.isEmpty()
          ? old.getIngredients()
          : ingInput;
        
        // Instructions
        System.out.println("Current instructions:");
        System.out.println(old.getInstructions());
        List<String> instrInput = readLinesUntilDone(scanner, "Enter new instructions:");
        StringBuilder newInstrBuilder = new StringBuilder();
        if(instrInput.isEmpty()){
            newInstrBuilder.append(old.getInstructions());
        } else {
            for (int i = 0; i < instrInput.size(); i++) {
                newInstrBuilder.append(i + 1).append(". ").append(instrInput.get(i)).append("\n");
            }
        }
        String newInstructions = newInstrBuilder.toString();
        
        // Tags
        System.out.printf("Current tags: %s%nEnter new tags (comma-sep) or Enter to keep: ",
                          String.join(", ", old.getTags()));
        List<String> tagInput = readLinesUntilDone(scanner, "Enter new tags:");
        List<String> newTags = tagInput.isEmpty()
          ? old.getTags()
          : tagInput;
        
          // Update recipe
        Recipe updated = new Recipe(newName, newIngredients, newInstructions, newTags);
        recipes.set(idx - 1, updated);
        System.out.println("Recipe updated successfully!");

        try {
            recipeManager.saveRecipesToFile(FILENAME);
          } catch (IOException e) {
            System.out.println("Error auto-saving: " + e.getMessage());
          }
          
      }

      private static List<String> readLinesUntilDone(Scanner scanner, String prompt) {
        System.out.println(prompt + " (one per line; type 'done' when finished):");
        List<String> lines = new ArrayList<>();
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if(line.isEmpty() || line.equalsIgnoreCase(line)){
                break;
            }
        }
        return lines;
    }
    

      /**
 * Nicely prints Name, Ingredients, and Instructions with blank lines
 */
private static void displayRecipe(Recipe r) {
    System.out.println();                              // leading blank line

    // Name
    System.out.println("Name:");
    System.out.println("  " + r.getName());

    // Ingredients
    System.out.println();                              // blank line
    System.out.println("Ingredients:");
    for (String ing : r.getIngredients()) {
        System.out.println("  - " + ing);
    }

    // Instructions
    System.out.println();                              // blank line
    System.out.println("Instructions:");
    for (String line : r.getInstructions().split("\\r?\\n")) {
        System.out.println("  " + line);
    }

    System.out.println();                              // trailing blank line
}

      

    private static void saveAndExit() {
        try {
            recipeManager.saveRecipesToFile(FILENAME);
            System.out.println("Recipes saved successfully. Goodbye!");
        } catch (IOException e) {
            System.out.println("Error saving recipes: " + e.getMessage());
        }
    }
}
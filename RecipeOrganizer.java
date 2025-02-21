import java.io.*;
import java.util.*;

public class RecipeOrganizer {
    private static final RecipeManager recipeManager = new RecipeManager();
    private static final String FILENAME = "recipes.dat";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Load recipes from file (if exists)
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
            System.out.println("5. Save and Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addRecipe(scanner);
                    break;
                case 2:
                    viewRecipes();
                    break;
                case 3:
                    searchRecipes(scanner);
                    break;
                case 4:
                    deleteRecipe(scanner);
                    break;
                case 5:
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

        System.out.print("Enter ingredients (comma-separated): ");
        List<String> ingredients = Arrays.asList(scanner.nextLine().split(",\\s*"));

        System.out.print("Enter instructions: ");
        String instructions = "";
        while (true){
            String line = scanner.nextLine();
            if (line.toLowerCase().equals("done")) {
                break;
            }
            instructions += line + "\n";
        }

        System.out.print("Enter tags (comma-separated): ");
        List<String> tags = Arrays.asList(scanner.nextLine().split(",\\s*"));

        Recipe recipe = new Recipe(name, ingredients, instructions, tags);
        recipeManager.addRecipe(recipe);

        System.out.println("Recipe added successfully!");
    }

    private static void viewRecipes() {
        List<Recipe> recipes = recipeManager.getAllRecipes();
        if (recipes.isEmpty()) {
            System.out.println("No recipes found.");
            return;
        }

        for (int i = 0; i < recipes.size(); i++) {
            System.out.println((i + 1) + ". " + recipes.get(i).getName());
        }

        System.out.print("Enter the number of the recipe to view (or 0 to go back): ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= recipes.size()) {
            System.out.println("\n" + recipes.get(choice - 1));
        }
        //scanner.close();
    }

    private static void searchRecipes(Scanner scanner) {
        System.out.print("Enter recipe name to search: ");
        String name = scanner.nextLine();

        Recipe recipe = recipeManager.searchRecipeByName(name);
        if (recipe != null) {
            System.out.println("\n" + recipe);
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

    private static void saveAndExit() {
        try {
            recipeManager.saveRecipesToFile(FILENAME);
            System.out.println("Recipes saved successfully. Goodbye!");
        } catch (IOException e) {
            System.out.println("Error saving recipes: " + e.getMessage());
        }
    }
}
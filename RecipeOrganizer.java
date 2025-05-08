import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1: addRecipe(scanner);       break;
                case 2: viewRecipes(scanner);     break;
                case 3: searchRecipes(scanner);   break;
                case 4: deleteRecipe(scanner);    break;
                case 5: editRecipe(scanner);      break;
                case 6: saveAndExit(); running = false; break;
                default: System.out.println("Invalid choice. Try again!");
            }
        }
        scanner.close();
    }

    private static void addRecipe(Scanner scanner) {
        System.out.print("\nEnter recipe name: ");
        String name = scanner.nextLine();

        List<String> ingredients     = readLinesUntilDone(scanner, "\nEnter ingredients");
        List<String> instructionsList = readLinesUntilDone(scanner, "\nEnter instructions");
        List<String> tags            = readLinesUntilDone(scanner, "\nEnter tags");

        // number the instructions
        StringBuilder instrBuilder = new StringBuilder();
        for (int i = 0; i < instructionsList.size(); i++) {
            instrBuilder.append(i+1).append(". ").append(instructionsList.get(i)).append("\n");
        }

        Recipe recipe = new Recipe(name, ingredients,
                                   instrBuilder.toString(),
                                   tags);
        recipeManager.addRecipe(recipe);
        System.out.println("Recipe added successfully!");

        autoSave();
    }

    private static void viewRecipes(Scanner scanner) {
        List<Recipe> recipes = recipeManager.getAllRecipes();
        if (recipes.isEmpty()) {
            System.out.println("No recipes found.");
            return;
        }
        for (int i = 0; i < recipes.size(); i++) {
            System.out.printf("%d. %s%n", i+1, recipes.get(i).getName());
        }
        System.out.print("Enter number to view (0 to back): ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice>=1 && choice<=recipes.size()) {
            displayRecipe(recipes.get(choice-1));
        }
    }

    private static void searchRecipes(Scanner scanner) {
        System.out.print("Enter recipe name to search: ");
        String name = scanner.nextLine();
        Recipe r = recipeManager.searchRecipeByName(name);
        if (r!=null) displayRecipe(r);
        else System.out.println("Recipe not found.");
    }

    private static void deleteRecipe(Scanner scanner) {
        System.out.print("Enter recipe name to delete: ");
        String name = scanner.nextLine();
        recipeManager.deleteRecipe(name);
        System.out.println("Recipe deleted (if existed).");
    }

    private static void editRecipe(Scanner scanner) {
        List<Recipe> recipes = recipeManager.getAllRecipes();
        if (recipes.isEmpty()) {
            System.out.println("No recipes to edit."); 
            return;
        }
        for (int i=0; i<recipes.size(); i++) {
            System.out.printf("%d. %s%n", i+1, recipes.get(i).getName());
        }
        System.out.print("Select recipe number to edit: ");
        int idx = scanner.nextInt();
        scanner.nextLine();
        if (idx<1 || idx>recipes.size()) {
            System.out.println("Invalid choice."); 
            return;
        }
        Recipe old = recipes.get(idx-1);

        System.out.printf("Current name: %s\n\nEnter new name (or blank to keep): ", old.getName());
        String newName = scanner.nextLine().trim();
        if (newName.isEmpty()) newName = old.getName();

        System.out.printf("Current ingredients: %s%n", String.join(", ", old.getIngredients()), "\n\n");
        System.out.println("\nEnter new ingredients one per line, or press Enter to keep existing:");
        String firstIng = scanner.nextLine().trim();
        List<String> newIngredients;
        if (firstIng.isEmpty()) {
            newIngredients = old.getIngredients();
        } else {
            newIngredients = new ArrayList<>();
            newIngredients.add(firstIng);
            while (true) {
                System.out.print("> ");
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("done")) break;
                if (!line.isEmpty()) newIngredients.add(line);
            }
        }

        System.out.println("Current instructions:");
        String[] stored = old.getInstructions().split("\\r?\\n");
        List<String> newInst = new ArrayList<>();
        for (String line : stored) {
            if(line.isBlank()) continue;
            newInst.add(line.replaceFirst("^\\d+\\.\\s*", ""));
        }

        printList(newInst);

        boolean editing = true;
        while (editing) {
            System.out.println("Choose [#] to edit, A to add, D to delete, or 0 when done:");
            System.out.print("> ");
            String cmd = scanner.nextLine().trim();
            if (cmd.equals("0")) {
                editing = false;
            } else if (cmd.equalsIgnoreCase("A")) {
                System.out.print("New instruction: ");
                newInst.add(scanner.nextLine().trim());
                printList(newInst);
            } else if (cmd.equalsIgnoreCase("D")) {
                System.out.print("Line to delete: ");
                int del = Integer.parseInt(scanner.nextLine().trim());
                if (del>=1 && del<=newInst.size()) newInst.remove(del-1);
                printList(newInst);
            } else {
                try {
                    int num = Integer.parseInt(cmd);
                    if (num>=1 && num<=newInst.size()) {
                        System.out.printf("Replace line %d: ", num);
                        newInst.set(num-1, scanner.nextLine().trim());
                        printList(newInst);
                    } else {
                        System.out.println("Invalid number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Unknown command.");
                }
            }
        }
        StringBuilder newInstrBuilder = new StringBuilder();
        for (int i=0; i<newInst.size(); i++) {
            newInstrBuilder.append(i+1).append(". ").append(newInst.get(i)).append("\n");
        }
        String newInstructions = newInstrBuilder.toString();

        System.out.printf("\nCurrent tags: %s%n", String.join(", ", old.getTags()));
        System.out.println("Enter new tags one per line, or press Enter to keep existing (type 'done' to finish):");
        String firstTag = scanner.nextLine().trim();
        List<String> newTags;
        if (firstTag.isEmpty()) {
            newTags = old.getTags();
        } else {
            newTags = new ArrayList<>();
            newTags.add(firstTag);
            while (true) {
                System.out.print("> ");
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("done")) break;
                if (!line.isEmpty()) newTags.add(line);
            }
        }

        Recipe updated = new Recipe(newName, newIngredients, newInstructions, newTags);
        recipes.set(idx-1, updated);
        System.out.println("Recipe updated successfully!");
        autoSave();
    }

    private static List<String> readLinesUntilDone(Scanner scanner, String prompt) {
        System.out.println(prompt + " (type each line; 'done' to finish):");
        List<String> lines = new ArrayList<>();
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("done")) break;
            if (!line.isEmpty()) lines.add(line);
        }
        return lines;
    }

    private static void printList(List<String> list) {
        System.out.println();
        for (int i=0; i<list.size(); i++) {
            System.out.printf("%d. %s%n", i+1, list.get(i));
        }
        System.out.println();
    }

    private static void displayRecipe(Recipe r) {
        System.out.println("\nName:\n  " + r.getName() + "\n");
        System.out.println("Ingredients:");
        for (String ing : r.getIngredients()) {
            System.out.println("  - " + ing);
        }
        System.out.println("\nInstructions:");
        for (String line : r.getInstructions().split("\\r?\\n")) {
            System.out.println("  " + line);
        }
        System.out.println("\nTags:");
        for (String tag : r.getTags()) {
            System.out.println("  " + tag);
        }
        System.out.println();
    }

    private static void autoSave() {
        try {
            recipeManager.saveRecipesToFile(FILENAME);
            System.out.println("Auto-saved recipes.");
        } catch (IOException e) {
            System.out.println("Error auto-saving: " + e.getMessage());
        }
    }

    private static void saveAndExit() {
        autoSave();
        System.out.println("Goodbye!");
    }
}

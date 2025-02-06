import java.io.*;
import java.util.*;

public class RecipeManager {
    private List<Recipe> recipes = new ArrayList<>();

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public List<Recipe> getAllRecipes() {
        return recipes;
    }

    public Recipe searchRecipeByName(String name) {
        for (Recipe recipe : recipes) {
            if (recipe.getName().equalsIgnoreCase(name)) {
                return recipe;
            }
        }
        return null;
    }

    public void deleteRecipe(String name) {
        recipes.removeIf(recipe -> recipe.getName().equalsIgnoreCase(name));
    }

    public void saveRecipesToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(recipes);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadRecipesFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            recipes = (List<Recipe>) ois.readObject();
        }
    }
}

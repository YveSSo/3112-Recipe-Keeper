import java.util.List;

public class Recipe {
    private String name;
    private List<String> ingredients;
    private String instructions;
    private List<String> tags;

    public Recipe(String name, List<String> ingredients, String instructions, List<String> tags) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n" +
               "Ingredients: " + String.join(", ", ingredients) + "\n" +
               "Instructions: " + instructions + "\n" +
               "Tags: " + String.join(", ", tags);
    }
}

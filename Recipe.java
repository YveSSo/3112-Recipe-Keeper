import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable{
    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<String> ingredients;
    private final String instructions;
    private final List<String> tags;

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

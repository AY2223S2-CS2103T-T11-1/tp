package seedu.wife.logic.commands.foodcommands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.wife.commons.core.Messages;
import seedu.wife.logic.commands.Command;
import seedu.wife.logic.commands.CommandResult;
import seedu.wife.logic.commands.exceptions.CommandException;
import seedu.wife.model.Model;
import seedu.wife.model.food.Food;
import seedu.wife.model.tag.Tag;

/**
 * Deletes a food identified using it's displayed index from WIFE.
 */
public class DeleteByTagCommand extends Command {

    public static final String COMMAND_WORD = "delbytag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes food that has specified tags.\n"
            + "Example: "
            + COMMAND_WORD + " n/vegetable\n"
            + COMMAND_WORD + " n/vegetable n/dairy n/grains";

    public static final String DELETE_FOOD_SUCCESS_MESSAGE = "Deleted Food:";

    private final Set<Tag> targetTags;

    /**
     * Constructor to create a new DeleteByTag object.
     */
    public DeleteByTagCommand(Tag targetTag) {
        this.targetTags = new HashSet<Tag>();
        this.targetTags.add(targetTag);
    }


    /**
     * Constructor to create a new DeleteByTag object.
     */
    public DeleteByTagCommand(Set<Tag> targetTag) {
        this.targetTags = targetTag;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Food> foodList = List.copyOf(model.getFoodList());
        String deletedFoodSuccessMessage = DELETE_FOOD_SUCCESS_MESSAGE;
        boolean throwError = true;


        for (Tag tag : this.targetTags) {
            for (Food food : foodList) {
                Set<Tag> foodTags = food.getTags();

                if (foodTags.contains(tag)) {
                    throwError = false;
                    deletedFoodSuccessMessage = deletedFoodSuccessMessage + "\n" + food;
                    model.deleteFood(food);
                }
            }
        }

        if (throwError) {
            throw new CommandException(String.format(Messages.MESSAGE_TAG_NOT_FOUND, this.targetTags));
        }

        return new CommandResult(deletedFoodSuccessMessage);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteByTagCommand // instanceof handles nulls
                && targetTags.equals(((DeleteByTagCommand) other).targetTags)); // state check
    }
}
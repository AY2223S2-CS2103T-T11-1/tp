package seedu.wife.logic.commands.tagcommands;

import static java.util.Objects.requireNonNull;
import static seedu.wife.commons.core.Messages.MESSAGE_DOUBLE_TAG;
import static seedu.wife.commons.core.Messages.MESSAGE_MAXIMUM_TAG_FOOD;
import static seedu.wife.commons.core.Messages.MESSAGE_SUCCESSFUL_FOOD_TAG;

import java.util.List;
import java.util.Set;

import seedu.wife.commons.core.Messages;
import seedu.wife.commons.core.index.Index;
import seedu.wife.logic.commands.Command;
import seedu.wife.logic.commands.CommandResult;
import seedu.wife.logic.commands.exceptions.CommandException;
import seedu.wife.model.Model;
import seedu.wife.model.food.Food;
import seedu.wife.model.tag.Tag;

/**
 * Command to tag food.
 */
public class TagFoodCommand extends Command {
    public static final String COMMAND_WORD = "tag";
    public static final String MESSAGE_USAGE = "Tag a food with any of your tags in the list.\n"
            + "Format:\n"
            + "tag <index> n/<tag name>\n"
            + "example: tag 1 n/Vege";
    private Tag tag;
    private Index index;

    /**
     * Constructor to create a new TagFoodCommand object.
     */
    public TagFoodCommand(String tagName, Index index) {
        requireNonNull(tagName);
        requireNonNull(index);
        this.tag = new Tag(tagName);
        this.index = index;
    }

    @Override
    public CommandResult<String> execute(Model model) throws CommandException {
        Food foodToTag = TagFoodCommand.getFoodToTag(model, tag, index);
        Set<Tag> foodTags = foodToTag.getCurrentTags();

        if (foodTags.size() >= 4) {
            throw new CommandException(String.format(MESSAGE_MAXIMUM_TAG_FOOD,
                    foodToTag.getName(), tag.getTagName()));
        }

        if (foodTags.contains(tag)) {
            throw new CommandException(String.format(MESSAGE_DOUBLE_TAG,
                    foodToTag.getName(), tag.getTagName()));
        }

        foodTags.add(tag);
        Food editedFood = foodToTag.createNewFoodWithNewTags(foodToTag, foodTags);
        model.setFood(foodToTag, editedFood);

        return CommandResult.from(MESSAGE_SUCCESSFUL_FOOD_TAG, "");
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagFoodCommand // instanceof handles nulls
                && tag.equals(((TagFoodCommand) other).tag))
                && index.equals(((TagFoodCommand) other).index); // state check
    }

    // Tag utils
    public static Food getFoodToTag(Model model, Tag tag, Index index) throws CommandException {
        requireNonNull(model);
        requireNonNull(tag);
        requireNonNull(index);

        if (!model.hasTag(tag)) {
            throw new CommandException(String.format(Messages.MESSAGE_TAG_NOT_FOUND, tag.getTagName()));
        }

        List<Food> foodList = model.getFilteredFoodList();

        if (index.getZeroBased() >= foodList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
        }

        return foodList.get(index.getZeroBased());
    }

}

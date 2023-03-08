package seedu.wife.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.wife.model.ReadOnlyWife;
import seedu.wife.model.Wife;
import seedu.wife.model.food.Food;
import seedu.wife.model.tag.Tag;

/**
 * Contains utility methods for populating {@code WIFE} with sample data.
 */
public class SampleDataUtil {
    public static Food[] getSampleFood() {
        return new Food[] {};
    }

    public static ReadOnlyWife getSampleWife() {
        Wife sampleWife = new Wife();
        for (Food sampleFood : getSampleFood()) {
            sampleWife.addFood(sampleFood);
        }
        return sampleWife;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
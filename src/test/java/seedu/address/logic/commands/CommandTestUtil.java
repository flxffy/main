package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXPIRY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUANTITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Inventory;
import seedu.address.model.Model;
import seedu.address.model.medicine.NameContainsKeywordsPredicate;
import seedu.address.model.medicine.Medicine;
import seedu.address.testutil.EditMedicineDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_QUANTITY_AMY = "111";
    public static final String VALID_QUANTITY_BOB = "222";
    public static final String VALID_EXPIRY_AMY = "27/11/2019";
    public static final String VALID_EXPIRY_BOB = "09/07/2019";
    public static final String VALID_COMPANY_AMY = "Merck & Co. (MSD)";
    public static final String VALID_COMPANY_BOB = "Sanofi";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String QUANTITY_DESC_AMY = " " + PREFIX_QUANTITY + VALID_QUANTITY_AMY;
    public static final String QUANTITY_DESC_BOB = " " + PREFIX_QUANTITY + VALID_QUANTITY_BOB;
    public static final String EXPIRY_DESC_AMY = " " + PREFIX_EXPIRY + VALID_EXPIRY_AMY;
    public static final String EXPIRY_DESC_BOB = " " + PREFIX_EXPIRY + VALID_EXPIRY_BOB;
    public static final String COMPANY_DESC_AMY = " " + PREFIX_COMPANY + VALID_COMPANY_AMY;
    public static final String COMPANY_DESC_BOB = " " + PREFIX_COMPANY + VALID_COMPANY_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_QUANTITY_DESC = " " + PREFIX_QUANTITY + "911a"; // 'a' not allowed in quantities
    public static final String INVALID_EXPIRY_DESC = " " + PREFIX_EXPIRY + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_COMPANY_DESC = " " + PREFIX_COMPANY; // empty string not allowed for company name
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditMedicineDescriptor DESC_AMY;
    public static final EditCommand.EditMedicineDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditMedicineDescriptorBuilder().withName(VALID_NAME_AMY)
                .withQuantity(VALID_QUANTITY_AMY).withExpiry(VALID_EXPIRY_AMY).withCompany(VALID_COMPANY_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditMedicineDescriptorBuilder().withName(VALID_NAME_BOB)
                .withQuantity(VALID_QUANTITY_BOB).withExpiry(VALID_EXPIRY_BOB).withCompany(VALID_COMPANY_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel} <br>
     * - the {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            CommandResult expectedCommandResult, Model expectedModel) {
        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);
        try {
            CommandResult result = command.execute(actualModel, actualCommandHistory);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
            assertEquals(expectedCommandHistory, actualCommandHistory);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandHistory, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage, Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, actualCommandHistory, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the inventory, filtered medicine list and selected medicine in {@code actualModel} remain unchanged <br>
     * - {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandFailure(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        Inventory expectedInventory = new Inventory(actualModel.getInventory());
        List<Medicine> expectedFilteredList = new ArrayList<>(actualModel.getFilteredMedicineList());
        Medicine expectedSelectedMedicine = actualModel.getSelectedMedicine();

        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);

        try {
            command.execute(actualModel, actualCommandHistory);
            throw new AssertionError("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedInventory, actualModel.getInventory());
            assertEquals(expectedFilteredList, actualModel.getFilteredMedicineList());
            assertEquals(expectedSelectedMedicine, actualModel.getSelectedMedicine());
            assertEquals(expectedCommandHistory, actualCommandHistory);
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the medicine at the given {@code targetIndex} in the
     * {@code model}'s inventory.
     */
    public static void showMedicineAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredMedicineList().size());

        Medicine medicine = model.getFilteredMedicineList().get(targetIndex.getZeroBased());
        final String[] splitName = medicine.getName().fullName.split("\\s+");
        model.updateFilteredMedicineList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredMedicineList().size());
    }

    /**
     * Deletes the first medicine in {@code model}'s filtered list from {@code model}'s inventory.
     */
    public static void deleteFirstMedicine(Model model) {
        Medicine firstMedicine = model.getFilteredMedicineList().get(0);
        model.deleteMedicine(firstMedicine);
        model.commitInventory();
    }

}

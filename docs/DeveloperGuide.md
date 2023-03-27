---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `AddressBookParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Item-related Features

#### General Consideration Design

The design of `Food` class follows closely to the original `Person` class in AB3, with some modifications made to fit
the usage of WIFE.

- `Person` class was renamed to `Food` with its attribute modified as mentioned above.
- Addition of individual tags storage for each item to store their associated `Tag`.

All foods are stored in the `UniqueFoodList`.

#### Adding a Food

The `add` command adds a new `Food` in WIFE.

**Implementation**

The first stage of the implementation is parsing the user input to `AddCommand`. `AddCommandParser` is used
to parse and check whether the user input for each attribute `Food` is valid. After which an `AddCommand` object is 
created with the new `Food` object.
The second stage requires AddCommand#execute() to be called.

**Usage Scenario**

1. The user specifies a set of attributes for the new `Food` when adding a new `Food`.
2. If any of the attributes is empty, an error response is returned and users will be prompted to key in the command 
   with all necessary attributes being inserted.
3. If any of the inserted attributes is invalid, an error response is returned and users will be prompted to key in the 
   command with valid attributes.
4. If the `Food` inserted by the user has the same name and expiry date as another `Food` in the `Model`, an error is 
   returned to inform the user that there is a duplicated copy of `Food` in the `UniqueFoodList`.
5. Completion of step 4 without any exceptions will result in successful addition of a new `Food` in WIFE and stored in
   `UniqueFoodList`

The following sequence diagram shows how the `add` command.

*insert sequence diagram*

#### Editing a Food

The `edit` command edits the attribute of selected `Food` in WIFE. The selected `Food` is specified
by retrieving from the one-indexed food list.

**Implementation**

The first stage of the implementation is parsing the user input to `EditCommand`. `EditCommandParser` is used
to parse and check whether the user input for the new attribute of `Food` is valid. After which an `EditCommand` object is
created with new `Food` with updated attributes.
The second stage requires EditCommand#execute() to be called.

**Usage Scenario**

1. The user specifies the food index that represents the `Food` to be edited.
2. If the index provided is a negative or zero index, an error response is returned and users will be 
   prompted to enter a valid index.
3. The user specifies the new attribute(s) for the `Food` when editing the `Food`. There must be at least one attribute
   provided when editing `Food`.
4. If any of the attributes is empty, an error response is returned and users will be prompted to key in the command
   with all necessary attributes being inserted.
5. If any of the inserted attributes is invalid, an error response is returned and users will be prompted to key in the
   command with valid attributes.
6. If the `Food` inserted by the user has the same name and expiry date as another `Food` in the `Model`, an error is
   returned to inform the user that there is a duplicated copy of `Food` in the `UniqueFoodList`.
7. Completion of step 6 without any exceptions will result in successful addition of a new `Food` in WIFE and stored in
   `UniqueFoodList`

The following sequence diagram shows how the `add` command.

*insert sequence diagram*

#### \[Implementing\] Increase/Decrease quantity of a food item.

**Overview**

The increase/decrease quantity feature is meant to be a shorthand for users to change the quantity of a particular food item.
Traditionally, to change the quantity of an item, the user would use the edit command to edit the quantity of a food item.
The user can now specify `inc` or `dec` to increase or decrease the quantity of the indexed food item respectively.

**Design considerations**

* **Alternative 1:** The command parameter will be the new quantity of the food item to edit
    * Pros:
        * Easily implemented. The command parameter will be set as the new quantity of the item.
    * Cons:
        * May not be intuitive for the user, as the command is to increase/decrease the quantity.
        * Can be unnecessarily complicated for the user, i.e entering a higher quantity than the current quantity for
          `inc` and vice versa.
        * Does not significantly value-add to the product as compared to just using the edit command.

* **Alternative 2 (Current implementation):** The command parameter will be the quantity to increase/decrease the
  quantity of the food item by.
    * Pros:
        * Intuitive for the user to key in the quantity they want to increase/decrease by.
    * Cons:
        * Parameter to increase/decrease quantity must be checked that it is a positive integer.
        * Users will have to use separate commands to increase and decrease quantity.

Another aspect that was considered when implementing this feature was having a default quantity to increase and decrease
if no quantity was specified. We realised it was intuitive for users to make the default increment or decrement set to
1, easing convenience for the user.

**Implementation**
<div> Note: The implementation for `inc` and `dec` are the same, except the variable names and logic used to calculate 
new quantity (Addition/Subtraction) The described implementation is for the `inc` command. </div>

The first stage of the implementation is parsing the user input to `IncreaseCommand`. `IncreaseCommandParser` is used
to parse and check whether the user input is valid. After which a `IncreaseCommand` object is created along with a
`IncreaseFoodDescriptor` instance to modify the current food item.

The second stage requires IncreaseCommand#execute() to be called.

**Usage Scenario**

1. The user specifies an index of the food item to be edited.
2. If the index is out of bounds from the food list, an error response is returned and users will be prompted to key in
   the command with the valid index.
3. If no specific quantity is specified, the quantity of the indexed food item will be increased by one.
4. If a specific quantity is specified, the quantity of the indexed food item will be increased by that value.
5. If the specific quantity is lesser than or equal to 0, an error response is returned and users will be prompted to
   key in the command with a valid quantity.


The following activity diagram shows the usage of the `inc` command.

**insert activity diagram

#### \[Implementing\] View details of a food item.

**Overview**
The view feature is meant to be a shorthand for users to view the details of a particular food item.
Traditionally, to view the details of an item, the user would use the list command to view the details of a food item.
The user can now specify `view` to view more details of the indexed food item.

The following UML diagram shows `view` and its associated class.

**Design considerations**

* **Alternative 1 (Current implementation):** The command view with the index of the food item to view the details of.
    * Pros:
        * Intuitive for the user to key in the quantity they want to increase/decrease by.
    * Cons:
        * Parameter to increase/decrease quantity must be checked that it is a positive integer.
        * Feels like a hacky solution to the problem.
        * Does not significantly value-add to the product as compared to just using the edit command.

* **Alternative 2 :** Have a ui switcher to switch between the different views i.e. list view and details view.
  * Pros:
    * Code should be easier to maintain, or add new views in the future.
  * Cons:
    * Difficulty in implementing the switcher.
    * Difficulty in implementing the different views.

**Implementation**
The first stage of the implementation is checking that the command is `view` and that the index is valid. `ViewCommandParser` is used
to parse and check whether the user input is valid. After which a `ViewCommand` object is created which will be used to switch the
view from the list view to the details view.

**Usage Scenario**

1. The user specifies an index of the food item to be viewed.
2. If the index is out of bounds from the food list, an error response is returned and users will be prompted to key in
   the command with the valid index.
3. If no specific quantity is specified, an error response is returned and users will be prompted to key in
   the command with the valid index.
4. If a specific quantity is specified, the item will be displayed in the view.
5. If the specific quantity is lesser than or equal to 0, an error response is returned and users will be prompted to
   key in the command with a valid index.

The following activity diagram shows the usage of the `view` command.

**insert activity diagram


#### \[Implemented\] List Food by tag.

**Overview**
The List by tag feature is meant to be a list all the food by the specified tags.

The following UML diagram shows `Tag` and its associated class.

*** insert uml

**Design considerations**

* **Alternative 1:** The command parameter will be the tag name of the food to display
    * Pros:
        * Easily implemented. The command parameter will show the food with the specified tag name
    * Cons:
        * May not be convenient for the user, as the command allows only 1 tag name at a time.

* **Alternative 2 (Current implementation):** The command parameter will be the tag name of the food to display.
    * Pros:
        * Convenient for the user to key in multiple tag names to display.
    * Cons:
        * Parameter for multiple tag names must be checked that it is not empty
        * Users will have to use separate commands by `n/` which maybe a hassle

_{more aspects and alternatives to be added}_

**Implementation**
The first stage of the implementation is parsing the user input to `ListByTagCommand`. `ListByTagCommandParser` is used
to parse and check whether the user input is valid. After which a `ListByTagCommand` object is created with the specified
tag name.

The second stage requires ListByTagCommand#execute() to be called.

**Usage Scenario**

1. The user specifies tags of the food item to be displayed.
2. If the tag does not exist in `UniqueTagList`, an error response is returned and users will be prompted to key in the command with the valid tag name.
3. If a valid tag is specified, the list indexed food item with the specified tags will be displayed.
4. If the specific quantity is lesser than or equal to 0, an error response is returned and users will be prompted to
   key in the command with a valid quantity.

The following activity diagram shows the usage of the `listbytag` command.

**insert activity diagram

### \[Implemented\] Tag-related Features

#### Overview
The tagging functionality is facilitated by the `UniqueTagList` stored in `WIFE`. Creating and deleting tags will
modify the tags within the `UniqueTagList` which contains all existing `Tag` objects. Additionally, every food item
within WIFE has its own collection of associated `Tag` objects stored in an internal Set<Tag>. Tagging/untagging a `Tag` to a `Food` will
add/remove the corresponding `Tag` object to/from the `Set<Tag>` stored within `Food` This allows for efficient tagging 
and organization of items across multiple lists.

The following UML diagram shows `Tag` and its associated class.

<img src="images/BetterModelClassDiagram.png" width="574" />

#### Design considerations:

**Aspect: How to store the tags for WIFE and each food item. **

* **Alternative 1 (current choice):** Store `Tag` in `UniqueTagList` and each `Food` stores its own set of associated
    `Tag` objects.
    * Pros: 
      * Easy to implement.
      * Users are able to create and reuse the same `Tag` that may be extensible for editing to suit their own use
        case instead of providing a preset list of tags that cannot be extended for other food items in the list.
    * Cons: 
      * May have performance issues in terms of memory usage as additional storage is used.

* **Alternative 2:** Instantiates `Tag` with a specified name and stores all food classified by the tag in the 
    instantiated class.
    * Pros:
        * Saves space. There is no need to store the set of associated `Tag`, `Set<Tag>` in `Food`. The association
          of `Food` to `Tag` is represented by `List<Food>` in `Tag` object.
        * Easily extensible. Creating a new `Tag` can be done by simply instantiating a new `Tag` object.
    * Cons:
        * May be more complicated in terms of implementation as compared to alternative 1.
        * Approach is not as intuitive as compared to alternative 1 (simpler to add each `Tag` to the `Food`)
      
_{more aspects and alternatives to be added}_

#### Creating a new Tag

User can create a new pre-defined `Tag` in WIFE with the `createtag` command. The `createtag` command creates a new tag 
in WIFE which can be used to create a new tag that can be used to classify food items in food lists. Once the tag is 
created using this command, the tag can be applied to food items using the `tag` command.

**Implementation**

The first stage of the implementation is parsing the user input to `CreateTagCommand`. `CreateTagCommandParser` is used 
to parse and check whether the user input is valid. After which a `CreateTagCommand` object is created with the specified
tag name. 
The second stage requires CreateTagCommand#execute() to be called.

**Usage Scenario**

1. The user specifies a tag name for the new tag when creating a new tag.
2. If the tag name is empty, an error response is returned and users will be prompted to key in the command with the valid
   tag name.
3. If the tag name is invalid, an error response is returned and users will be prompted to key in the command with a valid
   tag name.
4. If the tag name inserted by the user has the same tag name as another tag in the `Model`, an error is returned to
   inform the user that there is already a duplicated copy of tag in the `UniqueTagList`.
5. If the tag storage of WIFE is full, an error response is returned to inform that the user has reached the maximum
   capacity of the tag storage and will not be able to insert additional tag.
6. Completion of step 5 without any exceptions will result in successful creation of a new `Tag` in WIFE and stored in
   `UniqueTagList`

The following sequence diagram shows how the `createtag` command.

<img src="images/CreateTagSequenceDiagram.png" width="700" />

#### Deleting a Tag

The `deltag` command deletes existing tag(s) in WIFE. This means food that are initially tagged with the specified tag(s) will have that tag removed.

**Implementation**

The first stage of the implementation is parsing the user input to `DeleteTagCommand`. `DeleteTagCommandParser` is used to parse and check whether the user input is valid. After which a `DeleteTagCommand` object is created with the specified tag name. The second stage requires DeleteTagCommand#execute() to be called.

**Usage Scenario**

1. The user specifies tag name(s) for the tag(s) to be deleted.
2. If the tag name is empty, an error response is returned and users will be prompted to key in the command with the valid tag name.
3. If all tag name(s) are invalid, an error response is returned and users will be prompted to key in the command with a valid tag name.
4. If some tag name(s) are invalid, invalid tag names will be ignored and valid tag names will be processed.
5. Completion of step 4 without any exceptions will result in successful deletion of specified `Tag` in WIFE and `Food` with specified tag(s) will have that tag(s) removed

The following sequence diagram shows how the `deltag` command.

**insert sequence diagram

#### Tagging a Food

User may choose to tag a `Food` in `WIFE` with any of the pre-defined `Tag` created. This can simply
be done using the command `tag`. This tagging function allows user to easily classify their `Food` in
the fridge.

**Implementation**

The first stage of the implementation is parsing the user input to `TagFoodCommand`. `TagFoodCommandParser` is used
to parse and check whether the user input is valid. After which a `TagFoodCommand` object is created with the specified
index and tag name.
The second stage requires TagFoodCommand#execute() to be called.

**Usage Scenario**

1. The user specifies the index of the food to be tagged and the name of the tag.
2. If the index is out of bounds from the food list, an error response is returned and users will be prompted to key in
   the command with the valid index.
3. If no specific tag name is specified, an error response is returned and users will be prompted to key in
   the command with a tag name.
4. If the tag name specified does not exist in WIFE, an error response is returned and users will need to create the tag
   before having access to the tag.
5. Completion of step 4 without any exception will result in successful tagging of the food with the
   specified tag.

#### Untagging a Food

The `untag` feature removes a specified tag from a food item.

**Implementation**

The first stage of the implementation is parsing the user input to `UntagCommand`. `UntagCommandParser` is used to parse and check whether the user input is valid - if the tag exists or does not exist in the food item's internal tag set. After which, an `UntagCommand` object is created with the specified tag name to be removed. The second stage requires UntagCommand#execute() to be called which then removes the tag from the food item.

**Usage Scenario**

1. The user specifies tag name to be removed for a food item index.
2. If the food item does not have that tag, an error response is returned and users will be prompted to key in the command with a valid tag name.
3. Completion of step 1 without any exceptions will result in successful removal of the specified `Tag` from the `Food` item.

The sequence diagram of `untag` is similar to that of the `tag` command.

### \[Implemented\] Dynamic Help

![HelpCommandActivityDiagram.png](images%2FHelpCommandActivityDiagram.png)

The dynamic help mechanism allows the user to receive in-app help for the specific command being queried i.e. `help add`. It extends the traditional help functionality where the user only received general help. The help commands and respective outputs are stored internally as enums in `HelpMenu.java`. Additionally, `HelpMenu.java` implements the following operations:

HelpMenu#getGeneralHelp() — Retrieves a general help message if the user inputs `help`.
HelpMenu#getCommandHelp() — Retrieves the command specific help message.
HelpMenu#parseCommand() — Parses the command input in `help COMMAND` to ensure it is a valid command.

These operations are invoked in `HelpCommandParser.java` which calls HelpMenu#getGeneralHelp() or HelpMenu#getCommandHelp() depending on the help command input after parsing the input with HelpMenu#parseCommand().

#### Feature Details:

<div/>

Step 1. After successful retrieval of the help message, the message is passed to the `HelpCommand` object returned by `HelpCommandParser`.

Step 2. The `LogicManager` executes the `HelpCommand` object which generates a `Command Result` object with the help message.

Step 3. MainWindow#executeCommand() extracts the help message from the `CommandResult` and sends it to `HelpWindow` as the text to be set in the FXML `label`.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Fridge owner who cares about food waste and wish to track their fridge inventory.

**Value proposition**:
* The perfect solution to make sure you’re always one step ahead when managing your inventory, saving you time and money while reducing food waste.


### User stories *to be edited*

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                            | I want to …​                                          | So that I can…​                             |
|----------|------------------------------------|-------------------------------------------------------|---------------------------------------------|
| `* * *`  | user                               | add food items to my list                             | track what is in my fridge                  |
| `* * *`  | meal planner                       | set expiration dates for items in my fridge           | plan meals around items that are expiring   |
| `* * *`  | chef                               | set item categories for fridge items                  | easily find what I need when cooking.       |
| `* * *`  | user                               | delete ingredients                                    | know what ingredients I have thrown away    |
| `* *`    | user                               | layout items                                          | see where all my food items are             |
| `* *`    | fridge owner                       | transfer my list to another system                    | use the same system with another fridge     |
| `* *`    | user                               | search for specific food items                        | see whether I still have it in my fridge    |
| `* *`    | user                               | leave comments on items                               | remember any particular remarks             |
| `* *`    | user                               | attach images to items                                | remember what they look like                |
| `* *`    | user who manages finances          | know how much is spent every month                    | track my monthly finances                   |
| `* *`    | user who likes to try new products | rate the items                                        | know whether to buy the same item next time |
| `* *`    | forgetful user                     | see past purchases                                    | remember what I have bought in the past     |
| `* *`    | user                               | set reminders for items                               | remember to use them before they expire     |
| `*`      | student                            | see recipes with the food in my fridge as ingredients | decide what meals I can make                |
| `*`      | housewife                          | generate a grocery list                               | bring it to the supermarket                 |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `Well Informed Fridge Environment (WIFE)` and the **Actor** is the `user`, unless specified otherwise)

### **Use case UC01: Add a new food item**
**MSS**
1. User add item by giving WIFE the name of the item.
2. User confirms.
3. WIFE adds the item to the list and displays all current items in the fridge.
   Use case ends.

**Extensions**
* 2a. WIFE detects an error in the entered data.
  * 2a1. WIFE requests for the correct data.
  * 2a2. User enters new data.
  * Steps 2a1-2a2 are repeated until the data entered are correct. <br/>
  Use case resumes from step 3.

* *a. At any time, User chooses to cancel the transfer.
  * *a1. OBS requests to confirm the cancellation.
  * *a2. User confirms the cancellation. <br/>
  Use case ends.


### **Use case UC02: View all food items**
**MSS**
1. User ask to view all food items in WIFE.
2. WIFE displays all food items that are in the fridge. <br/>
   Use case ends.

**Extensions:**
* 1a. WIFE is empty.
  * 1a1. WIFE displays a message that tells the User that there are no items. <br/> 
  Use case ends.

### **Use case UC03: Delete a food item**
**MSS**
1. User requests to delete specified food items in WIFE.
2. WIFE deletes food item and displays successful deletion message. <br/>
   Use case ends.

**Extensions:**
* 1a. WIFE is empty.
  * 1a1. WIFE displays a message that tells the User that there are no items and cannot delete specified item. <br/>
    Use case ends. 
* 1b. User selects an item that does not exist.
  * 1b1. WIFE displays a message that tells the User that specified item does not exist. <br/>
    Use case ends.

### **Use case UC04: View help**
**MSS:**
1. User asks the WIFE for the help page
2. WIFE displays all available commands and corresponding formats <br/>
   Use case ends.


### **Use case UC05: Tag a food item**

**MSS**

1.  User requests to list the food items stored in WIFE.
2.  WIFE shows the full list of food items.
3.  User tag food item at specified index with tags pre-defined by WIFE.
4.  WIFE tagged the food item with the chosen tag.

    Use case ends.

**Extensions**

* 2a. The list is empty. WIFE informs the user that the list is empty.</br>
  Use case ends.

* 3a. The given index is invalid.
    * 3a1. WIFE displays an error message to inform the user that the index
    * inserted is invalid. </br>
      Use case resumes at step 2.

* 4a. Chosen tag is not in the pre-defined list of tags.
  * 4a1. WIFE shows an error message.
  * 4a2. WIFE asks if the user wish to add the new tags into the tag list. <br/>
    Use case resumes at step 2.

### **Use case UC06: Edit a food item**

**MSS**

1.  User requests to update an item in the fridge.
2.  WIFE updates the item and displays successful update message.

    Use case ends.

**Extensions**

* 1a. WIFE is empty.
  * 1a1. WIFE displays a message that tells the User that there are no items
  and cannot update specified item
  
    Use case ends.
* 1b. User selects an item that does not exist.
  * 1b1. WIFE displays a message that tells the User that specified item does not exist.
  
    Use case ends.

### **Use case UC07: Add a Pre-defined tag**

**MSS**

1. User adds a new tag by giving WIFE the name of the tag.
2. User confirms.
3. WIFE adds the tag to the list and displays all current tags in the fridge.
   Use case ends.

**Extensions**

* 1a. User keyed in an invalid tag name.
    * 1a1. WIFE displays a message that tells the User that there the tag name keyed in by
    * the user is invalid.

      Use case ends.
  
* 1b. Tag already exists in WIFE's list of pre-defined tags.
    * 1a1. WIFE displays a message that tells the User that there are already similar tags
    * in the list, hence, no action will be carried out.

      Use case ends.

### **Use case UC08: Increase the quantity of a food item**

**MSS**

1. User selects a food item to increase its quantity in WIFE.
2. WIFE informs the user that the increase has been completed.
   Use case ends.

**Extensions**

* 1a. User selects an invalid food item.
    * 1a1. WIFE displays a message that tells the User that the selected food item is invalid.
      Use case ends.

* 1b. The user does not specify the quantity of the food item to increase by
    * 1b1. WIFE increases the quantity of the specified food item by 1.
      Use case resumes from Step 2.
* 1c. The user specifies a value that is lesser than or equal to 0 for the quantity of the food item to increase by.
    * 1b1. WIFE displays a message that tells the User that the specified value is invalid.
      Use case ends.

### **Use case UC09: Decrease the quantity of a food item**
The same as Use Case UC08: Increment the quantity of a food item, except that it is to decrease the quantity of a food 
item.

### **Use case UC10: List food items by tags**

**MSS**

1. User requests to view food items with specified tags.
2. WIFE displays all the food items with the specified tags.
   Use case ends.

**Extensions**

* 1a. User requests an invalid tag.
    * 1a1. WIFE displays a message that tells the User that there are no food item tagged with the specified tag.
      Use case ends.

* 1b. The user specify valid and invalid tags togther.
    * 1b1. WIFE displays food items with valid tags. It also tells User which tag is valid or invalid.
      Use case ends.

### **Use case UC11: Delete food items by tags**

**MSS**

1. User requests to delete food items with specified tags.
2. WIFE deletes and displays all the food items with the specified tags.
   Use case ends.

**Extensions**

* 1a. User requests an invalid tag.
    * 1a1. WIFE displays a message that tells the User that there are no food item tagged with the specified tag to be deleted.
      Use case ends.

* 1b. The user specify valid and invalid tags togther.
    * 1b1. WIFE deletes and displays food items with valid tags.
      Use case ends.

### **Use case UC12: Delete tags**

**MSS**

1. User requests to delete pre-defined tags in WIFE.
2. WIFE deletes pre-defined tags in WIFE and untag itself from the food items. It then tells the users the tags that are deleted
   Use case ends.

**Extensions**

* 1a. User requests an invalid tag.
    * 1a1. WIFE displays a message that tells the User that the tag specified is does not exit in WIFE
      Use case ends.

* 1b. The user specify valid and invalid tags togther.
    * 1b1. WIFE ignores invalid tags.
      Use case resumes at step 2.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Application’s dimension should be a minimum of 450 by 600 pixels.
5.  Should support a minimum of 50 items in storage.
6.  There should be no more than 1 second of lag when a user inputs a command.
7.  A first-time user should be able to easily perform CRUD operations on items.
*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X <br/>
* **WIFE** - Well Informed Fridge Environment <br/>
*{more to be added}*

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person *[for reference]*

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_

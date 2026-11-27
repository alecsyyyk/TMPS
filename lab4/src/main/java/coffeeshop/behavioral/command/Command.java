package coffeeshop.behavioral.command;

// Command - Command interface.
// A user action (add a product, remove a product, pay) is turned into an
// object. Because it is an object, it can be saved in a history and undone.
public interface Command {

    // Does the action.
    void execute();

    // Reverts what execute() did.
    void undo();

    // A short text for the console, for example "Add Latte".
    String getDescription();
}

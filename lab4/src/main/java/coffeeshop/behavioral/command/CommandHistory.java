package coffeeshop.behavioral.command;

import java.util.ArrayList;
import java.util.List;

// Command - Invoker.
// Runs commands and keeps the ones that worked in a list (used as a stack),
// so the last one can be undone. It only knows the Command interface.
public class CommandHistory {
    private final List<Command> done = new ArrayList<>();

    // Runs a command. If execute() throws an exception (for example the state
    // does not allow the action), the command is NOT added to the history.
    public void run(Command command) {
        command.execute();
        done.add(command);
        System.out.println("Done: " + command.getDescription());
    }

    // Undoes the last command. Returns false if there is nothing to undo.
    // The command is removed from the history only if undo() worked.
    public boolean undoLast() {
        if (done.isEmpty()) {
            return false;
        }
        int lastIndex = done.size() - 1;
        Command last = done.get(lastIndex);
        last.undo();
        done.remove(lastIndex);
        System.out.println("Undone: " + last.getDescription());
        return true;
    }

    // How many commands can still be undone.
    public int size() {
        return done.size();
    }

    // Texts of all commands in the history, oldest first.
    public List<String> getDescriptions() {
        List<String> result = new ArrayList<>();
        for (Command command : done) {
            result.add(command.getDescription());
        }
        return result;
    }
}

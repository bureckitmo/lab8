package commands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

public class ClearCommand extends AbstractCommand {

    public ClearCommand(){
        cmdName = "clear";
        description = "очищает коллекцию";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {

        String retDelAll = databaseController.clearMovie(credentials);
        if (retDelAll == null) {
            collectionManager.clear();
            consoleManager.writeln("All elements deleted");
            return true;
        }else{
            consoleManager.writeln("Problem: " + retDelAll);
            return false;
        }
    }
}

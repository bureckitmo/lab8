package commands;

import database.Credentials;
import database.DatabaseController;
import exceptions.InvalidValueException;
import managers.CollectionManager;
import managers.ConsoleManager;

public class RemoveIdCommand extends AbstractCommand {

    public RemoveIdCommand(){
        cmdName = "remove";
        description = "удаляет элемент из коллекции по его id";
        argCount = 1;
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            throw new InvalidValueException(e.getMessage());
        }

        String cityID = databaseController.removeMovie(id, credentials);
        if (cityID == null) {
            if(collectionManager.remove(id)){ consoleManager.writeln("Element with id(" + id + ") - successfully deleted"); return true; }
            else{ consoleManager.writeln("Element with id(" + id + ") - doesn't exist"); return false; }
        } else {
            consoleManager.writeln("Have some problems: " + cityID);
            return false;
        }
    }
}

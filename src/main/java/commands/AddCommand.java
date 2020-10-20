package commands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;
import models.Movie;

public class AddCommand extends AbstractCommand {

    public AddCommand(){
        cmdName = "add";
        description = "добавляет новый элемент в коллекцию";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getMovie();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        String cityID = databaseController.addMovie((Movie) inputData, credentials);
        if (isNumeric(cityID)) {
            ((Movie) inputData).setId(Integer.valueOf(cityID));
            ((Movie) inputData).setUser_id(credentials.id);
            ((Movie) inputData).setUsername(credentials.username);
            collectionManager.addElement((Movie) inputData);
            consoleManager.writeln("New movie added");
        }

        inputData = null;

        return true;
    }
}

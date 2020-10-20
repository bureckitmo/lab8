package commands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

public class PrintFieldDescendingMpaaCommand extends AbstractCommand {

    public PrintFieldDescendingMpaaCommand(){
        cmdName = "print_field_descending_mpaa_rating";
        description = "выводит значения поля Mpaa_Rating всех элементов в порядке убывания";
    }


    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        collectionManager.sortByMpaaDes().forEach(x->consoleManager.writeln(x.toString()));

        return true;
    }
}

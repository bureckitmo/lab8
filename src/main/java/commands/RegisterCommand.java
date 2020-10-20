package commands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

public class RegisterCommand extends AbstractCommand {

    public RegisterCommand(){
        cmdName = "register";
        description = "регистрация пользователя в систему, для управления им его данными";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getCredentials();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        Object _credentials = databaseController.register((Credentials) this.inputData);
        inputData = null;
        if(_credentials instanceof Credentials){
            return _credentials;
        }else {
            consoleManager.writeln((String) _credentials);
            return false;
        }
    }
}

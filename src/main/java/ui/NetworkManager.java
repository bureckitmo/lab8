package ui;

import commands.*;
import database.Credentials;
import database.CurrentUser;

import lombok.extern.slf4j.Slf4j;
import managers.*;
import models.Movie;
import network.*;
import exceptions.*;
import ui.listener.EventListener;
import ui.listener.LoginListener;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.NoSuchElementException;

@Slf4j
public class NetworkManager implements Runnable {

    public LoginListener loginEvent;
    public LoginListener registerEvent;
    public EventListener removeEvent;
    public EventListener infoEvent;
    public EventListener addEvent;
    public EventListener showEvent;
    public EventListener updateEvent;
    public EventListener clearEvent;
    public EventListener ascendingTimezoneEvent;
    public EventListener countLessThanGovernorEvent;


    private static NetworkManager instance;
    private InetSocketAddress address;
    private ClientUdpChannel channel = null;
    private ConsoleManager consoleManager;
    private CommandReader commandReader;
    private CurrentUser currentUser = new CurrentUser(new Credentials(-1, "default", ""));

    public NetworkManager(InetSocketAddress address){
        this.address = address;
        instance = this;
    }

    public static NetworkManager getInstance() {
        return instance;
    }

    @Override
    public void run() {
        try {
            channel = new ClientUdpChannel();
        } catch (IOException ex) {
            log.error("Unable to connect to the server", ex);
            System.exit(-1);
        }

        consoleManager = new ConsoleManager(new InputStreamReader(System.in), new OutputStreamWriter(System.out), true);
        CommandManager commandsManager = new CommandManager();
        commandReader = new CommandReader(channel, commandsManager, consoleManager);
        ClientHandler clientHandler = new ClientHandler(channel, currentUser);


        while (true){
            try {
                if (!channel.isConnected())
                    channel.tryToConnect(address);

                clientHandler.checkForResponse();
                final long start = System.currentTimeMillis();
                while (channel.requestWasSent()) {
                    if (channel.requestWasSent() && System.currentTimeMillis() - start > 1000) {
                        System.out.println("Seems the server went down!");
                        channel.setConnectionToFalse();
                        break;
                    }
                }

            } catch (NoCommandException | AuthException ex) {
                System.out.println(ex.getMessage());
            } catch (NoSuchElementException ex) {
                commandReader.finishClient();
                clientHandler.finishReceiver();
            }catch (ArrayIndexOutOfBoundsException ex) {
                System.err.println("No argument passed");
            } catch (ClassNotFoundException e) {
                System.err.println("I/O Problems, check logs");
                log.error("I/O Problems", e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void logout(){
        currentUser.setCredentials(new Credentials(-1, "default", ""));
    }


    public void send(AbstractCommand cmd){
        new Thread(() -> CommandReader.getInstance().sendCommand(cmd, consoleManager, currentUser.getCredentials())).start();
        //CommandReader.getInstance().sendCommand(cmd, consoleManager, currentUser.getCredentials());
    }

    public void login(String username, String password, LoginListener event){
        this.loginEvent = event;

        LoginCommand lc = new LoginCommand();
        lc.setInputData(new Credentials(-1, username, password));
        this.send(lc);
    }

    public void register(String username, String password, LoginListener event){
        this.registerEvent = event;

        RegisterCommand lc = new RegisterCommand();
        lc.setInputData(new Credentials(-1, username, password));
        this.send(lc);
    }

    public void show(EventListener event){
        this.showEvent = event;
        this.send(new ShowCommand());
    }

    public void remove(Integer id, EventListener event){
        this.removeEvent = event;

        RemoveIdCommand ric = new RemoveIdCommand();
        ric.setArgs(new String[]{id.toString()});
        this.send(ric);
    }

    public void update(Integer id, Movie movie, EventListener event){
        this.updateEvent = event;

        UpdateIdCommand uic = new UpdateIdCommand();
        uic.setArgs(new String[]{id.toString()});
        uic.setInputData(movie);
        this.send(uic);
    }

    public void add(Movie movie, EventListener event){
        this.addEvent = event;

        AddCommand ac = new AddCommand();
        ac.setInputData(movie);
        this.send(ac);
    }

    public void clear(EventListener event){
        this.clearEvent = event;

        ClearCommand cc = new ClearCommand();
        this.send(cc);
    }

    public void info(EventListener event){
        this.infoEvent = event;

        InfoCommand ic = new InfoCommand();
        this.send(ic);
    }



    public void finishClient(){
        commandReader.finishClient();
    }

}

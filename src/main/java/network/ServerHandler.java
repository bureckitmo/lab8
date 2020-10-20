package network;

import commands.*;
import database.CollectionDBManager;
import database.Credentials;
import database.DatabaseController;
import exceptions.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import managers.CollectionManager;
import managers.ConsoleManager;
import network.packets.CommandExecutionPacket;
import network.packets.CommandPacket;
import utils.AppConstant;

import java.io.*;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ServerHandler {

    private class RequestReceiver extends Thread {

        @Override
        public void run() {
            while (true) {
                receiveData();
            }
        }

        /**
         * Функция для получения данных
         */
        public void receiveData() {
            SocketAddress addressFromClient = null;
            try {
                final ByteBuffer buf = ByteBuffer.allocate(AppConstant.MESSAGE_BUFFER);
                addressFromClient = socket.receiveDatagram(buf);
                buf.flip();
                final byte[] petitionBytes = new byte[buf.remaining()];
                buf.get(petitionBytes);

                if (petitionBytes.length > 0)
                    processRequest(petitionBytes, addressFromClient);

            } catch (SocketTimeoutException ignored) {
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Weird errors, check log");
                log.error("Weird errors processing the received data", e);
                executeObj("Weird errors, check log. " + e.getMessage(), addressFromClient);
            }
        }

        /**
         * Функция для десериализации данных
         * @param petitionBytes - полученные данные
         */
        private void processRequest(byte[] petitionBytes, SocketAddress addressFromClient) throws IOException, ClassNotFoundException {
            try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(petitionBytes))) {
                final Object obj = stream.readObject();

                log.trace("received object: {}", obj);
                if(obj instanceof CommandPacket)
                    log.info("[{}] - {}:({})", ((CommandPacket) obj).getCredentials().username, ((CommandPacket) obj).getCommand().toString(), ((CommandPacket) obj).getCommand().getArgs());


                if (obj == null)
                    throw new ClassNotFoundException();
                executeObj(obj, addressFromClient);
            }
        }
    }

    private final Set<SocketAddress> clientList = new HashSet<>();
    private final ServerSocket socket;
    private final RequestReceiver requestReceiver;
    private final ForkJoinPool executor;
    private final ConsoleManager consoleManager;
    private final CollectionManager collectionManager;
    private final DatabaseController databaseController;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public ServerHandler(ServerSocket socket, CollectionManager collectionManager, DatabaseController databaseController) {
        this.socket = socket;
        this.consoleManager = new ConsoleManager(new InputStreamReader(System.in), new OutputStreamWriter(outputStream), false);
        this.collectionManager = collectionManager;
        this.databaseController = databaseController;

        requestReceiver = new RequestReceiver();
        requestReceiver.setName("ServerReceiverThread");
        executor = new ForkJoinPool();
    }

    public void receiveFromWherever() {
        requestReceiver.start();
    }

    private void executeObj(Object obj, SocketAddress addressFromClient) {
        Future<Object> resulted = executor.submit(() -> {
            Object responseExecution;
            clientList.add(addressFromClient);
            if (obj instanceof String)
                responseExecution = obj;
            else {
                AbstractCommand command = ((CommandPacket) obj).getCommand();
                Credentials credentials = ((CommandPacket) obj).getCredentials();
                String messageType = command.getClass().getSimpleName();
                log.trace("[ {}, {}, {} ]", command.toString(), credentials.toString(), messageType);
                try {
                    outputStream.reset();
                    Object retObj = command.execute(consoleManager, collectionManager, databaseController, credentials);
                    if(retObj instanceof Credentials || command instanceof ShowCommand){
                        responseExecution = new CommandExecutionPacket(messageType, retObj, true);
                    }else if(retObj instanceof Boolean){
                        responseExecution = new CommandExecutionPacket(messageType, new String(outputStream.toByteArray()), (Boolean) retObj);
                        if(command instanceof ClearCommand ||command instanceof RemoveIdCommand || command instanceof UpdateIdCommand || command instanceof AddCommand)
                            new Thread(() -> { clientList.forEach(address -> executeObj(new CommandPacket(new ShowCommand(), credentials), address)); }).start();
                    }else {
                        responseExecution = new CommandExecutionPacket(messageType, new String(outputStream.toByteArray()), true);
                    }
                } catch (InvalidValueException ex) {
                    responseExecution = ex.getMessage();
                    log.error(ex.getMessage());
                }
            }
            socket.sendResponse(responseExecution, addressFromClient);
            return responseExecution;
        });

        try {
            log.trace("Object gotten from executor: {} \n", resulted.get());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error getting result from executor", e);
        }
    }


    public void disconnect() {
        log.info("Disconnecting the server...");
        System.out.println("Disconnecting the server...");
        try {
            executor.shutdown();
            executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("Interrupted executor during shutdown",e);
            System.out.println("Interrupted during finishing the queued tasks");
        }
        socket.getSocket().disconnect();
        requestReceiver.interrupt();
    }
}
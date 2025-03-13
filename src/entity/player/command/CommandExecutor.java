package entity.player.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class CommandExecutor {
    private final InputStream in;
    private final OutputStream out;
    private final CommandProvider provider;
    private final Scanner scanner;

    public CommandExecutor(InputStream inputStream, OutputStream outputStream, CommandProvider provider) {
        this.in = inputStream;
        this.out = outputStream;
        this.provider = provider;

        scanner = new Scanner(in);
        listenCommands();
    }

    protected void listenCommands() {
        CompletableFuture.runAsync(() -> {
           while (!Thread.currentThread().isInterrupted()) {
               String[] command = scanner.nextLine().split(" ");
               if (command[0].startsWith("/")) {

                   String[] arguments = Arrays.copyOfRange(command, 1, command.length);

                   provider.getCommands()
                           .stream()
                           .filter(cmd -> {
                               if (cmd.getName().equals(command[0].substring(1))) {
                                   return true;
                               } else {
                                   for (String s : cmd.getAliases()) {
                                       if (s.equals(command[0].substring(1))) {
                                           return true;
                                       }
                                   }
                               }
                               return false;
                           })
                           .findFirst()
                           .ifPresentOrElse(cmd -> cmd.execute(this, arguments),
                                   () -> printMessage("Command " + Arrays.toString(command) + " not found."));
               }
           }
        });
    }

    public final void printMessage(Command command, String message) {
        printMessage(String.format("[%s]: /%s: %s", command.getClass().getSimpleName(), command.getName(), message));
    }

    private void printMessage(String message) {
        message = "[Command Executor] " + message + "\n";
        try {
            out.write(message.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }

    public CommandProvider getProvider() {
        return provider;
    }
}

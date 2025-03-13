package entity.player.command;

import java.util.List;

public interface CommandProvider {

    Command register(Command command);

    void unregister(Command command);

    List<Command> getCommands();
}

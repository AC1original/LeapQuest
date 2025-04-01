package entity.player.command;

import entity.player.command.commands.HitBoxCommand;
import entity.player.command.commands.TeleportCommand;
import utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class CommandProviderImpl implements CommandProvider {
    private final List<Command> commands = new ArrayList<>();

    public final Command TELEPORT_COMMAND = register(new TeleportCommand());
    public final Command HITBOX_COMMAND = register(new HitBoxCommand());

    @Override
    public Command register(Command command) {
        commands.add(command);
        Logger.info(this, "Registered Command: " + command.getName());
        return command;
    }

    @Override
    public void unregister(Command command) {
        commands.remove(command);
        Logger.info(this, "Unregistered Command: " + command.getName());
    }

    @Override
    public List<Command> getCommands() {
        return commands;
    }
}

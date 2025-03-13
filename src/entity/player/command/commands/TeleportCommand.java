package entity.player.command.commands;

import entity.player.Player;
import entity.player.command.Command;
import entity.player.command.CommandExecutor;
import main.LeapQuest;

import java.util.Optional;

public class TeleportCommand implements Command {
    private final String[] aliases = {"tp"};
    private final String[] arguments = {"x", "y"};

    @Override
    public String getName() {
        return "teleport";
    }

    @Override
    public String[] getArguments() {
        return arguments;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public void execute(CommandExecutor executor, String[] args) {
        if (args.length != getArguments().length) {
            executor.printMessage(this, "Wrong arguments length!");
            return;
        }

        Player player = LeapQuest.instance.getEntityHelper().getPlayer();

        var optX = parseRelativeCoordinates(args[0], player.getX());
        var optY = parseRelativeCoordinates(args[1], player.getY());

        if (optX.isEmpty() || optY.isEmpty()) {
            executor.printMessage(this, "Arguments must be integers!");
            return;
        }

        player.teleport(optX.get(), optY.get());
    }

    private Optional<Integer> parseRelativeCoordinates(String arg, int replace) {
        int argInt = 0;
        if (arg.startsWith("~")) {
            argInt = replace;
            arg = arg.length() > 1 ? arg.replaceAll("~", "") : "0";
        }
        if (Command.isInteger(arg)) {
            argInt += Integer.parseInt(arg);
            return Optional.of(argInt);
        } else {
            return Optional.empty();
        }
    }
}

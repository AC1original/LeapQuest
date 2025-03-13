package entity.player.command;

public interface Command {
    String[] DEFAULT_EMPTY = new String[0];

    String getName();

    default String[] getArguments() {
        return DEFAULT_EMPTY;
    }

    default String[] getAliases() {
        return DEFAULT_EMPTY;
    }

    void execute(CommandExecutor executor, String[] args);

    static boolean isInteger(String... s) {
        for (String string : s) {
            if (!string.matches("-?\\d+")) {
                return false;
            }
        }
        return true;
    }
}

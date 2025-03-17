package entity.player.command.commands;

import entity.player.command.Command;
import entity.player.command.CommandExecutor;
import main.LeapQuest;

public class HitBoxCommand implements Command {
    private boolean hitBoxShown = false;
    private final String[] aliases = new String[]{"hb"};

    @Override
    public String getName() {
        return "hitbox";
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public void execute(CommandExecutor executor, String[] args) {
        hitBoxShown = !hitBoxShown;

        var lvlManager = LeapQuest.instance.getLevelManager();
        var entities = LeapQuest.instance.getEntityHelper();

        lvlManager.showHitBox(hitBoxShown);
        entities.getEntities().forEach(entity -> entity.showHitBox(hitBoxShown));

        executor.printMessage(this, "HitBox shown " + hitBoxShown);
    }
}

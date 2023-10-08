package ru.aniby.felmonbettermending;

import org.bukkit.entity.Player;

public class XPUtils {
    public static int getPlayerXP(Player player) {
        int experienceLevel = player.getLevel();
        float experienceProgress = player.getExp();
        int neededExpForNextLevel = player.getExpToLevel();
        return (int) (getExperienceForLevel(experienceLevel) + (experienceProgress * neededExpForNextLevel));
    }

    public static int getExperienceForLevel(int level) {
        if (level == 0) {
            return 0;
        }

        if (level > 0 && level < 16) {
            return (int) (Math.pow(level, 2) + 6 * level);
        } else if (level > 15 && level < 32) {
            return (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360);
        } else {
            return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
        }
    }
}

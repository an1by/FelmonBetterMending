package ru.aniby.felmonbettermending;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.plugin.java.JavaPlugin;

public final class FelmonBetterMending extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    public static int inWhatHand(PlayerInventory inventory, ItemStack item) {
        if (inventory.getItemInMainHand().isSimilar(item))
            return 1;
        else if (inventory.getItemInOffHand().isSimilar(item))
            return 2;
        return 0;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (player.isSneaking()
                && event.getAction() == Action.RIGHT_CLICK_AIR
                && item != null) {
            PlayerInventory playerInventory = player.getInventory();
            int hand = inWhatHand(playerInventory, item);
            ItemMeta meta = item.getItemMeta();
            if (hand != 0 && meta instanceof Damageable damageable && meta instanceof Repairable repairable) {
                if (damageable.hasDamage() && damageable.getEnchantLevel(Enchantment.MENDING) > 0) {
                    int damage = damageable.getDamage();

                    float ratio = repairable.getRepairCost();
                    if (ratio == 0)
                        ratio = 1.0f;
                    float playerXP = XPUtils.getPlayerXP(player);

                    int xpToRevoke = 0;

                    if (playerXP >= 30 && damage >= 20 * ratio)
                        xpToRevoke = 20;
                    else if (playerXP >= 2)
                        xpToRevoke = 2;

                    if (xpToRevoke > 0) {
                        damageable.setDamage(damage - (int) (xpToRevoke * ratio));
                        item.setItemMeta(damageable);

                        switch (hand) {
                            case 1 -> playerInventory.setItemInMainHand(item);
                            case 2 -> playerInventory.setItemInOffHand(item);
                        }

                        player.giveExp(xpToRevoke * -1);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}

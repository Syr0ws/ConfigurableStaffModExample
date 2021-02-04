package fr.syrows.configurablestaffmodexample.staffmod.items;

import fr.syrows.staffmodlib.events.items.ItemUseOnEntityEvent;
import fr.syrows.staffmodlib.staffmod.items.AbstractStaffModItem;
import fr.syrows.staffmodlib.util.Configurable;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class FreezeItem extends AbstractStaffModItem implements Configurable {

    private final Plugin plugin;
    private ItemStack item;
    private Listener listener;

    public FreezeItem(Player holder, Plugin plugin) {
        super(holder);
        this.plugin = plugin;
    }

    @Override
    public void onRegister() {
        // Registering our listener. Don't forget to store it into an instance variable
        // to allow to unregister it later.
        this.listener = new ItemListener();
        Bukkit.getPluginManager().registerEvents(this.listener, this.plugin);
    }

    @Override
    public void onUnregister() {
        // Unregistering the listener.
        HandlerList.unregisterAll(this.listener);
    }

    @Override
    public ItemStack getItemStack() {
        return this.item.clone();
    }

    @Override
    public void configure(ConfigurationSection parent) {

        // Retrieving the section of the item into a variable.
        ConfigurationSection section = parent.getConfigurationSection("freeze");

        this.item = section.getItemStack("item").clone();
        this.setSlot(section.getInt("slot"));
    }

    // This class will handle the event related with the item.
    private class ItemListener implements Listener {

        @EventHandler
        public void onItemUseOnEntity(ItemUseOnEntityEvent event) {

            Player player = event.getPlayer();

            // Necessary. Checking that the holder of the items and the player
            // associated with the event are the sames.
            if(!player.equals(getHolder())) return;

            // Necessary. Checking that the current item and the items associated
            // with the event are the sames.
            if(!event.getStaffModItem().equals(FreezeItem.this)) return;

            Entity entity = event.getEntity();

            if(!(entity instanceof Player)) return;

            Player target = (Player) entity;

            // Note that in this example, only messages are sent.
            // You should create by yourself the freeze logic.
            player.sendMessage(String.format("%s is now frozen.", target.getName()));
            target.sendMessage(String.format("You have been frozen by %s.", player.getName()));

            // Cancelling the interaction.
            event.setCancelled(true);
        }
    }
}

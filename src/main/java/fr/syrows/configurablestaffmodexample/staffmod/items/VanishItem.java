package fr.syrows.configurablestaffmodexample.staffmod.items;

import fr.syrows.staffmodlib.events.items.ItemUseEvent;
import fr.syrows.staffmodlib.events.items.ItemUseOnBlockEvent;
import fr.syrows.staffmodlib.events.items.ItemUseOnEntityEvent;
import fr.syrows.staffmodlib.events.items.StaffModItemEvent;
import fr.syrows.staffmodlib.staffmod.items.AbstractStaffModItem;
import fr.syrows.staffmodlib.util.Configurable;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class VanishItem extends AbstractStaffModItem implements Configurable {

    private final Plugin plugin;
    private ItemStack item;
    private Listener listener;
    private boolean vanished;

    public VanishItem(Player holder, Plugin plugin) {
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
        ConfigurationSection section = parent.getConfigurationSection("vanish");

        this.item = section.getItemStack("item").clone();
        this.setSlot(section.getInt("slot"));
    }

    private class ItemListener implements Listener {

        @EventHandler
        public void onItemUse(ItemUseEvent event) {
            this.handle(event);
        }

        @EventHandler
        public void onItemUseOnBlock(ItemUseOnBlockEvent event) {
            this.handle(event);
        }

        @EventHandler
        public void onItemUseOnEntity(ItemUseOnEntityEvent event) {
            this.handle(event);
        }

        // Using this method to not repeat code.
        private <T extends StaffModItemEvent & Cancellable> void handle(T event) {

            Player player = event.getPlayer();

            // Necessary. Checking that the holder of the items and the player
            // associated with the event are the sames.
            if(!player.equals(getHolder())) return;

            // Necessary. Checking that the current item and the items associated
            // with the event are the sames.
            if(!event.getStaffModItem().equals(VanishItem.this)) return;

            // Note that this example is not a real vanish because only
            // messages are sent. You should implement the vanish logic by yourself.
            if(!VanishItem.this.vanished) {

                player.sendMessage("You're now vanished.");
                VanishItem.this.vanished = true;

            } else {

                player.sendMessage("You're no longer vanished.");
                VanishItem.this.vanished = false;
            }
            // Cancelling the interaction.
            event.setCancelled(true);
        }
    }
}

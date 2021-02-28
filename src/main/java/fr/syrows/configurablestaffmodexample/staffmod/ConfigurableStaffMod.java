package fr.syrows.configurablestaffmodexample.staffmod;

import fr.syrows.configurablestaffmodexample.staffmod.items.FreezeItem;
import fr.syrows.configurablestaffmodexample.staffmod.items.InvseeItem;
import fr.syrows.configurablestaffmodexample.staffmod.items.VanishItem;
import fr.syrows.staffmodlib.bukkit.configuration.Configurable;
import fr.syrows.staffmodlib.bukkit.data.*;
import fr.syrows.staffmodlib.bukkit.items.BukkitStaffModItem;
import fr.syrows.staffmodlib.bukkit.staffmod.SimpleBukkitStaffMod;
import fr.syrows.staffmodlib.common.data.DataHandler;
import fr.syrows.staffmodlib.common.staffmod.StaffModManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class ConfigurableStaffMod extends SimpleBukkitStaffMod implements Configurable {

    // Plugin will be needed to register item listeners.
    private final Plugin plugin;

    private DataHandler<Player> handler;

    public ConfigurableStaffMod(StaffModManager<Player, ItemStack> manager, Plugin plugin) {
        super(manager);
        this.plugin = plugin;
    }

    @Override
    public void enable(Player holder) {

        // Registering items.
        this.registerItems(holder);

        // Saving player data.
        this.handler = this.createPlayerData();
        this.handler.save(holder);
        this.handler.clear(holder);

        super.enable(holder);
    }

    @Override
    public void disable(Player holder) {

        // Restoring player data.
        this.handler.clear(holder);
        this.handler.restore(holder);
        this.handler = null; // Avoiding reuse.

        super.disable(holder);
    }

    @Override
    public void configure(ConfigurationSection parent) {

        this.getItemsHeld().stream()
                .filter(item -> item instanceof Configurable)
                .map(item -> (Configurable) item)
                .forEach(configurable -> configurable.configure(parent));
    }

    private void registerItems(Player player) {

        // Registering our items. The method registerItem(StaffModItem item)
        // must be performed on each item you want to register because an item
        // may need an initialization.

        // Declaring items.
        BukkitStaffModItem vanishItem = new VanishItem(player, this.plugin);
        BukkitStaffModItem freezeItem = new FreezeItem(player, this.plugin);
        BukkitStaffModItem invseeItem = new InvseeItem(player, this.plugin);

        // Registering items.
        this.registerItem(vanishItem);
        this.registerItem(freezeItem);
        this.registerItem(invseeItem);

        // Retrieving config to configure items.
        FileConfiguration config = this.plugin.getConfig();

        // Configuring the staff mod.
        this.configure(config.getConfigurationSection("staffmod"));
    }

    private PlayerDataHandler createPlayerData() {

        // This method has for goal to create a PlayerData object
        // which will store player state before enabling the staff mod.

        List<DataHandler<Player>> data = Arrays.asList(
                new InventoryDataHandler(), // Storing inventory information.
                new PotionDataHandler(), // Storing potion effects information.
                new GameModeDataHandler(), // Storing current game mode.
                new HealthDataHandler(), // Storing health information.
                new FoodDataHandler() // Storing food information.
        );
        return new PlayerDataHandler(data);
    }
}

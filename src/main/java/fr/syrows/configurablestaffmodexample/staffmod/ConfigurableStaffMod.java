package fr.syrows.configurablestaffmodexample.staffmod;

import fr.syrows.configurablestaffmodexample.staffmod.items.FreezeItem;
import fr.syrows.configurablestaffmodexample.staffmod.items.InvseeItem;
import fr.syrows.configurablestaffmodexample.staffmod.items.VanishItem;
import fr.syrows.staffmodlib.bukkit.BukkitStaffModManager;
import fr.syrows.staffmodlib.bukkit.configuration.Configurable;
import fr.syrows.staffmodlib.bukkit.data.*;
import fr.syrows.staffmodlib.bukkit.items.BukkitStaffModItem;
import fr.syrows.staffmodlib.bukkit.staffmod.BukkitStaffMod;
import fr.syrows.staffmodlib.bukkit.staffmod.SimpleBukkitStaffMod;
import fr.syrows.staffmodlib.common.data.DataHandler;
import fr.syrows.staffmodlib.common.items.StaffModItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ConfigurableStaffMod extends SimpleBukkitStaffMod implements Configurable {

    // Plugin will be needed to register item listeners.
    private final Plugin plugin;

    private DataHandler<Player> handler;

    // By default, items are not stored even if you call the registerItem(StaffModItem item) method.
    // So, you have to create this list in this implementation.
    private final List<StaffModItem<ItemStack>> items = new ArrayList<>();

    public ConfigurableStaffMod(BukkitStaffModManager manager, Plugin plugin) {
        super(manager);
        this.plugin = plugin;
    }

    @Override
    public void registerItem(StaffModItem<ItemStack> item) {

        // Calling super method. Do not forget to do that !
        super.registerItem(item);

        // This line is very important because items are not stored by default.
        this.items.add(item);
    }

    @Override
    public void enable(Player holder) {

        this.registerItems(holder);

        this.handler = this.createPlayerData();
        this.handler.save(holder);
        this.handler.clear(holder);

        super.enable(holder);
    }

    @Override
    public void disable(Player holder) {

        this.handler.clear(holder);
        this.handler.restore(holder);
        this.handler = null;

        super.disable(holder);
    }

    @Override
    public Collection<StaffModItem<ItemStack>> getItemsHeld() {
        // Returning our own list of items.
        return Collections.unmodifiableCollection(this.items);
    }

    @Override
    public void configure(ConfigurationSection parent) {

        this.items.stream()
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

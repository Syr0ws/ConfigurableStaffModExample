package fr.syrows.configurablestaffmodexample.staffmod;

import fr.syrows.configurablestaffmodexample.staffmod.items.FreezeItem;
import fr.syrows.configurablestaffmodexample.staffmod.items.InvseeItem;
import fr.syrows.configurablestaffmodexample.staffmod.items.VanishItem;
import fr.syrows.staffmodlib.StaffModManager;
import fr.syrows.staffmodlib.data.*;
import fr.syrows.staffmodlib.staffmod.AbstractStaffMod;
import fr.syrows.staffmodlib.staffmod.items.StaffModItem;
import fr.syrows.staffmodlib.util.Configurable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ConfigurableStaffMod extends AbstractStaffMod implements Configurable {

    // Plugin will be needed to register item listeners.
    private final Plugin plugin;

    // By default, items are not stored even if you call the registerItem(StaffModItem item) method.
    // So, you have to create this list in this implementation.
    private final List<StaffModItem> items = new ArrayList<>();

    public ConfigurableStaffMod(StaffModManager manager, Plugin plugin) {
        super(manager);
        this.plugin = plugin;
    }

    @Override
    public void registerItem(StaffModItem item) {

        // Calling super method. Do not forget to do that !
        super.registerItem(item);

        // This line is very important because items are not stored by default.
        this.items.add(item);
    }

    @Override
    public void registerItems(Player player) {

        // Registering our items. The method registerItem(StaffModItem item)
        // must be performed on each item you want to register because an item
        // may need an initialization.

        // Declaring items.
        StaffModItem vanishItem = new VanishItem(player, this.plugin);
        StaffModItem freezeItem = new FreezeItem(player, this.plugin);
        StaffModItem invseeItem = new InvseeItem(player, this.plugin);

        // Registering items.
        this.registerItem(vanishItem);
        this.registerItem(freezeItem);
        this.registerItem(invseeItem);

        // Retrieving config to configure items.
        FileConfiguration config = this.plugin.getConfig();

        // Configuring the staff mod.
        this.configure(config.getConfigurationSection("staffmod"));
    }

    @Override
    public PlayerData createPlayerData() {

        // This method has for goal to create a PlayerData object
        // which will store player state before enabling the staff mod.

        List<Data> data = Arrays.asList(
                new InventoryData(), // Storing inventory information.
                new PotionData(), // Storing potion effects information.
                new GameModeData(), // Storing current game mode.
                new HealthData(), // Storing health information.
                new FoodData() // Storing food information.
        );
        return new PlayerData(data);
    }

    @Override
    public Collection<StaffModItem> getItems() {
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
}

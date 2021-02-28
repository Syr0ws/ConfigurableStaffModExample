package fr.syrows.configurablestaffmodexample;

import fr.syrows.configurablestaffmodexample.commands.CommandConfigurableStaffMod;
import fr.syrows.staffmodlib.bukkit.BukkitStaffModManager;
import fr.syrows.staffmodlib.common.staffmod.StaffModManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurableStaffModExample extends JavaPlugin {

    private StaffModManager<Player, ItemStack> manager;

    @Override
    public void onEnable() {

        // Loading default configuration.
        super.saveDefaultConfig();

        // Creating a new StaffModManager instance which will
        // be used by all the staff mods.
        this.manager = new BukkitStaffModManager(this);

        // Registering commands.
        this.registerCommands();
    }

    private void registerCommands() {
        super.getCommand("configurablestaffmod").setExecutor(new CommandConfigurableStaffMod(this, this.manager));
    }
}

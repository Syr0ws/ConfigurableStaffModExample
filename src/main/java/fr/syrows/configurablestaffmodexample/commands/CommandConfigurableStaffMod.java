package fr.syrows.configurablestaffmodexample.commands;

import fr.syrows.configurablestaffmodexample.staffmod.ConfigurableStaffMod;
import fr.syrows.staffmodlib.StaffModManager;
import fr.syrows.staffmodlib.staffmod.StaffMod;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandConfigurableStaffMod implements CommandExecutor {

    private final Plugin plugin;
    private final StaffModManager manager;

    public CommandConfigurableStaffMod(Plugin plugin, StaffModManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Checking that the command sender is a Player.
        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        // Checking if the player is in staff mod.
        if(!this.manager.isInStaffMod(player)) {

            ConfigurableStaffMod staffMod = new ConfigurableStaffMod(this.manager, this.plugin);
            staffMod.enable(player); // Enabling staff mod.

        } else {

            StaffMod staffMod = this.manager.getNullableStaffMod(player);
            staffMod.disable(player); // Disabling staff mod.
        }
        return true;
    }
}

package org.bedracket;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.bedracket.command.ModInfoCommand;
import org.bedracket.eventbus.BedRacket;
import org.bedracket.test.TestListener;

public class BedRacketMod implements ModInitializer {

    @Override
    public void onInitialize() {
        loadModsList();
        CommandRegistrationCallback.
                EVENT.register((dispatcher, registryAccess, environment) -> {
                    dispatcher.register(ModInfoCommand.COMMAND);
                });
        final BedRacket bus = BedRacket.EVENT_BUS;
        int r = bus.addListener(new TestListener());
        TestListener.LOGGER.info("Registered " + r + " BedRacket Events");
    }

    public static void loadModsList() {
        ModInfoCommand.MODS.clear();
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            ModInfoCommand.MODS.put(mod.getMetadata().getId(), mod.getMetadata());
        }
    }
}

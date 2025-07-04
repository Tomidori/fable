package org.tomidori.fable;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.tomidori.fable.command.SkillCommand;
import org.tomidori.fable.registry.FableRegistries;
import org.tomidori.fable.registry.FableRegistryKeys;

public final class FableMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                SkillCommand.register(dispatcher)
        );
        FableRegistries.initialize();
        FableRegistryKeys.initialize();
    }
}

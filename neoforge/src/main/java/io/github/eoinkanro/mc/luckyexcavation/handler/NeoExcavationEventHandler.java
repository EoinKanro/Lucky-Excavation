package io.github.eoinkanro.mc.luckyexcavation.handler;

import java.util.Optional;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class NeoExcavationEventHandler {

  @SubscribeEvent
  public static void handle(BlockEvent.BreakEvent event) {
    ExcavationEventHandler.handle(
        event.getPlayer(),
        Optional.ofNullable(event.getState())
            .map(it -> it.getBlock())
            .orElse(null),
        event.getPos(),
        Optional.ofNullable(event.getLevel())
            .filter(it -> it instanceof Level)
            .map(it -> (Level) it)
            .orElse(null)
    );
  }

}

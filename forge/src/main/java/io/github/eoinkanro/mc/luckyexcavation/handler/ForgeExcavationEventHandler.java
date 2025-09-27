package io.github.eoinkanro.mc.luckyexcavation.handler;

import java.util.Optional;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeExcavationEventHandler {

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

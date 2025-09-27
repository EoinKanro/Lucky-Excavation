package io.github.eoinkanro.mc.luckyexcavation.handler;

import io.github.eoinkanro.mc.luckyexcavation.conf.Config;
import java.util.Optional;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeExcavationEventHandler {

  private final ExcavationEventHandler eventHandler;

  public ForgeExcavationEventHandler(Config config) {
    this.eventHandler = new ExcavationEventHandler(config);
  }

  @SubscribeEvent
  public void handle(BlockEvent.BreakEvent event) {
    eventHandler.handle(
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

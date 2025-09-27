package io.github.eoinkanro.mc.luckyexcavation.handler;

import io.github.eoinkanro.mc.luckyexcavation.conf.Config;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

public class FabricExcavationEventHandler {

  private final ExcavationEventHandler eventHandler;

  public FabricExcavationEventHandler(Config config) {
    this.eventHandler = new ExcavationEventHandler(config);
  }

  public void register() {
    PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
      eventHandler.handle(
          player,
          state != null ? state.getBlock() : null,
          pos,
          world
      );
    });
  }

}

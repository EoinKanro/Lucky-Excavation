package io.github.eoinkanro.mc.luckyexcavation.conf;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.eoinkanro.mc.luckyexcavation.LuckyExcavation;

public class ModMenuScreen implements ModMenuApi {

  private final ConfigClothScreen clothScreen;

  public ModMenuScreen() {
    this.clothScreen = new ConfigClothScreen(LuckyExcavation.CONFIG);
  }

  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return clothScreen::createScreen;
  }

}

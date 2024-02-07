package com.github.UGHT.eternis_plush.mixin;

import com.github.UGHT.eternis_plush.EternisPlush;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Inject(method = "init", at = @At("TAIL"))
	public void onInit(CallbackInfo ci) {
		EternisPlush.LOGGER.info("c!Winsweep lore: Crazy? I was crazy once, they locked me in a room, an amarite room, an amarite room with rats, and rats make me crazy");
	}
}

package dev.mtechlab.manadisplay.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.client.gui.HUDHandler;


@Mixin(HUDHandler.class)
public class HUDHandlerMixin {

    @Inject(method = "renderManaBar", at = @At("TAIL"), remap = false)
    private static void mana_display$renderManaBar(GuiGraphics gui, int x, int y, int color, float alpha, int mana, int maxMana, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        String text = mana + " / " + maxMana;

        int textWidth = mc.font.width(text);
        int posX = x + 51 - textWidth / 2;
        int posY = y - mc.font.lineHeight - 11;

        int alphaComponent = (int) (alpha * 255) << 24;
        int textColor = (color & 0x00FFFFFF) | alphaComponent;

        gui.drawString(mc.font, text, posX, posY, textColor, false);
    }
}
package dev.mtechlab.manadisplay.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.client.core.helper.RenderHelper;

@Mixin(BindableSpecialFlowerBlockEntity.BindableFlowerWandHud.class)
public abstract class FlowersWandHudMixin {

    @Shadow(remap = false) @Final
    protected BindableSpecialFlowerBlockEntity flower;

    @Inject(method = "renderHUD(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/Minecraft;III)V", at = @At("HEAD"), cancellable = true, remap = false)
    public void mana_display$renderManaBar(GuiGraphics gui, Minecraft mc, int minLeft, int minRight, int minDown, CallbackInfo ci) {
        String name = I18n.get(flower.getBlockState().getBlock().getDescriptionId());
        int color = flower.getColor();
        int centerX = mc.getWindow().getGuiScaledWidth() / 2;
        int centerY = mc.getWindow().getGuiScaledHeight() / 2;

        int textWidth = mc.font.width(name);
        int boxWidth = Math.max(102, textWidth) + 4;
        int left = boxWidth / 2;

        RenderHelper.renderHUDBox(gui,
                centerX - left,
                centerY - 4,
                centerX + left + 20,
                centerY + 30
        );

        BotaniaAPIClient.instance().drawComplexManaHUD(gui, color, flower.getMana(), flower.getMaxMana(), name, flower.getHudIcon(), flower.isValidBinding()
        );

        ci.cancel();
    }
}
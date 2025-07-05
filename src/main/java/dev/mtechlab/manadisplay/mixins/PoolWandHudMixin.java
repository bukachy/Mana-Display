package dev.mtechlab.manadisplay.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

@Mixin(ManaPoolBlockEntity.WandHud.class)
public abstract class PoolWandHudMixin {

    @Shadow(remap = false) @Final
    private ManaPoolBlockEntity pool;

    @ModifyArg(
            method = "renderHUD",
            at = @At(
                    value = "INVOKE",
                    target = "Lvazkii/botania/client/core/helper/RenderHelper;renderHUDBox(Lnet/minecraft/client/gui/GuiGraphics;IIII)V"
            ),
            index = 2,
            remap = false
    )
    private int modifyHudBoxY(int originalY) {
        Minecraft mc = Minecraft.getInstance();
        int centerY = mc.getWindow().getGuiScaledHeight() / 2;
        return centerY - 5;
    }
}
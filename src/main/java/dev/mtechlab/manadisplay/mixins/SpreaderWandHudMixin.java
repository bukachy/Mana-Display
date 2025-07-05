package dev.mtechlab.manadisplay.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

@Mixin(ManaSpreaderBlockEntity.WandHud.class)
public class SpreaderWandHudMixin {

    @Shadow(remap = false)
    @Final
    private ManaSpreaderBlockEntity spreader;

    @Inject(method = "renderHUD", at = @At("HEAD"), cancellable = true, remap = false)
    public void mana_display$renderHUD(GuiGraphics gui, Minecraft mc, CallbackInfo ci) {

        if(spreader instanceof ManaSpreaderBlockEntityAccessor mtechlab_spreader){
            String spreaderName = (new ItemStack(this.spreader.getBlockState().getBlock())).getHoverName().getString();
            ItemStack lensStack = this.spreader.getItemHandler().getItem(0);
            ItemStack recieverStack = mtechlab_spreader.getReceiver() == null ? ItemStack.EMPTY : new ItemStack(Objects.requireNonNull(this.spreader.getLevel()).getBlockState(mtechlab_spreader.getReceiver().getManaReceiverPos()).getBlock());
            int width = 4 + Collections.max(Arrays.asList(102, mc.font.width(spreaderName), RenderHelper.itemWithNameWidth(mc, lensStack), RenderHelper.itemWithNameWidth(mc, recieverStack)));
            int height = 22 + (lensStack.isEmpty() ? 0 : 18) + (recieverStack.isEmpty() ? 0 : 18);
            int centerX = mc.getWindow().getGuiScaledWidth() / 2;
            int centerY = mc.getWindow().getGuiScaledHeight() / 2;
            RenderHelper.renderHUDBox(gui, centerX - width / 2, centerY - 5, centerX + width / 2, centerY + 8 + height);
            int color = this.spreader.getVariant().hudColor;
            BotaniaAPIClient.instance().drawSimpleManaHUD(gui, color, this.spreader.getCurrentMana(), this.spreader.getMaxMana(), spreaderName);
            RenderHelper.renderItemWithNameCentered(gui, mc, recieverStack, centerY + 30, color);
            RenderHelper.renderItemWithNameCentered(gui, mc, lensStack, centerY + (recieverStack.isEmpty() ? 30 : 48), color);
        }

        ci.cancel();
    }
}
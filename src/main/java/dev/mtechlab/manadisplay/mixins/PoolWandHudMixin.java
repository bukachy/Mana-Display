package dev.mtechlab.manadisplay.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.ManaTabletItem;

@Mixin(ManaPoolBlockEntity.WandHud.class)
public abstract class PoolWandHudMixin {

    private static final int MANA_COLOR = 0x0095FF;
    private static final int ARROW_V = 38;
    private static final int ARROW_WIDTH = 22;
    private static final int ARROW_HEIGHT = 15;

    @Shadow(remap = false) @Final
    private ManaPoolBlockEntity pool;

    @Inject(method = "renderHUD", at = @At("HEAD"), cancellable = true, remap = false)
    private void renderCustomManaHUD(PoseStack ms, Minecraft mc, CallbackInfo ci) {
        ci.cancel();

        int centerX = mc.getWindow().getGuiScaledWidth() / 2;
        int centerY = mc.getWindow().getGuiScaledHeight() / 2;
        int currentMana = pool.getCurrentMana();
        int maxMana = pool.getMaxMana();

        ItemStack poolStack = new ItemStack(pool.getBlockState().getBlock());
        String name = poolStack.getHoverName().getString();

        int nameWidth = mc.font.width(name);
        int width = Math.max(102, nameWidth) + 4;
        int boxTop = centerY - 5;
        int boxBottom = centerY + 48;

        mana_display$renderHudElements(ms, mc, centerX, centerY, width, boxTop, boxBottom, name, currentMana, maxMana, poolStack);
    }

    @Unique
    private void mana_display$renderHudElements(PoseStack ms, Minecraft mc, int centerX, int centerY, int width, int boxTop, int boxBottom, String name, int currentMana, int maxMana, ItemStack poolStack) {
        RenderHelper.renderHUDBox(ms, centerX - width / 2, boxTop, centerX + width / 2, boxBottom);

        BotaniaAPIClient.instance().drawSimpleManaHUD(ms, MANA_COLOR, currentMana, maxMana, name);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        mana_display$renderDirectionArrow(ms, centerX, centerY);

        mana_display$renderItems(ms, mc, centerX, centerY, poolStack);

        RenderSystem.disableBlend();
    }

    @Unique
    private void mana_display$renderDirectionArrow(PoseStack ms, int centerX, int centerY) {
        int arrowU = pool.isOutputtingPower() ? ARROW_WIDTH : 0;
        RenderSystem.setShaderTexture(0, HUDHandler.manaBar);
        RenderHelper.drawTexturedModalRect(ms, centerX - 11, centerY + 30, arrowU, ARROW_V, ARROW_WIDTH, ARROW_HEIGHT);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    }

    @Unique
    private void mana_display$renderItems(PoseStack ms, Minecraft mc, int centerX, int centerY, ItemStack poolStack) {
        ItemStack tablet = new ItemStack(BotaniaItems.manaTablet);
        ManaTabletItem.setStackCreative(tablet);

        mc.getItemRenderer().renderAndDecorateItem(tablet, centerX - 31, centerY + 30);
        mc.getItemRenderer().renderAndDecorateItem(poolStack, centerX + 15, centerY + 30);
    }
}
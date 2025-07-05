package dev.mtechlab.manadisplay.mixins;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.common.item.ManaTabletItem;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

@Mixin({ManaTabletItem.class, BaubleItem.class})
public abstract class ItemManaDisplay extends Item {

    public ItemManaDisplay(Properties properties) {
        super(properties);
    }

    @Inject(
            method = "appendHoverText",
            at = @At("TAIL")
    )
    private void onAppendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci) {
        ManaItem manaItem = XplatAbstractions.INSTANCE.findManaItem(stack);

        if (manaItem == null) return;

        if (stack.getItem() instanceof ManaTabletItem) {
            if (!ManaTabletItem.isStackCreative(stack)) {
                mana_Display$addManaTooltip(tooltipComponents, manaItem);
            }
        } else {
            mana_Display$addManaTooltip(tooltipComponents, manaItem);
        }
    }

    @Unique
    private void mana_Display$addManaTooltip(List<Component> tooltipComponents, ManaItem manaItem) {
        tooltipComponents.add(mana_display$manaLevel(manaItem.getMana(), manaItem.getMaxMana())
                .copy().withStyle(ChatFormatting.AQUA));
    }

    @Unique
    private static Component mana_display$manaLevel(int current, int max) {
        return Component.translatable("manadisplay.mana",
                Component.literal(String.valueOf(current)),
                Component.literal(String.valueOf(max))
        );
    }
}
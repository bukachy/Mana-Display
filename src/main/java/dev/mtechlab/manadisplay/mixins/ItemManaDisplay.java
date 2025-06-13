package dev.mtechlab.manadisplay.mixins;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        ManaItem manaItem = XplatAbstractions.INSTANCE.findManaItem(stack);

        if (manaItem != null && !ManaTabletItem.isStackCreative(stack)) {
            tooltip.add(mana_display$manaLevel(manaItem.getMana(), manaItem.getMaxMana()).copy().withStyle(ChatFormatting.AQUA));
        }
    }

    @Unique
    private static Component mana_display$manaLevel(int current, int max) {
        return Component.translatable("manadisplay.mana",
                Component.literal(String.valueOf(current)),
                Component.literal(String.valueOf(max))
        );
    }
}
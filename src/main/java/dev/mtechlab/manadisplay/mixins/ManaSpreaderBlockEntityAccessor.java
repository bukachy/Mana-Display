package dev.mtechlab.manadisplay.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;

@Mixin(value = ManaSpreaderBlockEntity.class)
public interface ManaSpreaderBlockEntityAccessor {

    @Accessor(value = "receiver", remap = false)
    ManaReceiver getReceiver();

}

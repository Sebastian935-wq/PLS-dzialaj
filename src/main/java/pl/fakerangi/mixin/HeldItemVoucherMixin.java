package pl.fakerangi.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pl.fakerangi.FakeVoucher;

/** Replaces only the LOCAL first-person render argument, never real inventory data. */
@Mixin(HeldItemRenderer.class)
public abstract class HeldItemVoucherMixin {
    @ModifyVariable(
        method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        at = @At("HEAD"), argsOnly = true, ordinal = 0
    )
    private ItemStack fakerangi$renderVoucher(ItemStack original) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (FakeVoucher.valid(client)
                && ItemStack.areItemsAndComponentsEqual(original, client.player.getMainHandStack())) {
            ItemStack visual = FakeVoucher.visualStack();
            if (!visual.isEmpty()) return visual;
        }
        return original;
    }
}

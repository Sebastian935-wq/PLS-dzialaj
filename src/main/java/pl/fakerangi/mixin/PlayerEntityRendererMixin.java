package pl.fakerangi.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pl.fakerangi.FakeRangiClient;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    // Modyfikacja TYLKO renderowanego podpisu swojego gracza, bez zmiany danych serwera.
    @ModifyVariable(method = "renderLabelIfPresent", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private Text fakerangi$ownNameplate(Text original, PlayerEntityRenderState state, Text label,
            MatrixStack matrices, VertexConsumerProvider consumers, int light) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (FakeRangiClient.config == null || !FakeRangiClient.config.enabled
                || !FakeRangiClient.config.showNametag || mc.player == null
                || state.name == null || !state.name.equals(mc.player.getGameProfile().getName())) return original;
        return Text.literal(FakeRangiClient.config.rank.displayName() + " ")
                .formatted(FakeRangiClient.config.rank.formatting())
                .append(Text.literal(mc.player.getGameProfile().getName()).formatted(Formatting.WHITE));
    }
}

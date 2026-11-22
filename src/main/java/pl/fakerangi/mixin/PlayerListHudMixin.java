package pl.fakerangi.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.fakerangi.FakeRangiClient;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {
    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void fakerangi$localTabName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (FakeRangiClient.config == null || !FakeRangiClient.config.enabled || client.player == null) return;
        // Zmieniamy tylko wlasny wpis na liscie TAB, nigdy cudzy.
        if (!entry.getProfile().getId().equals(client.player.getUuid())) return;
        String name = entry.getProfile().getName();
        Text changed = Text.literal(FakeRangiClient.config.rank.displayName() + " ")
                .formatted(FakeRangiClient.config.rank.formatting())
                .append(Text.literal(name).formatted(Formatting.WHITE));
        cir.setReturnValue(changed);
    }
}

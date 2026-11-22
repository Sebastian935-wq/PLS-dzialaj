package pl.fakerangi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public final class FakeRangiClient implements ClientModInitializer {
    public static RankConfig config;
    private static KeyBinding openMenu;
    private static boolean wasRightPressed;
    private static final Identifier[] VOUCHER_TEXTURES = {
        Identifier.of("fakerangianarchia", "textures/gui/vip.png"),
        Identifier.of("fakerangianarchia", "textures/gui/svip.png"),
        Identifier.of("fakerangianarchia", "textures/gui/gvip.png")
    };

    @Override public void onInitializeClient() {
        config = RankConfig.load();
        openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fakerangianarchia.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z,
                "category.fakerangianarchia.settings"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenu.wasPressed()) client.setScreen(new RankScreen());
            // Edge-detect RMB while actually in a world. Does NOT alter/server-use the real item.
            if (client.player == null) { FakeVoucher.clear(); wasRightPressed = false; return; }
            if (FakeVoucher.holding() && !FakeVoucher.valid(client)) FakeVoucher.clear();
            boolean down = GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
            if (client.currentScreen == null && down && !wasRightPressed && FakeVoucher.holding()) {
                FakeVoucher.use(client);
            }
            wasRightPressed = down;
        });
        // Opcjonalny lokalny podglad; nie wysyla nic na serwer.
        HudRenderCallback.EVENT.register((context, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.options.hudHidden) return;
            if (FakeVoucher.holding() && FakeVoucher.valid(client)) {
                int x = context.getScaledWindowWidth() / 2 - 10;
                int y = context.getScaledWindowHeight() - 22;
                Rank voucher = FakeVoucher.heldRank();
                context.fill(x - 2, y - 2, x + 22, y + 21, 0xCC1B1522);
                context.drawTexture(RenderLayer::getGuiTextured, VOUCHER_TEXTURES[voucher == Rank.SVIP ? 1 : voucher == Rank.GVIP ? 2 : 0], x + 2, y + 2, 0, 0, 16, 16, 16, 16);
                context.drawTextWithShadow(client.textRenderer, voucher.displayName(), x - 1, y - 12, voucher.rgb());
            }
            if (config.fakeCreative) {
                // Symulowany status, bez zmiany prawdziwego trybu gry / server packets.
                String status = "TRYB KREATYWNY (FAKE)";
                int w = client.textRenderer.getWidth(status);
                int x = context.getScaledWindowWidth() - w - 14;
                context.fill(x - 5, 5, x + w + 5, 21, 0xCC172338);
                context.drawTextWithShadow(client.textRenderer, status, x, 9, 0xFF55FFFF);
            }
            if (!config.enabled || !config.showOverlay) return;
            String nick = client.player.getGameProfile().getName();
            Text label = Text.literal(config.rank.displayName() + " ").formatted(config.rank.formatting())
                    .append(Text.literal(nick).formatted(Formatting.WHITE));
            context.fill(5, 5, 13 + client.textRenderer.getWidth(label), 21, 0x990B0912);
            context.drawTextWithShadow(client.textRenderer, label, 9, 9, 0xFFFFFFFF);
        });
    }
}

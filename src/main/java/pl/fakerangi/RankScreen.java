package pl.fakerangi;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import java.util.Arrays;

/** Lokalny ekran ustawień; nie zmienia ekwipunku ani serwera. */
public final class RankScreen extends Screen {
    private static final Rank[] VOUCHERS = {Rank.VIP, Rank.SVIP, Rank.GVIP};
    private static final Identifier[] BOOKS = {
        Identifier.of("fakerangianarchia", "textures/gui/vip.png"),
        Identifier.of("fakerangianarchia", "textures/gui/svip.png"),
        Identifier.of("fakerangianarchia", "textures/gui/gvip.png")
    };
    private int left, top;
    private boolean inventoryTab = true;

    public RankScreen() { super(Text.literal("Fake Rangi - Anarchia.GG")); }

    @Override protected void init() {
        left = width / 2 - 170;
        top = height / 2 - 116;
        addDrawableChild(ButtonWidget.builder(Text.literal("RANGA EKWIPUNEK"), b -> { inventoryTab = true; rebuild(); })
            .dimensions(left + 7, top + 27, 135, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("USTAWIENIA"), b -> { inventoryTab = false; rebuild(); })
            .dimensions(left + 7, top + 51, 135, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("COFNIJ / PRZYWROC"), b -> { FakeVoucher.undo(); rebuild(); })
            .dimensions(left + 7, top + 179, 135, 20).build());
        addDrawableChild(ButtonWidget.builder(toggleLabel(), b -> {
            FakeRangiClient.config.enabled = !FakeRangiClient.config.enabled; rebuild();
        }).dimensions(left + 155, top + 179, 177, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Zamknij"), b -> close())
            .dimensions(left + 155, top + 205, 177, 20).build());

        if (inventoryTab) {
            Rank[] ranks = Rank.values();
            for (int i = 0; i < ranks.length; i++) {
                Rank rank = ranks[i];
                int x = left + 155 + (i % 3) * 59;
                int y = top + 50 + (i / 3) * 22;
                addDrawableChild(ButtonWidget.builder(Text.literal(rank.displayName()).formatted(rank.formatting()), b -> {
                    FakeRangiClient.config.rank = rank;
                    FakeRangiClient.config.enabled = true;
                    rebuild();
                }).dimensions(x, y, 55, 20).build());
            }
        } else {
            addDrawableChild(ButtonWidget.builder(Text.literal("HUD: " + (FakeRangiClient.config.showOverlay ? "ON" : "OFF")), b -> {
                FakeRangiClient.config.showOverlay = !FakeRangiClient.config.showOverlay; rebuild();
            }).dimensions(left + 155, top + 72, 177, 20).build());
            addDrawableChild(ButtonWidget.builder(Text.literal("Napis nad glowa: " + (FakeRangiClient.config.showNametag ? "ON" : "OFF")), b -> {
                FakeRangiClient.config.showNametag = !FakeRangiClient.config.showNametag; rebuild();
            }).dimensions(left + 155, top + 98, 177, 20).build());
            addDrawableChild(ButtonWidget.builder(Text.literal("FAKE CREATIVE: " + (FakeRangiClient.config.fakeCreative ? "ON" : "OFF")), b -> {
                FakeRangiClient.config.fakeCreative = !FakeRangiClient.config.fakeCreative; rebuild();
            }).dimensions(left + 155, top + 124, 177, 20).build());
        }
    }

    private Text toggleLabel() {
        return Text.literal("FAKE RANGI: " + (FakeRangiClient.config.enabled ? "ON" : "OFF"))
            .formatted(FakeRangiClient.config.enabled ? Formatting.GREEN : Formatting.RED);
    }
    private void rebuild() { FakeRangiClient.config.save(); clearAndInit(); }
    private boolean hasSingleItem() {
        return client != null && client.player != null && !client.player.getMainHandStack().isEmpty()
            && client.player.getMainHandStack().getCount() == 1;
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (inventoryTab) {
            for (int i = 0; i < VOUCHERS.length; i++) {
                int x = left + 180 + i * 42;
                int y = top + 141;
                if (mouseX >= x && mouseX < x + 36 && mouseY >= y && mouseY < y + 35) {
                    if (button == 0 && client != null) { if (FakeVoucher.pickup(client, VOUCHERS[i])) rebuild(); return true; }
                    if (button == 1 && client != null && FakeVoucher.heldRank() == VOUCHERS[i]) {
                        FakeVoucher.use(client); rebuild(); return true;
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Ostre, nieprzezroczyste tło wewnątrz okna; rozmycie świata kontrolują ustawienia Minecrafta.
        context.fill(left, top, left + 340, top + 232, 0xFF18151E);
        context.fill(left + 1, top + 1, left + 339, top + 23, 0xFF30243C);
        context.drawCenteredTextWithShadow(textRenderer, "FAKE RANGI | ANARCHIA.GG", left + 170, top + 8, 0xFFFFFFFF);
        context.fill(left + 148, top + 27, left + 149, top + 175, 0xFF654474);
        context.drawCenteredTextWithShadow(textRenderer, inventoryTab ? "WYBIERZ RANGE" : "USTAWIENIA", left + 244, top + 31, 0xFFFFD85A);
        if (inventoryTab) {
            context.drawCenteredTextWithShadow(textRenderer, "VOUCHERY (LPM = WEZ)", left + 244, top + 129, 0xFF55FF55);
            for (int i=0;i<VOUCHERS.length;i++) {
                Rank r = VOUCHERS[i];
                int x = left + 180 + i * 42, y = top + 141;
                context.fill(x, y, x + 36, y + 35, 0xFF403746);
                context.drawTexture(RenderLayer::getGuiTextured, BOOKS[i], x+10, y+2, 0, 0, 16, 16, 16, 16);
                context.drawCenteredTextWithShadow(textRenderer, r.name(), x+18, y+22, r.rgb());
            }
            context.drawCenteredTextWithShadow(textRenderer, "WYMAGANA 1 SZT. PRZEDMIOTU!", left + 74, top + 101, 0xFFFF5555);
            context.drawCenteredTextWithShadow(textRenderer, "COFNIJ = przywroc widok", left + 74, top + 133, 0xFFCCCCCC);
            String status = FakeVoucher.holding() ? "Voucher: " + FakeVoucher.heldRank().name() :
                hasSingleItem() ? "Trzymasz 1 szt. - OK" : "Trzymaj 1 szt. w rece!";
            context.drawCenteredTextWithShadow(textRenderer, status, left + 74, top + 115,
                hasSingleItem() ? 0xFF55FF55 : 0xFFFF5555);
        } else {
            context.drawCenteredTextWithShadow(textRenderer, "FAKE CREATIVE = tylko wyglad", left + 243, top + 155, 0xFFFFD85A);
            context.drawCenteredTextWithShadow(textRenderer, "Bez lotu, itemow i uprawnien", left + 243, top + 167, 0xFFBBBBBB);
        }
        context.drawCenteredTextWithShadow(textRenderer, "Wybrana: " + FakeRangiClient.config.rank.displayName(),
            left + 74, top + 151, FakeRangiClient.config.rank.rgb());
        context.drawCenteredTextWithShadow(textRenderer,
            FakeRangiClient.config.enabled ? "FAKE RANGA ON" : "PRAWDZIWA RANGA",
            left + 74, top + 165, FakeRangiClient.config.enabled ? 0xFF55FF55 : 0xFFFF5555);
        super.render(context, mouseX, mouseY, delta);
        if (inventoryTab) {
            for (int i=0;i<VOUCHERS.length;i++) {
                int x = left + 180 + i*42, y = top + 141;
                if(mouseX >= x && mouseX < x+36 && mouseY >= y && mouseY < y+35) {
                    context.drawTooltip(textRenderer, Arrays.asList(RankVouchers.tooltip(VOUCHERS[i])), mouseX, mouseY);
                    break;
                }
            }
        }
    }
    @Override public void close() {
        FakeRangiClient.config.save();
        if (client != null) client.setScreen(null);
    }
}

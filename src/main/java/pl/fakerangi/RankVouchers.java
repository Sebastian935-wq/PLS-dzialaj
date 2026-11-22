package pl.fakerangi;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/** Tekst wizualny wzorowany na zrzutach ekranu; bez prawdziwego przedmiotu. */
public final class RankVouchers {
    private RankVouchers() {}
    public static Text[] tooltip(Rank rank) {
        return new Text[] {
            Text.literal("VOUCHER NA " + rank.displayName()).formatted(rank.formatting())
                .append(Text.literal(" (30 dni)").formatted(Formatting.WHITE)),
            Text.literal("Niezniszczalnosc X").formatted(Formatting.DARK_GRAY),
            Text.literal("» Uzywajac go otrzymasz range").formatted(Formatting.GRAY),
            Text.literal("» ").formatted(Formatting.DARK_GRAY)
                .append(Text.literal(rank.displayName()).formatted(rank.formatting()))
                .append(Text.literal(" na okres 30 dni!").formatted(Formatting.GRAY)),
            Text.literal(" "),
            Text.literal("WYMAGANE: 1 SZT. DOWOLNEGO PRZEDMIOTU!").formatted(Formatting.RED),
            Text.literal("» Lewy klik: wez | prawy klik: uzyj fake!").formatted(Formatting.GREEN)
        };
    }
}

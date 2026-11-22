package pl.fakerangi;

import net.minecraft.util.Formatting;

/** Kolory VIP/SVIP/GVIP wzorowane na screenach, kolory administracji: podane przez użytkownika. */
public enum Rank {
    VIP(Formatting.YELLOW, 0xFFFF55),
    SVIP(Formatting.GOLD, 0xFFAA00),
    GVIP(Formatting.AQUA, 0x55FFFF),
    POMOCNIK(Formatting.AQUA, 0x55AAFF),
    ADMINISTRATOR(Formatting.RED, 0xEF5555),
    HEADADMIN(Formatting.DARK_RED, 0xAA0000),
    TWORCA(Formatting.DARK_PURPLE, 0xAA00AA),
    WLASCICIEL(Formatting.RED, 0xFF5555),
    VOUCHER(Formatting.LIGHT_PURPLE, 0xFF55FF);

    private final Formatting formatting;
    private final int rgb;
    Rank(Formatting formatting, int rgb) { this.formatting = formatting; this.rgb = rgb; }
    public Formatting formatting() { return formatting; }
    public int rgb() { return rgb; }
    public String displayName() {
        return switch (this) {
            case POMOCNIK -> "Pomocnik";
            case ADMINISTRATOR -> "Administrator";
            case HEADADMIN -> "Headadmin";
            case TWORCA -> "TWÓRCA";
            case WLASCICIEL -> "Właściciel";
            default -> name();
        };
    }
}

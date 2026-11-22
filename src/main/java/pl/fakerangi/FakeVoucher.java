package pl.fakerangi;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.Arrays;

/** Client-only visual voucher. Never modifies player inventory or sends packets. */
public final class FakeVoucher {
    private FakeVoucher() {}
    private static Rank held;
    private static int selectedSlot = -1;
    private static ItemStack original = ItemStack.EMPTY;

    /** Client render-only model, not placed in any inventory slot. */
    public static ItemStack visualStack() {
        if (held == null) return ItemStack.EMPTY;
        ItemStack shown = new ItemStack(Items.PAPER);
        String name = held.name().toLowerCase(java.util.Locale.ROOT);
        shown.set(DataComponentTypes.ITEM_MODEL, Identifier.of("fakerangianarchia", name));
        shown.set(DataComponentTypes.CUSTOM_NAME, Text.literal("VOUCHER NA " + held.displayName()).formatted(held.formatting()));
        shown.set(DataComponentTypes.LORE, new LoreComponent(Arrays.asList(RankVouchers.tooltip(held))));
        return shown;
    }

    public static boolean holding() { return held != null; }
    public static Rank heldRank() { return held; }

    public static boolean pickup(MinecraftClient client, Rank rank) {
        if (client.player == null) return false;
        ItemStack stack = client.player.getMainHandStack();
        if (stack.isEmpty() || stack.getCount() != 1) return false;
        held = rank;
        selectedSlot = client.player.getInventory().selectedSlot;
        original = stack.copy();
        return true;
    }

    public static boolean valid(MinecraftClient client) {
        if (held == null || client.player == null) return false;
        ItemStack current = client.player.getMainHandStack();
        return client.player.getInventory().selectedSlot == selectedSlot
                && !current.isEmpty() && current.getCount() == 1
                && ItemStack.areItemsAndComponentsEqual(current, original);
    }

    public static boolean use(MinecraftClient client) {
        if (!valid(client)) return false;
        FakeRangiClient.config.rank = held;
        FakeRangiClient.config.enabled = true;
        FakeRangiClient.config.save();
        clear();
        return true;
    }

    public static void clear() {
        held = null;
        original = ItemStack.EMPTY;
        selectedSlot = -1;
    }

    public static void undo() {
        clear();
        FakeRangiClient.config.enabled = false;
        FakeRangiClient.config.save();
    }
}

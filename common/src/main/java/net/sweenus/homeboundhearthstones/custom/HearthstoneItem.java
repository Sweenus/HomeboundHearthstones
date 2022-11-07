package net.sweenus.homeboundhearthstones.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.sweenus.homeboundhearthstones.config.HomeboundHearthstonesConfig;
import net.sweenus.homeboundhearthstones.util.HelperMethods;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HearthstoneItem extends Item {

    private final int abilityCooldown = (int) HomeboundHearthstonesConfig.getGeneralSettings("hearthstone_cooldown_duration") * 20;
    private int checkCooldown;
    private boolean canTeleport;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.world.isClient()) {
            ItemStack stack = user.getEquippedStack(EquipmentSlot.MAINHAND);


            if (!user.isSneaking()) {

                if (!HomeboundHearthstonesConfig.getBooleanValue(("can_transport_ores"))) {
                    Inventory inventory = user.getInventory();
                    int inventorySize = inventory.size();

                    for (int i = inventorySize; i > 0; i--) {

                        ItemStack currentItem = inventory.getStack(i);
                        //if (currentItem.isIn(ItemTags.COPPER_ORES)) {
                        if (currentItem.getName().toString().toLowerCase().contains("raw")
                                || currentItem.getName().toString().toLowerCase().contains("ore")
                                || currentItem.getName().toString().toLowerCase().contains("ingot")
                                || currentItem.getName().getString().equals("Diamond")) {
                            user.sendMessage(Text.translatable("item.homeboundhearthstones.hearthstoneitem.cannotteleport", currentItem.getName().getString()).formatted(Formatting.DARK_RED), false);
                            canTeleport = false;
                        }
                    }
                }

                if (world.getRegistryKey() != World.OVERWORLD){
                    canTeleport = false;
                    user.sendMessage(Text.translatable("item.homeboundhearthstones.hearthstoneitem.interdimensionalfalse").formatted(Formatting.DARK_RED), false);
                }

                if (canTeleport) {
                    //System.out.println("Trying to recall");
                    int xx = stack.getOrCreateNbt().getInt("returnX");
                    int yy = stack.getOrCreateNbt().getInt("returnY");
                    int zz = stack.getOrCreateNbt().getInt("returnZ");
                    if (zz != 0 && xx != 0) {

                        //System.out.println("We're recalling!");
                        world.playSoundFromEntity(null, user, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 2f, 2f);
                        user.requestTeleport(xx, yy, zz);

                        checkCooldown = abilityCooldown;
                        user.getItemCooldownManager().set(this, abilityCooldown);
                        user.incrementStat(Stats.USED.getOrCreateStat(this));
                        stack.damage(1, user, (p) -> {
                            p.sendToolBreakStatus(user.getActiveHand());
                        });
                    }
                }
                canTeleport = true;
            }
            else {
                double xpos = user.getX() - 4;
                double ypos = user.getY();
                double zpos = user.getZ() - 4;

                for (int i = 6; i > 0; i--) {
                    for (int j = 6; j > 0; j--) {
                        BlockPos poscheck = new BlockPos(xpos + i, ypos, zpos + j);
                        BlockState currentState = world.getBlockState(poscheck);
                        if (currentState.isIn(BlockTags.BEDS)) {
                            stack.getOrCreateNbt().putInt("returnX", poscheck.getX());
                            stack.getOrCreateNbt().putInt("returnY", poscheck.getY());
                            stack.getOrCreateNbt().putInt("returnZ", poscheck.getZ());
                            //System.out.println("Successfully saved recall coords");
                            world.playSoundFromEntity(null, user, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 2f, 1f);
                            user.sendMessage(Text.translatable("item.homeboundhearthstones.hearthstoneitem.savedcoords").formatted(Formatting.GREEN), false);
                            user.getItemCooldownManager().set(this, 40);
                            break;
                        }


                    }
                }
            }
        }


        return super.use(world, user, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && (entity instanceof PlayerEntity player)) {

            if (checkCooldown > 0)
                checkCooldown --;

            if (!player.getItemCooldownManager().isCoolingDown(stack.getItem()) && checkCooldown > 40) {
                player.getItemCooldownManager().set(this, checkCooldown);

            }

        }
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.literal(""));
        if (checkCooldown > 0) {
            tooltip.add(Text.translatable("item.homeboundhearthstones.hearthstone.remainingcooldown",
                    checkCooldown / 1200).formatted(Formatting.GOLD, Formatting.ITALIC));
            tooltip.add(Text.literal(""));
        }
        tooltip.add(Text.translatable("item.homeboundhearthstones.onrightclick").formatted(Formatting.BOLD, Formatting.GREEN));
        tooltip.add(Text.translatable("item.homeboundhearthstones.hearthstone.tooltip1"));
        tooltip.add(Text.translatable("item.homeboundhearthstones.sneakanduse").formatted(Formatting.BOLD, Formatting.GREEN));
        tooltip.add(Text.translatable("item.homeboundhearthstones.hearthstone.tooltip2"));
        tooltip.add(Text.literal(""));

    }



    public HearthstoneItem(Settings settings) {
        super(settings);
    }
}

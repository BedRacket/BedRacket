package org.bedracket.test;

import com.mojang.logging.LogUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.bedracket.block_events.BlockBreakEvent;
import org.bedracket.entity_events.EntityDeathEvent;
import org.bedracket.entity_events.EntityExplosionEvent;
import org.bedracket.entity_events.EntityJoinWorldEvent;
import org.bedracket.entity_events.EntityMoveEvent;
import org.bedracket.eventbus.EventHandler;
import org.bedracket.eventbus.Listener;
import org.slf4j.Logger;

public class TestListener implements Listener {

    public static final Logger LOGGER = LogUtils.getLogger();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getDefaultState().isOf(Blocks.IRON_BLOCK)) {
            event.getPlayer().sendMessage(Text.literal("You are breaking a iron block!"));
        }
    }

    @EventHandler
    public void onSheepDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof SheepEntity) {
            LOGGER.info("EntityDeathEvent test!");
        }
    }

    @EventHandler
    public void onCreeperDeath(EntityExplosionEvent event) {
        if (event.getEntity() instanceof CreeperEntity) {
            LOGGER.info("EntityExplosionEvent test !");
        }
    }

    @EventHandler
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            LOGGER.info("EntityJoinWorldEvent test!");
        }
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
        if (event.getEntity() instanceof CowEntity) {
            LOGGER.info("EntityMoveEvent test!");
        }
    }
}

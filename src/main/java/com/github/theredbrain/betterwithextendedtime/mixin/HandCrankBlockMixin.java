package com.github.theredbrain.betterwithextendedtime.mixin;

import com.bwt.blocks.HandCrankBlock;
import com.bwt.blocks.MechPowerBlockBase;
import com.bwt.sounds.BwtSoundEvents;
import com.github.theredbrain.betterwithextendedtime.BetterWithExtendedTime;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandCrankBlock.class)
public abstract class HandCrankBlockMixin extends Block {

	@Shadow
	public static IntProperty CLICK_TIMER;

	@Shadow
	public boolean checkForOverpower(World world, BlockPos pos) {
		throw new AssertionError();
	}

	@Shadow
	public void playClick(World world, BlockPos pos) {
		throw new AssertionError();
	}

	@Shadow
	public void breakWithDrop(World world, BlockPos pos) {
		throw new AssertionError();
	}

	public HandCrankBlockMixin(Settings settings) {
		super(settings);
	}

	/**
	 * @author TheRedBrain
	 * @reason integrate stamina cost
	 */
	@Overwrite
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if ((hit.getPos().y - (double)pos.getY()) * 16.0 <= 4.0) {
			return ActionResult.FAIL;
		} else {
			int clickTimer = (Integer)state.get(CLICK_TIMER);
			if (clickTimer != 0) {
				return ActionResult.FAIL;
			} else if (BetterWithExtendedTime.getCurrentStamina(player) <= 0) {
				if (world.isClient) {
					player.sendMessage(Text.of("You're too exhausted for manual labor."), true);
				}

				return ActionResult.FAIL;
			} else {
				BetterWithExtendedTime.addStamina(player, -2.0F);
				if (world.isClient) {
					return ActionResult.SUCCESS;
				} else {
					if (!this.checkForOverpower(world, pos)) {
						world.setBlockState(pos, (BlockState)state.with(CLICK_TIMER, 1));
						this.playClick(world, pos);
						world.scheduleBlockTick(pos, this, 3);
					} else {
						this.breakWithDrop(world, pos);
					}

					return ActionResult.SUCCESS;
				}
			}
		}
	}

}
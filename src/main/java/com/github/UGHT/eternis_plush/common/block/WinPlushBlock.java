package com.github.UGHT.eternis_plush.common.block;


import net.minecraft.block.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Equippable;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class WinPlushBlock extends Block implements Equippable, Waterloggable {
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final VoxelShape FRONT_SHAPE =
		Block.createCuboidShape(3, 0.01, 3, 13, 15, 13);

	public static final VoxelShape SIDE_SHAPE = FRONT_SHAPE;
	public static final VoxelShape DEBUG_SHAPE = FRONT_SHAPE;


	public WinPlushBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getStateManager().getDefaultState()
			.with(FACING, Direction.NORTH)
			.with(WATERLOGGED, false));
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState()
			.with(FACING, ctx.getPlayerFacing().getOpposite())
			.with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return switch (state.get(FACING).ordinal()) {
			case 2 -> FRONT_SHAPE; // North
			case 3 -> FRONT_SHAPE; // South
			case 4 -> SIDE_SHAPE; // West
			case 5 -> SIDE_SHAPE; // East
			default -> DEBUG_SHAPE;
		};
	}


	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context){
		return switch (state.get(FACING).ordinal()){
			case 2 -> FRONT_SHAPE;
			case 3 -> FRONT_SHAPE;
			case 4 -> SIDE_SHAPE;
			case 5 -> SIDE_SHAPE;
			default -> DEBUG_SHAPE;
		};
	}


	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public EquipmentSlot getPreferredSlot() {
		return EquipmentSlot.HEAD;
	}
}

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
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.stream.Stream;

public class EternisPlushBlock extends Block implements Equippable, Waterloggable {
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final VoxelShape FRONT_SHAPE =
		Stream.of(
			Stream.of(
				Block.createCuboidShape(3, 5, 3, 13, 15, 3),
				Block.createCuboidShape(3, 5, 3, 13, 5, 13),
				Block.createCuboidShape(3, 15, 3, 13, 15, 13),
				Block.createCuboidShape(3, 5, 13, 13, 15, 13),
				Block.createCuboidShape(13, 5, 3, 13, 15, 13),
				Block.createCuboidShape(3, 5, 3, 3, 15, 13)
			).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get(),
		Block.createCuboidShape(3.75, 5.75, 4.25, 12.25, 14.25, 12.75),
		Block.createCuboidShape(4.25, 5.75, 7.75, 12, 13.75, 7.75),
		Stream.of(
		Block.createCuboidShape(1.25, 0.5, 7, 2.25, 2.5, 8),
		Block.createCuboidShape(2, 0, 7, 6, 1, 8),
		Block.createCuboidShape(2.3732616091400134, 0, 8.064617549342113, 4.373261609140013, 1, 13.064617549342113),
		Block.createCuboidShape(2.8732616091400134, 0, 13.064617549342113, 3.8732616091400134, 1, 15.064617549342113),
		Block.createCuboidShape(4.728347178446585, 0, 12.338351465817052, 5.728347178446585, 3, 13.338351465817052),
		Block.createCuboidShape(6, 0, 9, 10, 4, 10),
		Block.createCuboidShape(7, 0, 9, 9, 1, 12),
		Block.createCuboidShape(7, 0, 11.5, 9, 2, 12.5),
		Block.createCuboidShape(7.5, 2, 11.5, 8.5, 4, 12.5),
		Block.createCuboidShape(8.876397475541854, 0, 8.566712868283192, 10.876397475541854, 1, 13.566712868283192),
		Block.createCuboidShape(9.376397475541854, 0, 13.566712868283192, 10.376397475541854, 1, 16.56671286828319),
		Block.createCuboidShape(9, 0, 8, 14, 1, 10),
		Block.createCuboidShape(13, 1, 8, 14, 2, 10),
		Block.createCuboidShape(13, 2, 8.5, 14, 4, 9.5),
		Block.createCuboidShape(4, 0, 6, 5, 1, 11)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get(),
		Stream.of(
		Block.createCuboidShape(4, 0, 2, 7, 3, 9),
		Block.createCuboidShape(9, 0, 2, 12, 3, 9),
		Block.createCuboidShape(5, 0, 6.5, 11, 5, 9.5),
		Block.createCuboidShape(1, 3, 7, 5, 5, 9),
		Block.createCuboidShape(11, 3, 7, 15, 5, 9)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get(),
		Block.createCuboidShape(0, 0, 0, 16, 0, 16)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

	public static final VoxelShape SIDE_SHAPE = FRONT_SHAPE;
	public static final VoxelShape DEBUG_SHAPE = FRONT_SHAPE;


	public EternisPlushBlock(Settings settings) {
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

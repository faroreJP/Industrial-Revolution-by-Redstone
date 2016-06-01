package jp.plusplus.ir2;

//import codechicken.nei.api.API;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.EntityRegistry;
import jp.plusplus.ir2.blocks.render.*;
import jp.plusplus.ir2.entity.EntityNeedle;
import jp.plusplus.ir2.entity.render.RenderNeedle;
import jp.plusplus.ir2.nei.NEILoader;
import jp.plusplus.ir2.nei.ProcessorRecipeHandler;
import jp.plusplus.ir2.tileentities.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


/**
 * Created by plusplus_F on 2015/01/31.
 */
public class ProxyClient extends ProxyServer {
    public static ProcessorRecipeHandler processor;

    @Override
    public EntityPlayer getClientPlayer(){ return Minecraft.getMinecraft().thePlayer; }
    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public void registerTileEntity() {
        super.registerTileEntity();

        RenderingRegistry.registerEntityRenderingHandler(EntityNeedle.class, new RenderNeedle());

        TileEntitySpecialRenderer tesr;

        IR2.renderCableId = registerRenderer(new RenderCable());
        IR2.renderDirectionalId = registerRenderer(new RenderDirectional());
        IR2.renderAmplifierId = registerRenderer(new RenderAmplifier());
        IR2.renderPipeMiningId = registerRenderer(new RenderPipeMining());
        IR2.renderMultiId = registerRenderer(new RenderBlockMulti());

        tesr = new RenderFluidTank();
        IR2.renderFluidTankId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTank.class, tesr);

        tesr = new RenderCableNew();
        IR2.renderCableNewId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCable.class, tesr);

        tesr = new RenderGenerator();
        IR2.renderPGId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGenerator.class, tesr);

        tesr = new RenderFountain();
        IR2.renderFountainId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFountain.class, tesr);

        tesr = new RenderSpawner();
        IR2.renderSpawnerId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoSpawner.class, tesr);

        tesr = new RenderPole();
        IR2.renderPoleId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPole.class, tesr);

        tesr = new RenderMachineSpinning();
        IR2.renderMachineSpinningId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpinning.class, tesr);

        tesr = new RenderCrusher();
        IR2.renderCrusherId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrusher.class, tesr);

        tesr = new RenderPipe();
        IR2.renderPipeId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPipeBase.class, tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPipeFluid.class, tesr);

        tesr = new RenderSower();
        IR2.renderSowerId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySower.class, tesr);

        tesr = new RenderFan();
        IR2.renderFanId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        IR2.renderCeilingLightId = registerRenderer(new RenderCeilingLight());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityForRender.class, tesr);

        tesr = new RenderChunkLoader();
        IR2.renderChunkLoaderId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChunkLoader.class, tesr);

        tesr = new RenderTransmitter();
        IR2.renderTransmitterId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTransmitter.class, tesr);

        tesr = new RenderPump();
        IR2.renderPumpId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPump.class, tesr);

        tesr = new RenderHarvester();
        IR2.renderHarvesterId = registerRenderer((ISimpleBlockRenderingHandler) tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHarvester.class, tesr);

        /*
        tesr=new RenderSyntheticFurnace();
        IR2.renderSyntheticFurnaceId=registerRenderer((ISimpleBlockRenderingHandler)tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySyntheticFurnace.class, tesr);
        */
        IR2.renderSyntheticFurnaceId = registerTERender(TileEntitySyntheticFurnace.class, new RenderSyntheticFurnace());
    }

    @Override
    public int registerRenderer(ISimpleBlockRenderingHandler renderer){
        int id=RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(id, renderer);
        //FMLLog.severe(renderer.toString());
        return id;
    }
    @Override
    public void loadNEI(){
        if(Loader.isModLoaded("NotEnoughItems")){
            NEILoader.LoadNEI();
        }
    }

    public int registerTERender(Class<? extends TileEntity> c,  TileEntitySpecialRenderer tesr){
        ClientRegistry.bindTileEntitySpecialRenderer(c, tesr);
        return registerRenderer((ISimpleBlockRenderingHandler)tesr);
    }

/*
    @Override
    public void registerAchievement(){
    }
*/
}

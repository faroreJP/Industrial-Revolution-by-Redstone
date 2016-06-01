package jp.plusplus.ir2.nei;

import codechicken.nei.api.API;
import codechicken.nei.recipe.FurnaceRecipeHandler;
import jp.plusplus.ir2.gui.*;

/**
 * Created by plusplus_F on 2015/06/27.
 */
public class NEILoader {
    private static CrusherRecipeHandler crh;
    private static WeavingRecipeHandler wrh;
    private static RSFurnaceRecipeHandler frh;
    private static RSAdvFurnaceRecipeHandler frh2;
    private static ExtractingRecipeHandler erh;
    private static SpinningRecipeHandler srh;
    private static AlloyingRecipeHandler arh;
    private static RustyAlloyingRecipeHandler rarh;
    private static MixingRecipeHandler mrh;
    private static CompositionVWHandler ch;

    private static CrafterRecipeHandler1 crh1;
    private static CrafterRecipeHandler2 crh2;

    public static void LoadNEI(){
        crh=new CrusherRecipeHandler();
        API.registerRecipeHandler(crh);
        API.registerUsageHandler(crh);
        API.registerGuiOverlay(GuiCrusher.class, crh.getOverlayIdentifier(), 0, 0);

        wrh=new WeavingRecipeHandler();
        API.registerRecipeHandler(wrh);
        API.registerUsageHandler(wrh);
        API.registerGuiOverlay(GuiLoom.class, wrh.getOverlayIdentifier(), 0, 0);

        frh=new RSFurnaceRecipeHandler();
        API.registerRecipeHandler(frh);
        API.registerUsageHandler(frh);
        API.registerGuiOverlay(GuiFurnaceRS.class, frh.getOverlayIdentifier(), 0, 0);

        frh2=new RSAdvFurnaceRecipeHandler();
        API.registerRecipeHandler(frh2);
        API.registerUsageHandler(frh2);
        API.registerGuiOverlay(GuiFurnaceAdvanced.class, frh2.getOverlayIdentifier(), 0, 0);

        erh=new ExtractingRecipeHandler();
        API.registerRecipeHandler(erh);
        API.registerUsageHandler(erh);
        API.registerGuiOverlay(GuiExtractor.class, erh.getOverlayIdentifier(), 0, 0);

        srh=new SpinningRecipeHandler();
        API.registerRecipeHandler(srh);
        API.registerUsageHandler(srh);
        API.registerGuiOverlay(GuiSpinning.class, srh.getOverlayIdentifier(), 0, 0);

        rarh=new RustyAlloyingRecipeHandler();
        API.registerRecipeHandler(rarh);
        API.registerUsageHandler(rarh);
        API.registerGuiOverlay(GuiAlloySmelterRusty.class, rarh.getOverlayIdentifier(), 0, 0);

        arh=new AlloyingRecipeHandler();
        API.registerRecipeHandler(arh);
        API.registerUsageHandler(arh);
        API.registerGuiOverlay(GuiAlloySmelter.class, arh.getOverlayIdentifier(), 0, 0);


        mrh=new MixingRecipeHandler();
        API.registerRecipeHandler(mrh);
        API.registerUsageHandler(mrh);
        API.registerGuiOverlay(GuiAlloySmelter.class, mrh.getOverlayIdentifier(), 0, 0);

        ch=new CompositionVWHandler();
        API.registerRecipeHandler(ch);
        API.registerUsageHandler(ch);
        API.registerGuiOverlay(GuiSyntheticFurnace.class, ch.getOverlayIdentifier(), 0, 0);

        crh1=new CrafterRecipeHandler1();
        API.registerRecipeHandler(crh1);
        API.registerUsageHandler(crh1);
        API.registerGuiOverlay(GuiAutoCrafter.class, crh1.getOverlayIdentifier(), 0, 0);

        crh2=new CrafterRecipeHandler2();
        API.registerRecipeHandler(crh2);
        API.registerUsageHandler(crh2);
        API.registerGuiOverlay(GuiAutoCrafter.class, crh2.getOverlayIdentifier(), 0, 0);
    }
}

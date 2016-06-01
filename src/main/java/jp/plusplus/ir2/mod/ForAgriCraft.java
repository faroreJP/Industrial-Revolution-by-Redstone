package jp.plusplus.ir2.mod;

import com.InfinityRaider.AgriCraft.api.API;
import com.InfinityRaider.AgriCraft.api.APIBase;
import com.InfinityRaider.AgriCraft.api.v1.APIv1;
import com.InfinityRaider.AgriCraft.farming.cropplant.CropPlantVanilla;
import jp.plusplus.ir2.blocks.BlockCore;
import jp.plusplus.ir2.items.ItemCore;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.ItemSeeds;

/**
 * Created by plusplus_F on 2015/11/05.
 */
public class ForAgriCraft {
    public static void setup(){
        APIBase api= API.getAPI(1);
        if(api.getStatus().isOK() && api.getVersion()==1){
            APIv1 av1=(APIv1)api;
            av1.registerCropPlant(new CropPlantVanilla((BlockCrops) BlockCore.cropCotton, (ItemSeeds) ItemCore.seedCotton));
            av1.registerCropPlant(new CropPlantVanilla((BlockCrops)BlockCore.cropWheatGlow, (ItemSeeds)ItemCore.seedWheatGlow));
            av1.registerCropPlant(new CropPlantMelon());
            av1.registerCropPlant(new CropPlantVanilla((BlockCrops)BlockCore.cropPotatoQuartz, (ItemSeeds)ItemCore.potatoQuartz));
            av1.registerCropPlant(new CropPlantReed());
        }
    }
}

package jp.plusplus.ir2.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by plusplus_F on 2015/03/09.
 */
public class ContainerFackingDummy extends Container{
    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return false;
    }
}

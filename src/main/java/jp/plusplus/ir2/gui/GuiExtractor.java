package jp.plusplus.ir2.gui;

import jp.plusplus.ir2.api.TileEntityMachineBase;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;

/**
 * Created by plusplus_F on 2015/06/27.
 */
public class GuiExtractor extends GuiProcessor {
    public GuiExtractor(Container container, TileEntityMachineBase tileEntity, ISidedInventory inventory) {
        super(container, tileEntity, inventory);
    }
}

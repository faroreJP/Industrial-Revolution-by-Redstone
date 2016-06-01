package jp.plusplus.ir2.blocks.model;
// Date: 2015/06/26 22:49:30
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCableToCableSide extends ModelBase implements IColored {
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;

    public ModelCableToCableSide(int i) {
        textureWidth = 64;
        textureHeight = 32;

        float ssY=-5f;

        Shape1 = new ModelRenderer(this, 16*(i%8), 8*(i/4));
        Shape1.addBox(-1F, -1*(-1F+ssY), 1F, 2, 2, 6);
        Shape1.setRotationPoint(0F, 0F, 0F);
        Shape1.setTextureSize(64, 32);
        //core.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);

        Shape2 = new ModelRenderer(this, 10+16*(i%8), 8*(i/4));
        Shape2.addBox(-1F, -1*(-1F+ssY), 8F, 2, 2, 1);
        Shape2.setRotationPoint(0F, 0F, 0F);
        Shape2.setTextureSize(64, 32);
        //Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5);
        Shape1.render(f5);
        Shape2.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
    }

    @Override
    public void setColor(int meta) {
        Shape1.setTextureOffset(16*(meta&3), 8*(meta>>2));
    }
}
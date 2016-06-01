// Date: 2015/08/06 22:28:42
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package jp.plusplus.ir2.blocks.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelChunkLoader extends ModelBase {
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;
    ModelRenderer Shape9;

    public ModelChunkLoader() {
        textureWidth = 64;
        textureHeight = 32;

        Shape1 = new ModelRenderer(this, 0, 9);
        Shape1.addBox(-4F, -2F, -2F, 8, 2, 4);
        Shape1.setRotationPoint(0F, 0F, 0F);
        Shape1.setTextureSize(64, 32);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 15);
        Shape2.addBox(-6F, -14F, -2F, 12, 12, 5);
        Shape2.setRotationPoint(0F, 0F, 0F);
        Shape2.setTextureSize(64, 32);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 0, 0);
        Shape3.addBox(-6F, -3F, 0F, 12, 1, 1);
        Shape3.setRotationPoint(0F, 0F, -3F);
        Shape3.setTextureSize(64, 32);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 0, 0);
        Shape4.addBox(-6F, -14F, -3F, 12, 1, 1);
        Shape4.setRotationPoint(0F, 0F, 0F);
        Shape4.setTextureSize(64, 32);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 26, 0);
        Shape5.addBox(-6F, -13F, -3F, 1, 10, 1);
        Shape5.setRotationPoint(0F, 0F, 0F);
        Shape5.setTextureSize(64, 32);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, 0F);
        Shape6 = new ModelRenderer(this, 26, 0);
        Shape6.addBox(5F, -13F, -3F, 1, 10, 1);
        Shape6.setRotationPoint(0F, 0F, 0F);
        Shape6.setTextureSize(64, 32);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0F);
        Shape7 = new ModelRenderer(this, 4, 6);
        Shape7.addBox(-3F, -15F, -1F, 6, 1, 2);
        Shape7.setRotationPoint(0F, 0F, 0F);
        Shape7.setTextureSize(64, 32);
        Shape7.mirror = true;
        setRotation(Shape7, 0F, 0F, 0F);
        Shape8 = new ModelRenderer(this, 0, 2);
        Shape8.addBox(-0.5F, 0F, 0F, 1, 5, 1);
        Shape8.setRotationPoint(0F, -8F, -3F);
        Shape8.setTextureSize(64, 32);
        Shape8.mirror = true;
        setRotation(Shape8, 0F, 0F, 0F);
        Shape9 = new ModelRenderer(this, 4, 2);
        Shape9.addBox(-0.5F, 0F, 0F, 1, 3, 1);
        Shape9.setRotationPoint(0F, -8F, -3F);
        Shape9.setTextureSize(64, 32);
        Shape9.mirror = true;
        setRotation(Shape9, 0F, 0F, 3.141593F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5);
        Shape1.render(f5);
        Shape2.render(f5);
        Shape3.render(f5);
        Shape4.render(f5);
        Shape5.render(f5);
        Shape6.render(f5);
        Shape7.render(f5);
    }
    public void renderNeedles(float rate, float f5){
        float angle=12*2*3.141593f*rate;

        //長針
        Shape8.rotateAngleZ=angle;
        Shape8.render(f5);

        //短針 長針の1/12の速度で回る
        Shape9.rotateAngleZ=0.083334f*angle+3.141593F;
        Shape9.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
    }
}

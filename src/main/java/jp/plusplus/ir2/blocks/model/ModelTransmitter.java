// Date: 2015/08/16 23:51:32
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package jp.plusplus.ir2.blocks.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTransmitter extends ModelBase {
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape23;
    ModelRenderer Antenna;

    public ModelTransmitter() {
        textureWidth = 64;
        textureHeight = 64;
        setTextureOffset("Antenna.Shape7", 0, 35);
        setTextureOffset("Antenna.Shape8", 0, 33);
        setTextureOffset("Antenna.Shape9", 0, 48);
        setTextureOffset("Antenna.Shape10", 0, 48);
        setTextureOffset("Antenna.Shape11", 0, 0);
        setTextureOffset("Antenna.Shape12", 48, 0);
        setTextureOffset("Antenna.Shape13", 0, 0);
        setTextureOffset("Antenna.Shape14", 0, 8);
        setTextureOffset("Antenna.Shape15", 0, 8);
        setTextureOffset("Antenna.Shape16", 48, 0);
        setTextureOffset("Antenna.Shape17", 0, 0);
        setTextureOffset("Antenna.Shape18", 0, 0);
        setTextureOffset("Antenna.Shape19", 48, 8);
        setTextureOffset("Antenna.Shape20", 48, 0);
        setTextureOffset("Antenna.Shape21", 48, 8);
        setTextureOffset("Antenna.Shape22", 0, 8);


        Shape1 = new ModelRenderer(this, 0, 0);
        Shape1.addBox(8F, -1F, -8F, 16, 1, 16);
        Shape1.setRotationPoint(0F, 0F, 0F);
        Shape1.setTextureSize(64, 64);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 0);
        Shape2.addBox(8F, -16F, -8F, 16, 1, 16);
        Shape2.setRotationPoint(0F, 0F, 0F);
        Shape2.setTextureSize(64, 64);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 0, 17);
        Shape3.addBox(8F, -15F, -8F, 1, 14, 1);
        Shape3.setRotationPoint(0F, 0F, 0F);
        Shape3.setTextureSize(64, 64);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 0, 17);
        Shape4.addBox(8F, -15F, 7F, 1, 14, 1);
        Shape4.setRotationPoint(0F, 0F, 0F);
        Shape4.setTextureSize(64, 64);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 0, 17);
        Shape5.addBox(23F, -15F, -8F, 1, 14, 1);
        Shape5.setRotationPoint(0F, 0F, 0F);
        Shape5.setTextureSize(64, 64);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, 0F);
        Shape6 = new ModelRenderer(this, 0, 17);
        Shape6.addBox(23F, -15F, 7F, 1, 14, 1);
        Shape6.setRotationPoint(0F, 0F, 0F);
        Shape6.setTextureSize(64, 64);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0F);
        Shape23 = new ModelRenderer(this, 4, 35);
        Shape23.addBox(3F, -3F, 14F, 13, 3, 7);
        Shape23.setRotationPoint(0F, 0F, 0F);
        Shape23.setTextureSize(64, 64);
        Shape23.mirror = true;
        setRotation(Shape23, 0F, 0F, 0F);
        Antenna = new ModelRenderer(this, "Antenna");
        Antenna.setRotationPoint(8F, 0F, 17F);
        setRotation(Antenna, 0F, 0F, 0F);
        Antenna.mirror = true;
        Antenna.addBox("Shape7", 0F, -16F, 0F, 1, 13, 1);
        Antenna.addBox("Shape8", -13F, -17F, 0F, 27, 1, 1);
        Antenna.addBox("Shape9", 14F, -17F, -7F, 1, 1, 15);
        Antenna.addBox("Shape10", -14F, -17F, -7F, 1, 1, 15);
        Antenna.addBox("Shape11", 6F, -17F, -7F, 1, 1, 7);
        Antenna.addBox("Shape12", 6F, -17F, 1F, 1, 1, 7);
        Antenna.addBox("Shape13", 10F, -17F, 1F, 1, 1, 7);
        Antenna.addBox("Shape14", 2F, -17F, 1F, 1, 1, 7);
        Antenna.addBox("Shape15", 10F, -17F, -7F, 1, 1, 7);
        Antenna.addBox("Shape16", -6F, -17F, -7F, 1, 1, 7);
        Antenna.addBox("Shape17", -2F, -17F, -7F, 1, 1, 7);
        Antenna.addBox("Shape18", -6F, -17F, 1F, 1, 1, 7);
        Antenna.addBox("Shape19", -2F, -17F, 1F, 1, 1, 7);
        Antenna.addBox("Shape20", 2F, -17F, -7F, 1, 1, 7);
        Antenna.addBox("Shape21", -10F, -17F, -7F, 1, 1, 7);
        Antenna.addBox("Shape22", -10F, -17F, 1F, 1, 1, 7);
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
        Shape23.render(f5);
        Antenna.render(f5);
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

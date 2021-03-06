// Date: 2015/07/12 19:52:26
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package jp.plusplus.ir2.blocks.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSower extends ModelBase {
    //fields
    ModelRenderer Piece1;
    ModelRenderer Shape7;
    ModelRenderer Shape8;

    public ModelSower() {
        textureWidth = 64;
        textureHeight = 32;
        setTextureOffset("Piece1.Shape4", 22, 0);
        setTextureOffset("Piece1.Shape5", 0, 8);
        setTextureOffset("Piece1.Shape6", 17, 8);
        setTextureOffset("Piece1.Shape1", 0, 0);
        setTextureOffset("Piece1.Shape2", 22, 0);
        setTextureOffset("Piece1.Shape3", 0, 0);

        Piece1 = new ModelRenderer(this, "Piece1");
        Piece1.setRotationPoint(0F, -1F, 4F);
        setRotation(Piece1, 0F, 0F, 0F);
        Piece1.mirror = true;
        Piece1.addBox("Shape4", 4F, -1.5F, 13F, 1, 1, 7);
        Piece1.addBox("Shape5", -4F, -0.5F, 13F, 8, 1, 7);
        Piece1.addBox("Shape6", -1F, -0.5F, 0F, 2, 1, 13);
        Piece1.addBox("Shape1", -5F, -1.5F, 20F, 10, 1, 1);
        Piece1.addBox("Shape2", -5F, -1.5F, 13F, 1, 1, 7);
        Piece1.addBox("Shape3", -5F, -1.5F, 12F, 10, 1, 1);

        Shape7 = new ModelRenderer(this, 0, 2);
        Shape7.addBox(-2F, -2F, 3F, 1, 2, 2);
        Shape7.setRotationPoint(0F, 0F, 0F);
        Shape7.setTextureSize(64, 32);
        Shape7.mirror = true;
        setRotation(Shape7, 0F, 0F, 0F);

        Shape8 = new ModelRenderer(this, 0, 2);
        Shape8.addBox(1F, -2F, 3F, 1, 2, 2);
        Shape8.setRotationPoint(0F, 0F, 0F);
        Shape8.setTextureSize(64, 32);
        Shape8.mirror = true;
        setRotation(Shape8, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5);
        Shape7.render(f5);
        Shape8.render(f5);
    }
    public void renderCatapult(float rate, float f5){
        float angle=0;

        if(rate<0.25f) angle=-2f+(185f+2f)*rate/0.25f;
        else if(rate<0.5f) angle=185f;
        else angle=185f-(185f+2f)*(rate-0.5f)/0.5f;

        Piece1.rotateAngleX=angle/180f*3.14f;
        Piece1.render(f5);
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

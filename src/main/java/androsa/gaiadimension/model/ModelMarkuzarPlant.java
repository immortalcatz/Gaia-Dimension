package androsa.gaiadimension.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * ModelMarkuzarPlant - Androsa
 * Created using Tabula 7.0.0
 */
public class ModelMarkuzarPlant extends ModelBase {
    public ModelRenderer stalkLow;
    public ModelRenderer stalkMid;
    public ModelRenderer stalkHigh;
    public ModelRenderer leafR;
    public ModelRenderer leafL;
    public ModelRenderer bulb;
    public ModelRenderer petal1;
    public ModelRenderer petal2;
    public ModelRenderer petal3;
    public ModelRenderer petal4;
    public ModelRenderer jewel;
    public ModelRenderer decor;

    public ModelMarkuzarPlant() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.petal1 = new ModelRenderer(this, 0, 19);
        this.petal1.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.petal1.addBox(0.0F, 0.0F, -1.5F, 9, 1, 3, 0.0F);
        this.setRotation(petal1, 0.0F, -0.7853981633974483F, 0.0F);
        this.petal2 = new ModelRenderer(this, 24, 19);
        this.petal2.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.petal2.addBox(0.0F, 0.0F, -1.5F, 9, 1, 3, 0.0F);
        this.setRotation(petal2, 0.0F, 0.7853981633974483F, 0.0F);
        this.stalkHigh = new ModelRenderer(this, 48, 0);
        this.stalkHigh.setRotationPoint(0.0F, -10.0F, 0.0F);
        this.stalkHigh.addBox(-1.5F, -8.0F, -1.5F, 3, 8, 3, 0.0F);
        this.leafL = new ModelRenderer(this, -7, 40);
        this.leafL.setRotationPoint(2.5F, -6.0F, 0.0F);
        this.leafL.addBox(0.0F, 0.0F, -3.5F, 10, 0, 7, 0.0F);
        this.setRotation(leafL, 0.3490658503988659F, 0.0F, 0.0F);
        this.leafR = new ModelRenderer(this, -7, 48);
        this.leafR.setRotationPoint(-2.5F, -6.0F, 0.0F);
        this.leafR.addBox(-10.0F, 0.0F, -3.5F, 10, 0, 7, 0.0F);
        this.setRotation(leafR, 0.3490658503988659F, 0.0F, 0.0F);
        this.petal3 = new ModelRenderer(this, 0, 23);
        this.petal3.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.petal3.addBox(0.0F, 0.0F, -1.5F, 9, 1, 3, 0.0F);
        this.setRotation(petal3, 0.0F, 2.356194490192345F, 0.0F);
        this.petal4 = new ModelRenderer(this, 24, 23);
        this.petal4.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.petal4.addBox(0.0F, 0.0F, -1.5F, 9, 1, 3, 0.0F);
        this.setRotation(petal4, 0.0F, -2.356194490192345F, 0.0F);
        this.decor = new ModelRenderer(this, -13, 27);
        this.decor.setRotationPoint(0.0F, -0.4F, 0.0F);
        this.decor.addBox(-6.5F, 0.0F, -6.5F, 13, 0, 13, 0.0F);
        this.jewel = new ModelRenderer(this, 26, 35);
        this.jewel.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.jewel.addBox(-1.0F, -3.0F, -1.0F, 2, 3, 2, 0.0F);
        this.stalkLow = new ModelRenderer(this, 0, 0);
        this.stalkLow.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.stalkLow.addBox(-3.5F, -12.0F, -3.5F, 7, 12, 7, 0.0F);
        this.bulb = new ModelRenderer(this, 44, 11);
        this.bulb.setRotationPoint(0.0F, -9.0F, 0.0F);
        this.bulb.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.setRotation(bulb, 1.1344640137963142F, 0.0F, 0.0F);
        this.stalkMid = new ModelRenderer(this, 28, 0);
        this.stalkMid.setRotationPoint(0.0F, -12.0F, 0.0F);
        this.stalkMid.addBox(-2.5F, -10.0F, -2.5F, 5, 10, 5, 0.0F);
        this.bulb.addChild(this.petal1);
        this.bulb.addChild(this.petal2);
        this.stalkMid.addChild(this.stalkHigh);
        this.stalkMid.addChild(this.leafL);
        this.stalkMid.addChild(this.leafR);
        this.bulb.addChild(this.petal3);
        this.bulb.addChild(this.petal4);
        this.bulb.addChild(this.decor);
        this.bulb.addChild(this.jewel);
        this.stalkHigh.addChild(this.bulb);
        this.stalkLow.addChild(this.stalkMid);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.stalkLow.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotation(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity par7Entity) {

        //Oh dear god, this is unsettling to watch
        this.stalkLow.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount * 0.5F;
        this.stalkLow.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.stalkMid.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount * 0.5F;
        this.stalkMid.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.stalkHigh.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount * 0.5F;
        this.stalkHigh.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

        this.leafL.rotateAngleY = MathHelper.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount * 0.5F;
        this.leafL.rotateAngleY -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.leafR.rotateAngleZ = MathHelper.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount * 0.5F;
        this.leafR.rotateAngleZ -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
    }
}

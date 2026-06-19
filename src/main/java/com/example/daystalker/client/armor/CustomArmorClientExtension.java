package com.example.daystalker.client.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Supplies a custom HumanoidModel lazily (model is baked on first use,
 * not at item registration time when Minecraft instance is not ready).
 */
public class CustomArmorClientExtension implements IClientItemExtensions {

    private final ModelLayerLocation layerLocation;
    private final Function<net.minecraft.client.model.geom.ModelPart, HumanoidModel<LivingEntity>> modelFactory;
    private HumanoidModel<LivingEntity> model;

    public CustomArmorClientExtension(
            ModelLayerLocation layerLocation,
            Function<net.minecraft.client.model.geom.ModelPart, HumanoidModel<LivingEntity>> modelFactory) {
        this.layerLocation = layerLocation;
        this.modelFactory = modelFactory;
    }

    private HumanoidModel<LivingEntity> getModel() {
        if (model == null) {
            model = modelFactory.apply(
                    Minecraft.getInstance().getEntityModels().bakeLayer(layerLocation)
            );
        }
        return model;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull HumanoidModel<?> getHumanoidArmorModel(
            LivingEntity entity, ItemStack stack,
            EquipmentSlot slot, HumanoidModel<?> defaultModel) {
        HumanoidModel<LivingEntity> m = getModel();
        ((HumanoidModel) defaultModel).copyPropertiesTo((HumanoidModel) m);
        m.setAllVisible(false);

        if (m instanceof VikingBootsModel bootModel) {
            bootModel.setBootsVisible(slot == EquipmentSlot.FEET);
        }

        if (slot == EquipmentSlot.HEAD) {
            m.head.visible = true;
            m.hat.visible = true;
        } else if (slot == EquipmentSlot.CHEST) {
            m.body.visible = true;
            m.rightArm.visible = true;
            m.leftArm.visible = true;
        } else if (slot == EquipmentSlot.LEGS) {
            m.rightLeg.visible = true;
            m.leftLeg.visible = true;
        }

        return m;
    }
}

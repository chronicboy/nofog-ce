package btw.community.nofog.mixin;

import net.minecraft.src.EntityRenderer;
import net.minecraft.src.Minecraft;
import net.minecraft.src.Potion;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @Shadow private Minecraft mc;
    @Shadow private float farPlaneDistance;

    @Inject(method = "setupFog", at = @At("RETURN"))
    private void onSetupFog(int par1, float par2, CallbackInfo ci) {
        // Only modify land fog (not blindness, lava, or underwater)
        if (par1 != 999 && this.mc.thePlayer != null && !this.mc.thePlayer.isPotionActive(Potion.blindness)) {
            int fogMode = GL11.glGetInteger(GL11.GL_FOG_MODE);
            
            if (fogMode == GL11.GL_LINEAR) {
                // Create a tight fog wall in the final 1% of the render distance to hide the void
                GL11.glFogf(GL11.GL_FOG_START, this.farPlaneDistance * 0.99f);
                GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
            }
        }
    }
}

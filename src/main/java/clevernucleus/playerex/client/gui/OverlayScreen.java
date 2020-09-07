package clevernucleus.playerex.client.gui;

import java.util.function.Supplier;

import org.lwjgl.opengl.GL11;

import clevernucleus.playerex.client.event.GuiEvents;
import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.util.Util;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;

/**
 * Overlay screen object.
 */
public class OverlayScreen extends AbstractGui {
	private Supplier<Minecraft> mc;
	
	public OverlayScreen(Supplier<Minecraft> par0) {
		this.mc = par0;
	}
	
	/**
	 * @param par0 The player.
	 * @return The y-location of the health bar that should be rendered.
	 */
	private int healthLat(final PlayerEntity par0) {
		if(par0.isPotionActive(Effects.WITHER)) return 210;
		if(par0.isPotionActive(Effects.POISON)) return 202;
		if(par0.isPotionActive(Effects.ABSORPTION)) return 194;
		
		return 186;
	}
	
	/**
	 * Draws this screen object.
	 * @param par0 Is pre-text? True to draw before text, false to draw text/after text.
	 */
	public void draw(final boolean par0) {
		if(this.mc.get() == null) return;
		
		ClientPlayerEntity var0 = this.mc.get().player;
		
		if(var0 == null) return;
		
		MainWindow var1 = this.mc.get().getMainWindow();
		
		int varX = var1.getScaledWidth();
		int varY = var1.getScaledHeight();
		
		if(par0) {
			int var2 = (int)(78F / var0.getMaxHealth() * var0.getHealth());
			
			this.mc.get().getTextureManager().bindTexture(PlayerElementsScreen.GUI);
			this.blit((varX / 2) - 91, varY - 37, 0, 178, 78, 8);
			this.blit((varX / 2) - 91, varY - 37, 0, healthLat(var0), var2, 8);
			this.blit((varX / 2) - 91, varY - 27, 0, 166, 182, 3);
			
			Registry.ELEMENTS.apply(var0).ifPresent(var -> {
				int var3 = (int)(182F * Util.expCoeff((float)var.get(var0, Registry.LEVEL), (float)var.get(var0, Registry.EXPERIENCE)));
				
				if(GuiEvents.isAltDown()) {
					this.blit((varX / 2) - 91, varY - 27, 0, 169, var3, 3);
				} else {
					int var4 = var0.xpBarCap();
					
					if(var4 > 0) {
						int var5 = (int)(var0.experience * 183.0F);
						
						if(var5 > 0) {
							this.blit((varX / 2) - 91, varY - 27, 0, 172, var5, 3);
						}
					}
				}
			});
			
			if(var0.isRidingHorse()) {
				Entity var3 = var0.getRidingEntity();
				
				if(var3 instanceof LivingEntity) {
					LivingEntity var4 = (LivingEntity)var3;
					
					float var5 = var0.getHorseJumpPower();
					int var6 = (int)(var5 * 183.0F);
					int var7 = (int)(78F / var4.getMaxHealth() * var4.getHealth());
					
					this.blit((varX / 2) - 91, varY - 46, 0, 178, 78, 8);
					this.blit((varX / 2) - 91, varY - 46, 0, 186, var7, 8);
					
					if(var6 > 0) {
						this.blit((varX / 2) - 91, varY - 27, 0, 175, var6, 3);
					}
				}
			}
			
			this.mc.get().getTextureManager().bindTexture(GUI_ICONS_LOCATION);
			
			boolean var3 = var0.isPotionActive(Effects.HUNGER);
			int var4 = (int)(100F * Math.max((float)var0.getAir(), 0F) / (float)var0.getMaxAir());
			
			this.blit((varX / 2) + 12, varY - 38, var3 ? 133 : 16, 27, 9, 9);
			this.blit((varX / 2) + 12, varY - 38, var3 ? 88 : 52, 27, 9, 9);
			this.blit((varX / 2) + 44, varY - 38, 34, 9, 9, 9);
			
			if(var4 < 100) {
				this.blit((varX / 2) + 75, varY - 38, 16, 18, 9, 9);
			}
		} else {
			FontRenderer var2 = this.mc.get().fontRenderer;
			String var3 = Util.FORMAT.apply("#.##").format(var0.getHealth() + var0.getAbsorptionAmount()) + "/" + Util.FORMAT.apply("#.##").format(var0.getMaxHealth());
			String var4 = "x" + var0.getTotalArmorValue();
			String var5 = (int)(100F * (float)var0.getFoodStats().getFoodLevel() / 20F) + "%";
			int var6 = (int)(100F * Math.max((float)var0.getAir(), 0F) / (float)var0.getMaxAir());
			int var7 = (varX - var2.getStringWidth(var3)) / 2;
			
			GL11.glPushMatrix();
			GL11.glScalef(0.8F, 0.8F, 0.8F);
			
			var2.drawString(var3, 1.25F * (var7 - 48), 1.25F * (varY - 36F), 0xFFFFFF);
			var2.drawString(var4, 1.25F * ((varX / 2) + 54), 1.25F * (varY - 36F), 0xFFFFFF);
			var2.drawString(var5, 1.25F * ((varX / 2) + 22), 1.25F * (varY - 36F), 0xFFFFFF);
			
			if(var6 < 100) {
				var2.drawString(var6 + "%", 1.25F * ((varX / 2) + 85), 1.25F * (varY - 36F), 0xFFFFFF);
			}
			
			if(var0.isRidingHorse()) {
				Entity var8 = var0.getRidingEntity();
				
				if(var8 instanceof LivingEntity) {
					LivingEntity var9 = (LivingEntity)var8;
					String var10 = Util.FORMAT.apply("#.##").format(var9.getHealth()) + "/" + Util.FORMAT.apply("#.##").format(var9.getMaxHealth());
					int var11 = (varX - var2.getStringWidth(var10)) / 2;
					
					var2.drawString(var10, 1.25F * (var11 - 48), 1.25F * (varY - 45F), 0xFFFFFF);
				}
			}
			
			GL11.glPopMatrix();
			
			Registry.ELEMENTS.apply(var0).ifPresent(var -> {
				int var8 = 0, var9 = 0, var10 = varY - 36;
				
				if(GuiEvents.isAltDown()) {
					var8 = (int)var.get(var0, Registry.LEVEL);
					var9 = 16759296;
				} else {
					var8 = var0.experienceLevel;
					var9 = 8453920;
				}
				
				if(var8 <= 0) return;
				
				String var11 = "" + var8;
				int var12 = (varX - var2.getStringWidth(var11)) / 2;
				
				var2.drawString(var11, (float)(var12 + 1), (float)var10, 0);
				var2.drawString(var11, (float)(var12 - 1), (float)var10, 0);
				var2.drawString(var11, (float)var12, (float)(var10 + 1), 0);
				var2.drawString(var11, (float)var12, (float)(var10 - 1), 0);
				var2.drawString(var11, (float)var12, (float)var10, var9);
			});
		}
	}
}

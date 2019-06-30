/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 8:34:01 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.PersistentVariableHelper;
import vazkii.botania.client.core.helper.FontHelper;
import vazkii.botania.client.gui.lexicon.GuiLexiconIndex;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class GuiButtonInvisible extends GuiButtonLexicon {

	private static final ResourceLocation dogResource = new ResourceLocation(LibResources.GUI_DOG);
	private static final IPressable ON_PRESS = b -> {
		GuiButtonInvisible button = (GuiButtonInvisible) b;
		if (button.dog) {
			button.enableDog = true;
			PersistentVariableHelper.dog = true;
			PersistentVariableHelper.saveSafe();
		} else {
			// todo 1.13 is this the right place? moved from GuiLexiconIndex.actionPerformed
			int index = id + button.gui.page * 12;
			button.gui.openEntry(index);
		}
	};

	private final GuiLexiconIndex gui;
	public ItemStack displayStack = ItemStack.EMPTY;
	public boolean dog = false;
	private float timeHover = 0;

	private boolean enableDog = false;
	private double dogPos = 0;

	public GuiButtonInvisible(GuiLexiconIndex gui, int x, int y, int width, int height, String text) {
		super(x, y, width, height, text, ON_PRESS);
		this.gui = gui;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		if(enableDog) {
			dogPos += ClientTickHandler.delta * 10;

			Minecraft.getInstance().textureManager.bindTexture(dogResource);
			float f = 1F / 64F;
			GlStateManager.translated(dogPos, 0, 0);
			GlStateManager.color4f(1F, 1F, 1F, 1F);
			vazkii.botania.client.core.helper.RenderHelper.drawTexturedModalRect(0, y, zLevel + 10, dogPos % 100 < 50 ? 23 : 0, 0, 23, 19, f, f);
			x = (int) Math.max(x, dogPos + 10);

			GlStateManager.translated(-dogPos, 0, 0);
		}

		isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		int k = getYImage(isHovered());
		boolean showStack = !displayStack.isEmpty() && !getMessage().isEmpty();

		if(!getMessage().isEmpty() && k == 2) {
			timeHover = Math.min(5, timeHover + gui.timeDelta);
			gui.setHoveredButton(this);
		} else timeHover = Math.max(0, timeHover - gui.timeDelta);

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GlStateManager.disableAlphaTest();
		int color = 0;
		String format = FontHelper.getFormatFromString(getMessage());
		if(format.length() > 1) {
			char key = format.charAt(format.length() - 1);
			if(key == 'o' && format.length() > 3)
				key = format.charAt(1);

			for(TextFormatting ecf : TextFormatting.class.getEnumConstants())
				if(ecf.toString().indexOf(ecf.toString().length() - 1) == key) {
					if(ecf.ordinal() > 15)
						ecf = TextFormatting.BLACK;
					color = LibMisc.CONTROL_CODE_COLORS[ecf.ordinal()];
					break;
				}
		}

		int maxalpha = 0x22;
		int alpha = Math.min(maxalpha, (int) (timeHover / 4 * maxalpha));
		fill(x - 5, y, (int) (x - 5 + timeHover * 24), y + height, alpha << 24 | color);
		GlStateManager.enableAlphaTest();

		Minecraft.getInstance().fontRenderer.drawString(getMessage(), x + (showStack ? 7 : 0), y + (height - 8) / 2, 0);

		if(showStack) {
			GlStateManager.scalef(0.5F, 0.5F, 0.5F);
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableRescaleNormal();
			Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(displayStack, x * 2 - 6, y * 2 + 4);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.enableBlend();
		}
		GlStateManager.popMatrix();
	}

}

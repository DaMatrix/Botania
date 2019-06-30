/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 2, 2015, 6:01:26 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.gui.lexicon.GuiLexiconHistory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonHistory extends GuiButtonLexicon {

	final GuiLexicon gui;

	public GuiButtonHistory(int x, int y, String str, GuiLexicon gui) {
		super(x, y, gui.bookmarkWidth(str) + 5, 11, str, b -> {
			Minecraft.getInstance().displayGuiScreen(new GuiLexiconHistory());
			ClientTickHandler.notifyPageChange();
		});
		this.gui = gui;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		gui.drawBookmark(x, y, getMessage(), false);
		isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		int k = getYImage(isHovered());

		List<String> tooltip = new ArrayList<>();
		tooltip.add(I18n.format("botaniamisc.historyLong"));
		tooltip.add(TextFormatting.GRAY + I18n.format("botaniamisc.historyDesc"));

		int tooltipY = (tooltip.size() + 1) * 5;
		if(k == 2)
			RenderHelper.renderTooltip(mouseX, mouseY + tooltipY, tooltip);
	}

}

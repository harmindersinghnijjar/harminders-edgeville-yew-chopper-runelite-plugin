package net.runelite.client.plugins.harminder;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;

public class BankButtonOverlay extends Overlay {
    private final Client client;
    private final Color[] colors = new Color[]{
            Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE,
            Color.PINK, Color.CYAN, Color.MAGENTA, Color.GRAY, Color.DARK_GRAY,
            Color.LIGHT_GRAY, new Color(128, 0, 128), new Color(255, 165, 0),
            new Color(173, 216, 230), new Color(0, 100, 0), new Color(139, 0, 139),
            new Color(255, 20, 147), new Color(0, 255, 255), new Color(255, 0, 255),
            new Color(70, 130, 180)
    };

    @Inject
    public BankButtonOverlay(Client client) {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.client = client;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        // Specifically fill the close button only the right most 30 pixels
        Widget closeButton = client.getWidget(WidgetID.BANK_GROUP_ID, 3); // Assuming 3 is the close button ID
        if (closeButton != null && !closeButton.isHidden()) {
            Rectangle bounds = closeButton.getBounds();
            graphics.setColor(Color.RED); // Set color for the close button
            graphics.fillRect(bounds.x + bounds.width - 25, bounds.y, 25, bounds.height);
        }
        // Specifically fill the deposit button
        Widget depositButton = client.getWidget(WidgetID.BANK_GROUP_ID, 42); // Assuming 42 is the deposit button ID
        if (depositButton != null && !depositButton.isHidden()) {
            Rectangle bounds = depositButton.getBounds();
            graphics.setColor(Color.BLUE); // Set color for the deposit button
            graphics.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        return null;
    }
}

package net.runelite.client.plugins.harminder;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.ObjectComposition;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;

public class GameObjectOverlay extends Overlay {

    private final Client client;

    private final HarmindersPlugin plugin;

    @Inject
    public GameObjectOverlay(Client client, HarmindersPlugin plugin) {
        setPosition(OverlayPosition.DYNAMIC);
        this.client = client;
        this.plugin = plugin;
    }

    public ObjectComposition getObjectComposition(int objectId) {
        return client == null ? null : client.getObjectDefinition(objectId);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        for (GameObject gameObject : plugin.getNearbyGameObjects()) {
            gameObject.getLocalLocation();
            Shape objectHull = gameObject.getConvexHull();
            if (objectHull != null) {
                String objectName = getObjectComposition(gameObject.getId()).getName();

                // Check for Yew trees
                if (objectName.equals("Yew tree")) {
                    drawOverlay(graphics, objectHull, Color.RED, objectName);
                }

                // Check for Bank booths
                if (objectName.equals("Bank booth")) {
                    drawOverlay(graphics, objectHull, Color.BLUE, objectName);
                }
            }
        }
        return null;
    }

    public void drawOverlay(Graphics2D graphics, Shape objectHull, Color color, String objectName) {
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255));
        graphics.draw(objectHull);
        graphics.fill(objectHull);

        Point textLocation = objectHull.getBounds().getLocation();
        graphics.setColor(Color.WHITE);
        graphics.drawString(objectName, textLocation.x, textLocation.y);
    }
}

//package net.runelite.client.plugins.harminder;
//
//import net.runelite.api.*;
//import net.runelite.api.coords.WorldPoint;
//import net.runelite.api.events.GameTick;
//import net.runelite.api.widgets.Widget;
//import net.runelite.api.widgets.WidgetInfo;
//import net.runelite.client.eventbus.Subscribe;
//import net.runelite.client.plugins.Plugin;
//import net.runelite.client.plugins.PluginDescriptor;
//import net.runelite.client.ui.overlay.OverlayManager;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.inject.Inject;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@PluginDescriptor(
//        name = "Harminder's Plugin",
//        description = "A plugin that captures various game attributes and sends them to an API"
//)
//public class HarmindersPlugin extends Plugin {
//    private static final Logger log = LoggerFactory.getLogger(HarmindersPlugin.class);
//    private static final String API_BASE_URL = "http://127.0.0.1:42069";
//    private static final List<Integer> WOODCUTTING_ANIMATIONS = Arrays.asList(
//            AnimationID.WOODCUTTING_BRONZE, AnimationID.WOODCUTTING_IRON,
//            AnimationID.WOODCUTTING_STEEL, AnimationID.WOODCUTTING_BLACK,
//            AnimationID.WOODCUTTING_MITHRIL, AnimationID.WOODCUTTING_ADAMANT,
//            AnimationID.WOODCUTTING_RUNE, AnimationID.WOODCUTTING_GILDED,
//            AnimationID.WOODCUTTING_DRAGON, AnimationID.WOODCUTTING_DRAGON_OR,
//            AnimationID.WOODCUTTING_INFERNAL, AnimationID.WOODCUTTING_3A_AXE,
//            AnimationID.WOODCUTTING_CRYSTAL, AnimationID.WOODCUTTING_TRAILBLAZER,
//            AnimationID.WOODCUTTING_2H_BRONZE, AnimationID.WOODCUTTING_2H_IRON,
//            AnimationID.WOODCUTTING_2H_STEEL, AnimationID.WOODCUTTING_2H_BLACK,
//            AnimationID.WOODCUTTING_2H_MITHRIL, AnimationID.WOODCUTTING_2H_ADAMANT,
//            AnimationID.WOODCUTTING_2H_RUNE, AnimationID.WOODCUTTING_2H_DRAGON,
//            AnimationID.WOODCUTTING_2H_CRYSTAL, AnimationID.WOODCUTTING_2H_3A
//    );
//
//    @Inject
//    private OverlayManager overlayManager;
//
//    @Inject
//    private ItemContainerOverlay itemContainerOverlay;
//
//    @Inject
//    private Client client;
//
//    @Override
//    protected void startUp() {
//        overlayManager.add(itemContainerOverlay);
//        log.info("Harminder's Plugin started!");
//    }
//
//    @Override
//    protected void shutDown() {
//        overlayManager.remove(itemContainerOverlay);
//        log.info("Harminder's Plugin stopped!");
//    }
//
//    @Subscribe
//    public void onGameTick(GameTick tick) {
//        Player localPlayer = client.getLocalPlayer();
//        if (localPlayer == null || client.getGameState() != GameState.LOGGED_IN) {
//            return; // Exit if player is null or not logged in
//        }
//        updatePlayerStatus(localPlayer);
//        updateAndSendInventoryItems();
//        List<GameObject> nearbyObjects = getNearbyGameObjects();
//        List<NPC> nearbyNPCs = getNearbyNPCs();
//        // Additional logic if needed for nearbyObjects
//    }
//
//
//    public List<NPC> getNearbyNPCs() {
//        int maxDistance = 50; // Define the maximum distance for nearby NPCs
//        List<NPC> nearbyNPCs = new ArrayList<>();
//        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
//
//        for (NPC npc : client.getNpcs()) {
//            if (npc != null && npc.getWorldLocation().distanceTo(playerLocation) <= maxDistance) {
//                nearbyNPCs.add(npc);
//            }
//        }
//
//        // Optional: Log or format the list of NPCs if needed
//        String formattedNPCList = formatNPCList(nearbyNPCs);
//        log.info("Nearby NPCs: " + formattedNPCList);
//
//        return nearbyNPCs;
//    }
//
//    private String formatNPCList(List<NPC> npcs) {
//        return npcs.stream()
//                .map(npc -> {
//                    String name = npc.getName();
//                    return String.format("[%s, %s]", npc.getId(), name);
//                })
//                .collect(Collectors.joining(", "));
//    }
//
//    public NPCComposition getNpcComposition(int npcId) {
//        return client == null ? null : client.getNpcDefinition(npcId);
//    }
//
//
//    public List<GameObject> getNearbyGameObjects() {
//        int maxDistance = 50; // Define the maximum distance for nearby objects
//        List<GameObject> nearbyObjects = new ArrayList<>();
//        Scene scene = client.getScene();
//        Tile[][][] tiles = scene.getTiles();
//        int z = client.getPlane();
//        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
//
//        // Iterate over all tiles within the scene
//        for (int x = 0; x < Constants.SCENE_SIZE; x++) {
//            for (int y = 0; y < Constants.SCENE_SIZE; y++) {
//                Tile tile = tiles[z][x][y];
//                if (tile != null) {
//                    for (GameObject gameObject : tile.getGameObjects()) {
//                        if (gameObject != null && gameObject.getWorldLocation().distanceTo(playerLocation) <= maxDistance) {
//                            nearbyObjects.add(gameObject);
//                        }
//                    }
//                }
//            }
//        }
//
//        String formattedGameObjectList = formatGameObjectList(nearbyObjects);
//        // log.info("Nearby game objects: " + formattedGameObjectList);
//        return nearbyObjects;
//    }
//
//    public ObjectComposition getObjectComposition(int objectId) {
//        return client == null ? null : client.getObjectDefinition(objectId);
//    }
//
//    private String formatGameObjectList(List<GameObject> gameObjects) {
//        return gameObjects.stream()
//                .map(gameObject -> {
//                    ObjectComposition objectComposition = getObjectComposition(gameObject.getId());
//                    String name = objectComposition != null ? objectComposition.getName() : "Unknown";
//                    return String.format("[%s, %s]", gameObject.getId(), name);
//                })
//                .collect(Collectors.joining(", "));
//    }
//
//    private void updateAndSendInventoryItems() {
//        ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
//        if (inventory == null) {
//            return;
//        }
//
//        Item[] items = inventory.getItems();
//        Map<Integer, String> inventoryMap = new HashMap<>();
//        for (int i = 0; i < items.length; i++) {
//            Item item = items[i];
//            String itemName = getItemName(item.getId());
//            if (!itemName.equals("Empty Slot")) {
//                inventoryMap.put(i + 1, itemName + " (" + item.getQuantity() + ")");
//            } else {
//                inventoryMap.put(i + 1, itemName);
//            }
//        }
//
//        String jsonPayload = convertMapToJson(inventoryMap);
//        log.info("Inventory items: " + jsonPayload);
//        sendToAPI("/updateInventory", jsonPayload);
//    }
//
//    private String getItemName(int itemId) {
//        // Adjust this method to return "Empty Slot" if the item is not valid
//        if (client == null || itemId <= 0) {
//            return "Empty Slot";
//        }
//        ItemComposition itemComposition = client.getItemDefinition(itemId);
//        return itemComposition == null ? "Empty Slot" : itemComposition.getName();
//    }
//
//    private String convertMapToJson(Map<Integer, String> map) {
//        StringBuilder json = new StringBuilder("{");
//        for (Map.Entry<Integer, String> entry : map.entrySet()) {
//            json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
//        }
//        if (json.length() > 1) {
//            json.deleteCharAt(json.length() - 1); // Remove the trailing comma if necessary
//        }
//        json.append("}");
//        return json.toString();
//    }
//
//
//    private boolean isBankOpen() {
//        Widget bankWidget = client.getWidget(WidgetInfo.BANK_CONTAINER);
//        return bankWidget != null && !bankWidget.isHidden();
//    }
//
//    private void updatePlayerStatus(Player localPlayer) {
//        int animationId = localPlayer.getAnimation();
//        int poseAnimationId = localPlayer.getPoseAnimation();
//        String woodcuttingStatus = WOODCUTTING_ANIMATIONS.contains(animationId) ? "Woodcutting" : "Not Woodcutting";
//        String movementStatus = (poseAnimationId != 808) ? "Moving" : "Not Moving";
//        String idleStatus = (animationId == AnimationID.IDLE) ? "Idle" : "Animating";
//        // Additional status updates based on animationId
//
//
//        String tileLocation = String.format("X: %d, Y: %d, Z: %d",
//                localPlayer.getWorldLocation().getX(),
//                localPlayer.getWorldLocation().getY(),
//                localPlayer.getWorldLocation().getPlane());
//
//        boolean bankOpen = isBankOpen();
//
//        // Log player status
//        log.info("Woodcutting status: " + woodcuttingStatus);
//        log.info("Movement status: " + movementStatus);
//        log.info("Idle status: " + idleStatus);
//        log.info("Tile location: " + tileLocation);
//        log.info("Bank open: " + bankOpen);
//
//        // Send status to API
//        sendToAPI("/updatePlayerStatus", String.format(
//                "{\"WoodcuttingStatus\": \"%s\", \"MovementStatus\": \"%s\", \"IdleStatus\": \"%s\", \"TileLocation\": \"%s\", \"BankOpen\": %b}",
//                woodcuttingStatus, movementStatus, idleStatus, tileLocation, bankOpen));
//    }
//
//    private void sendToAPI(String endpoint, String jsonPayload) {
//        try {
//            URL url = new URL(API_BASE_URL + endpoint);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("POST");
//            con.setDoOutput(true);
//            con.setRequestProperty("Content-Type", "application/json");
//
//            try (OutputStream os = con.getOutputStream()) {
//                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
//                os.write(input, 0, input.length);
//            }
//
//            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
//                StringBuilder response = new StringBuilder();
//                String responseLine;
//                while ((responseLine = br.readLine()) != null) {
//                    response.append(responseLine.trim());
//                }
//                log.info("API Response: " + response.toString());
//            }
//
//            con.disconnect();
//        } catch (Exception e) {
//            log.error("Error sending data to API", e);
//        }
//    }
//}

package net.runelite.client.plugins.harminder;

import com.google.gson.Gson;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@PluginDescriptor(
        name = "Harminder's Plugin",
        description = "A plugin that captures various game attributes and sends them to an API"
)
public class HarmindersPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(HarmindersPlugin.class);
    private static final String API_BASE_URL = "http://127.0.0.1:42069";
    private static final int MAX_DISTANCE = 50;
    private static final List<Integer> WOODCUTTING_ANIMATIONS = Arrays.asList(
            AnimationID.WOODCUTTING_BRONZE, AnimationID.WOODCUTTING_IRON,
            AnimationID.WOODCUTTING_STEEL, AnimationID.WOODCUTTING_BLACK,
            AnimationID.WOODCUTTING_MITHRIL, AnimationID.WOODCUTTING_ADAMANT,
            AnimationID.WOODCUTTING_RUNE, AnimationID.WOODCUTTING_GILDED,
            AnimationID.WOODCUTTING_DRAGON, AnimationID.WOODCUTTING_DRAGON_OR,
            AnimationID.WOODCUTTING_INFERNAL, AnimationID.WOODCUTTING_3A_AXE,
            AnimationID.WOODCUTTING_CRYSTAL, AnimationID.WOODCUTTING_TRAILBLAZER,
            AnimationID.WOODCUTTING_2H_BRONZE, AnimationID.WOODCUTTING_2H_IRON,
            AnimationID.WOODCUTTING_2H_STEEL, AnimationID.WOODCUTTING_2H_BLACK,
            AnimationID.WOODCUTTING_2H_MITHRIL, AnimationID.WOODCUTTING_2H_ADAMANT,
            AnimationID.WOODCUTTING_2H_RUNE, AnimationID.WOODCUTTING_2H_DRAGON,
            AnimationID.WOODCUTTING_2H_CRYSTAL, AnimationID.WOODCUTTING_2H_3A
    );

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private GameObjectOverlay gameObjectOverlay;

    @Inject
    private BankButtonOverlay bankButtonOverlay;

    @Inject
    private Client client;

    @Override
    protected void startUp() {
        overlayManager.add(bankButtonOverlay);
        overlayManager.add(gameObjectOverlay);
        log.info("Harminder's Plugin started!");
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(bankButtonOverlay);
        overlayManager.add(gameObjectOverlay);
        log.info("Harminder's Plugin stopped!");
    }

    @Subscribe
    public void onGameTick(GameTick tick) {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null || client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        updatePlayerStatus(localPlayer);
        getInventoryItems();
        List<GameObject> nearbyObjects = getNearbyGameObjects();
        List<NPC> nearbyNPCs = getNearbyNPCs();
    }

    private List<NPC> getNearbyNPCs() {
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        return client.getNpcs().stream()
                .filter(npc -> npc != null && npc.getWorldLocation().distanceTo(playerLocation) <= MAX_DISTANCE)
                .collect(Collectors.toList());
    }

    private String formatNPCList(List<NPC> npcs) {
        return npcs.stream()
                .map(npc -> String.format("[%s, %s]", npc.getId(), npc.getName()))
                .collect(Collectors.joining(", "));
    }

    public List<GameObject> getNearbyGameObjects() {
        int maxDistance = 50; // Define the maximum distance for nearby objects
        List<GameObject> nearbyObjects = new ArrayList<>();
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();
        int z = client.getPlane();
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();

        // Iterate over all tiles within the scene
        for (int x = 0; x < Constants.SCENE_SIZE; x++) {
            for (int y = 0; y < Constants.SCENE_SIZE; y++) {
                Tile tile = tiles[z][x][y];
                if (tile != null) {
                    for (GameObject gameObject : tile.getGameObjects()) {
                        if (gameObject != null && gameObject.getWorldLocation().distanceTo(playerLocation) <= maxDistance) {
                            nearbyObjects.add(gameObject);
                        }
                    }
                }
            }
        }

        String formattedGameObjectList = formatGameObjectList(nearbyObjects);
        // log.info("Nearby game objects: " + formattedGameObjectList);
        return nearbyObjects;
    }

    private String formatGameObjectList(List<GameObject> gameObjects) {
        return gameObjects.stream()
                .map(gameObject -> {
                    ObjectComposition objectComposition = client.getObjectDefinition(gameObject.getId());
                    String name = objectComposition != null ? objectComposition.getName() : "Unknown";
                    return String.format("[%s, %s]", gameObject.getId(), name);
                })
                .collect(Collectors.joining(", "));
    }

    public List<Map<String, Object>> getInventoryItems() {
        ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
        if (inventory == null) {
            return null;
        }

        List<Map<String, Object>> itemList = new ArrayList<>();
        Item[] items = inventory.getItems();
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            String itemName = getItemName(item.getId());
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("SlotNumber", i + 1);
            itemMap.put("ItemName", itemName);
            itemMap.put("Quantity", item.getQuantity());
            itemList.add(itemMap);
        }

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(itemList);
        log.info("Inventory items: " + jsonPayload);
        sendToAPI("/updateInventory", jsonPayload);
        return itemList;
    }

    private String getItemName(int itemId) {
        if (client == null || itemId <= 0) {
            return "Empty Slot";
        }
        ItemComposition itemComposition = client.getItemDefinition(itemId);
        return itemComposition == null ? "Empty Slot" : itemComposition.getName();
    }

    private boolean isBankOpen() {
        Widget bankWidget = client.getWidget(WidgetInfo.BANK_CONTAINER);
        return bankWidget != null && !bankWidget.isHidden();
    }

    private void updatePlayerStatus(Player localPlayer) {
        int animationId = localPlayer.getAnimation();
        int poseAnimationId = localPlayer.getPoseAnimation();
        String woodcuttingStatus = WOODCUTTING_ANIMATIONS.contains(animationId) ? "Woodcutting" : "Not Woodcutting";
        String movementStatus = (poseAnimationId != 808) ? "Moving" : "Not Moving";
        String idleStatus = (animationId == AnimationID.IDLE) ? "Idle" : "Animating";
        String tileLocation = String.format("X: %d, Y: %d, Z: %d",
                localPlayer.getWorldLocation().getX(),
                localPlayer.getWorldLocation().getY(),
                localPlayer.getWorldLocation().getPlane());
        boolean bankOpen = isBankOpen();
        log.info("Woodcutting status: " + woodcuttingStatus);
        log.info("Movement status: " + movementStatus);
        log.info("Idle status: " + idleStatus);
        log.info("Tile location: " + tileLocation);
        log.info("Bank open: " + bankOpen);
        sendToAPI("/updatePlayerStatus", String.format(
                "{\"WoodcuttingStatus\": \"%s\", \"MovementStatus\": \"%s\", \"IdleStatus\": \"%s\", \"TileLocation\": \"%s\", \"BankOpen\": %b}",
                woodcuttingStatus, movementStatus, idleStatus, tileLocation, bankOpen));
    }

    private void sendToAPI(String endpoint, String jsonPayload) {
        try {
            URL url = new URL(API_BASE_URL + endpoint);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                log.info("API Response: " + response.toString());
            }
            con.disconnect();
        } catch (IOException e) {
            log.error("Error sending data to API", e);
        }
    }
}

package the_fireplace.chatcensor.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import the_fireplace.chatcensor.ChatCensor;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PlayerDataManager {
    private static HashMap<UUID, PlayerData> playerData = Maps.newHashMap();
    private static final File playerDataLocation = new File(ChatCensor.getMinecraftHelper().getServer().getWorld(0).getSaveHandler().getWorldDirectory(), "chatcensor/player");

    //region getters
    public static boolean getIgnoresCensor(UUID player) {
        return getPlayerData(player).ignoresCensor;
    }
    public static boolean isCensored(UUID player) {
        return getPlayerData(player).isCensored;
    }
    public static boolean getReceivedCensoredMessage(UUID player) {
        return getPlayerData(player).receivedCensoredMessage;
    }
    //endregion

    //region saved data setters
    public static void setIgnoresCensor(UUID player, boolean ignoresCensor) {
        getPlayerData(player).setIgnoresCensor(ignoresCensor);
    }
    public static void setCensored(UUID player, boolean isCensored) {
        getPlayerData(player).setIsCensored(isCensored);
    }
    public static void setReceivedCensoredMessage(UUID player, boolean receivedCensoredMessage) {
        getPlayerData(player).setReceivedCensoredMessage(receivedCensoredMessage);
    }

    /**
     * @return true if the target is muted after this
     */
    public static boolean toggleMute(UUID player, UUID target) {
        return getPlayerData(player).toggleMute(target);
    }

    /**
     * @return true if the target is muted by the given player
     */
    public static boolean hasMuted(UUID player, UUID target) {
        return getPlayerData(player).isMuted(target);
    }
    //endregion

    //region cached data setters
    public static void setShouldDisposeReferences(UUID player, boolean shouldDisposeReferences) {
        getPlayerData(player).shouldDisposeReferences = shouldDisposeReferences;
    }
    //endregion

    //region getPlayerData
    private static PlayerData getPlayerData(UUID player) {
        if(!playerData.containsKey(player))
            playerData.put(player, new PlayerData(player));
        return playerData.get(player);
    }
    //endregion

    //region save
    public static void save() {
        for(Map.Entry<UUID, PlayerData> entry : Sets.newHashSet(playerData.entrySet())) {
            entry.getValue().save();
            if(entry.getValue().shouldDisposeReferences)
                playerData.remove(entry.getKey());
        }
    }
    //endregion

    private static class PlayerData {
        private final File playerDataFile;
        private boolean isChanged, saving, shouldDisposeReferences = false;
        private boolean ignoresCensor, isCensored, receivedCensoredMessage;
        private final List<UUID> mutedPlayers = Lists.newArrayList();

        private PlayerData(UUID playerId) {
            playerDataFile = new File(playerDataLocation, playerId.toString()+".json");
            load(playerId);
        }

        private void load(UUID playerId) {
            if(!playerDataLocation.exists()) {
                playerDataLocation.mkdirs();
                return;
            }

            JsonParser jsonParser = new JsonParser();
            try {
                Object obj = jsonParser.parse(new FileReader(playerDataFile));
                if(obj instanceof JsonObject) {
                    JsonObject jsonObject = (JsonObject) obj;
                    ignoresCensor = jsonObject.has("ignoresCensor") && jsonObject.getAsJsonPrimitive("ignoresCensor").getAsBoolean();
                    isCensored = jsonObject.has("isCensored") && jsonObject.getAsJsonPrimitive("isCensored").getAsBoolean();
                    for(JsonElement e: jsonObject.has("muted") ? jsonObject.getAsJsonArray("muted") : new JsonArray())
                        mutedPlayers.add(UUID.fromString(e.getAsString()));
                }
            } catch (FileNotFoundException ignored) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void save() {
            if(!isChanged || saving)
                return;
            saving = true;
            new Thread(() -> {
                JsonObject obj = new JsonObject();
                obj.addProperty("ignoresCensor", ignoresCensor);
                obj.addProperty("isCensored", isCensored);
                //Do not write receivedCensoredMessage, that doesn't need to persist.
                JsonArray muted = new JsonArray();
                for(UUID id: mutedPlayers)
                    muted.add(new JsonPrimitive(id.toString()));

                try {
                    FileWriter file = new FileWriter(playerDataFile);
                    file.write(new GsonBuilder().setPrettyPrinting().create().toJson(obj));
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                saving = isChanged = false;
            }).start();
        }

        public void setIgnoresCensor(boolean ignoresCensor) {
            if(this.ignoresCensor != ignoresCensor) {
                this.ignoresCensor = ignoresCensor;
                isChanged = true;
            }
        }

        public void setIsCensored(boolean isCensored) {
            if(this.isCensored != isCensored) {
                this.isCensored = isCensored;
                isChanged = true;
            }
        }

        public void setReceivedCensoredMessage(boolean receivedCensoredMessage) {
            if(this.receivedCensoredMessage != receivedCensoredMessage) {
                this.receivedCensoredMessage = receivedCensoredMessage;
                //Do not save receivedCensoredMessage, that doesn't need to persist.
            }
        }

        /**
         * @return true if the target is muted after doing this
         */
        public boolean toggleMute(UUID target) {
            if(mutedPlayers.remove(target))
                return false;
            mutedPlayers.add(target);
            return true;
        }

        public boolean isMuted(UUID target) {
            return mutedPlayers.contains(target);
        }
    }
}

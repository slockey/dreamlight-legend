package com.slockey.dreamlightlegend.gameobjects.map;

import java.util.Arrays;

public class Exit {

    protected String id;
    protected ExitState exitState;
    protected String description;
    protected String[] linkedRoomIds = new String[2];

    public Exit() {
        this.id = IdGenerator.getRandomIdString();
        this.exitState = ExitState.NONE;
        this.description = "You stare at unyielding stone. There is no exit here.";
        // default the linked roomIds to null ids
        Arrays.fill(linkedRoomIds, IdGenerator.getNullIdString());
    }
    
    public Exit(String linkedRoomId) {
        this();
        addLinkedRoomId(linkedRoomId);
    }

    public String getId() {
        return id;
    }

    public ExitState getExitState() {
        return exitState;
    }

    public String getDescriptionString() {
        return description;
    }

    public String leadsToRoom(String roomId) {
        // verify - the passed roomId is in the array
        if (linkedRoomIds[0].equals(roomId)) {
            return linkedRoomIds[1];
        } else if (linkedRoomIds[1].equals(roomId)) {
            return linkedRoomIds[0];
        }
        // return a null id
        return IdGenerator.getNullIdString();
    }

    // NOTE: This means we cannot null out linked rooms via this method
    public boolean addLinkedRoomId(String roomId) {
        if (linkedRoomIds[0].equals(IdGenerator.getNullIdString())) {
            linkedRoomIds[0] = roomId;
            return true;
        }
        if (linkedRoomIds[1].equals(IdGenerator.getNullIdString())) {
            linkedRoomIds[1] = roomId;
            return true;
        }
        return false;
    }

    public boolean setLinkedRoomIds(String[] linkedRoomIds) {
        if ((linkedRoomIds != null) && (linkedRoomIds.length == 2)) {
            if (!linkedRoomIds[0].isEmpty() && !linkedRoomIds[2].isEmpty()) {
                this.linkedRoomIds = linkedRoomIds;
                return true;
            }
        }
        return false;
    }

    public void setExitState(ExitState exitState) {
        this.exitState = exitState;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

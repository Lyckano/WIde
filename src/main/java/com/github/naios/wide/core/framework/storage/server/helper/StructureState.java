package com.github.naios.wide.core.framework.storage.server.helper;

public enum StructureState
{
    /**
     * Is pushed on the history stack so we know the time when the value is in sync with the database
     */
    STATE_IN_SYNC,

    /**
     * Is used in the Structure to mark that it has changed.
     */
    STATE_UPDATED,

    /**
     * Is used if the current database state is unknown. (due to connection lost)
     */
    STATE_UNKNOWN,

    /**
     * Is pushed on the history stack so we know if the value was created
     */
    STATE_CREATED,

    /**
     * Is pushed on the history stack so we know if the value was deleted
     */
    STATE_DELETED;

    public boolean isInSync()
    {
        return this.equals(STATE_IN_SYNC);
    }
}

package com.github.naios.wide.core.framework.entities.server;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.StringProperty;

import com.github.naios.wide.core.framework.game.UnitClass;
import com.github.naios.wide.core.framework.game.UnitFlags;
import com.github.naios.wide.core.framework.storage.StorageName;
import com.github.naios.wide.core.framework.storage.server.ServerStorage;
import com.github.naios.wide.core.framework.storage.server.ServerStorageKey;
import com.github.naios.wide.core.framework.storage.server.ServerStorageStructure;
import com.github.naios.wide.core.framework.storage.server.types.EnumProperty;
import com.github.naios.wide.core.framework.storage.server.types.FlagProperty;

@StorageName(name="creature_template")
public abstract class CreatureTemplate extends ServerStorageStructure
{
    public CreatureTemplate(final ServerStorage<CreatureTemplate> owner)
    {
        super(owner);
    }

    public abstract ReadOnlyIntegerProperty entry();

    public abstract StringProperty name();

    public abstract FlagProperty<UnitFlags> unit_flags();

    public abstract EnumProperty<UnitClass> unit_class();

    public static ServerStorageKey<CreatureTemplate> createKey(final int entry)
    {
        return new ServerStorageKey<CreatureTemplate>(entry);
    }
}

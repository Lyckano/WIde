package wide.scripts.test;

import wide.core.framework.storage.StorageStructure;

@StorageStructure(filename="Map.dbc")
public interface MapStructure
{
    public int getMapId();

    public String getName();
}

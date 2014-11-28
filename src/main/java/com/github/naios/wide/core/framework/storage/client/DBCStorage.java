package com.github.naios.wide.core.framework.storage.client;

import com.github.naios.wide.core.framework.storage.StorageException;

/**
 * Implementation of Blizzards DBC files as described in:
 * http://www.pxr.dk/wowdev/wiki/index.php?title=DBC
 */
public class DBCStorage<T extends ClientStorageStructure> extends ClientStorage<T>
{
    private final static int HEADER_SIZE = 20;

    private final static String MAGIC = "WDBC";

    protected final static String EXTENSION = ".dbc";

    public DBCStorage(final Class<? extends ClientStorageStructure> type) throws StorageException
    {
        super(type);
    }

    public DBCStorage(final Class<? extends ClientStorageStructure> type, final String path) throws ClientStorageException
    {
        super(type, path);
    }

    @Override
    protected int getHeaderSize()
    {
        return HEADER_SIZE;
    }

    @Override
    protected String getMagicSig()
    {
        return MAGIC;
    }

    @Override
    protected String getExtension()
    {
        return EXTENSION;
    }
}


/*
 * Copyright (c) 2013 - 2014 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.framework.internal.storage.name;

import com.github.naios.wide.framework.internal.storage.client.ClientStorageImpl;
import com.github.naios.wide.framework.internal.storage.client.ClientStorageSelector;
import com.github.naios.wide.framework.internal.storage.client.UnknownClientStorageStructure;
import com.github.naios.wide.framework.storage.client.ClientStorageException;
import com.github.naios.wide.framework.storage.client.ClientStoragePolicy;

public class ClientNameStorage extends NameStorage
{
    private final int entryColumn, nameColumn;

    private final String name;

    public ClientNameStorage(final String name, final int entryColumn, final int nameColumn)
    {
        this.name = name;
        this.entryColumn = entryColumn;
        this.nameColumn = nameColumn;

        setup();
    }

    @Override
    public void setup()
    {
        /*TODO
        WIde.getHooks().addListener(new HookListener(Hook.ON_CONFIG_LOADED, this)
        {
            @Override
            public void informed()
            {
                load();
            }
        });

        if (WIde.getConfig().isLoaded())
            load();*/
    }

    @Override
    public void load()
    {
        /*TODO
        if (WIde.getEnviroment().isTraceEnabled())
            System.out.println(String.format("Loading Client Namstorage: %s", name));*/

        try
        {
            final ClientStorageImpl<UnknownClientStorageStructure> storage =
                    new ClientStorageSelector<UnknownClientStorageStructure>
                            (ClientStorageImpl.getPathForStorage(name),
                                ClientStoragePolicy.POLICY_ESTIMATE_ONLY).select();

            final Object[][] objects = storage.asObjectArray();
            for (int i = 0; i < storage.getRecordsCount(); ++i)
                add((int)objects[i][entryColumn], (String)objects[i][nameColumn]);
        }
        catch (final ClientStorageException e)
        {
            e.printStackTrace();
        }
    }
}
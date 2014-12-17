
/*
 * Copyright (c) 2013 - 2014 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.core.framework.storage.mapping;

import java.util.List;

import com.github.naios.wide.core.framework.util.Pair;

public interface Mapping<BASE> extends Iterable<Pair<BASE, MappingMetaData>>
{
    public List<Pair<BASE, MappingMetaData>> getKeys();

    public List<Object> getKeyObjects();

    public List<Pair<BASE, MappingMetaData>> getValues();

    public Pair<BASE, MappingMetaData> getEntryByName(String name) throws UnknownMappingEntryException;

    public boolean setDefaultValues();
}

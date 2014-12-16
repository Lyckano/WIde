
/*
 * Copyright (c) 2013 - 2014 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.core.framework.storage.mapping;

import com.github.naios.wide.core.framework.storage.mapping.schema.Schema;

public class JsonMapper<FROM, TO extends Mapping<?>> extends MapperBase<FROM, TO>
{
    private final Schema schema;

    public JsonMapper(final Schema schema)
    {
        this.schema = schema;
    }

    @Override
    public TO map(final FROM from)
    {
        return null;
    }
}

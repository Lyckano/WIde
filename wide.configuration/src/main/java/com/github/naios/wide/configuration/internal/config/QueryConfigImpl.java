
/*
 * Copyright (c) 2013 - 2014 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.configuration.internal.config;

import java.util.List;

import javafx.beans.property.BooleanProperty;

import com.github.naios.wide.configuration.QueryConfig;

public class QueryConfigImpl implements QueryConfig
{
    private BooleanProperty compress;

    private List<QueryTypeConfigImpl> type;

    @Override
    public BooleanProperty compress()
    {
        return compress;
    }

    @Override
    public List<QueryTypeConfigImpl> getType()
    {
        return type;
    }
}
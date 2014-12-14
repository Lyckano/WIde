
/*
 * Copyright (c) 2013 - 2014 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.core.session.config;

import java.util.List;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.StringProperty;

public class BaseConfig
{
    private StringProperty title, description, active_enviroment;

    private List<EnviromentConfig> enviroments;

    private QueryConfig querys;

    public ReadOnlyStringProperty title()
    {
        return title;
    }

    public ReadOnlyStringProperty description()
    {
        return description;
    }

    public StringProperty active_enviroment()
    {
        return active_enviroment;
    }

    public List<EnviromentConfig> getEnviroments()
    {
        return enviroments;
    }

    public QueryConfig getQuerys()
    {
        return querys;
    }
}

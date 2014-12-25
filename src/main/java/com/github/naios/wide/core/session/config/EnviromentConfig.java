
/*
 * Copyright (c) 2013 - 2014 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.core.session.config;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.github.naios.wide.core.framework.game.GameBuild;

@SuppressWarnings("serial")
class MissingDatabaseConfig extends RuntimeException
{
    public MissingDatabaseConfig(final String name)
    {
        super(String.format("Requested database (%s) is not present in the config!", name));
    }
}

public class EnviromentConfig
{
    private final StringProperty name;

    private final GameBuild build;

    private final StringProperty alias_definition;

    private final ClientStorageConfig client_storages;

    private final List<DatabaseConfig> databases;

    public EnviromentConfig(final GameBuild build, final StringProperty alias_definition,
            final ClientStorageConfig client_storages)
    {
        this.name = new SimpleStringProperty();
        this.alias_definition = new SimpleStringProperty();
        this.build = build;
        this.client_storages = client_storages;
        this.databases = new ArrayList<>();
    }

    public StringProperty name()
    {
        return name;
    }

    public StringProperty alias_definition()
    {
        return alias_definition;
    }

    public ClientStorageConfig getClientStorageConfig()
    {
        return client_storages;
    }

    public GameBuild getBuild()
    {
        return build;
    }

    public List<DatabaseConfig> getDatabases()
    {
        return databases;
    }

    public boolean isDatabasePresent(final String id)
    {
        for (final DatabaseConfig db : databases)
            if (db.id().get().equals(id))
                return true;

        return false;
    }

    public DatabaseConfig getDatabaseConfig(final String id)
    {
        for (final DatabaseConfig db : databases)
            if (db.id().get().equals(id))
                return db;

        throw new MissingDatabaseConfig(id);
    }
}
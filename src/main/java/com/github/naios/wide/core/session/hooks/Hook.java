
/*
 * Copyright (c) 2013 - 2014 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.core.session.hooks;

public enum Hook
{
    // Hook Name                // Emitter
    // -----------------------------------
    ON_APPLICATION_LAUNCH,      // WIde
    ON_APPLICATION_STOP,        // WIde

    ON_ENVIROMENT_LOADED,        // Arguments

    ON_CONFIG_LOADED,           // Config
    ON_CONFIG_CHANGED,          // Config

    ON_MODULES_LOADED,          // Module Loader
    ON_MODULES_RELOADED,        // Module Loader
    ON_MODULES_UNLOADED,        // Module Loader

    ON_SCRIPTS_LOADED,          // Script Loader
    ON_SCRIPTS_UNLOADED,        // Script Loader

    ON_DATABASE_ESTABLISHED,    // Database
    ON_DATABASE_ERROR,          // Database
    ON_DATABASE_LOST,           // Database
    ON_DATABASE_CLOSE,          // Database

    ON_UNKNOWN
}

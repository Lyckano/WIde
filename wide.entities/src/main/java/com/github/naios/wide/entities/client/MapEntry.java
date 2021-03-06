
/*
 * Copyright (c) 2013 - 2015 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.entities.client;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;

import com.github.naios.wide.api.framework.storage.client.ClientStorageStructure;

public interface MapEntry extends ClientStorageStructure
{
    public ReadOnlyIntegerProperty entry();

    public ReadOnlyStringProperty directory();

    public ReadOnlyStringProperty name();
}

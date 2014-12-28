
/*
 * Copyright (c) 2013 - 2015 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.configuration.internal.util;

/**
 * Used to initialize objects at first usage.
 */
public abstract class LateAllocate<T>
{
    private T object;

    public T get()
    {
        if (object == null)
            object = allocate();

        return object;
    }

    public abstract T allocate();
}
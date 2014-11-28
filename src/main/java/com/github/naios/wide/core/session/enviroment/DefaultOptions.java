package com.github.naios.wide.core.session.enviroment;

import org.apache.commons.cli.Options;

@SuppressWarnings("serial")
public abstract class DefaultOptions extends Options
{
    public DefaultOptions()
    {
        configure();
    }

    public abstract void configure();
}

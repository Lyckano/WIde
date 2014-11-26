package wide.scripts.hello;

import java.util.Arrays;

import wide.core.framework.extensions.scripts.Script;
import wide.scripts.ScriptDefinition;

public class HelloWorld extends Script
{
    public HelloWorld(ScriptDefinition definition)
    {
        super(definition);
    }

    @Override
    public void run(String[] args)
    {
        System.out.println("Hello World!");

        if (args.length > 0)
            System.out.println("\tWith Arguments: " + Arrays.toString(args));
    }

    @Override
    public String getUsage()
    {
        return "";
    }
}

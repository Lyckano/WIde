package wide.scripts.fetchwowhead;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import wide.core.Constants;
import wide.core.WIde;
import wide.core.framework.extensions.scripts.Script;

class FetchWorker implements Runnable
{
    private final int id;

    private final String type, origin, target;

    private final AtomicInteger count;

    private boolean overwrite;

    public FetchWorker(int id, String type, String origin, String target, AtomicInteger count, boolean overwrite)
    {
        this.id = id;
        this.type = type;
        this.origin = origin;
        this.target = target;
        this.count = count;
        this.overwrite = overwrite;
    }

    @Override
    public void run()
    {
        try
        {
            if (!overwrite)
                if (new File(target).exists())
                    return;

            final URL url = new URL(origin);

            final ReadableByteChannel rbc = Channels.newChannel(url.openStream());

            final FileOutputStream fos = new FileOutputStream(target);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();

            if (WIde.getEnviroment().isTraceEnabled())
                System.out.println(String.format("Fetched %s id: %s", type, id));

            count.incrementAndGet();

        } catch (final Exception e)
        {
        }
    }
}

public class FetchWowHead extends Script
{
    final String WOWHEAD_URL = "http://www.wowhead.com";

    public FetchWowHead()
    {
        super("fetchwowhead");
    }

    @Override
    public void run(String[] args)
    {
        if (args.length < 2)
        {
            System.out.println(toString() + ": Wrong Argument count!");
            return;
        }

        final ExecutorService pool = Executors.newFixedThreadPool(8);

        final String type = args[0];

        final int begin, end;

        final boolean overwrite = (args.length >= 4) && args[3].equals("o");

        try
        {
            begin = Integer.valueOf(args[1]);

            if (args.length < 3)
                end = begin;
            else
                end = Integer.valueOf(args[2]);

        } catch (final Exception e)
        {
            e.printStackTrace();
            return;
        }

        String targetdir = WIde.getEnviroment().getPath() + "/" + WIde.getConfig().getProperty(Constants.PROPERTY_DIR_CACHE).get();

        WIde.getEnviroment().createDirectory(targetdir);

        targetdir = targetdir + "/" + type;

        WIde.getEnviroment().createDirectory(targetdir);

        final AtomicInteger count = new AtomicInteger();

        for (int i = begin; i <= end; ++i)
            pool.execute(new FetchWorker(i, type, WOWHEAD_URL + "/" + type + "=" + i, targetdir + "/"+ i + ".html", count, overwrite));

        pool.shutdown();

        try
        {
            pool.awaitTermination(5, TimeUnit.MINUTES);
        } catch (final InterruptedException e)
        {
        }

        System.out.println("Done, fetched " + count.get() + " " + type + " files.");
    }
}

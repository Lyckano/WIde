package wide.session.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import wide.session.WIde;
import wide.session.hooks.Hook;
import wide.session.hooks.HookListener;

public class Database
{
    private Map<DatabaseTypes, Connection> connections = new HashMap<>();

    private static String GetConnectionStringForDatabase(String db)
    {        
        return "jdbc:mysql://" + WIde.getConfig().getProperty("DB:Host").get() + ":"
                + WIde.getConfig().getProperty("DB:Port").get() + "/" + db + "?" + "user="
                + WIde.getConfig().getProperty("DB:User").get() + "&" + "password="
                + WIde.getConfig().getProperty("DB:Password").get();
    }

    public Database()
    {
        // Try connect after the config was updated
        WIde.getHooks().addListener(new HookListener(Hook.ON_CONFIG_CHANGED, this)
        {
            @Override
            public void informed()
            {
                if (!isConnected())
                    connect();
            }
        });

        // Close all Connections at end
        WIde.getHooks().addListener(new HookListener(Hook.ON_APPLICATION_STOP, this)
        {
            @Override
            public void informed()
            {
                close();
            }
        });
    }

    public boolean isConnected()
    {
        if (connections.size() != DatabaseTypes.values().length)
            return false;
        
        final Collection<Connection> con_list = connections.values();
        for (Connection con : con_list)
            try
            {
                if (con.isClosed())
                    return false;

            } catch (SQLException e)
            {
                e.printStackTrace();
                
                return false;
            }

        return true;
    }

    private void connect()
    {
        for (DatabaseTypes type : DatabaseTypes.values())
        {
            final String con_string = GetConnectionStringForDatabase(WIde.getConfig().getProperty(type.getStorageName()).get());
            try
            {
                Connection connection = DriverManager.getConnection(con_string);
                connections.put(type, connection);
                
            } catch (SQLException e)
            {
                e.printStackTrace();
                close();
                return;
            }
        }
    }

    private void close()
    {
        final Collection<Connection> con_list = connections.values();
        for (Connection con : con_list)
            try
            {
                con.close();

            } catch (SQLException e)
            {
            }
        
        connections.clear();
    }

    public Connection getConnection(DatabaseTypes type)
    {
        return connections.get(type);
    }
}

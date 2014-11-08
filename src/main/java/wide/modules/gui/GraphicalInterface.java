package wide.modules.gui;

import wide.core.WIde;
import wide.core.framework.extensions.Module;
import wide.core.framework.ui.UserInferface;

public class GraphicalInterface extends Module implements UserInferface
{
    public GraphicalInterface()
    {
        super("default_gui");
    }

    @Override
    public boolean check()
    {
        return WIde.getArgs().isGuiApplication();
    }
    
    @Override
    public void enable()
    {
    }

    @Override
    public void disable()
    {
    }

    @Override
    public void show()
    {
        
    }
}

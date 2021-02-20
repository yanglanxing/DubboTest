package icons;

import com.intellij.openapi.util.IconLoader;

import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2021.02.20 16:46
 * @since 1.0.0
 */
public class PluginIcons {
    /** ICON_FOLDER */
    private static final String ICON_FOLDER = "/icons/";

    /** DUBBO */
    public static final Icon DUBBO = load("dubbo.svg");

    /**
     * Load
     *
     * @param iconFilename icon filename
     * @return the icon
     * @since 1.0.0
     */
    @NotNull
    private static Icon load(String iconFilename) {
        return IconLoader.getIcon(ICON_FOLDER + iconFilename, PluginIcons.class);
    }
}

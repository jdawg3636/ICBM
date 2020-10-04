package icbm.classic.lib;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static org.lwjgl.glfw.GLFW.*;

public class IOUtil {

    /**
     * @param key Numeric ID of key as defined in {@link org.lwjgl.glfw.GLFW}
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean isKeyPressed(int key) {
        return glfwGetKey(glfwGetCurrentContext(), key) == GLFW_PRESS;
    }

}

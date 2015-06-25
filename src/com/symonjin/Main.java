package com.symonjin;

import com.symonjin.entities.Entity;
import com.symonjin.models.Model;
import com.symonjin.models.TexturedModel;
import com.symonjin.terrain.Terrain;
import com.symonjin.texture.ModelTexture;
import com.symonjin.vector.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 680;

    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;

    private long windowHandler;
    public static boolean isMacOSX = false;

    Loader loader = new Loader();
    MasterRenderer renderer;
    Camera cam = new Camera();

    public void run() {
        try {
            onStart();
            update();
        } finally {
            onStop();
        }

    }

    private void onStart() {
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        if (glfwInit() != GL11.GL_TRUE)
            throw new IllegalStateException("Something went wrong when initializing GLFW");

        //Construct the main window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);

        //Some important things for OpenGL to work properly on OSX
        if(isMacOSX){
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        }


        windowHandler = glfwCreateWindow(WIDTH, HEIGHT, "PolyTerrain", NULL, NULL);
        if (windowHandler == NULL)
            throw new RuntimeException("Something went wrong when creating the main window");


        //TODO: Implement proper keyboard interaction for the camera
        glfwSetKeyCallback(windowHandler, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, GL_TRUE);

                else if (action == GLFW_REPEAT) {
                    cam.move(key);
                }
            }
        });


        //More vital things to setup
        glfwMakeContextCurrent(windowHandler);
        glfwSwapInterval(1);
        glfwShowWindow(windowHandler);
    }


    private void update() {
        //Link openGL to current thread
        GLContext.createFromCurrent();

        Model model = OBJLoader.loadObjModel("stall", loader);
        TexturedModel tmodel = new TexturedModel(model,
                new ModelTexture(loader.loadTexture("res/stallTexture.png")));

        tmodel.getModelTexture().setReflectivity(2);
        tmodel.getModelTexture().setShineDamper(1);

        Entity entity = new Entity(tmodel, new Vector3f(0, 0, -20), 0, 0, 0, 1);
        Light light = new Light(new Vector3f(0,50,0), new Vector3f(0.8f,0.5f,0.2f));

        renderer = new MasterRenderer();

        Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("res/paving.png")));
        Terrain terrain2 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("res/gravel.png")));


        while (glfwWindowShouldClose(windowHandler) == GL_FALSE) {
            entity.increaseRotation(0,0.5f,0);

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processEntity(entity);
            //TODO: Separate light and camera rendering
            renderer.render(light, cam);

            //Important things to have in the rendering loop
            glfwSwapBuffers(windowHandler);
            glfwPollEvents();
        }
    }


    private void onStop() {
        renderer.unload();
        loader.unloadVAO();

        keyCallback.release();
        errorCallback.release();

        glfwDestroyWindow(windowHandler);
        glfwTerminate();
    }

    //TODO: Update getHeight and getWidth for changing window size

    public static int getHeight() {
        return HEIGHT;
    }

    public static int getWidth() {
        return WIDTH;
    }

    static {
        /*
            On Mac OSX: LWJGL has to be executed with the JVM argument -XstartOnFirstThread
            This consequently blocks Java's AWT and SWT threads from working properly,
            making other libraries such as ImageIO hang without any error messages.

            One trick to avoid this is to set -java.awt.headless to true
         */
        String operatingSystem = System.getProperty("os.name");
        if(operatingSystem.startsWith("Mac")){
            isMacOSX = true;
            System.setProperty("java.awt.headless", "true");
        }

    }

    public static void main(String[] args) {
        new Main().run();
    }

}

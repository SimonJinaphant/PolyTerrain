package com.symonjin;

import com.symonjin.entities.Entity;
import com.symonjin.geometricObjects.Cube;
import com.symonjin.models.Model;
import com.symonjin.models.TexturedModel;
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
        //Setup GLFW's custom stack-tracing system
        //---REMEMBER TO RELEASE THE CALLBACK WHEN CLOSING THE PROGRAM!---
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        if (glfwInit() != GL11.GL_TRUE)
            throw new IllegalStateException("Something went wrong when initializing GLFW");

        //Construct the main window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        //Some important things for openGL to work properly...
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);


        windowHandler = glfwCreateWindow(WIDTH, HEIGHT, "PolyTerrain", NULL, NULL);
        if (windowHandler == NULL)
            throw new RuntimeException("Something went wrong when creating the main window");


        //Add ESC key callback to close window
        //---REMEMBER TO RELEASE THE CALLBACK WHEN CLOSING THE PROGRAM!---
        glfwSetKeyCallback(windowHandler, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, GL_TRUE);

                else if (action == GLFW_REPEAT) {
                    switch (key) {
                        case GLFW_KEY_W:
                            cam.move(0);
                            break;
                        case GLFW_KEY_D:
                            cam.move(1);
                            break;
                        case GLFW_KEY_A:
                            cam.move(2);
                            break;
                    }
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


        Model model = OBJLoader.loadObjModel("dragon", loader);
        TexturedModel tmodel = new TexturedModel(model,
                new ModelTexture(loader.loadTexture("res/white.png")));

        tmodel.getModelTexture().setReflectivity(10);
        tmodel.getModelTexture().setShineDamper(1);

        Entity entity = new Entity(tmodel, new Vector3f(0, -1, -20), 0, 0, 0, 1);
        Light light = new Light(new Vector3f(0,5,-15), new Vector3f(0.5f,0.5f,0.5f));

        renderer = new MasterRenderer();

        while (glfwWindowShouldClose(windowHandler) == GL_FALSE) {
            entity.increaseRotation(0,1,0);
            renderer.processEntity(entity);
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
            Only on Mac OSX, LWJGL has to be executed with the JVM argument -XstartOnFirstThread
            This consequently blocks Java's AWT and SWT threads from working properly,
            thus making other libraries such as ImageIO hang without any error messages.

            One trick to avoid this is to set -java.awt.headless to true inside a static block
         */
        System.setProperty("java.awt.headless", "true");
    }

    public static void main(String[] args) {
        new Main().run();
    }

}
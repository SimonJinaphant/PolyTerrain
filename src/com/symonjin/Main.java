package com.symonjin;

import com.symonjin.entities.Entity;
import com.symonjin.vector.Vector3f;
import com.symonjin.models.Model;
import com.symonjin.models.TexturedModel;
import com.symonjin.shaders.StaticShader;
import com.symonjin.texture.ModelTexture;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;

public class Main{

    private static final int WIDTH = 800;
    private static final int HEIGHT = 680;

    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;

    private long windowID;

    float[] vertices = {
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f
    };

    int[] indices = {0,1,3,3,1,2};
    float[] textureCoords = {0,0, 0,1, 1,1, 1,0};
    /*float[] vertices = {
            -0.5f,0.5f,-0.5f,
            -0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,0.5f,-0.5f,

            -0.5f,0.5f,0.5f,
            -0.5f,-0.5f,0.5f,
            0.5f,-0.5f,0.5f,
            0.5f,0.5f,0.5f,

            0.5f,0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,0.5f,
            0.5f,0.5f,0.5f,

            -0.5f,0.5f,-0.5f,
            -0.5f,-0.5f,-0.5f,
            -0.5f,-0.5f,0.5f,
            -0.5f,0.5f,0.5f,

            -0.5f,0.5f,0.5f,
            -0.5f,0.5f,-0.5f,
            0.5f,0.5f,-0.5f,
            0.5f,0.5f,0.5f,

            -0.5f,-0.5f,0.5f,
            -0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,0.5f

    };

    float[] textureCoords = {
            0,0, 0,1, 1,1, 1,0,
            0,0, 0,1, 1,1, 1,0,
            0,0, 0,1, 1,1, 1,0,
            0,0, 0,1, 1,1, 1,0,
            0,0, 0,1, 1,1, 1,0,
            0,0, 0,1, 1,1, 1,0
    };

    int[] indices = {
            0,1,3,
            3,1,2,
            4,5,7,
            7,5,6,
            8,9,11,
            11,9,10,
            12,13,15,
            15,13,14,
            16,17,19,
            19,17,18,
            20,21,23,
            23,21,22
    };*/

    Loader loader = new Loader();
    RenderDaemon renderer;
    Model rectangle;
    StaticShader rectangleShader;
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
            throw new IllegalStateException("Something went wrong with initializing GLFW");


        //Construct the main window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        //Some important things for openGL to work properly...
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);


        windowID = glfwCreateWindow(WIDTH, HEIGHT, "PolyTerrain", NULL, NULL);
        if ( windowID == NULL )
            throw new RuntimeException("Something went wrong with creating the main window");


        //Add ESC key callback to close window
        //---REMEMBER TO RELEASE THE CALLBACK WHEN CLOSING THE PROGRAM!---
        glfwSetKeyCallback(windowID, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, GL_TRUE);

                else if (key == GLFW_KEY_W && action == GLFW_REPEAT)
                    cam.move(0);
                else if (key == GLFW_KEY_D && action == GLFW_REPEAT)
                    cam.move(1);
                else if (key == GLFW_KEY_A && action == GLFW_REPEAT)
                    cam.move(2);
            }
        });


        //More vital things to setup
        glfwMakeContextCurrent(windowID);
        glfwSwapInterval(1);
        glfwShowWindow(windowID);
    }


    private void update() {
        //Link openGL to current thread
        GLContext.createFromCurrent();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        rectangle = loader.loadToVAO(vertices, textureCoords, indices);
        rectangleShader = new StaticShader();
        renderer = new RenderDaemon(rectangleShader);

        ModelTexture texture = new ModelTexture(loader.loadTexture("res/gravel.png"));
        TexturedModel tmodel = new TexturedModel(rectangle, texture);
        Entity entity = new Entity(tmodel, new Vector3f(0,0,-2),0,0,0,1);
        glEnable(GL11.GL_DEPTH_TEST);

        while ( glfwWindowShouldClose(windowID) == GL_FALSE ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            //entity.increaseRotation(1,1,0);
            rectangleShader.start();

            //Why load the matrix every loop?
            rectangleShader.loadViewMatrix(cam);

            renderer.render(entity, rectangleShader);
            rectangleShader.stop();

            //Important things to have in the rendering loop
            glfwSwapBuffers(windowID);
            glfwPollEvents();

        }

    }


    private void onStop(){
        rectangleShader.unloadShaders();
        loader.unloadVAO();

        keyCallback.release();
        errorCallback.release();

        glfwDestroyWindow(windowID);
        glfwTerminate();
    }

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
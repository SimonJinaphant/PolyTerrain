package com.symonjin;

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

    Loader loader = new Loader();
    RenderDaemon renderer = new RenderDaemon();
    Model rectangle;
    StaticShader rectangleShader;


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


        windowID = glfwCreateWindow(600, 480, "PolyTerrain", NULL, NULL);
        if ( windowID == NULL )
            throw new RuntimeException("Something went wrong with creating the main window");


        //Add ESC key callback to close window
        //---REMEMBER TO RELEASE THE CALLBACK WHEN CLOSING THE PROGRAM!---
        glfwSetKeyCallback(windowID, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, GL_TRUE);
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

        ModelTexture texture = new ModelTexture(loader.loadTexture("res/gravel.png"));
        TexturedModel tmodel = new TexturedModel(rectangle, texture);

        while ( glfwWindowShouldClose(windowID) == GL_FALSE ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            rectangleShader.start();
            renderer.render(tmodel);
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
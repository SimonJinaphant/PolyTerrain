package com.symonjin;

import com.symonjin.models.Model;
import com.symonjin.models.TexturedModel;
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

    Loader loader = new Loader();
    RenderDaemon renderer = new RenderDaemon();
    Model triangle;
    StaticShader triangleShader;


    public void run() {
        System.out.println("LWJGL Initiated!");

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

        System.out.println("Creating window");
        windowID = glfwCreateWindow(600, 480, "PolyTerrain", NULL, NULL);
        System.out.println("Created window");


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

        float[] textureCoords = {0,0, 0,1, 1,1, 1,0};

        triangle = loader.loadToVAO(vertices,textureCoords, indices);
        triangleShader = new StaticShader();

        ModelTexture texture = new ModelTexture(loader.loadTexture("res/gravel.png"));
        TexturedModel tmodel = new TexturedModel(triangle, texture);

        while ( glfwWindowShouldClose(windowID) == GL_FALSE ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            triangleShader.start();
            renderer.render(tmodel);
            triangleShader.stop();

            //Important things to have in the rendering loop
            glfwSwapBuffers(windowID);
            glfwPollEvents();
        }

    }


    private void onStop(){
        triangleShader.unloadShaders();
        loader.unloadVAO();

        keyCallback.release();
        errorCallback.release();

        glfwDestroyWindow(windowID);
        glfwTerminate();
    }

    static {
        //TO PREVENT MAC OSX FROM FAILING
        System.setProperty("java.awt.headless", "true");
    }
    public static void main(String[] args) {
        new Main().run();
    }

}
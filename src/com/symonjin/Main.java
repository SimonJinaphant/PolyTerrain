package com.symonjin;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;

public class Main {

    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback   keyCallback;

    private long windowID;

    public void run() {
        System.out.println("LWJGL Initiated!");

        try {
            init();
            loop();

            glfwDestroyWindow(windowID);
            keyCallback.release();
        } finally {
            glfwTerminate();
            errorCallback.release();
        }

    }

    private void init() {
        //Setup GLFW's custom stack-tracing system
        //---REMEMBER TO RELEASE THE CALLBACK WHEN CLOSING THE PROGRAM!---
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        if (glfwInit() != GL11.GL_TRUE)
            throw new IllegalStateException("Something went wrong with initializing GLFW");


        //Construct the main window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

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

    private void loop() {

        GLContext.createFromCurrent();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        while ( glfwWindowShouldClose(windowID) == GL_FALSE ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            //...Render Code Here...

            glfwSwapBuffers(windowID);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }

}
package com.symonjin;

import com.symonjin.vector.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Simon on 2015-05-12.
 */
public class Camera {
    private Vector3f position = new Vector3f(0, 1, 0);
    private float pitch, yaw, roll;

    public void move(int key) {
        switch (key) {
            case GLFW_KEY_W:
                position.z -= 0.3f;
                break;
            case GLFW_KEY_D:
                position.x += 0.3f;
                break;
            case GLFW_KEY_A:
                position.x -= 0.3f;
                break;
            case GLFW_KEY_S:
                position.z += 0.3f;
                break;
            case GLFW_KEY_Q:
                position.y += 0.3f;
                pitch -= 0.1f;
                break;
            case GLFW_KEY_Z:
                position.y -= 0.3f;
                pitch += 0.1f;
                break;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

}

package com.symonjin;

import com.symonjin.vector.Vector3f;

/**
 * Created by Simon on 2015-05-12.
 */
public class Camera {
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch, yaw, roll;

    public void move(int action){
        switch (action){
            case 0:
                position.z -= 0.02f; break;
            case 1:
                position.x += 0.02f; break;
            case 2:
                position.x -= 0.02f; break;
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

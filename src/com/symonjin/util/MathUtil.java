package com.symonjin.util;

import com.symonjin.Camera;
import com.symonjin.vector.Matrix4f;
import com.symonjin.vector.Vector;
import com.symonjin.vector.Vector3f;

/**
 * Created by Simon on 2015-05-12.
 */
public class MathUtil {
    public static Matrix4f createTransformation(
            Vector3f translation, float rx, float ry, float rz, float scale ){

        Matrix4f mat = new Matrix4f();
        mat.setIdentity();
        Matrix4f.translate(translation, mat, mat);
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), mat, mat);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), mat, mat);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), mat, mat);
        Matrix4f.scale(new Vector3f(scale, scale, scale), mat, mat);

        return mat;
    }

    public static Matrix4f createViewMatrix(Camera camera){
        Matrix4f mat = new Matrix4f();
        mat.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), mat, mat);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), mat, mat);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f.translate(negativeCameraPos, mat,mat);

        return mat;

    }

}

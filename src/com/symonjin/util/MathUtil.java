package com.symonjin.util;

import com.symonjin.Camera;
import com.symonjin.vector.Matrix4f;
import com.symonjin.vector.Vector3f;

public class MathUtil {
    public static Matrix4f createTransformation(
            Vector3f translation, float rx, float ry, float rz, float scale) {

        Matrix4f mat = new Matrix4f();
        mat.translate(translation);
        mat.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0));
        mat.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0));
        mat.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1));
        mat.scale(new Vector3f(scale, scale, scale));

        return mat;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f mat = new Matrix4f();
        mat.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
        mat.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0));

        Vector3f cameraPos = camera.getPosition();
        mat.translate(new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z));

        return mat;
    }

}

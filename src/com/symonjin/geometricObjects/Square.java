package com.symonjin.geometricObjects;

/**
 * Created by Simon on 2015-05-16.
 */
public class Square {

    public static float[] vertices = {
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f
    };

    public static int[] indices = {0, 1, 3, 3, 1, 2};

    public static float[] textureCoords = {
            0, 0,
            0, 1,
            1, 1,
            1, 0};

}

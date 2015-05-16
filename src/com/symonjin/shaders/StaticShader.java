package com.symonjin.shaders;

import com.symonjin.Camera;
import com.symonjin.util.MathUtil;
import com.symonjin.vector.Matrix4f;

public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/com/symonjin/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/com/symonjin/shaders/fragmentShader.txt";

    private int location_transformMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttribute() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocation() {
        location_transformMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    public void loadTransformMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f view = MathUtil.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, view);
    }
}

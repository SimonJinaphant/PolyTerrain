package com.symonjin.shaders;


import com.symonjin.math.Matrix4f;

public class StaticShader extends ShaderProgram{
    private static final String VERTEX_FILE = "src/com/symonjin/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/com/symonjin/shaders/fragmentShader.txt";

    private int location_transformMatrix;

    public StaticShader(){
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttribute() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocation(){
        location_transformMatrix = super.getUniformLocation("transformationMatrix");
    }

    public void loadTransformMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformMatrix, matrix);
    }
}

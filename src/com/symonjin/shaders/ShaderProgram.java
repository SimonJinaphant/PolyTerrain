package com.symonjin.shaders;

import com.symonjin.vector.Matrix4f;
import com.symonjin.vector.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class ShaderProgram {
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private FloatBuffer buffer = BufferUtils.createFloatBuffer(4 * 4);

    public ShaderProgram(String vertexFile, String fragmentFile) {
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();

        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);

        bindAttribute();

        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocation();
    }

    protected abstract void getAllUniformLocation();

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    protected void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    protected void loadVector(int location, Vector3f vector) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadBoolean(int location, boolean value) {
        GL20.glUniform1f(location, (value) ? 1.0f : 0.0f);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        matrix.store(buffer);
        buffer.flip();
        GL20.glUniformMatrix4fv(location, false, buffer);
    }

    protected abstract void bindAttribute();

    protected void bindAttribute(int attribute, String name) {
        GL20.glBindAttribLocation(programID, attribute, name);
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void unloadShaders() {
        stop();

        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);

        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);

        GL20.glDeleteProgram(programID);
    }

    private static int loadShader(String filepath, int type) {
        String shaderSourceCode = null;
        try {
            shaderSourceCode = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException error) {
            System.out.println("Failed to read shader file: " + filepath);
        }

        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSourceCode);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.out.println("Failed to compile shader");
        }

        return shaderID;

    }

}
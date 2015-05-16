package com.symonjin;

import com.symonjin.models.Model;
import com.symonjin.texture.TextureLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;


public class Loader {

    /*
        We need to keep track of any OpenGL generated objects
        because when the application closes, these objects must be
        properly deleted with its corresponding glDelete function
     */
    private ArrayList<Integer> vertexArrayHandlers = new ArrayList<>(8);
    private ArrayList<Integer> bufferHandlers = new ArrayList<>(8);
    private ArrayList<Integer> textureHandlers = new ArrayList<>(8);


    public Model loadToVAO(float[] position, int[] indices, float[] textureCoords) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeInAttrList(0, 3, position);
        storeInAttrList(1, 2, textureCoords);
        unbindVAO();
        return new Model(vaoID, indices.length);
    }

    public int loadTexture(String fileName) {
        int textureID = TextureLoader.loadTexture(fileName);
        textureHandlers.add(textureID);
        return textureID;
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vertexArrayHandlers.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeInAttrList(int attrNumber, int coordinateSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        bufferHandlers.add(vboID);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

        FloatBuffer buffer = storeInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attrNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

    }

    private void bindIndicesBuffer(int[] indices) {
        int vboId = GL15.glGenBuffers();
        bufferHandlers.add(vboId);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = storeInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

    }

    private FloatBuffer storeInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private IntBuffer storeInIntBuffer(int data[]) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    public void unloadVAO() {
        for (int vao : vertexArrayHandlers) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : bufferHandlers) {
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture : textureHandlers) {
            GL11.glDeleteTextures(texture);
        }

        vertexArrayHandlers.clear();
        bufferHandlers.clear();
        textureHandlers.clear();
    }

}

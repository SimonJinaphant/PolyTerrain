package com.symonjin;

import com.symonjin.models.Model;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.FileInputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;


public class Loader {

    private ArrayList<Integer> vaoManager = new ArrayList<>(8);
    private ArrayList<Integer> vboManager = new ArrayList<>(8);
    private ArrayList<Integer> textureManager = new ArrayList<>(8);

    public Model loadToVAO(float[] position, int[] indices){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeInAttrList(0, position);
        unbindVAO();
        return new Model(vaoID, indices.length);
    }

    public int loadTexture(String fileName){
        Texture texture = null;
        try{
            texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("src/com/symonjin/texture/" + fileName + ".png"));
        }catch (Exception err){
            //derp
        }

        int textureID = texture.getTextureID();
        textureManager.add(textureID);

        return textureID;
    }

    private int createVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaoManager.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeInAttrList(int attrNumber, float[] data){
        int vboID = GL15.glGenBuffers();
        vboManager.add(vboID);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

        FloatBuffer buffer = storeInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attrNumber, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

    }

    private void bindIndicesBuffer(int[] indices){
        int vboId = GL15.glGenBuffers();
        vboManager.add(vboId);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = storeInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

    }

    private FloatBuffer storeInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private IntBuffer storeInIntBuffer(int data[]){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    public void unloadVAO(){
        for (int vao: vaoManager){
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo:vboManager){
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture:textureManager){
            GL11.glDeleteTextures(texture);
        }

        vaoManager.clear();
        vboManager.clear();
        textureManager.clear();
    }

}

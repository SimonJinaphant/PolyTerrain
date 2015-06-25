package com.symonjin;

import com.symonjin.models.Model;
import com.symonjin.shaders.TerrainShader;
import com.symonjin.terrain.Terrain;
import com.symonjin.texture.ModelTexture;
import com.symonjin.util.MathUtil;
import com.symonjin.vector.Matrix4f;
import com.symonjin.vector.Vector3f;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class TerrainRenderer {
    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix){
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(List<Terrain> terrains){
        for(Terrain terrain : terrains){
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

            unbindTexturedModel();
        }
    }

    private void prepareTerrain(Terrain terrain){
        Model model = terrain.getModel();

        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture t = terrain.getTexture();
        shader.loadShineVariables(t.getShineDamper(), t.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexture().getTextureID());


    }

    private void unbindTexturedModel(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void loadModelMatrix(Terrain terrain){
        Matrix4f transformMatrix = MathUtil.createTransformation(new Vector3f(terrain.getX(),
                0, terrain.getZ()), 0, 0, 0, 1);
        shader.loadTransformMatrix(transformMatrix);
    }

}

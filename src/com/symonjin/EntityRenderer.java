package com.symonjin;

import com.symonjin.entities.Entity;
import com.symonjin.models.Model;
import com.symonjin.models.TexturedModel;
import com.symonjin.shaders.StaticShader;
import com.symonjin.texture.ModelTexture;
import com.symonjin.util.MathUtil;
import com.symonjin.vector.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class EntityRenderer {


    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }



    public void render(Map<TexturedModel, List<Entity>> entities){
        for(TexturedModel model:entities.keySet()){
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity:batch){
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel tmodel){
        Model model = tmodel.getModel();

        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture t = tmodel.getModelTexture();
        shader.loadShineVariables(t.getShineDamper(), t.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmodel.getModelTexture().getTextureID());


    }

    private void unbindTexturedModel(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity){
        Matrix4f transformMatrix = MathUtil.createTransformation(
                entity.getPosition(), entity.getRotx(), entity.getRoty(), entity.getRotz(),
                entity.getScale());
        shader.loadTransformMatrix(transformMatrix);
    }




}

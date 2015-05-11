package com.symonjin;

import com.symonjin.math.Matrix4f;
import com.symonjin.models.Model;
import com.symonjin.models.TexturedModel;
import com.symonjin.entities.Entity;

import com.symonjin.shaders.StaticShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class RenderDaemon {

    public void render(Entity entity, StaticShader shader) {
        TexturedModel texturedmodel = entity.getTmodel();
        Model model = texturedmodel.getModel();

        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        Matrix4f transformMatrix = Matrix4f.createTransformation(entity.getPosition(),
                entity.getRotx(),entity.getRoty(), entity.getRotz(), entity.getScale());
        shader.loadTransformMatrix(transformMatrix);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedmodel.getModelTexture().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }
}

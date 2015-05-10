package com.symonjin.models;


import com.symonjin.models.Model;
import com.symonjin.texture.ModelTexture;

public class TexturedModel {
    private Model model;
    private ModelTexture texture;

    public TexturedModel(Model model, ModelTexture texture){
        this.model = model;
        this.texture = texture;
    }

    public Model getModel(){
        return model;
    }

    public ModelTexture getModelTexture(){
        return texture;
    }
}

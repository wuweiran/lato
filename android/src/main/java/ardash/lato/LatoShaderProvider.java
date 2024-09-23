package ardash.lato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;

import ardash.lato.screens.GameScreen.LatoShaders;

public class LatoShaderProvider extends DefaultShaderProvider {
    DefaultShader.Config xshader;
    DefaultShader.Config fogshader;
    DefaultShader.Config toonshader;
    private final LatoShaders shaderType;

    public LatoShaderProvider(DefaultShader.Config defaultConfig, LatoShaders ls) {
        super(defaultConfig);
        this.shaderType = ls;
        xshader = new DefaultShader.Config();
        xshader.vertexShader = Gdx.files.internal("shaders/xvertex.glsl").readString();
        xshader.fragmentShader = Gdx.files.internal("shaders/xfragment.glsl").readString();
        fogshader = new DefaultShader.Config();
        fogshader.vertexShader = Gdx.files.internal("shaders/fog.vertex.glsl").readString();
        fogshader.fragmentShader = Gdx.files.internal("shaders/fog.fragment.glsl").readString();
        toonshader = new DefaultShader.Config();
        toonshader.vertexShader = Gdx.files.internal("shaders/toon.vertex.glsl").readString();
        toonshader.fragmentShader = Gdx.files.internal("shaders/toon.fragment.glsl").readString();
        toonshader.numDirectionalLights = 2;
//		toonshader.defaultCullFace = 0;
    }

    @Override
    protected Shader createShader(Renderable renderable) {
        if (shaderType == LatoShaders.BACK)
            return new DefaultShader(renderable, fogshader);
//		if (shaderType == LatoShaders.THREED)
//		{
//			if (renderable.material.has(TextureAttribute.Diffuse))
//			return new DefaultShader(renderable, toonshader);
//
//		}
//		else
        return super.createShader(renderable);
    }
}

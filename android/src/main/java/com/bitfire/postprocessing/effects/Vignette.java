package com.bitfire.postprocessing.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitfire.postprocessing.PostProcessorEffect;
import com.bitfire.postprocessing.filters.Vignetting;

public final class Vignette extends PostProcessorEffect {
    private Vignetting vignetting;
    private boolean controlSaturation;
    private float oneOnW, oneOnH;

    public Vignette(int viewportWidth, int viewportHeight, boolean controlSaturation) {
        this.controlSaturation = controlSaturation;
        oneOnW = 1f / (float) viewportWidth;
        oneOnH = 1f / (float) viewportHeight;
        vignetting = new Vignetting(controlSaturation);
    }

    @Override
    public void dispose() {
        vignetting.dispose();
    }

    public boolean doesSaturationControl() {
        return controlSaturation;
    }

    public void setCoords(float x, float y) {
        vignetting.setCoords(x, y);
    }

    public void setX(float x) {
        vignetting.setX(x);
    }

    public void setY(float y) {
        vignetting.setY(y);
    }

    public void setLutTexture(Texture texture) {
        vignetting.setLut(texture);
    }

    public void setLutIndexVal(int index, int value) {
        vignetting.setLutIndexVal(index, value);
    }

    public void setLutIndexOffset(float value) {
        vignetting.setLutIndexOffset(value);
    }

    /**
     * Specify the center, in screen coordinates.
     */
    public void setCenter(float x, float y) {
        vignetting.setCenter(x * oneOnW, 1f - y * oneOnH);
    }

    public float getIntensity() {
        return vignetting.getIntensity();
    }

    public void setIntensity(float intensity) {
        vignetting.setIntensity(intensity);
    }

    public float getLutIntensity() {
        return vignetting.getLutIntensity();
    }

    public void setLutIntensity(float value) {
        vignetting.setLutIntensity(value);
    }

    public int getLutIndexVal(int index) {
        return vignetting.getLutIndexVal(index);
    }

    public Texture getLut() {
        return vignetting.getLut();
    }

    public float getCenterX() {
        return vignetting.getCenterX();
    }

    public float getCenterY() {
        return vignetting.getCenterY();
    }

    public float getCoordsX() {
        return vignetting.getX();
    }

    public float getCoordsY() {
        return vignetting.getY();
    }

    public float getSaturation() {
        return vignetting.getSaturation();
    }

    public void setSaturation(float saturation) {
        vignetting.setSaturation(saturation);
    }

    public float getSaturationMul() {
        return vignetting.getSaturationMul();
    }

    public void setSaturationMul(float saturationMul) {
        vignetting.setSaturationMul(saturationMul);
    }

    public boolean isGradientMappingEnabled() {
        return vignetting.isGradientMappingEnabled();
    }

    @Override
    public void rebind() {
        vignetting.rebind();
    }

    @Override
    public void render(FrameBuffer src, FrameBuffer dest) {
        restoreViewport(dest);
        vignetting.setInput(src).setOutput(dest).render();
    }

    ;
}

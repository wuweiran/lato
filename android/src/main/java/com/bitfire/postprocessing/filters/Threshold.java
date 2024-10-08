

package com.bitfire.postprocessing.filters;

import com.bitfire.utils.ShaderLoader;

public final class Threshold extends Filter<Threshold> {

    public enum Param implements Parameter {
        // @formatter:off
		Texture("u_texture0", 0), Threshold("threshold", 0), ThresholdInvTx("thresholdInvTx", 0);
		// @formatter:on

        private final String mnemonic;
        private final int elementSize;

        Param(String mnemonic, int elementSize) {
            this.mnemonic = mnemonic;
            this.elementSize = elementSize;
        }

        @Override
        public String mnemonic() {
            return this.mnemonic;
        }

        @Override
        public int arrayElementSize() {
            return this.elementSize;
        }
    }

    public Threshold() {
        super(ShaderLoader.fromFile("screenspace", "threshold"));
        rebind();
    }

    private float gamma = 0;

    public void setThreshold(float gamma) {
        this.gamma = gamma;
        setParams(Param.Threshold, gamma);
        setParams(Param.ThresholdInvTx, 1f / (1 - gamma)).endParams();
    }

    public float getThreshold() {
        return gamma;
    }

    @Override
    protected void onBeforeRender() {
        inputTexture.bind(u_texture0);
    }

    @Override
    public void rebind() {
        setParams(Param.Texture, u_texture0);
        setThreshold(this.gamma);
    }
}

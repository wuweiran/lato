

package ardash.lato.weather;

import com.badlogic.gdx.graphics.Color;

public enum EnvColors implements HasNext {
    /**
     * daytime
     */
    DAY(new Color(222f / 255f, 216f / 255f, 201f / 255f, 1f), new Color(128f / 255f, 175f / 255f, 163f / 255f, 1f),
        new Color(10 / 255f, 87 / 255f, 94 / 255f, 1f), new Color(126 / 255f, 174 / 255f, 162 / 255f, 1f),
        new Color(255f / 255f, 255f / 255f, 211f / 255f, 1f)) {
        @Override
        public EnvColors next() {
            return DUSK;
        }
    },

    /**
     * sunset
     */
    DUSK(new Color(73 / 255f, 48 / 255f, 35 / 255f, 1f), new Color(170 / 255f, 116 / 255f, 77 / 255f, 1f),
        new Color(46 / 255f, 61 / 255f, 83 / 255f, 1f), new Color(168 / 255f, 115 / 255f, 77 / 255f, 1f),
        new Color(255 / 255f, 115 / 255f, 77 / 255f, 1f)) {
        @Override
        public EnvColors next() {
            return NIGHT;
        }
    },

    /**
     * night time
     */
    NIGHT(new Color(25 / 255f, 33 / 255f, 41 / 255f, 1f), new Color(43 / 255f, 56 / 255f, 69 / 255f, 1f),
        new Color(10 / 255f, 15 / 255f, 19 / 255f, 1f), new Color(78 / 255f, 105 / 255f, 122 / 255f, 1f),
        new Color(78 / 255f, 105 / 255f, 122 / 255f, 1f) /*sky bottom color*/) {
        @Override
        public EnvColors next() {
            return DAWN;
        }
    },

    /**
     * sun rise
     */
    DAWN(new Color(56 / 255f, 33 / 255f, 30 / 255f, 1f), new Color(150 / 255f, 106 / 255f, 98 / 255f, 1f),
        new Color(36 / 255f, 45 / 255f, 76 / 255f, 1f), new Color(148 / 255f, 105 / 255f, 97 / 255f, 1f),
        new Color(255 / 255f, 105 / 255f, 97 / 255f, 1f)) {
        @Override
        public EnvColors next() {
            return DAY;
        }
    };

    public final Color ambient;
    public final Color fog;
    public final Color skyTop;
    public final Color skyBottom;
    public final Color sun;

    private EnvColors(Color ambient, Color fog, Color skyTop, Color skyBottom, Color sun) {
        this.ambient = ambient;
        this.fog = fog;
        this.skyTop = skyTop;
        this.skyBottom = skyBottom;
        this.sun = sun;
    }

}

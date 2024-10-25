package ardash.lato;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader.ParticleEffectParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Logger;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is the Asset class from the Game Apple Flinger and open defence. It is very robust and convenient. It works better than the
 * AnnotationAssetManager.
 *
 * @author Andreas Redmer
 */
public class A {

    private static final AssetManager manager = new AssetManager();
    private static final Logger log = new Logger("A", Application.LOG_NONE);

    static {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        Texture.setAssetManager(manager);
    }


//	/**
//	 * skin is not an enum because there is only one
//	 */
//	public static final AssetDescriptor<Skin> SKIN = new AssetDescriptor<>("shade/uiskin.json", Skin.class);

    /**
     * this is the preparation of anync load, so it knows what is supposed to be loaded
     * for load() it is included
     */
    public static void enqueueAll() {
        // manager.load(MAsset.BG.toString(), Music.class);
        // can iterate over all enums, but even if not, the
        // get{Music|Sound|Texture} Methods will load it
//		manager.load(SKIN);
        for (ModelAsset ma : ModelAsset.values()) {
            manager.load(ma.toString(), Model.class);
        }
        for (SoundAsset sa : SoundAsset.values()) {
            manager.load(sa.toString(), Sound.class);
        }
        for (MusicAsset ma : MusicAsset.values()) {
            manager.load(ma.toString(), Music.class);
        }
        for (AtlasAsset aa : AtlasAsset.values()) {
//			manager.
            manager.load(aa.toString(), TextureAtlas.class);
        }
//		for (SpriteAsset sa : SpriteAsset.values()) {
//			manager.load(sa.toString(), Sprite.class);
//		}
//		for (TextureAsset aa : TextureAsset.values()) {
//			manager.load(aa.toString(), Texture.class);
//		}
//		for (ParticleAsset pa : ParticleAsset.values()) { // TODO needs atlas paramter here
//			manager.load(pa.toString(), ParticleEffect.class);
//		}
    }

//	public enum TextureAsset {
//		MENUBACK;
//		@Override
//		public String toString() {
//			return "" + super.toString().toLowerCase() + ".jpg";
//		}
//	}

    // must be called repeatedly, will then load stuff in background
    public static boolean loadAsync() {
        return manager.update();
    }

    public static Model getModel(ModelAsset ma) {
        final String path = ma.toString();
        if (!manager.isLoaded(path)) {
            manager.load(path, Model.class);
            manager.finishLoading();
        }
        return manager.get(path, Model.class);
    }

    public static Music getMusic(MusicAsset m) {
        final String path = m.toString();
        if (!manager.isLoaded(path)) {
            manager.load(path, Music.class);
            manager.finishLoading();
        }
        final Music music = manager.get(path, Music.class);
        music.setLooping(true);
        return music;
    }

    public static Sound getSound(SoundAsset s) {
        final String path = s.toString();
        if (!manager.isLoaded(path)) {
            manager.load(path, Sound.class);
            manager.finishLoading();
        }
        return manager.get(path, Sound.class);
    }

    public static ParticleEffect getParticleEffect(ParticleAsset pa) {
        final String path = pa.toString();
        if (!manager.isLoaded(path)) {
            ParticleEffectParameter p = new ParticleEffectParameter();
            p.atlasFile = AtlasAsset.SCENE.toString();
            manager.load(path, ParticleEffect.class, p);
            manager.finishLoading();
        }
        return manager.get(path, ParticleEffect.class);
    }


//	public enum SpriteAsset {
//
////		DORK_0,DORK_1,DORK_2,DORK_3,
////
////		PENG_0,
////		EYES_CLOSED_PENG,
////
////		BIRD_0,BIRD_1,BIRD_2,BIRD_3,BIRD_4,BIRD_5,
////
////		BIRD_6,BIRD_7,BIRD_8,BIRD_9,BIRD_10,BIRD_11,
////
////		WOOD_TNT_0,
////		WOOD_BL_11_0,WOOD_BL_11_1,WOOD_BL_11_2,WOOD_BL_11_3,
////
////		WOOD_BL_21_0,WOOD_BL_21_1,WOOD_BL_21_2,WOOD_BL_21_3,
////		WOOD_BL_22_0,WOOD_BL_22_1,WOOD_BL_22_2,WOOD_BL_22_3,
////
////		WOOD_BL_41_0,WOOD_BL_41_1,WOOD_BL_41_2,WOOD_BL_41_3,
////		WOOD_BL_42_0,WOOD_BL_42_1,WOOD_BL_42_2,WOOD_BL_42_3,
////
////		WOOD_BL_81_0,WOOD_BL_81_1,WOOD_BL_81_2,WOOD_BL_81_3,
////
////		WOOD_TRIA_0,WOOD_TRIA_1,WOOD_TRIA_2,WOOD_TRIA_3,
////		WOOD_RECT_0,WOOD_RECT_1,WOOD_RECT_2,WOOD_RECT_3,
////
////		EYES_CLOSED,
////		EYES_DOWN,
////		EYES_INNER,
////		EYES_LEFT,
////		EYES_OUTER,
////		EYES_RIGHT,
////		EYES_UP,
////
////		DIALOG,
////		SLIDERBACK,
////		FLAG_DE,
////		FLAG_EN,
////		FLAG_EO,
////		FLAG_ES,
////		FLAG_FR,
////		FLAG_PL,
////		FLAG_RU,
////		BTN_INFO,
////		BTN_JOYPAD,
////		BTN_LEADER,
////		BTN_ACHI,
////		BTN_SQ_EMPTY,
////		BTN_FL_EMPTY,
////		BTN_TW,
////		BTN_FB,
////		BTN_GP,
////		BTN_PI,
////		BTN_CLOSE,
////		BTN_PLAY,
////		BTN_1PLAYER,
////		BTN_2PLAYERS,
////		BTN_SETTINGS,
////		BTN_SOUND_ON,
////		BTN_SOUND_OFF,
////		BTN_BACK,
////		BTN_PAUSE,
////		BTN_REFRESH,
////		BTN_ABORT,
////		BTN_BLANK,
////		BTN_WORLD;
//
//		static {
//			// init all sprites
//			for (SpriteAsset e : SpriteAsset.values()) {
//				// first the name and index must be set, so thString(works properly)
//				final String lowername = e.name().toLowerCase(Locale.ENGLISH);
//				if (lowername.contains("_"))
//				{
//					final int uscp = lowername.lastIndexOf('_'); // underscore position
//					e.rname = lowername.substring(0, uscp);
//					final String lIndex = lowername.substring(uscp+1, lowername.length());
//					try {
//						e.rindex = Integer.valueOf(lIndex);
//					} catch (NumberFormatException e1) {
//						e.rname = lowername;
//						e.rindex = -1;
//					}
//				}
//				else
//				{
//					e.rname = lowername;
//					e.rindex = -1;
//				}
//
//				// the sprite could be found automatically here, by searching all regions in the atlas for that name
//				// but then all atlasses would have to be preloaded right away as soon as only one sprite is needed. is that good?
//
//				// try it
//				TextureAtlas foundAtlas = null;
//				for (AtlasAsset aa : AtlasAsset.values())
//				{
//					final TextureAtlas atlas = getAtlas(aa);
//					final AtlasRegion foundRegion = atlas.findRegion(e.rname,e.rindex);
//					if (foundRegion!= null)
//					{
//						foundAtlas = atlas;
//						if (foundRegion.index !=e.rindex)
//							throw new RuntimeException("found region "+ e.toString() + " but with wrong index");
//						break;
//					}
//				}
//				if (foundAtlas == null)
//					throw new RuntimeException("No atlas found for "+e.toString());
//				foundAtlas.getTextures().first().setFilter(TextureFilter.Linear, TextureFilter.Linear);
//				e.sprite= foundAtlas.createSprite(e.rname,e.rindex);
//				if (e.sprite == null)
//					throw new RuntimeException("Sprite not created for "+e.toString());
//			}
//		}
//
//		private Sprite sprite;
//		private String rname ="";
//		private int rindex=-1;
//		public Sprite get()
//		{
//			return sprite;
//		}
//
//		@Override
//		public String toString() {
//			return "" + rname + (rindex ==-1 ? "" : "_"+rindex); // "eyes_closed"
//		}
//	}

    /**
     * Direct access (by name) to the image regions of the actors atlas.
     * This is needed because the enemy texture names are stored in JSON and
     * are not accesses via source code constant.
     *
     * @param The texture name of an enemy that is mentioned in the enemy-json-description.
     * @return
     */
    public static AtlasRegion getTextureRegion(String regionName, boolean finishLoading) {
        if (finishLoading) {
            manager.finishLoadingAsset(AtlasAsset.SCENE.toString());
        }

        AtlasRegion found = A.getAtlas(AtlasAsset.SCENE).findRegion(regionName);
//		if (found==null)
//			found = A.getAtlas(AtlasAsset.GUI).findRegion(regionName);
        if (found != null)
            return found;
        throw new RuntimeException(regionName + " was not found on any atlas");
    }

    public static AtlasRegion getTextureRegion(ARAsset tra) {
        return getTextureRegion(tra.toString(), false);
    }

    public static Array<AtlasRegion> getTextureRegions(String regionArray) {
        Array<AtlasRegion> found = A.getAtlas(AtlasAsset.SCENE).findRegions(regionArray);
        if (found != null)
            return found;
        throw new RuntimeException(regionArray + " was not found on any atlas");
    }

    public static Array<AtlasRegion> getTextureRegions(ARAsset tra) {
        return getTextureRegions(tra.toString());
    }

    public static Sound getRandomSound(SoundGroupAsset sg) {
        final SoundAsset random = sg.getRandom();
        final Sound sound = getSound(random);
        if (sound == null)
            log.error("sound was null");
        //throw new RuntimeException("sound was null");
        return sound;
    }

    public static AtlasRegion getRandomAtlasRegion(SpriteGroupAsset sg) {
        final int i = MathUtils.random(0, sg.size() - 1);
        final String sgname = sg.name().toLowerCase(Locale.ENGLISH);
        return A.getTextureRegions(sgname).get(i);
    }

    // TODO make private again?!
    public static TextureAtlas getAtlas(AtlasAsset aa) {
        final String path = aa.toString();
        if (!manager.isLoaded(path)) {
            manager.load(path, TextureAtlas.class);
            manager.finishLoading();
        }
        return manager.get(path, TextureAtlas.class);
    }

    private static FreeTypeFontGenerator getFontGenerator(FontGeneratorAsset aa) {
        final String path = aa.toString();
        if (!manager.isLoaded(path)) {
            manager.load(path, FreeTypeFontGenerator.class);
            manager.finishLoading();
        }
        return manager.get(path, FreeTypeFontGenerator.class);
    }

    public static I18NBundle getI18NBundle() {
        String i18nBundlePath = "i18n/Translation";
        if (!manager.isLoaded(i18nBundlePath)) {
            manager.load(i18nBundlePath, I18NBundle.class);
            manager.finishLoading();
        }
        return manager.get(i18nBundlePath, I18NBundle.class);
    }

    /**
     * early access all enum, so errors throw up early
     */
    public static void validate() {
        for (SoundGroupAsset sg : SoundGroupAsset.values()) {
            sg.toString();
        }
    }

//	public static Sprite getSprite(SpriteAsset s) {
//		final String path = s.toString();
//		if (!manager.isLoaded(path)) {
//			manager.load(path, Sprite.class);
//			manager.finishLoading();
//		}
//		return manager.get(path, Sprite.class);
//	}

    public static void dispose() {
        for (FontAsset fa : FontAsset.values()) {
            try {
                fa.font.dispose(); // can throw if already disposed
            } catch (Exception e) {
//				e.printStackTrace();
            }
        }
        manager.dispose();
    }

    public static int getPercentLoaded() {
        return (int) (manager.getProgress() * 100);
    }

    public static float getProgress() {
        return manager.getProgress();
    }

    public enum LabelStyleAsset {
        DISTANCE_LABEL, SMALL_TEXT;

        static {
            DISTANCE_LABEL.style = new LabelStyle();
            DISTANCE_LABEL.style.font = FontAsset.F1_30_BOLD.font;
            DISTANCE_LABEL.style.fontColor = Color.WHITE;
            SMALL_TEXT.style = new LabelStyle();
            SMALL_TEXT.style.font = FontAsset.F1_15.font;
            SMALL_TEXT.style.fontColor = Color.WHITE;
        }

        public LabelStyle style;
    }

    public enum FontAsset {
        HEADLINE_75, F1_30_BOLD, F1_15;
        // init
        static {
            final float FONT_SIZE_LARGE = getActualPixelHeight(28); // TODO set to gui height
            final float FONT_SIZE_SMALL = getActualPixelHeight(14); // TODO set to gui height
            {
                FreeTypeFontGenerator generator;
                FreeTypeFontParameter parameter;
                parameter = defaultParameter((int) Math.ceil(FONT_SIZE_LARGE), 0);
                generator = A.getFontGenerator(FontGeneratorAsset.SourceHanSans);
                generator.scaleForPixelHeight((int) Math.ceil(FONT_SIZE_LARGE));
                parameter.borderWidth = 2;
                parameter.borderColor = parameter.color;
                F1_30_BOLD.font = generator.generateFont(parameter);
//				generator.dispose(); // do not dispose. it will cause an exception upon app exit
            }
            {
                FreeTypeFontGenerator generator;
                FreeTypeFontParameter parameter;
                parameter = defaultParameter((int) Math.ceil(FONT_SIZE_SMALL), 0);
                generator = A.getFontGenerator(FontGeneratorAsset.SourceHanSans);
                generator.scaleForPixelHeight((int) Math.ceil(FONT_SIZE_SMALL));
                F1_15.font = generator.generateFont(parameter);
//				generator.dispose(); // do not dispose. it will cause an exception upon app exit
            }

        }

        public BitmapFont font;

        private static int getActualPixelHeight(int pixelHeight) {
            float factor = (Gdx.graphics.getDensity() + 0.5f) / 1.5f;
            return (int) (pixelHeight * factor);
        }

        /* extracted method to save some lines, returns some default params for fonts */
        private static FreeTypeFontParameter defaultParameter(int size, float borderWidth) {
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.minFilter = TextureFilter.Nearest;
            parameter.magFilter = TextureFilter.MipMapLinearNearest;
            parameter.borderColor = Color.BLACK;
            parameter.borderStraight = false;
            parameter.borderWidth = borderWidth;
            parameter.size = size;
            I18NBundle i18NBundle = A.getI18NBundle();
            Set<Character> allChars;
            String DEFAULT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$€-%+=#_&~*";
            allChars = Stream.concat(i18NBundle.keys().stream()
                    .map(i18NBundle::get), Stream.of(DEFAULT_CHARS))
                .map(String::chars)
                .flatMap(chars -> chars.mapToObj(c -> (char) c))
                .collect(Collectors.toSet());
            StringBuilder stringBuilder = new StringBuilder();
            for (Character character : allChars) {
                stringBuilder.append(character);
            }
            parameter.characters = stringBuilder.toString();
            return parameter;
        }


        @Override
        public String toString() {
            return "size" + super.toString().replaceAll("[^\\d.]", "") + ".ttf"; // "size72.ttf"
        }
    }


//	/**
//	 * for textures that are not part of an atlas (usually big ones).
//	 * @param s
//	 * @return
//	 */
//	public static Texture getTexture(TextureAsset s) {
////		if (s==TextureAsset.INVISIBLE)
////			return null;
//		final String path = s.toString();
//		if (!manager.isLoaded(path)) {
//			final TextureParameter param = new TextureParameter();
//			param.minFilter = TextureFilter.MipMapLinearLinear;
//			param.magFilter= TextureFilter.Linear;
//			param.genMipMaps = true;
//			manager.load(path, Texture.class,param);
//			manager.finishLoading();
//		}
//		final Texture texture = manager.get(path, Texture.class);
//		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//		//texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearLinear);
//		return texture;
//	}

    private enum FontGeneratorAsset {
        SourceHanSans;

        @Override
        public String toString() {
            return super.toString() + ".ttf"; // example "arial.ttf"
        }
    }

    public enum ParticleAsset {
        RAIN, SNOW, SPRAY;

        @Override
        public String toString() {
            return super.toString().toLowerCase(Locale.ENGLISH) + ".p";
        }
    }

    public enum ModelAsset {
        YCOIN, FARMHOUSE;

        @Override
        public String toString() {
            return "3d/" + super.toString().toLowerCase(Locale.ENGLISH) + ".g3db";
        }
    }

    public enum MusicAsset {
        BG, SAD;

        @Override
        public String toString() {
            return "music/" + super.toString().toLowerCase(Locale.ENGLISH) + ".mp3";
        }
    }

    /**
     * Sound asset (for single sounds)
     */
    public enum SoundAsset {
        COINDROP;

        @Override
        public String toString() {
            return "sounds/" + super.toString().toLowerCase(Locale.ENGLISH) + ".mp3";
        }
    }

//	public static Skin getSkin() {
//		if (!manager.isLoaded(SKIN.fileName)) {
//			manager.load(SKIN);
//			manager.finishLoadingAsset(SKIN.fileName);
//		}
//		return manager.get(SKIN);
//	}


    // 	misc_atlas = new TextureAtlas(Gdx.files.internal("misc.atlas"));
//	seyes_closed = misc_atlas.createSprite("eyes_closed");
    public enum AtlasAsset {
        SCENE;

        @Override
        public String toString() {
            return "" + super.toString().toLowerCase(Locale.ENGLISH) + ".atlas"; // "misc.atlas"
        }
    }

    /**
     * AtlasRegionAsset = the name is just too long and spoils the code.
     * so renamed it ARAsset
     * regions on atlasses
     */
    public enum ARAsset {

        MOUNT, MOUNT_PIX, MOUNT_PIX2, MOUNTAINFOG,

        /**
         * A white pixel. it is also being used for the scarf segments
         */
        FOG_PIX,

        // performer
        P1_RIDE, P1_JUMP, P1_DUCK, P1_CRASH_ASS, P1_CRASH_NOSE, //P1_ROLL,

        // stones
        STONE_0, STONE_1, STONE_2, STONE_3, STONE_4, STONE_5, STONE_6, STONE_7, STONE_8, STONE_9, STONE_10, STONE_11,

        CLIFF_LEFT, CLIFF_RIGHT,

        SUN_SHAPE, GLOW, FLARE,
        ADD_FLARE, PAUSE, ADD_FLARE_A, TITLESCREEN, MOON_SHAPE;

        @Override
        public String toString() {
            return super.toString().toLowerCase(Locale.ENGLISH);
        }
    }

    /**
     * Sound group
     */
    public enum SoundGroupAsset {
        WHIZZ;

        static {
            for (SoundGroupAsset e : SoundGroupAsset.values()) {
                if (e.members.isEmpty()) {
                    // apply default format - get format from own name
                    final String format = e.name() + "_%d";
                    e.fillMembersByFormat(format, e.members);
                }
                if (e.members.isEmpty())
                    throw new RuntimeException("Empty Asset Group created in " + e);
            }
        }

        public final EnumSet<SoundAsset> members; // EnumSet has no efficient way of
        // choosing a random element

        /**
         * creates the sound group exactly from the specified enum set
         *
         * @param members
         */
        SoundGroupAsset(EnumSet<SoundAsset> members) {
            if (members == null)
                throw new RuntimeException("members can't be null");
            this.members = members;
            if (members.isEmpty())
                throw new RuntimeException("Empty Asset Group created");
        }

        /**
         * creates the sound group by the specified format
         *
         * @param format must contain %d ,which will be replaced by a number
         *               [0,+inf]
         */
        SoundGroupAsset(final String format) {
            final EnumSet<SoundAsset> result = EnumSet.noneOf(SoundAsset.class);
            fillMembersByFormat(format, result);

            this.members = result;
        }

        /**
         * creates the members automatically using its own name as format,
         * happens in a static block after <init>
         */
        SoundGroupAsset() {
            this.members = EnumSet.noneOf(SoundAsset.class);
        }

        /**
         * @param format must contain %d ,which will be replaced by a number
         *               [0,+inf]
         * @param result the memebrs array
         */
        private void fillMembersByFormat(final String format, final EnumSet<SoundAsset> result) {
            for (int i = 0; i < SoundAsset.values().length; i++) {
                // get all the SAsset values that match this name structure
                try {
                    final SoundAsset newE = SoundAsset.valueOf(String.format(format, i));
                    result.add(newE);
                } catch (Throwable t) {
                    break;
                }
            }
        }

        public SoundAsset getRandom() {
            return (SoundAsset) (members.toArray())[MathUtils.random(0, members.size() - 1)];
        }
    }

    public enum SpriteGroupAsset {
        STONE;

        static {
            for (SpriteGroupAsset e : SpriteGroupAsset.values()) {
                if (e.members.isEmpty()) {
                    // apply default format - get format from own name
                    final String format = e.name() + "_%d";
                    e.fillMembersByFormat(format, e.members);
                }
                if (e.members.isEmpty())
                    throw new RuntimeException("Empty Asset Group created in " + e);
            }
        }

        public final EnumSet<ARAsset> members;

        /**
         * creates the sprite group by the specified format
         *
         * @param format must contain %d ,which will be replaced by a number
         *               [0,+inf]
         */
        SpriteGroupAsset(final String format) {
            final EnumSet<ARAsset> result = EnumSet.noneOf(ARAsset.class);
            fillMembersByFormat(format, result);

            this.members = result;
        }

        /**
         * creates the members automatically using its own name as format,
         * happens in a static block after <init>
         */
        SpriteGroupAsset() {
            this.members = EnumSet.noneOf(ARAsset.class);
        }

        /**
         * @param format must contain %d ,which will be replaced by a number
         *               [0,+inf]
         * @param result the members array
         */
        private void fillMembersByFormat(final String format, final EnumSet<ARAsset> result) {
            for (int i = 0; i < ARAsset.values().length; i++) {
                // get all the SAsset values that match this name structure
                try {
                    final ARAsset newE = ARAsset.valueOf(String.format(format, i));
                    result.add(newE);
                } catch (Throwable t) {
                    break;
                }
            }
        }

        public ARAsset getRandom() {
            return (ARAsset) (members.toArray())[MathUtils.random(0, members.size() - 1)];
        }

        public ARAsset get(int i) {
            return (ARAsset) (members.toArray())[i];
        }

        public int size() {
            return members.size();
        }
    }
}

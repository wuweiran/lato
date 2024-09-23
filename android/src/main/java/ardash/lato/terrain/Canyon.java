
package ardash.lato.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

import ardash.lato.actors3.AbyssCollider;
import ardash.lato.actors3.CliffLeft;
import ardash.lato.actors3.CliffRight;
import ardash.lato.actors3.Coin;
import ardash.lato.actors3.TerrainItem;
import ardash.lato.terrain.TerrainSeg.TSType;

/**
 *
 */
public class Canyon extends Section {
    public Canyon() {
        add(new Vector2(0, 0), new Vector2(10, 3), Interpolation.smooth);
        add(new Vector2(10, -30), new Vector2(25, -30), Interpolation.smooth, TSType.ABYSS);
        add(new Vector2(25, -5), new Vector2(40, -7), Interpolation.smooth);
        // TODO add cliff-sides
        // TODO add fog in bottom
        // TODO

//		surroundingItems.add(new AbyssCollider(10, -40, 25, -5));
        // note: we can't use the collider to show fog
        surroundingItems.add(new AbyssCollider(9, -30, 17, 24.5f));
//		surroundingItems.add(new AbyssMist(9-35, -30-10, 17+35*2, 24.5f+10*2));
//		AbyssMist am2 = new AbyssMist(9-35, -30-10, 17+35*2, 24.5f+10*2);
//		am2.setZ(am2.getZ()+1);
//		surroundingItems.add(am2);
//
        // cliffs can't be attached perfectly to the edge, because the share renderer moves it slightly, especially when removing old items
        TerrainItem cliffLeft = new CliffLeft(3.90f, -27.695f);
        surroundingItems.add(cliffLeft);
        TerrainItem cliffRight = new CliffRight(21.50f, -35.4595f);
        surroundingItems.add(cliffRight);
        // TODO don't do culling the t segments when a canyon is on the screen

        // mist in the abyss

        // for a certain percentage of all canyons out a large array of coins above
        final boolean putCoinsAboveCanyon = MathUtils.randomBoolean(0.4f);
        if (!putCoinsAboveCanyon)
            return;

        final float centerXofCanyon = 17.5f;
        final int amountOfCoins = 15;
        float xOfCoin = centerXofCanyon;
        float yOfCoin = 7f;
        for (int i = 0; i < amountOfCoins; i++) {
            final Coin coin = Pools.get(Coin.class).obtain();
            coin.init();
            coin.setPosition(xOfCoin, yOfCoin);
            surroundingItems.add(coin);
            xOfCoin += 1;

            switch (i) {
                case 0:
                    yOfCoin -= 1f;
                    xOfCoin = centerXofCanyon - 0.5f;
                    break;
                case 2:
                    yOfCoin -= 1f;
                    xOfCoin = centerXofCanyon - 1.0f;
                    break;
                case 5:
                    yOfCoin -= 1f;
                    xOfCoin = centerXofCanyon - 1.5f;
                    break;
                case 9:
                    yOfCoin -= 1f;
                    xOfCoin = centerXofCanyon - 2.0f;
                    break;

                default:
                    break;
            }
        }

    }
}

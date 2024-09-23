
package ardash.lato.terrain;

public enum CoinPattern {
    COINS_ABOVE_STONE_1(1), COINS_ABOVE_STONE_3(2), COINS_ABOVE_STONE_6(3);

    public final int index;

    CoinPattern(int index) {
        this.index = index;
    }
//
//    public int getIndex() {
//        return index;
//    }
}

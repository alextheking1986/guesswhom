/*
 *========================================================================
 * Coin.java
 * Oct 15, 2011 2:11:54 PM | variable
 * Copyright (c) 2011 Richard Banasiak
 *========================================================================
 * This file is part of CoinFlip.
 *
 *    CoinFlip is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    CoinFlip is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with CoinFlip.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sameer.gupshap;

import java.util.Random;

public class Coin
{
    // Debugging tag.
    private static final String TAG = "Coin";

    Random generator = new Random();

    public boolean flip()
    {
        //Log.d(TAG, "flip()");
        return generator.nextBoolean();
    }

}

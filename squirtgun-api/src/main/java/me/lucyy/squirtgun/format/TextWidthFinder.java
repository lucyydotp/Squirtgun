/*
 * Copyright © 2021 Lucy Poulton
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lucyy.squirtgun.format;

import java.util.HashMap;
import java.util.Map;

/**
 * Calculates the pixel of a string when using the default font.
 */
public class TextWidthFinder {
    private static final Map<Character, Integer> charWidths = new HashMap<>();

    static {
        charWidths.put('A', 5);
        charWidths.put('a', 5);
        charWidths.put('B', 5);
        charWidths.put('b', 5);
        charWidths.put('C', 5);
        charWidths.put('c', 5);
        charWidths.put('D', 5);
        charWidths.put('d', 5);
        charWidths.put('E', 5);
        charWidths.put('e', 5);
        charWidths.put('F', 5);
        charWidths.put('f', 4);
        charWidths.put('G', 5);
        charWidths.put('g', 5);
        charWidths.put('H', 5);
        charWidths.put('h', 5);
        charWidths.put('I', 3);
        charWidths.put('i', 1);
        charWidths.put('J', 5);
        charWidths.put('j', 5);
        charWidths.put('K', 5);
        charWidths.put('k', 4);
        charWidths.put('L', 5);
        charWidths.put('l', 1);
        charWidths.put('M', 5);
        charWidths.put('m', 5);
        charWidths.put('N', 5);
        charWidths.put('n', 5);
        charWidths.put('O', 5);
        charWidths.put('o', 5);
        charWidths.put('P', 5);
        charWidths.put('p', 5);
        charWidths.put('Q', 5);
        charWidths.put('q', 5);
        charWidths.put('R', 5);
        charWidths.put('r', 5);
        charWidths.put('S', 5);
        charWidths.put('s', 5);
        charWidths.put('T', 5);
        charWidths.put('t', 4);
        charWidths.put('U', 5);
        charWidths.put('u', 5);
        charWidths.put('V', 5);
        charWidths.put('v', 5);
        charWidths.put('W', 5);
        charWidths.put('w', 5);
        charWidths.put('X', 5);
        charWidths.put('x', 5);
        charWidths.put('Y', 5);
        charWidths.put('y', 5);
        charWidths.put('Z', 5);
        charWidths.put('z', 5);
        charWidths.put('1', 5);
        charWidths.put('2', 5);
        charWidths.put('3', 5);
        charWidths.put('4', 5);
        charWidths.put('5', 5);
        charWidths.put('6', 5);
        charWidths.put('7', 5);
        charWidths.put('8', 5);
        charWidths.put('9', 5);
        charWidths.put('0', 5);
        charWidths.put('!', 1);
        charWidths.put('@', 6);
        charWidths.put('#', 5);
        charWidths.put('$', 5);
        charWidths.put('%', 5);
        charWidths.put('^', 5);
        charWidths.put('&', 5);
        charWidths.put('*', 5);
        charWidths.put('(', 4);
        charWidths.put(')', 4);
        charWidths.put('-', 5);
        charWidths.put('_', 5);
        charWidths.put('+', 5);
        charWidths.put('=', 5);
        charWidths.put('{', 4);
        charWidths.put('}', 4);
        charWidths.put('[', 3);
        charWidths.put(']', 3);
        charWidths.put(':', 1);
        charWidths.put(';', 1);
        charWidths.put('"', 3);
        charWidths.put('\'', 1);
        charWidths.put('<', 4);
        charWidths.put('>', 4);
        charWidths.put('?', 5);
        charWidths.put('/', 5);
        charWidths.put('\\', 5);
        charWidths.put('|', 1);
        charWidths.put('~', 6);
        charWidths.put('`', 2);
        charWidths.put('.', 1);
        charWidths.put(',', 1);
        charWidths.put(' ', 3);
    }

    /**
     * Calculates the pixel of a string when using the default font.
     *
     * @param input the string to calculate
     * @return the pixel countc
     */
    public static int findWidth(final String input) {
        int out = 0;
        for (char character : input.toCharArray()) {
            out += charWidths.getOrDefault(character, 5) + 1;
        }
        return out;
    }
}

package quiz.system.util;

import java.security.SecureRandom;

public class StringGenerator {
   private static final SecureRandom random = new SecureRandom();
   private static final char[] defaultSymbols = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();

   public static String generate(int length, char[] symbols) {
      StringBuilder sb = new StringBuilder(length);
      int symbolsLength = symbols.length;

      for(int i = 0; i < length; ++i) {
         sb.append(symbols[random.nextInt(symbolsLength)]);
      }

      return sb.toString();
   }

   public static String generate(int length) {
      return generate(length, defaultSymbols);
   }
}

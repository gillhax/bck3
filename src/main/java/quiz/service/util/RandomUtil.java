package quiz.service.util;

import org.apache.commons.lang3.RandomStringUtils;

public final class RandomUtil {
   private static final int DEF_COUNT = 20;
   private static final int MAIL_COUNT = 15;

   public static String generatePassword() {
      return RandomStringUtils.randomAlphanumeric(20);
   }

   public static String generateActivationKey() {
      return RandomStringUtils.randomNumeric(20);
   }

   public static String generateResetKey() {
      return RandomStringUtils.randomAlphanumeric(15);
   }

   public static String generateImageName(int lenght) {
      return RandomStringUtils.randomAlphanumeric(lenght);
   }
}

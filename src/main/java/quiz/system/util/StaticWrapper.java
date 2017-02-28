package quiz.system.util;

import java.util.HashMap;
import quiz.system.error.handler.dto.ResponseDto;

public class StaticWrapper {
   public static ResponseDto wrap(Object value) {
      return new ResponseDto(value);
   }

   public static ResponseDto wrap(String key, Object value) {
      HashMap map = new HashMap(1, 1.0F);
      map.put(key, value);
      return new ResponseDto(map);
   }

   public static ResponseDto wrap() {
      return new ResponseDto();
   }
}

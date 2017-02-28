package quiz.system.error;

import quiz.system.error.exception.BadRequestException;
import quiz.system.error.exception.ForbiddenException;
import quiz.system.error.exception.InternalServerErrorException;
import quiz.system.error.exception.UnauthorizedException;
import quiz.system.error.exception.UnprocessableException;
import quiz.system.error.exception.notfound.NotFoundException;

public class ApiAssert {
   public static void notFound(boolean isThrow) {
      if(isThrow) {
         throw new NotFoundException();
      }
   }

   public static void notFound(boolean isThrow, String message) {
      if(isThrow) {
         throw new NotFoundException(message);
      }
   }

   public static void notFound(boolean isThrow, String message, String devMessage) {
      if(isThrow) {
         throw new NotFoundException(message, devMessage);
      }
   }

   public static void forbidden(boolean isThrow) {
      if(isThrow) {
         throw new ForbiddenException();
      }
   }

   public static void unauthorized(boolean isThrow) {
      if(isThrow) {
         throw new UnauthorizedException();
      }
   }

   public static void unauthorized(boolean isThrow, String message) {
      if(isThrow) {
         throw new UnauthorizedException(message);
      }
   }

   public static void badRequest(boolean isThrow) {
      if(isThrow) {
         throw new BadRequestException();
      }
   }

   public static void badRequest(boolean isThrow, String message) {
      if(isThrow) {
         throw new BadRequestException(message);
      }
   }

   public static void badRequest(boolean isThrow, String message, String devMessage) {
      if(isThrow) {
         throw new BadRequestException(message, devMessage);
      }
   }

   public static void unprocessable(boolean isThrow) {
      if(isThrow) {
         throw new UnprocessableException();
      }
   }

   public static void unprocessable(boolean isThrow, String message) {
      if(isThrow) {
         throw new UnprocessableException(message);
      }
   }

   public static void internal(boolean isThrow) {
      if(isThrow) {
         throw new InternalServerErrorException();
      }
   }

   public static void internal(boolean isThrow, String message) {
      if(isThrow) {
         throw new InternalServerErrorException(message);
      }
   }

   public static void internal(boolean isThrow, String message, String devMessage) {
      if(isThrow) {
         throw new InternalServerErrorException(message, devMessage);
      }
   }
}

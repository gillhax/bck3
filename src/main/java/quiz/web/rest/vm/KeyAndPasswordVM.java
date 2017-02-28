package quiz.web.rest.vm;

public class KeyAndPasswordVM {
   private String key;
   private String newPassword;

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public String getNewPassword() {
      return this.newPassword;
   }

   public void setNewPassword(String newPassword) {
      this.newPassword = newPassword;
   }
}

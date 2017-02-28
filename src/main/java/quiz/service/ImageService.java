package quiz.service;

import java.awt.image.BufferedImage;
import java.beans.ConstructorProperties;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import quiz.service.util.RandomUtil;
import quiz.system.error.ApiAssert;

@Service
@Transactional(
   propagation = Propagation.MANDATORY
)
public class ImageService {
   @Value("${project.image-folder}")
   private String IMAGE_FOLDER;
   private final Log LOGGER = LogFactory.getLog(this.getClass());

   public void save(MultipartFile image, String folder, ImageService.ImageBridge... imageBridges) {
      ImageService.ImageType realImageType = this.getRealImageType(image);
      String[] oldFiles = this.getOldFiles(imageBridges);

      try {
         ImageService.ImageBridge[] e = imageBridges;
         int var7 = imageBridges.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            ImageService.ImageBridge imageBridge = e[var8];
            String imageName = this.saveFile(image, folder, realImageType.extension);
            imageBridge.saveNew(imageName);
         }
      } catch (IOException | SecurityException var11) {
         ApiAssert.internal(true, var11.toString());
      }

      this.deleteOldFiles(oldFiles);
   }

   public void delete(String path) {
      this.deleteOldFiles(new String[]{path});
   }

   public String saveBase64ImageNoLimit(String base64Image, String folder) {
      return this.saveBase64Image(base64Image, folder, false);
   }

   public String saveBase64ImageWithLimit(String base64Image, String folder) {
      return this.saveBase64Image(base64Image, folder, true);
   }

   private String saveBase64Image(String base64Image, String folder, boolean withLimit) {
      try {
         byte[] e = Base64.decodeBase64(base64Image);
         String extension = this.checkImage(e, withLimit);
         String fileName = this.generateUniqueFileName(folder, extension);
         Path destinationFile = Paths.get(this.getFullPath(fileName), new String[0]);
         Files.write(destinationFile, e, new OpenOption[]{StandardOpenOption.CREATE});
         return fileName;
      } catch (IOException | SecurityException var8) {
         ApiAssert.internal(true, var8.toString());
         return null;
      }
   }

   private String checkImage(byte[] bytes, boolean withLimit) throws IOException {
      short maxSize = 2048;
      byte[] jpegMagicNumber = new byte[]{(byte)-1, (byte)-40, (byte)-1, (byte)-32};
      byte[] pngMagicNumber = new byte[]{(byte)-119, (byte)80, (byte)78, (byte)71};
      byte[] magicNumber = Arrays.copyOf(bytes, 4);
      String extension = "";
      if(Arrays.equals(jpegMagicNumber, magicNumber)) {
         extension = "jpg";
      } else if(Arrays.equals(pngMagicNumber, magicNumber)) {
         extension = "png";
      } else {
         ApiAssert.badRequest(true, "bad-image");
      }

      ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
      BufferedImage bufferedImage = null;

      try {
         bufferedImage = ImageIO.read(bis);
      } catch (IIOException var11) {
         ApiAssert.badRequest(true, "bad-image");
      }

      ApiAssert.badRequest(bufferedImage == null, "bad-image");
      if(withLimit) {
         ApiAssert.badRequest(bufferedImage.getWidth() > maxSize || bufferedImage.getHeight() > maxSize, "big-image");
      }

      return extension;
   }

   private String[] getOldFiles(ImageService.ImageBridge[] imageBridges) {
      String[] oldFiles = new String[imageBridges.length];

      for(int i = 0; i < imageBridges.length; ++i) {
         oldFiles[i] = imageBridges[i].getOld();
      }

      return oldFiles;
   }

   private void deleteOldFiles(String... files) {
      String[] var2 = files;
      int var3 = files.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String file = var2[var4];
         if(file != null && !file.isEmpty()) {
            try {
               Files.deleteIfExists(Paths.get(this.getFullPath(file), new String[0]));
            } catch (IOException var7) {
               this.LOGGER.error("Cannot delete old file", var7);
            }
         }
      }

   }

   private ImageService.ImageType getRealImageType(MultipartFile image) {
      String contentType = image.getContentType();
      if(contentType != null) {
         if(contentType.equals("image/jpeg")) {
            return ImageService.ImageType.JPEG;
         }

         if(contentType.equals("image/png")) {
            return ImageService.ImageType.PNG;
         }
      }

      ApiAssert.badRequest(true, "file-not-image");
      throw new RuntimeException();
   }

   private String saveFile(MultipartFile image, String folder, String originExtension) throws IOException {
      String uniqueFileName = this.generateUniqueFileName(folder, originExtension);
      Files.write(Paths.get(this.getFullPath(uniqueFileName), new String[0]), image.getBytes(), new OpenOption[0]);
      return uniqueFileName;
   }

   private String generateUniqueFileName(String folder, String extension) throws IOException {
      String fileName;
      do {
         fileName = folder + this.generateRandomFileName(extension);
      } while(Files.exists(Paths.get(this.getFullPath(fileName), new String[0]), new LinkOption[0]));

      this.createFolders(fileName);
      return fileName;
   }

   private void createFolders(String fileName) throws IOException {
      String onlyFolder = fileName.substring(0, fileName.lastIndexOf(47));
      Files.createDirectories(Paths.get(this.getFullPath(onlyFolder), new String[0]), new FileAttribute[0]);
   }

   private String generateRandomFileName(String extension) {
      return "/" + RandomUtil.generateImageName(32).toLowerCase() + "." + extension;
   }

   private String getFullPath(String fileName) {
      return this.IMAGE_FOLDER + "/" + fileName;
   }

   public abstract static class ImageBridge {
      abstract void saveNew(String var1);

      abstract String getOld();
   }

   public static class Size {
      final int width;
      final int height;
      final boolean cropToSquare;

      @ConstructorProperties({"width", "height", "cropToSquare"})
      public Size(int width, int height, boolean cropToSquare) {
         this.width = width;
         this.height = height;
         this.cropToSquare = cropToSquare;
      }
   }

   public static enum ImageType {
      ORIGINAL((String)null),
      JPEG("jpg"),
      PNG("png");

      String extension;

      private ImageType(String extension) {
         this.extension = extension;
      }
   }
}

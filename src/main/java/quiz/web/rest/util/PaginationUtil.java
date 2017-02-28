package quiz.web.rest.util;

import java.net.URISyntaxException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

public final class PaginationUtil {
   public static HttpHeaders generatePaginationHttpHeaders(Page page, String baseUrl) throws URISyntaxException {
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-Total-Count", "" + page.getTotalElements());
      String link = "";
      if(page.getNumber() + 1 < page.getTotalPages()) {
         link = "<" + generateUri(baseUrl, page.getNumber() + 1, page.getSize()) + ">; rel=\"next\",";
      }

      if(page.getNumber() > 0) {
         link = link + "<" + generateUri(baseUrl, page.getNumber() - 1, page.getSize()) + ">; rel=\"prev\",";
      }

      int lastPage = 0;
      if(page.getTotalPages() > 0) {
         lastPage = page.getTotalPages() - 1;
      }

      link = link + "<" + generateUri(baseUrl, lastPage, page.getSize()) + ">; rel=\"last\",";
      link = link + "<" + generateUri(baseUrl, 0, page.getSize()) + ">; rel=\"first\"";
      headers.add("Link", link);
      return headers;
   }

   private static String generateUri(String baseUrl, int page, int size) throws URISyntaxException {
      return UriComponentsBuilder.fromUriString(baseUrl).queryParam("page", new Object[]{Integer.valueOf(page)}).queryParam("size", new Object[]{Integer.valueOf(size)}).toUriString();
   }
}

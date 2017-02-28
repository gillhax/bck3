package quiz.config.apidoc;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

@Component
@Order(-2147482648)
@Profile({"swagger"})
public class PageableParameterBuilderPlugin implements ParameterBuilderPlugin {
   private final TypeNameExtractor nameExtractor;
   private final TypeResolver resolver;

   @Autowired
   public PageableParameterBuilderPlugin(TypeNameExtractor nameExtractor, TypeResolver resolver) {
      this.nameExtractor = nameExtractor;
      this.resolver = resolver;
   }

   public boolean supports(DocumentationType delimiter) {
      return true;
   }

   private Function createModelRefFactory(ParameterContext context) {
      ModelContext modelContext = ModelContext.inputParam(context.resolvedMethodParameter().getParameterType(), context.getDocumentationType(), context.getAlternateTypeProvider(), context.getGenericNamingStrategy(), context.getIgnorableParameterTypes());
      return ResolvedTypes.modelRefFactory(modelContext, this.nameExtractor);
   }

   public void apply(ParameterContext context) {
      ResolvedMethodParameter parameter = context.resolvedMethodParameter();
      Class type = parameter.getParameterType().getErasedType();
      if(type != null && Pageable.class.isAssignableFrom(type)) {
         Function factory = this.createModelRefFactory(context);
         ModelReference intModel = (ModelReference)factory.apply(this.resolver.resolve(Integer.TYPE, new Type[0]));
         ModelReference stringModel = (ModelReference)factory.apply(this.resolver.resolve(List.class, new Type[]{String.class}));
         ArrayList parameters = Lists.newArrayList(new Parameter[]{context.parameterBuilder().parameterType("query").name("page").modelRef(intModel).description("Page number of the requested page").build(), context.parameterBuilder().parameterType("query").name("size").modelRef(intModel).description("Size of a page").build(), context.parameterBuilder().parameterType("query").name("sort").modelRef(stringModel).allowMultiple(true).description("Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.").build()});
         context.getOperationContext().operationBuilder().parameters(parameters);
      }

   }
}

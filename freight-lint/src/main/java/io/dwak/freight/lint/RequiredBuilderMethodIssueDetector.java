package io.dwak.freight.lint;

import com.android.tools.lint.client.api.JavaParser;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.MethodInvocation;
import lombok.ast.Node;

public class RequiredBuilderMethodIssueDetector
        extends Detector
        implements Detector.JavaScanner, Detector.ClassScanner {

  public static final String HAS_REQUIRED_METHODS_ANNOTATION
          = "io.dwak.freight.internal.annotation.HasRequiredMethods";
  public static final Issue ISSUE = Issue.create("RequiredMethod", "Required method not called!",
          "This builder has required methods that haven't been called.",
          Category.CORRECTNESS, 6, Severity.FATAL,
          new Implementation(RequiredBuilderMethodIssueDetector.class,
                  Scope.JAVA_FILE_SCOPE));

  @Override
  public Speed getSpeed() {
    return Speed.FAST;
  }

  @Override
  public List<String> getApplicableMethodNames() {
    return Collections.singletonList("build");
  }

  @Override
  public void visitMethod(JavaContext context, AstVisitor visitor, MethodInvocation node) {
    JavaParser.ResolvedNode resolvedNode = context.resolve(node);
    if (resolvedNode instanceof JavaParser.ResolvedMethod) {
      JavaParser.ResolvedMethod resolvedMethod = (JavaParser.ResolvedMethod) resolvedNode;
      List<String> requiredMethods = Arrays.asList((String[]) resolvedMethod
              .getAnnotation(HAS_REQUIRED_METHODS_ANNOTATION)
              .getValue());
      List<String> methodsToRemove = new ArrayList<>();
      String methodChainString = "";
      for (Node node1 : node.getChildren()) {
        methodChainString += node1.toString();
        context.log(null, methodChainString);
      }
      for (String requiredMethod : requiredMethods) {
        if(methodChainString.contains(requiredMethod)){
          methodsToRemove.add(requiredMethod);
        }
      }
      for (String s : methodsToRemove) {
        requiredMethods.remove(s);
      }
      if(!requiredMethods.isEmpty()){
        context.report(ISSUE, node, context.getLocation(node),
                String.format("This builder still requires the following methods: %s",
                        requiredMethods.toString()));
      }
    }
  }
}
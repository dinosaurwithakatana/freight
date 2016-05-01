package io.dwak.freight.lint;

import com.android.tools.lint.detector.api.Issue;

import java.util.Collections;
import java.util.List;

public class IssueRegistry extends com.android.tools.lint.client.api.IssueRegistry {
  public List<Issue> getIssues() {
    return Collections.singletonList(RequiredBuilderMethodIssueDetector.ISSUE);
  }
}

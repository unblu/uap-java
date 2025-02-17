/**
 * Copyright 2012 Twitter, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua_parser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Operating System parser using ua-parser. Extracts OS information from user agent strings.
 *
 * @author Steve Jiang (@sjiang) &lt;gh at iamsteve com&gt;
 */
public class OSParser {

  private final List<OSPattern> patterns;

  public OSParser() {
    this.patterns = Regexes.getOSPatterns();
  }

  public OS parse(String agentString) {
    if (agentString == null) {
      return null;
    }

    OS os;
    for (OSPattern p : patterns) {
      if ((os = p.match(agentString)) != null) {
        return os;
      }
    }
    return OS.OTHER;
  }

  protected static class OSPattern {
    private final Pattern pattern;
    private final String osReplacement, v1Replacement, v2Replacement, v3Replacement;

    public OSPattern(Pattern pattern, String osReplacement, String v1Replacement, String v2Replacement, String v3Replacement) {
      this.pattern = pattern;
      this.osReplacement = osReplacement;
      this.v1Replacement = v1Replacement;
      this.v2Replacement = v2Replacement;
      this.v3Replacement = v3Replacement;
    }

    public OS match(String agentString) {
      String family = null, v1 = null, v2 = null, v3 = null, v4 = null;
      Matcher matcher = pattern.matcher(agentString);

      if (!matcher.find()) {
        return null;
      }

      int groupCount = matcher.groupCount();

      if (osReplacement != null) {
          if (groupCount >= 1) {
              family = Pattern.compile("(" + Pattern.quote("$1") + ")")
                              .matcher(osReplacement)
                              .replaceAll(matcher.group(1));
          } else { 
              family = osReplacement; 
          }
      } else if (groupCount >= 1) {
        family = matcher.group(1);
      }

      if (v1Replacement != null) {
        v1 = getReplacement(matcher, v1Replacement);
      } else if (groupCount >= 2) {
        v1 = matcher.group(2);
      }
      if (v2Replacement != null) {
        v2 = getReplacement(matcher, v2Replacement);
      } else if (groupCount >= 3) {
        v2 = matcher.group(3);
      }
      if (v3Replacement != null) {
        v3 = getReplacement(matcher, v3Replacement);
      } else if (groupCount >= 4) {
        v3 = matcher.group(4);
      }
      if (groupCount >= 5) {
        v4 = matcher.group(5);
      }

      return family == null ? null : new OS(family, v1, v2, v3, v4);
    }
    
    private String getReplacement(Matcher matcher, String replacement) {
    	if (isBackReference(replacement)) {
    		int group = getGroup(replacement);
    		return matcher.group(group);
    	} else {
    		return replacement;
    	}
    }
    
    /**
     * Checks if the replacement is a backreference (i.e. $1, $2, $3, etc) to a capturing group in the regular expression.
     */
    private boolean isBackReference(String replacement) {
    	return replacement.startsWith("$");
    }
    
    /**
     * Extracts the group number from a backreference like $1, $2, $3, etc.
     */
    private int getGroup(String backReference) {
    	return Integer.valueOf(backReference.substring(1));
    }
  }
}

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

import static org.hamcrest.Matchers.is;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

/**
 * Tests parsing results match the expected results in the test_resources yamls
 *
 * @author Steve Jiang (@sjiang) <gh at iamsteve com>
 */
public class ParserTest {
  final String TEST_RESOURCE_PATH = "/ua_parser/";
  Yaml yaml = new Yaml(RegexesBuilder.getDefaultLoaderOptions());;
  Parser parser;

  @Before
  public void initParser() {
    parser = new Parser();
  }

  @Test
  public void testParseUserAgent() {
    testUserAgentFromYaml("test_ua.yaml");
  }

  @Test
  public void testParseOS() {
    testOSFromYaml("test_os.yaml");
  }

  @Test
  public void testParseAdditionalOS() {
    testOSFromYaml("additional_os_tests.yaml");
  }


  @Test
  public void testParseDevice() {
    testDeviceFromYaml("test_device.yaml");
  }

  @Test
  public void testParseFirefox() {
    testUserAgentFromYaml("firefox_user_agent_strings.yaml");
  }

  @Test
  public void testParsePGTS() {
    testUserAgentFromYaml("pgts_browser_list.yaml");
  }

  @Test
  public void testParseAll() {
    String agentString1 = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; fr; rv:1.9.1.5) Gecko/20091102 Firefox/3.5.5,gzip(gfe),gzip(gfe)";
    String agentString2 = "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9B206 Safari/7534.48.3";

    Client expected1 = new Client(new UserAgent("Firefox", "3", "5", "5"),
                                  new OS("Mac OS X", "10", "4", null, null),
                                  new Device("Mac"));
    Client expected2 = new Client(new UserAgent("Mobile Safari", "5", "1", null),
                                  new OS("iOS", "5", "1", "1", null),
                                  new Device("iPhone"));

    MatcherAssert.assertThat(parser.parse(agentString1), is(expected1));
    MatcherAssert.assertThat(parser.parse(agentString2), is(expected2));
  }

  void testUserAgentFromYaml(String filename) {
    InputStream yamlStream = this.getClass().getResourceAsStream(TEST_RESOURCE_PATH + filename);

    @SuppressWarnings("unchecked")
    Map<String, List<Map<String,String>>> entries = (Map<String, List<Map<String,String>>>)yaml.load(yamlStream);
    
    List<Map<String, String>> testCases = entries.get("test_cases");
    for(Map<String, String> testCase : testCases) {
      // Skip tests with js_ua as those overrides are not yet supported in java
      if (testCase.containsKey("js_ua")) continue;

      String uaString = testCase.get("user_agent_string");
      MatcherAssert.assertThat(uaString, parser.parseUserAgent(uaString), is(UserAgent.fromMap(testCase)));
    }
  }

  void testOSFromYaml(String filename) {
    InputStream yamlStream = this.getClass().getResourceAsStream(TEST_RESOURCE_PATH + filename);

    @SuppressWarnings("unchecked")
    Map<String, List<Map<String,String>>> entries = (Map<String, List<Map<String,String>>>)yaml.load(yamlStream);
    
    List<Map<String,String>> testCases = entries.get("test_cases");
    for(Map<String, String> testCase : testCases) {
      // Skip tests with js_ua as those overrides are not yet supported in java
      if (testCase.containsKey("js_ua")) continue;

      String uaString = testCase.get("user_agent_string");
      MatcherAssert.assertThat(uaString, parser.parseOS(uaString), is(OS.fromMap(testCase)));
    }
  }

  void testDeviceFromYaml(String filename) {
    InputStream yamlStream = this.getClass().getResourceAsStream(TEST_RESOURCE_PATH + filename);

    @SuppressWarnings("unchecked")
    Map<String, List<Map<String,String>>> entries = (Map<String, List<Map<String,String>>>)yaml.load(yamlStream);
    
    List<Map<String,String>> testCases = entries.get("test_cases");
    for(Map<String, String> testCase : testCases) {

      String uaString = testCase.get("user_agent_string");
      MatcherAssert.assertThat(uaString, parser.parseDevice(uaString), is(Device.fromMap(testCase)));
    }
  }
}

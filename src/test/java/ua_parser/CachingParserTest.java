package ua_parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

/**
 * These tests really only redo the same tests as in ParserTest but with a
 * different Parser subclass Also the same tests will be run several times on
 * the same user agents to validate the caching works correctly.
 *
 * @author niels
 *
 */
public class CachingParserTest extends ParserTest {

  @Before
  public void initParser() {
    parser = new CachingParser();
  }

  @Test
  public void testCachingParserCorrectSizeInit() throws Exception {
      parser = new CachingParser(10);
  }

  @Test (expected = java.lang.AssertionError.class)
  public void testCachingParserIncorrectSizeInit() throws Exception{
      parser = new CachingParser(0);
  }

  @Test
  public void testCachedParseUserAgent() {
    super.testParseUserAgent();
    super.testParseUserAgent();
    super.testParseUserAgent();
  }

  @Test
  public void testCachedParseOS() {
    super.testParseOS();
    super.testParseOS();
    super.testParseOS();
  }

  @Test
  public void testCachedParseAdditionalOS() {
    super.testParseAdditionalOS();
    super.testParseAdditionalOS();
    super.testParseAdditionalOS();
  }

  @Test
  public void testCachedParseDevice() {
    super.testParseDevice();
    super.testParseDevice();
    super.testParseDevice();
  }

  @Test
  public void testCachedParseFirefox() {
    super.testParseFirefox();
    super.testParseFirefox();
    super.testParseFirefox();
  }

  @Test
  public void testCachedParsePGTS() {
    super.testParsePGTS();
    super.testParsePGTS();
    super.testParsePGTS();
  }

  @Test
  public void testCachedParseAll() {
    super.testParseAll();
    super.testParseAll();
    super.testParseAll();
  }

}

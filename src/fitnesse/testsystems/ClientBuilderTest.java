package fitnesse.testsystems;

import fitnesse.components.ClassPathBuilder;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPageUtil;
import fitnesse.wiki.mem.InMemoryPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class ClientBuilderTest {

  private WikiPage root;

  @Before
  public void setUp() throws Exception {
    root = InMemoryPage.makeRoot("RooT");
  }

  @Test
  public void buildFullySpecifiedTestSystemName() throws Exception {
    WikiPage testPage = WikiPageUtil.addPage(root, PathParser.parse("TestPage"),
            "!define TEST_SYSTEM {system}\n" +
                    "!define TEST_RUNNER {runner}\n");
    String testSystemName = new WikiPageDescriptor(testPage.readOnlyData(), false, "").getTestSystemName();
    Assert.assertEquals("system:runner", testSystemName);
  }

  @Test
  public void buildDefaultTestSystemName() throws Exception {
    WikiPage testPage = WikiPageUtil.addPage(root, PathParser.parse("TestPage"), "");
    String testSystemName = new WikiPageDescriptor(testPage.readOnlyData(), false, new ClassPathBuilder().getClasspath(testPage)).getTestSystemName();
    Assert.assertEquals("fit:fit.FitServer", testSystemName);
  }

  @Test
  public void buildTestSystemNameWhenTestSystemIsSlim() throws Exception {
    WikiPage testPage = WikiPageUtil.addPage(root, PathParser.parse("TestPage"), "!define TEST_SYSTEM {slim}\n");
    String testSystemName = new WikiPageDescriptor(testPage.readOnlyData(), false, "").getTestSystemName();
    Assert.assertEquals("slim:fitnesse.slim.SlimService", testSystemName);
  }

  @Test
  public void buildTestSystemNameWhenTestSystemIsUnknownDefaultsToFit() throws Exception {
    WikiPage testPage = WikiPageUtil.addPage(root, PathParser.parse("TestPage"), "!define TEST_SYSTEM {X}\n");
    String testSystemName = new WikiPageDescriptor(testPage.readOnlyData(), false, "").getTestSystemName();
    Assert.assertEquals("X:fit.FitServer", testSystemName);
  }

  @Test
  public void shouldReplaceMarkWithValue() {
    assertEquals("Hello world", WikiPageDescriptor.replace("Hello %p", "%p", "world"));
    assertEquals("/path/to/somewhere", WikiPageDescriptor.replace("/path/%p/somewhere", "%p", "to"));
    assertEquals("/path/to/somewhere", WikiPageDescriptor.replace("/path%p", "%p", "/to/somewhere"));
    assertEquals("\\path\\to\\somewhere", WikiPageDescriptor.replace("\\path\\%p\\somewhere", "%p", "to"));
    assertEquals("\\path\\to\\somewhere", WikiPageDescriptor.replace("\\path%p", "%p", "\\to\\somewhere"));
  }

  @Test
  public void shouldIncludeStandaloneJarByDefault() {
    assertEquals("fitnesse.jar", WikiPageDescriptor.fitnesseJar("fitnesse.jar"));
    assertEquals("fitnesse-20121220.jar",
            WikiPageDescriptor.fitnesseJar("fitnesse-20121220.jar"));
    assertEquals("fitnesse-standalone.jar",
            WikiPageDescriptor.fitnesseJar("fitnesse-standalone.jar"));
    assertEquals("fitnesse-standalone-20121220.jar",
            WikiPageDescriptor.fitnesseJar("fitnesse-standalone-20121220.jar"));
    assertEquals("fitnesse.jar",
            WikiPageDescriptor.fitnesseJar("fitnesse-book.jar"));
    assertEquals(
            "fitnesse-standalone-20121220.jar",
            WikiPageDescriptor.fitnesseJar(String
                    .format("irrelevant.jar%1$sfitnesse-book.jar%1$sfitnesse-standalone-20121220.jar",
                            System.getProperty("path.separator"))));
    assertEquals(String.format("lib%sfitnesse-standalone.jar", File.separator),
            WikiPageDescriptor.fitnesseJar(String.format("lib%sfitnesse-standalone.jar", File.separator)));
  }
}

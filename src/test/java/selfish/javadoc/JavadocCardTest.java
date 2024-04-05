package selfish.javadoc;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("javadoc")
@Tag("Card")
public class JavadocCardTest {

  String FQCN = "selfish.deck.Card";

  @Test
  public void testAllMembersDocumented() {
    assertTrue(JavadocMemberHelper.allMembersAreDocumented(FQCN));
  }

  @Test
  public void testConstructorDocumented() {
    JavadocMemberHelper._testmemberIsDocumented(FQCN, "Card\\s*\\(.*");
  }

  @Test
  public void testGetDescriptionDocumented() {
    JavadocMemberHelper._testmemberIsDocumented(FQCN, "String\\s*getDescription\\s*\\(.*");
  }

  @Test
  public void testToStringDocumented() {
    JavadocMemberHelper._testmemberIsDocumented(FQCN, "String\\s*toString\\s*\\(.*");
  }

}
package selfish.javadoc;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("javadoc")
@Tag("GameException")
public class JavadocGameExceptionTest {

  String FQCN = "selfish.GameException";

  @Test
  public void testAllMembersDocumented() {
    assertTrue(JavadocMemberHelper.allMembersAreDocumented(FQCN));
  }

  @Test
  public void testConstructorDocumented() {
    JavadocMemberHelper._testmemberIsDocumented(FQCN, "GameException\\s*\\(.*");
  }

}
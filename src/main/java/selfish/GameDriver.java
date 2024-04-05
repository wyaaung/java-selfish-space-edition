package selfish;

public class GameDriver {

  public GameDriver() {
  }

  /**
   * A helper function to centre text in a longer String.
   *
   * @param width The length of the return String.
   * @param s     The text to centre.
   * @return A longer string with the specified text centred.
   */
  public static String centreString(int width, String s) {
    return String.format("%-" + width + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
  }

  public static void main(String[] args) {
    System.out.println(new GameDriver().centreString(720, "Sample"));
  }

}
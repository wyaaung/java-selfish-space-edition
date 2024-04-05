package selfish;

/**
 * GameException Class
 *
 * @author Wai Yan Aung
 * @version 1
 */
public class GameException extends Exception {

  /**
   * GameException's constructor
   *
   * @param sIn The detail message.
   * @param e   Throwable exception
   */
  public GameException(String sIn, Throwable e) {
    super(sIn, e);
  }
}

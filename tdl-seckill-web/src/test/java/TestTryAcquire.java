/**
 * Description.
 *
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2019/2/16 10:37
 */
public class TestTryAcquire {
    static int SHARED_SHIFT   = 16;
    static int EXCLUSIVE_MASK = (1 << SHARED_SHIFT) - 1;
    public static void main(String[] args) {
        System.out.println((1 << SHARED_SHIFT));
        System.out.println(EXCLUSIVE_MASK);
        System.out.println( exclusiveCount(1));

    }

    static int exclusiveCount(int c) { return c & EXCLUSIVE_MASK; }
}

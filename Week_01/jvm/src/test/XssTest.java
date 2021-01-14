package test;

/**
 * 栈大小测试
 * -Xss256k
 * -Xss1m
 */
public class XssTest {
    private static int count =1;

    /**
     * 本机jdk1.8_131的情况下，默认Xss的深度为11000左右，即默认值为1m
     * Xss256k-------2500左右
     * Xss512k-------5500左右
     * Xss1m--------11000左右
     */
    public static void main(String[] args) {
        System.out.println(count);
        count++;
        main(args);
    }
}

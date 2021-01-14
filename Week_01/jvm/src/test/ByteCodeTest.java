package test;

import java.util.Arrays;

/**
 * 字节码分析用
 */
public class ByteCodeTest {
    public static void main(String[] args) {
        int a = 1;
        double b = 2.0D; //ldc2_w 加载常量到操作数栈
        byte c = 'a';
        long d = 100000L;
        String[] e = new String[]{"a", "b", "c", "d"};

        double v = a + b;
        int v1 = a - c;
        long v2 = a * d;
        double v3 = a / b;

        if (v3 > 1) {  // dcmpl 比较， ifle条件分支
            System.out.println("a/b>1");
        }
        for (int i = 0; i < 10; i++) {
            a++;
        }
        System.out.println(a);
        // invokestatic调用stream()
        // invokedynamic执行具体的function -> System.out.println(str);
        // invokeinterface调用 Stream.foreach
        Arrays.stream(e).forEach(str -> {
            System.out.println(str);
        });


    }
}

package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 自定义加载Hello.xlass文件
 */
public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        MyClassLoader classLoader = new MyClassLoader();
        Class<?> hello = classLoader.findClass("Hello");
        Object o = hello.newInstance();
        hello.getDeclaredMethod("hello").invoke(o);
    }

    @Override
    protected Class<?> findClass(String name) {
        File file = new File(MyClassLoader.class.getResource(name + ".xlass").getPath());
        int length = (int) file.length();
        byte[] data = new byte[length];
        try (FileInputStream fi = new FileInputStream(file)) {
            fi.read(data, 0, length);
            for (int i = 0; i < data.length; i++) {
                data[i] = (byte) (255 - data[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defineClass(name, data, 0, length);
    }
}


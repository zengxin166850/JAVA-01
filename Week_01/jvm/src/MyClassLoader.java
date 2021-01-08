import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Title: JvmClassLoader
 * Description: TODO
 * Copyright: Copyright (c) 2007
 * Company 北京华宇信息技术有限公司
 *
 * @author zengxin@thunisoft.com
 * @version 1.0
 * @date 2021/1/7 11:18
 */
public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) {
        MyClassLoader classLoader = new MyClassLoader();
        Class<?> hello = classLoader.findClass("Hello");
        try {
            Object o = hello.newInstance();
            hello.getDeclaredMethod("hello").invoke(o);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) {
        File file = new File(MyClassLoader.class.getResource("Hello.xlass").getPath());
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


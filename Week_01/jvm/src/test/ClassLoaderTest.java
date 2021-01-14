package test;

import java.io.IOException;
import java.io.InputStream;
/**
 * 尝试使用不同加载器，加载同一个类
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf('.')+1)+".class";
                    InputStream inputStream = ClassLoaderTest.class.getResourceAsStream(fileName);
                    if(inputStream == null){
                        return super.loadClass(name);
                    }
                    byte[] b = new byte[inputStream.available()];
                    inputStream.read(b);
                    return defineClass(name,b,0,b.length);
                } catch (IOException e) {
                    throw new  ClassNotFoundException();
                }
            }
        };
        Object o = classLoader.loadClass("test.ClassLoaderTest").newInstance();
        System.out.println(o.getClass());
        System.out.println(o instanceof test.ClassLoaderTest);
    }
}

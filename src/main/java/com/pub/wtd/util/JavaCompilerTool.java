package com.pub.wtd.util;

import javax.tools.*;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hekun 158109016@qq.com</> on 2017/3/7.
 */
public class JavaCompilerTool {
    private static  JavaCompiler compiler= ToolProvider.getSystemJavaCompiler();;
    private static  URL[] urls ;
    private static URLClassLoader urlClassLoader;
    static  String sp = System.getProperty("file.separator");
    private static Method addURL = initAddMethod();
    private static String jarPath = GlobalInfo.rootPath + sp + "bin"+sp+"lib";
    private static String sourceDir = GlobalInfo.rootPath+sp+"wtdwebuicases";
    private static String targetDir = GlobalInfo.rootPath + sp + "bin" + sp + "webuicaseclasses";
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
    StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
    String jars=null;
    Iterable<String> options = null;
    boolean isDebug =  false;

    public JavaCompilerTool(URLClassLoader urlClassLoader){
         jars=getJarFiles();
        options =  Arrays.asList("-encoding", "UTF-8", "-classpath", jars, "-d", targetDir, "-sourcepath", sourceDir);

        if(null!=urlClassLoader){
            this.urlClassLoader=urlClassLoader;
        }else{
            try {
                urls = new URL[]{new URL("file:/" + targetDir)};
                this.urlClassLoader = new URLClassLoader(urls);
                isDebug = true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        try {
            addURL(new File( GlobalInfo.rootPath+ sp+"bin"+sp+"webuicaseclasses"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean comiple(String outPath, String sourceFile){
        boolean status=false;
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(new File(sourceFile));
        try {
            if(jars== null || jars.trim()==""){
              int  statusInt = compiler.run(null, null, null, "-d", targetDir, sourceFile);
              if(statusInt==0){
                  status=true;
              }else{
                  status=false;
              }
            }else{
                status= compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits).call();
            }

             //status = compiler.run(null, null, null, "-d", targetDir, sourceFile);
        }catch (Exception e){
            if(e.getClass().getSimpleName().equals("NullPointerException")){
                System.out.println("请将本机jdk/lib中的tool.jar拷贝到%JAVA_HOME%/jre/lib中");
            }else{
                e.printStackTrace();
            }
        }
    return status;
    }
    public static URLClassLoader getClassLoader(){

            return urlClassLoader;
    }

    public void setClassLoader(URLClassLoader classLoader) {
        this.urlClassLoader = classLoader;
    }

    private static Method initAddMethod() {
        try {
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            add.setAccessible(true);
            return add;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void addURL(File file) {
        try {
            addURL.invoke(urlClassLoader, new Object[] { file.toURI().toURL() });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找该目录下的所有的jar文件
     *
     * @throws Exception
     */
    private static String getJarFiles()  {
        File libDir = new File(GlobalInfo.rootPath + sp + "bin" + sp + "lib");
        List<String> includeJarFiles = new ArrayList<String>();
        String jars = "";
        includeJarFiles.add("wtd.jar");
        includeJarFiles.add("dom4j-1.6.1.jar");
        includeJarFiles.add("jaxen-1.1.1.jar");
        includeJarFiles.add("log4j-1.2.17.jar");
        includeJarFiles.add("selenium-server-standalone-2.53.0.jar");
        includeJarFiles.add("activation-1.1.1.jar");
        includeJarFiles.add("javax.mail-1.5.6.jar");
        includeJarFiles.add("mysql-connector-java-5.0.5.jar");
        includeJarFiles.add("substance-7.3.jar");
        includeJarFiles.add("trident-1.3.jar");
        includeJarFiles.add("trident-7.3-swing.jar");
        File[] files = libDir.listFiles();
        if(null==files)return  "";
        for (File file : files) {
            if (includeJarFiles.contains(file.getName())) {
                jars = jars + file.getPath() + ";";
            }
        }
        return jars;
    }
}

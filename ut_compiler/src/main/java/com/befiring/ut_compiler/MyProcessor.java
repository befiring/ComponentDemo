package com.befiring.ut_compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static javax.lang.model.element.Modifier.PUBLIC;

@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor{

    /**
     * 文件相关辅助类
     */
    private Filer mFiler;
    /**
     * 元素相关辅助类
     */
    private Elements mElementsUtils;
    /**
     * 日志相关辅助类
     */
    private Messager mMessager;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElementsUtils = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        TypeSpec.Builder helper = TypeSpec.classBuilder("MyTestClass")
                .addJavadoc("this is generated code , please do not edit !")
                .addModifiers(PUBLIC);

        ParameterSpec objectParamSpec = ParameterSpec.builder(TypeName.OBJECT, "target").build();
        MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder("inject")
                .addModifiers(PUBLIC)
                .addParameter(objectParamSpec);
        helper.addMethod(injectMethodBuilder.build());
        JavaFile javaFile = JavaFile.builder("com.befiring.generated",helper.build()).build();

        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add("ut.com.cn.ut_annotation.MyTest");
        return types;
    }
}

package demo.annotation.liuyang.com.appcompiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * 定义一个被注解类对象AnnotedClass,用于保存哪些被注解的对象
 * Created by ly on 2018/11/23.
 */

class AnnotatedClass {
    private static class TypeUtil {
        static final ClassName BINDER = ClassName.get("demo.annotation.liuyang.com.appapi", "ViewBinder");
        static final ClassName PROVIDER = ClassName.get("demo.annotation.liuyang.com.appapi", "ViewFinder");
    }

    private TypeElement mTypeElement;
    private ArrayList<BindViewField> mFields;
    private Elements mElements;
    private Messager mMessager;
    private Filer mFiler;

    AnnotatedClass(TypeElement typeElement, Elements elements, Messager messager, Filer filer) {
        mTypeElement = typeElement;
        mElements = elements;
        mFields = new ArrayList<>();
        mMessager = messager;
        mFiler = filer;
    }

    void addField(BindViewField field) {
        mFields.add(field);
    }

    JavaFile generateFile() {
        //generateMethod
        MethodSpec.Builder bindViewMethod = MethodSpec.methodBuilder("bindView")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mTypeElement.asType()), "host")
                .addParameter(TypeName.OBJECT, "source")
                .addParameter(TypeUtil.PROVIDER, "finder");

        for (BindViewField field : mFields) {
            // find views 在bindView方法中增加findView host.fieldName = (type)(finder.findView(source, resId))
            bindViewMethod.addStatement("host.$N = ($T)(finder.findView(source, $L))", field.getFieldName(), ClassName.get(field.getFieldType()), field.getResId());
        }

        MethodSpec.Builder unBindViewMethod = MethodSpec.methodBuilder("unBindView")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(mTypeElement.asType()), "host")
                .addAnnotation(Override.class);
        for (BindViewField field : mFields) {
            unBindViewMethod.addStatement("host.$N = null", field.getFieldName());
        }

        //generaClass
        TypeSpec injectClass = TypeSpec.classBuilder(mTypeElement.getSimpleName() + "$$ViewBinder")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.BINDER, TypeName.get(mTypeElement.asType())))
                .addMethod(bindViewMethod.build())
                .addMethod(unBindViewMethod.build())
                .build();

        String packageName = mElements.getPackageOf(mTypeElement).getQualifiedName().toString();

        mMessager.printMessage(Diagnostic.Kind.WARNING, "dudumuta's packageName:" + packageName);
        mMessager.printMessage(Diagnostic.Kind.WARNING, "dudumuta's claseName:" + injectClass.name);

        try {
            List<Element> originatingElements = injectClass.originatingElements;
            JavaFileObject filerSourceFile = mFiler.createSourceFile(packageName,
                    originatingElements.toArray(new Element[originatingElements.size()]));
            mMessager.printMessage(Diagnostic.Kind.WARNING, "dudumuta's fileName:" + filerSourceFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return JavaFile.builder(packageName, injectClass).build();
    }
}
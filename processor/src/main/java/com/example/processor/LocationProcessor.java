package com.example.processor;

import com.example.annotation.LocationAsst;
import com.example.annotation.LocationClient;
import com.example.annotation.MapType;
import com.example.annotation.onLocationReceived;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class LocationProcessor extends AbstractProcessor {
    private Messager messager;
    private Filer filer;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(LocationAsst.class);
        for (Element element : elements) {

            if (!element.getKind().isClass())
                continue;

            Set<MethodSpec> methodSpecs = new LinkedHashSet<>();
            Set<FieldSpec> fieldSpecs = new LinkedHashSet<>();

            TypeElement typeElement = (TypeElement) element;

            List<? extends Element> members = elementUtils.getAllMembers(typeElement);

            String path = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();

            ClassName context = ClassName.get("android.content", "Context");

            ClassName provider = ClassName.get("com.kenny.annotatelocation", "OptionProvider");

            ClassName generateClass = ClassName.get(path, "LA" + typeElement.getSimpleName().toString());

            MapType mapType = typeElement.getAnnotation(LocationAsst.class).value();

            ClassName clientType = null;
            ClassName optionType = null;
            ClassName locationType = null;

            if (mapType == MapType.TYPE_BDMap) {
                clientType = ClassName.get("com.kenny.annotatelocation", "BDLocationClient");
                optionType = ClassName.get("com.baidu.location", "LocationClientOption");
                locationType = ClassName.get("com.baidu.location", "BDLocation");
            } else if (mapType == mapType.TYPE_AMap) {
                clientType = ClassName.get("com.kenny.annotatelocation", "GDapLocationClient");
                optionType = ClassName.get("com.amap.api.location", "AMapLocationClientOption");
                locationType = ClassName.get("com.amap.api.location", "AMapLocation");
            }


            MethodSpec.Builder construct = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(context, "context")
                    .addParameter(ClassName.get(typeElement.asType()), "target")
                    .addStatement("this.target = target");

            MethodSpec.Builder bind = MethodSpec.methodBuilder("bind")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(context, "context")
                    .addParameter(ClassName.get(typeElement.asType()), "target")
                    .beginControlFlow("if( instance == null )")
                    .addStatement("instance = new $T(context,target);", generateClass)
                    .endControlFlow();

            MethodSpec.Builder unBind = MethodSpec.methodBuilder("unBind")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .beginControlFlow("if ( instance != null )");

            MethodSpec.Builder locationOverride = MethodSpec.methodBuilder("onLocationReceived")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addParameter(locationType, "location");

            for (Element item : members) {
                if (item.getAnnotation(LocationClient.class) != null) {
                    construct.addStatement("target.$N = new $T($N, ($T) $T.getDefaultOption($N),this)", item.getSimpleName(), clientType, "context", optionType, provider, mapType.name());
                    unBind.addStatement("target.$N.destroyClient()", item.getSimpleName())
                            .addStatement("instance = null")
                            .endControlFlow();
                }

                if (item.getAnnotation(onLocationReceived.class) != null) {
                    locationOverride.addStatement("target.$N(location)", item.getSimpleName());
                }
            }

            FieldSpec target = FieldSpec.builder(ClassName.get(element.asType()), "target")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .build();

            FieldSpec instance = FieldSpec.builder(generateClass, "instance")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .build();

            methodSpecs.add(construct.build());
            methodSpecs.add(locationOverride.build());
            methodSpecs.add(bind.build());
            methodSpecs.add(unBind.build());

            fieldSpecs.add(target);
            fieldSpecs.add(instance);

            try {
                TypeSpec diClass = TypeSpec.classBuilder(generateClass)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addMethods(methodSpecs)
                        .addFields(fieldSpecs)
                        .addSuperinterface(ParameterizedTypeName.get(ClassName.get("com.kenny.annotatelocation", "LocationReceivedListener"),
                                locationType))
                        .build();

                JavaFile javaFile = JavaFile.builder(path, diClass)
                        .addFileComment(" This codes are generated automatically. Do not modify!")
                        .addStaticImport(ClassName.get("com.example.annotation", "MapType"), "*")
                        .build();
                javaFile.writeTo(filer);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(LocationClient.class.getCanonicalName());
        annotations.add(onLocationReceived.class.getCanonicalName());
        annotations.add(LocationAsst.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}

package ipw;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static ipw.WrapperExtension.assertCodeCompletion;
import static ipw.WrapperExtension.ipw;
import static ipw.WrapperExtension.tempDir;

@ExtendWith(WrapperExtension.class)
class SimpleTests {

    @Test
    void javaProjectAbsolutePathTest() {
        var projectDir = tempDir + "/projects/simpleJavaProject";
        var file = projectDir + "/Test.java";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openProject(projectDir);
            assertCodeCompletion(project, file, List.of("ArrayList"));
            project.closeProject();
        });
    }

    @Test
    void javaProjectRelativePathTest() {
        var projectDir = tempDir + "/projects/simpleJavaProject";
        var file = "Test.java";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openProject(projectDir);
            assertCodeCompletion(project, file, projectDir, List.of("ArrayList"));
            project.closeProject();
        });
    }

    @Test
    void kotlinProjectAbsolutePathTest() {
        var projectDir = tempDir + "/projects/simpleKotlinProject";
        var file = projectDir + "/test.kt";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openProject(projectDir);
            assertCodeCompletion(project, file, List.of("SortedMap"));
            project.closeProject();
        });
    }

    @Test
    void kotlinProjectRelativePathTest() {
        var projectDir = tempDir + "/projects/simpleKotlinProject";
        var file = "test.kt";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openProject(projectDir);
            assertCodeCompletion(project, file, projectDir, List.of("SortedMap"));
            project.closeProject();
        });
    }

    @Test
    void javaOneFileTest() {
        var file = tempDir + "/files/TestSo.java";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openFile(file);
            assertCodeCompletion(project, file, List.of("SortedMap"));
            project.closeProject();
        });
    }

    @Test
    void kotlinOneFileTest() {
        var file = tempDir + "/files/test.kt";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openFile(file);
            assertCodeCompletion(project, file, List.of("SortedMap"));
            project.closeProject();
        });
    }

    @Test
    void kotlinRepeatableCallTest() {
        var file = tempDir + "/files/test.kt";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openFile(file);
            assertCodeCompletion(project, file, List.of("SortedMap"));
            assertCodeCompletion(project, file, List.of("SortedMap"));
            project.closeProject();
        });
    }

    @Test
    void kotlinSortedListTest() {
        var file = tempDir + "/files/ListTest.kt";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openFile(file);
            assertCodeCompletion(project, file,
                List.of("[]", "ensureCapacity", "trimToSize", "size", "get", "add", "add", "addAll", "addAll"),
                true);
            project.closeProject();
        });
    }

    @Test
    void fileProjectTest() {
        var javaProjectDir = tempDir + "/projects/simpleJavaProject";
        var javaFile = "Test.java";
        var kotlinFile = tempDir + "/files/test.kt";
        Assertions.assertDoesNotThrow(() -> {
            var javaProject = ipw.openProject(javaProjectDir);
            var kotlinProject = ipw.openFile(kotlinFile);
            assertCodeCompletion(kotlinProject, kotlinFile, List.of("SortedMap"));
            assertCodeCompletion(javaProject, javaFile, javaProjectDir, List.of("ArrayList"));

            javaProject.closeProject();
            assertCodeCompletion(kotlinProject, kotlinFile, List.of("SortedMap"));

            javaProject = ipw.openProject(javaProjectDir);
            assertCodeCompletion(kotlinProject, kotlinFile, List.of("SortedMap"));
            assertCodeCompletion(javaProject, javaFile, javaProjectDir, List.of("ArrayList"));

            kotlinProject.closeProject();
            assertCodeCompletion(javaProject, javaFile, javaProjectDir, List.of("ArrayList"));
            javaProject.closeProject();
        });
    }

}

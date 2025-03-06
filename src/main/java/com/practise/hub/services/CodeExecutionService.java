package com.practise.hub.services;

import com.practise.hub.dto.CodeSubmission;
import com.practise.hub.dto.ExecutionResult;
import org.springframework.stereotype.Service;
import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.*;

@Service
public class CodeExecutionService {
    
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    private static final long TIMEOUT_SECONDS = 5;
    
    public ExecutionResult executeCode(CodeSubmission submission) {
        if (!"java".equalsIgnoreCase(submission.getLanguage())) {
            return new ExecutionResult("", "Only Java is supported", false, 0);
        }

        try {
            // Create source file
            String className = "Solution";
            String sourceCode = "public class " + className + " {\n" + 
                              "    public static void main(String[] args) {\n" + 
                              submission.getCode() + "\n" +
                              "    }\n" +
                              "}";
            
            // Compile
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
            
            JavaFileObject file = new JavaSourceFromString(className, sourceCode);
            Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
            
            JavaCompiler.CompilationTask task = compiler.getTask(
                null, fileManager, diagnostics, null, null, compilationUnits);

            boolean success = task.call();
            if (!success) {
                StringBuilder errors = new StringBuilder();
                for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                    errors.append(diagnostic.getMessage(null)).append("\n");
                }
                return new ExecutionResult("", errors.toString(), false, 0);
            }

            // Execute
            long startTime = System.currentTimeMillis();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            
            Future<?> future = executor.submit(() -> {
                PrintStream originalOut = System.out;
                try {
                    System.setOut(printStream);
                    Class.forName(className).getDeclaredMethod("main", String[].class)
                        .invoke(null, (Object) new String[0]);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } finally {
                    System.setOut(originalOut);
                }
            });

            try {
                future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                long executionTime = System.currentTimeMillis() - startTime;
                return new ExecutionResult(outputStream.toString(), "", true, (int)executionTime);
            } catch (TimeoutException e) {
                future.cancel(true);
                return new ExecutionResult("", "Execution timed out", false, (int) (TIMEOUT_SECONDS * 1000));
            } finally {
                executor.shutdownNow();
            }
            
        } catch (Exception e) {
            return new ExecutionResult("", e.getMessage(), false, 0);
        }
    }
}

class JavaSourceFromString extends SimpleJavaFileObject {
    final String code;

    JavaSourceFromString(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
} 
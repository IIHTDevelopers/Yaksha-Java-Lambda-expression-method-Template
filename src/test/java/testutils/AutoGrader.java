package testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;

public class AutoGrader {

	// Test if the code correctly implements lambda expressions within methods
	public boolean testLambdaMethod(String filePath) throws IOException {
		System.out.println("Starting testLambdaMethod with file: " + filePath);

		File participantFile = new File(filePath); // Path to participant's file
		if (!participantFile.exists()) {
			System.out.println("File does not exist at path: " + filePath);
			return false;
		}

		FileInputStream fileInputStream = new FileInputStream(participantFile);
		JavaParser javaParser = new JavaParser();
		CompilationUnit cu;
		try {
			cu = javaParser.parse(fileInputStream).getResult()
					.orElseThrow(() -> new IOException("Failed to parse the Java file"));
		} catch (IOException e) {
			System.out.println("Error parsing the file: " + e.getMessage());
			throw e;
		}

		System.out.println("Parsed the Java file successfully.");

		// Flags to check for lambda expression and method usage
		boolean hasMainMethod = false;
		boolean usesLambdaInMethod = false;
		boolean usesMethodReference = false;
		boolean usesCalculatorLambda = false;

		// Check for method declarations
		for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
			String methodName = method.getNameAsString();
			// Check for the presence of the main method
			if (methodName.equals("main")) {
				hasMainMethod = true;
			}
		}

		// Check for method calls using lambda expressions
		for (MethodCallExpr methodCallExpr : cu.findAll(MethodCallExpr.class)) {
			String methodName = methodCallExpr.getNameAsString();
			if (methodName.equals("processList") || methodName.equals("calculate")) {
				usesLambdaInMethod = true;
			}
		}

		// Check for method reference usage (System.out::println)
		for (ExpressionStmt stmt : cu.findAll(ExpressionStmt.class)) {
			if (stmt.getExpression() instanceof MethodCallExpr) {
				MethodCallExpr methodCallExpr = (MethodCallExpr) stmt.getExpression();
				if (methodCallExpr.getScope().isPresent()
						&& methodCallExpr.getScope().get().toString().equals("System.out")) {
					usesMethodReference = true;
				}
			}
		}

		// Check for Calculator lambda usage
		for (LambdaExpr lambdaExpr : cu.findAll(LambdaExpr.class)) {
			if (lambdaExpr.toString().contains("a, b")) {
				usesCalculatorLambda = true;
			}
		}

		// Log results of the checks
		System.out.println("Lambda expression is used within a method: " + (usesLambdaInMethod ? "YES" : "NO"));
		System.out.println("Method reference (System.out::println) is used: " + (usesMethodReference ? "YES" : "NO"));
		System.out.println("Calculator lambda expression is used: " + (usesCalculatorLambda ? "YES" : "NO"));

		// Final result - all conditions should be true
		boolean result = hasMainMethod && usesLambdaInMethod && usesMethodReference && usesCalculatorLambda;

		System.out.println("Test result: " + result);
		return result;
	}
}

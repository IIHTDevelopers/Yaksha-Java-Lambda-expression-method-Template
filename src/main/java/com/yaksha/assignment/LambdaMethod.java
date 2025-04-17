package com.yaksha.assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LambdaMethod {

	public static void main(String[] args) {
		// Task 1: Lambda expression as a method argument
		List<String> names = new ArrayList<>();
		names.add("Alice");
		names.add("Bob");
		names.add("Charlie");
		names.add("Dave");

		// Using a lambda expression to print each element
		processList(names, (name) -> System.out.println("Name: " + name));

		// Task 2: Method reference to simplify lambda expression
		// Using method reference to print each element
		processList(names, System.out::println);

		// Task 3: Lambda expression that returns a result
		int sum = calculate((a, b) -> a + b, 5, 3); // Lambda expression that adds two numbers
		System.out.println("Sum: " + sum);
	}

	// Task 1: Method that accepts a Consumer as a parameter to process each element
	// of the list
	public static void processList(List<String> list, Consumer<String> consumer) {
		for (String name : list) {
			consumer.accept(name); // Applying the Consumer lambda expression
		}
	}

	// Task 3: Method that accepts a lambda expression and performs a calculation
	public static int calculate(Calculator calculator, int a, int b) {
		return calculator.add(a, b); // Using the lambda to perform the addition
	}

	// Functional interface for Calculator
	@FunctionalInterface
	public interface Calculator {
		int add(int a, int b); // Method to add two numbers
	}
}

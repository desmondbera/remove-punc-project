package com.desmond.remove_punctuation_project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemovePunctuation {

	public static String line;
	public static int punctuationCount = 0;
	public static ArrayList<String> puncArr = new ArrayList<>();
	public static ArrayList<String> updatedLine = new ArrayList<>();
	public static boolean hasLineBeenUpdated = false;
	
	public static void main(String[] args) {
		System.out.println("Initializing Remove Punctuation Program.");
		getUserInput();
		
	}
	
	public static void getUserInput() {
		System.out.println("Please enter a punctuation character to remove: ");
		
		try(
			InputStreamReader in = new InputStreamReader(System.in);
			BufferedReader buffer = new BufferedReader(in)) {
			while((line = buffer.readLine()) != null) {
//				System.out.println("Our line is currently: " + line);
				if(isValidInput(line)) {
					//1. check if it already exists (already doing that in our isValidInput Method)
					//2. Add to our array
					puncArr.add(line);
					punctuationCount++;
					//3. Send line to our method that cleans out the file
//					System.out.println("Sending our line to a new function to edit txt file: " + line);
					editFile(line);
					if(punctuationCount == 7) {
						createNewFile();
						System.out.println("DONE. Check for a new file!");
						break;
					}
				}
//				System.out.println("puncArr is: " + puncArr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
	public static boolean isValidInput(String input) {
//		System.out.println("Validating input. Our input is: " + input);
		
		boolean isValid = false;
		boolean dupes = false;
		
//		String regExp = "[.?!,;:{}\\[\\]()\"]";
		String regExp = "[.?!,;:\"]";
		Pattern pattern = Pattern.compile(regExp);
		Matcher match = pattern.matcher(input);
		boolean matches = match.matches();
//		System.out.println("our matches is: " + matches);
		if(matches) {
			isValid = true;
		}
		
		if(!dupes) {
			for(String x : puncArr) {
				if(x.contentEquals(input)) {
					dupes = true;
					break;
				}
			}
		}
		//While our user input is the FALSE type we keep asking for userInput
		while(dupes || matches == false) {
			Scanner retryUserInput = new Scanner(System.in);
			if(dupes) {
				System.out.println("Your input was already selected. Please enter a new valid punctuation character: ");
			} else {
				System.out.println("Your input was not a valid punctuation. Please enter a valid punctuation character: ");
			}
			String retryUserText = retryUserInput.next();
			Matcher retryMatch = pattern.matcher(retryUserText);
			boolean retryMatches = retryMatch.matches();
//			System.out.println("Our retry matches is: " + retryMatches);
//			System.out.println("Our dupes is: " + dupes);
			for(String x : puncArr) {
				if(x.contentEquals(retryUserText)) {
					dupes = true;
					break;
				} else {
					dupes = false;
				}
			}
			if(retryMatches && !dupes) {
//				System.out.println("our retryMatch is true and dupes is false");
				isValid = true;
				matches = retryMatches;
				line = retryUserText;
				break;
			}
		}
		
		return isValid && !dupes;
	}
	
	
	public static void editFile(String currChar) throws FileNotFoundException {
//		System.out.println("Inside edit file...");
//		System.out.println("Our current character is: "+ currChar);
		
		//1. read the line using scanner / buffered read. Remember the whole text must be one line
		BufferedReader reader;
		try {
			// TODO: Add validation if the input txt file is empty.
			
			// This will run on the first input and go thru the text.txt file
			if(!hasLineBeenUpdated) {
				reader = new BufferedReader(new FileReader("text.txt"));
				String line = reader.readLine();
				// TODO: Add validation if the input txt file is empty.
				String[] lineArr = line.split(" ");
				for(int x = 0; x < lineArr.length; x++) {
	//				System.out.println("X is: " + lineArr[x].charAt(lineArr[x].length() - 1));
					String lastCharInStr = Character.toString(lineArr[x].charAt(lineArr[x].length() - 1));
	//				char lastCharInStr = lineArr[x].charAt(lineArr[x].length() - 1);
//					System.out.println("Our last char in our current str is: " + lastCharInStr);
					if(currChar.equals(lastCharInStr)) {
//						System.out.println("Found a match at position "+ x);
						// Trim the end of the array and push it to the new array in the global scope.
//						System.out.println("lineArr[x] is: " + lineArr[x]);
						StringBuilder builder = new StringBuilder(lineArr[x]);
						String newStr = builder.deleteCharAt(lineArr[x].length() - 1).toString();
//						System.out.println("builder after removing last char: " + newStr);
						updatedLine.add(newStr);
					} else {
						// don't trim and just push the regular item at index to the new array in global scope
						updatedLine.add(lineArr[x]);
					}
					
				}
				hasLineBeenUpdated = true;
			} else {
				// This will run once we already have filled out updatedLine array 
				// and we will loop thru that instead of going thru the text file
//				System.out.println("+++Inside else bc we already have an array to go thru+++");
				for(int x = 0; x < updatedLine.size(); x++) {
//					System.out.println("x is: " + updatedLine.get(x).charAt(updatedLine.get(x).length() -1));
					String lastCharInStr = Character.toString(updatedLine.get(x).charAt((updatedLine.get(x).length() - 1)));
					if(currChar.equals(lastCharInStr)) {
//						System.out.println("found a match at positon: " + x);
//						System.out.println("updatedLine.get(x) is:" + updatedLine.get(x));
						StringBuilder builder = new StringBuilder(updatedLine.get(x));
						String newStr = builder.deleteCharAt(updatedLine.get(x).length() - 1).toString();
//						System.out.println("Our new str after deleting char at : "+ newStr);
						updatedLine.set(x, newStr);
					}
				}
			}
			
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
//		System.out.println("Our updatedLine arr is: " + updatedLine);
//		System.out.println("Our punctCount is: " + punctuationCount);
		
		//2. split the line using the CURRENT CHARACTER 
		//3. re-build the line without the CURRENT CHARACTERS
		//4. write to a NEW file using the NEW UPDATED LINE
		
	}
	
	public static void createNewFile() {
//		System.out.println("Inside create new file!");
		try {
			PrintWriter writer = new PrintWriter("new-text.txt", "UTF-8");
			for(String x : updatedLine) {
//				System.out.println("Our x inside create new file is: " + x);
				writer.write(x.toString());
				writer.write("\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
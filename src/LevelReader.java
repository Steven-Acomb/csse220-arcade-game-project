

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Class: LevelReader
 * 
 * @author freynejk <br>
 *         Purpose: Reads a level from file Restrictions: Only able to take in
 *         files in our format <br>
 *         For Example:
 * 
 *         <pre>
 *LevelReader reader = new LevelReader()
 *         </pre>
 */
public class LevelReader {

	private int numLevel;
	private int maxLevel;

	/**
	 * ensures: intializes the levelReader to a currentLevel of 1 and a maximumLevel
	 * of 5
	 */
	public LevelReader() {
		this.numLevel = 1;
		this.maxLevel = 5; // this should be the number of txt files in levels folder // don't forget^^
	}

	/**
	 * Reads the level from the file
	 * 
	 * @param inputFile the file address, i.e. "levels/lvl_1"
	 * @return a single String that contains a Level's data
	 * @throws IllegalFileFormatException
	 */
	public String readFile(String inputFile) throws IllegalFileFormatException {
		Scanner scanner = null;
		String lvlSet = "";
		// File f1 = null;
		InputStream f1 = this.getClass().getClassLoader().getResourceAsStream(inputFile);
		// f1 = new File(inputFile);
		try {
			scanner = new Scanner(f1);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lvlSet += line + '|';
			}
			if (!lvlSet.contains("P")) {
				throw new IllegalFileFormatException("Invalid file format");
			}
			return lvlSet;
		} catch (IllegalFileFormatException e) {
			System.out.println("Incorrect File Format");
		} finally {
			if (scanner != null)
				scanner.close();
		}
		return " ";

		// end try-catch
	}

	/**
	 * Runs the ReadFile() Method with an input constructed from the current
	 * numLevel and a folder address
	 * 
	 * @return a single String that contains a Level's data
	 * @throws IllegalFileFormatException
	 */
	public String runReadFiles() throws IllegalFileFormatException {
		// String inputFile = "levels/level" + this.numLevel;
		String inputFile = "level" + this.numLevel;
		return readFile(inputFile);

	}

	/**
	 * Ups the current level count by 1 and runs the ReadFiles method
	 * 
	 * @throws IllegalFileFormatException
	 */
	public void levelUp() throws IllegalFileFormatException {
		if(this.numLevel < 5)
			this.numLevel = this.numLevel + 1;
		this.runReadFiles();
	}

	/**
	 * Drops the current level count by 1 and runs the ReadFiles method
	 * 
	 * @throws IllegalFileFormatException
	 */
	public void levelDown() throws IllegalFileFormatException {
		if(this.numLevel > 1)
		this.numLevel = this.numLevel - 1;
		this.runReadFiles();

	}

	/**
	 * @return the current Level
	 */
	public int getLvlNum() {
		return this.numLevel;
	}

	/**
	 * 
	 * @return the max Level
	 */
	public int getLvlMax() {
		return this.maxLevel;
	}

}

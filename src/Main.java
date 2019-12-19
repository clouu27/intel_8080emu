import java.util.*;
import java.io.*;
import Cpu.*;

public class Main
{
	///   ROM LENGTH   ///
	public static final int PROGRAM_LENGTH = 0xffff;

	///   SPLIT ROMS LIST   ///
	static final String[] romName = {
		"invaders.h",
		"invaders.g",
		"invaders.f",
		"invaders.e"
		//"cpudiag.bin"
	};
	
	///   LOAD ADDRESS   ///
	static final int[] romAddr = {
		0x0000,
		0x0800,
		0x1000,
		0x1800
		//0x0100
	};
	
	
	private static CpuEmulation cpu;
	
	final static String STORAGE_INTERNAL = "~/src/";	// Change file's path
	final static String FILE_NAME = "invaders";
	// final static String FILE_NAME = "cpudiag.bin";
	
	// MAIN
	public static void main(String[] args) {
		startEmulator(FILE_NAME);
	}
	
	// EMULATION
	public static void startEmulator(String romName) {
		boolean isSplit = true;
		
		if(fileExist(isSplit)) {
			System.out.println("File online!");
		} else {
			System.out.println("File could not be found!");
			return;
		}
		
		initRom(romName);
		cpu = new CpuEmulation(loadRom(isSplit));
	}
	
	// ROM META
	private static void initRom(String romName) {
			RomInfo.title = romName;
			RomInfo.length = PROGRAM_LENGTH;
	}
	
	// LOAD ROM
	private static int[] loadRom(boolean isSplit) {
		// Prepare empty container
		int[] holder = new int[PROGRAM_LENGTH];
		
		if (isSplit) {
			
			for(int i = 0; i < romName.length; i++) {
				InputStream file = openFile(romName[i]);
				int readFile = 0;
				int currentAddr = romAddr[i];
			
				try	{
					int counter = 0;
				
					while ((readFile = file.read()) != -1) {
						holder[currentAddr + counter] = readFile;
						counter++;
					}
				
				} catch (IOException e) {
					System.out.println(romAddr[i] + " cannot be read!");
					return null;
				}
			}
			
		} else {
			try {
				InputStream file = openFile(FILE_NAME);
				int readFile = 0;
				int counter = 0;
				
				while ((readFile = file.read()) != -1) {
					holder[counter] = readFile;
					counter++;
				}

			} catch (IOException e) {
				System.out.println(romName + " cannot be read!");
				return null;
			}
		}
		
		return holder;
	}
	
	// FILE EXISTENCE CHECK
	private static boolean fileExist(boolean isSplit) {
		if(isSplit) {
			for(int i = 0; i < romName.length; i++) {
				
				if (openFile(romName[i]) == null) {
					return false;
				}
			}
		} else {
			
			if (openFile(FILE_NAME) == null) {
				return false;
			}
		}
		
		return true;
	}
	
	// FILE SUBROUTINE
	private static InputStream openFile(String romName) {
		try {
			return new FileInputStream(STORAGE_INTERNAL + romName);
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	// ACTUAL ROM FILELENGTH
	private static int getFileLength(String romName) {
		InputStream file = openFile(romName);
		int size = 0;
		
		try
		{
	
			while (file.read() != -1) {
				size++;
			}
			
		} catch (IOException e) {
			return -1;
		}

		return size;
	}
}

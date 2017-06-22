import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ALUtest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	ALU alu = new ALU();
	
	@Test
	public void integerRepresentationTest() {
		assertEquals(alu.integerRepresentation("-2", 12) , "111111111110");
		assertEquals(alu.integerRepresentation("0", 5), "00000");
		assertEquals(alu.integerRepresentation("-1", 5), "11111");
		assertEquals(alu.integerRepresentation("5", 5), "00101");
		assertEquals(alu.integerRepresentation("-7", 8), "11111001");
	}

	@Test
	public void integerTrueValueTest() {
		ALU alu = new ALU();
		assertEquals(alu.integerTrueValue("11111111"), "-1");
		assertEquals(alu.integerTrueValue("0"),"0");
	    assertEquals(alu.integerTrueValue("010001"),"17");
		assertEquals(alu.integerTrueValue("11111001"), "-7");
		assertEquals(alu.integerTrueValue("0011"), "3");
		assertEquals(alu.integerTrueValue("1"), "-1");
		assertEquals(alu.integerTrueValue("111111"), "-1");
	}
	
	@Test
	public void negationTest() {
		ALU alu = new ALU();
		assertEquals(alu.negation("00001001"), "11110110");
		assertEquals(alu.negation("11111"), "00000");
		assertEquals("11111",alu.negation("00000"));
		assertEquals("00101",alu.negation("11010"));
		assertEquals("11111001",alu.negation("00000110"));
	} 
	
	@Test
	public void leftShiftTest(){
		ALU alu = new ALU();
		assertEquals(alu.leftShift("10100001", 2), "10000100");
		assertEquals("00000",alu.leftShift("01111", 5));
		assertEquals("11111",alu.leftShift("11111", 0));
		assertEquals("00100",alu.leftShift("00010", 1));
		assertEquals("11111000",alu.leftShift("01011111", 3));
	}
	
	@Test
	public void logRightShiftTest(){
		ALU alu = new ALU();
		assertEquals(alu.logRightShift("11110110", 2), "00111101");
		assertEquals("010111001",alu.logRightShift("101110011", 1));
		assertEquals("11111",alu.logRightShift("11111", 0));
		assertEquals("00001",alu.logRightShift("00010", 1));
		assertEquals("00000",alu.logRightShift("00001", 3));
		assertEquals("00010",alu.logRightShift("10001", 3));
		
	}
	
	@Test
	public void ariRightShiftTest(){
		ALU alu = new ALU();
		assertEquals(alu.ariRightShift("11110110", 2), "11111101");
		assertEquals("110111001",alu.ariRightShift("101110011", 1));
		assertEquals("11111",alu.ariRightShift("11111", 0));
		assertEquals("00001",alu.ariRightShift("00010", 1));
		assertEquals("00000",alu.ariRightShift("00001", 3));
	}
	
	@Test
	public void fullAdderTest(){
		ALU alu = new ALU();
		assertEquals(alu.fullAdder('1', '1', '0'), "10");
		assertEquals(alu.fullAdder('0', '0', '0'), "00");
		assertEquals(alu.fullAdder('1', '0', '0'), "01");
		assertEquals(alu.fullAdder('1', '1', '0'), "10");
		assertEquals(alu.fullAdder('0', '0', '1'), "01");
		assertEquals(alu.fullAdder('1', '0', '1'), "10");
		assertEquals(alu.fullAdder('1', '1', '1'), "11");
	}
	
	@Test
	public void claAdderTest(){
		assertEquals(alu.claAdder("1111", "0001", '0'),	"10000");
		assertEquals(alu.claAdder("1001", "0011", '0'), "01100");
		assertEquals(alu.claAdder("1001", "0001", '1'), "01011");
	}
	
	@Test
	public void oneAdderTest(){
		assertEquals(alu.oneAdder("00001001"), "000001010");
		assertEquals(alu.oneAdder("01111111"), "110000000");
		assertEquals(alu.oneAdder("11111111"), "000000000");
	}
	
	@Test
	public void AdderTest(){
		assertEquals(alu.adder("0100","0011",'0',8), "000000111");
	}

	@Test
	public void integerAdditionTest(){
		assertEquals(alu.integerAddition("1000","0111",8), "011111111");
		assertEquals("00100",alu.integerAddition("0100","0000",4));
		assertEquals("10101",alu.integerAddition("1000","1101",4));
	}
	
	@Test
	public void integerSubtractionTest(){
		assertEquals(alu.integerSubtraction("0100", "0011", 8),	"000000001");
		assertEquals("11011",alu.integerSubtraction("0101","1010",4));
		assertEquals("00100",alu.integerSubtraction("0111","0011",4));
		assertEquals("00001",alu.integerSubtraction("0001","0000",4));
		assertEquals("11000",alu.integerSubtraction("0111","1111",4));
		assertEquals("10011",alu.integerSubtraction("1001","0110",4));
	}
	
	@Test
	public void integerMultiplicationTest(){
		assertEquals(alu.integerMultiplication("1111", "1000", 8), "000001000");
		assertEquals("00110",alu.integerMultiplication("0011", "0010", 4));
		assertEquals("01000",alu.integerMultiplication("1110", "0100", 4));
		assertEquals("11000",alu.integerMultiplication("0100", "0010", 4));
		assertEquals("00110",alu.integerMultiplication("1101", "1110", 4));
	}
	
	@Test
	public void integerDivisionTest(){
		assertEquals(alu.integerDivision("0100", "0011", 8),"00000000100000001");
		assertEquals("000100001",alu.integerDivision("0111", "0011", 4));
		assertEquals("011100001",alu.integerDivision("0111", "1101", 4));
		assertEquals("011101111",alu.integerDivision("1001", "0011", 4));
		assertEquals("000101111",alu.integerDivision("1001", "1101", 4));
	//  assertEquals("000110000",alu.integerDivision("1111", "1111", 4));
	  /*assertEquals("000110000",alu.integerDivision("1010", "1110", 4));
		assertEquals("011010000",alu.integerDivision("0110", "1110", 4));
		assertEquals("011010000",alu.integerDivision("1010", "0010", 4));*/
	}
	
	@Test
	public void floatTrueValueTest() {
		assertEquals("0",alu.floatTrueValue("0"+ "00000000"+ "00000000000000000000000", 8, 23));
		assertEquals("0",alu.floatTrueValue("1"+ "00000000"+ "00000000000000000000000", 8, 23));
		assertEquals("+Inf",alu.floatTrueValue("0"+ "11111111"+ "00000000000000000000000", 8, 23));
		assertEquals("-Inf",alu.floatTrueValue("1"+ "11111111"+ "00000000000000000000000", 8, 23));
		assertEquals("NaN",alu.floatTrueValue("0"+ "11111111"+ "00000000000000000000100",  8, 23));
		assertEquals("NaN",alu.floatTrueValue("1"+ "11111111"+ "00000001111000000000001",  8, 23));
		assertEquals("384.1300048828125",alu.floatTrueValue("0"	+ "10000111"+ "10000000001000010100100", 8, 23));
		assertEquals("85.625",alu.floatTrueValue("0"+ "100101"+ "010101101",6,9));
		assertEquals("-65.25",alu.floatTrueValue("1"+ "10000101"+ "00000101000000000000000", 8, 23));
	}
	
	@Test
	public void floatRepresentationTest() {
		assertEquals("11000000111000000000000000000000",alu.floatRepresentation("-7",8,23));
		assertEquals("10111111110000000000000000000000",alu.floatRepresentation("-1.5", 8, 23));
	    assertEquals("01000011110000000001000010100011",alu.floatRepresentation("384.13", 8,23));          
		assertEquals("00111111010000000000000000000000",alu.floatRepresentation("0.75", 8,23));
		assertEquals("11000010100000101000000000000000",alu.floatRepresentation("-65.25", 8,23));
		assertEquals("0100110101100101",alu.floatRepresentation("217.36", 6,9));
		assertEquals("0100010000110010",alu.floatRepresentation("8.79", 6,9));
		assertEquals("0100101010101101",alu.floatRepresentation("85.643", 6,9));
		assertEquals("0011101011110101",alu.floatRepresentation("0.37", 6,9));
		assertEquals("0100100100111010",alu.floatRepresentation("51.658", 6,9));
		assertEquals("0100100100111001",alu.floatRepresentation("51.607", 6,9));
		assertEquals("0000000000000000",alu.floatRepresentation("0", 6,9));
		assertEquals("0100000000100000000000000000000000000000000000000000000000000000",alu.floatRepresentation("8", 11 ,52));
//		assertEquals("01111111100000000000000000000000",alu.floatRepresentation(Double.MAX_VALUE+"", 23, 8));
//		assertEquals("01111111100000000000000000000000",alu.floatRepresentation(Double.MAX_VALUE/2+"", 23, 8));
//		assertEquals("01111111100000000000000000000000",alu.floatRepresentation(Double.MAX_VALUE/100+"", 23, 8));
//		assertEquals("11111111100000000000000000000000",alu.floatRepresentation(-Double.MAX_VALUE+"", 23, 8));
		assertEquals("01111111100000000000000000000001",alu.floatRepresentation("NaN", 8, 23));
//		assertEquals("00000000100000000000000000000000",alu.floatRepresentation(Float.MIN_NORMAL+"", 23, 8));
//		assertEquals("00000000000000000000000000000010",alu.floatRepresentation(Float.MIN_VALUE*2+"", 23, 8));
	}
	
	@Test
	public void aluIeee754Test(){
		assertEquals(alu.ieee754("11.375", 32), "01000001001101100000000000000000");
	}
	
	@Test
	public void aluSignedAdditionTest(){
		assertEquals(alu.signedAddition("1100", "1011", 8),"0100000111");
	}
	
	@Test
	public void floatAdditionTest(){
		assertEquals(alu.floatAddition("00111111001000000", "00111111010100000", 8, 8, 4),"000111111101110000");
		assertEquals(alu.floatAddition("10111110111000000000000000000000", "00111111000000000000000000000000", 8, 23, 9),"000111101100000000000000000000000");
		assertEquals(alu.floatAddition("11000010100000101000000000000000", "00111111010000000000000000000000", 8, 23, 9),"011000010100000010000000000000000");
		assertEquals(alu.floatAddition("11000010100000101000000000000000", "01000010100000101000000000000000", 8, 23, 9), "000000000000000000000000000000000");
		assertEquals(
				"0"+alu.floatRepresentation("6",6,9),
				alu.floatAddition(
						alu.floatRepresentation("2.25",6,9), 
						alu.floatRepresentation("3.75",6,9)
						, 6,9, 0));
		assertEquals(
				"0"+alu.floatRepresentation("0",8,23),
				alu.floatAddition(
						alu.floatRepresentation("-4",8,23), 
						alu.floatRepresentation("4",8,23)
						,8,23, 0));
		/*assertEquals(
				"0"+alu.floatRepresentation("1.4",8,23),
				alu.floatAddition(
						alu.floatRepresentation("1.1",8,23), 
						alu.floatRepresentation("0.3",8,23)
						,8,23,4));*/
		assertEquals(
				"0" + alu.floatRepresentation("7.368", 8, 23),
				alu.floatAddition(
						alu.floatRepresentation("1.256", 8, 23), 
						alu.floatRepresentation("6.112", 8, 23)
						, 8, 23, 0));
		assertEquals(
				"0"+alu.floatRepresentation("-7.368", 23, 8),
				alu.floatAddition(
						alu.floatRepresentation("-1.256", 23, 8), 
						alu.floatRepresentation("-6.112", 23, 8)
						, 23, 8, 0));
		assertEquals(
				"0"+alu.floatRepresentation("-64.5",8,23),
				alu.floatAddition(
						alu.floatRepresentation("0.75",8,23), 
						alu.floatRepresentation("-65.25",8,23)
						,8,23,9));
		assertEquals(
				"0"+alu.floatRepresentation("-63.5", 23, 8),
				alu.floatAddition(
						alu.floatRepresentation("1.75", 23, 8), 
						alu.floatRepresentation("-65.25", 23, 8)
						, 23, 8, 0));
		assertEquals(
				"0"+alu.floatRepresentation("0.0625", 6,9),
				alu.floatAddition(
						alu.floatRepresentation("51.658", 6,9), 
						alu.floatRepresentation("-51.607", 6,9)
						, 6,9, 0));
		assertEquals(
				"000111111101110000",
				alu.floatAddition(
						"00111111010100000", 
						"00111111001000000"
						,8,8, 0));
		assertEquals(
				"0"+alu.floatRepresentation("85.875",6,9),
				alu.floatAddition(
						alu.floatRepresentation("85.643", 6,9), 
						alu.floatRepresentation("0.37",6,9)
						,6,9, 0));
		assertEquals(
				"0"+alu.floatRepresentation("208.49", 6,9),
				alu.floatAddition(
						alu.floatRepresentation("217.36", 6,9), 
						alu.floatRepresentation("-8.79", 6,9)
						, 6,9, 20));
		assertEquals(
				"0"+alu.floatRepresentation("0.9375",8,23),
				alu.floatAddition(
						alu.floatRepresentation("0.5",8,23), 
						alu.floatRepresentation("0.4375",8,23)
						,8,23, 0));
		assertEquals(
				"0"+alu.floatRepresentation("208.5", 6,9),
				alu.floatAddition(
						alu.floatRepresentation("217.36", 6,9), 
						alu.floatRepresentation("-8.79", 6,9)
						, 6,9, 0));
	}
	
	@Test
	public void floatSubtractionTest(){
		assertEquals(alu.floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 4), "000111110010000000");
		assertEquals(alu.floatSubtraction("00111111010000000000000000000000", "11000010100000101000000000000000", 8, 23, 9),"001000010100001000000000000000000");
		assertEquals(
				"0"+alu.floatRepresentation("3", 8,23),
				alu.floatSubtraction(
						alu.floatRepresentation("1",8,23), 
						alu.floatRepresentation("-2",8,23)
						,8,23, 0));
		assertEquals(
				"0"+alu.floatRepresentation("0.9375",8,23),
				alu.floatSubtraction(
						alu.floatRepresentation("0.5",8,23), 
						alu.floatRepresentation("-0.4375",8,23)
						,8,23, 0));
		assertEquals(
				"0"+alu.floatRepresentation("0.0625",8,23),
				alu.floatSubtraction(
						alu.floatRepresentation("0.5",8,23), 
						alu.floatRepresentation("0.4375",8,23)
						,8,23,0));
		assertEquals(
				"0"+alu.floatRepresentation("85.825", 9, 6),
				alu.floatSubtraction(
						alu.floatRepresentation("85.643", 9, 6), 
						alu.floatRepresentation("-0.37", 9, 6)
						, 9, 6, 6));
		assertEquals(
				"0"+alu.floatRepresentation("7.368", 23, 8),
				alu.floatSubtraction(
						alu.floatRepresentation("1.256", 23, 8), 
						alu.floatRepresentation("-6.112", 23, 8)
						, 23, 8, 0));
		assertEquals(
				"0"+alu.floatRepresentation("-7.368", 23, 8),
				alu.floatSubtraction(
						alu.floatRepresentation("-1.256", 23, 8), 
						alu.floatRepresentation("6.112", 23, 8)
						, 23, 8, 0));
		assertEquals(
				"0"+alu.floatRepresentation("-64.5", 23, 8),
				alu.floatSubtraction(
						alu.floatRepresentation("0.75", 23, 8), 
						alu.floatRepresentation("65.25", 23, 8)
						, 23, 8, 0));
		assertEquals(
				"0"+alu.floatRepresentation("85.875", 9, 6),
				alu.floatSubtraction(
						alu.floatRepresentation("85.643", 9, 6), 
						alu.floatRepresentation("-0.37", 9, 6)
						, 9, 6, 0));
		assertEquals(
				"0"+alu.floatRepresentation("-63.5", 8,23),
				alu.floatSubtraction(
						alu.floatRepresentation("1.75",8,23), 
						alu.floatRepresentation("65.25",8,23)
						,8,23, 0));
	}
	
	@Test
	public void floatMultiplicationTest(){
		assertEquals(alu.floatMultiplication("00111110111000000", "00111111000000000", 8, 8), "000111110011000000");
		assertEquals("0"+alu.floatRepresentation("0.21875",8,23),alu.floatMultiplication(
				alu.floatRepresentation("0.5", 8,23), 
				alu.floatRepresentation("0.4375",8,23),8,23));
		assertEquals("0"+alu.floatRepresentation("0.328125",8,8),alu.floatMultiplication(
				alu.floatRepresentation("0.75",8,8), 
				alu.floatRepresentation("0.4375", 8,8),8,8));
		assertEquals("0"+alu.floatRepresentation("-0.328125", 8, 23),alu.floatMultiplication(
				alu.floatRepresentation("-0.75", 8, 23), 
				alu.floatRepresentation("0.4375", 8, 23), 8, 23));
		assertEquals("0"+alu.floatRepresentation("0.328125", 23, 8),alu.floatMultiplication(
				alu.floatRepresentation("-0.75", 23, 8), 
				alu.floatRepresentation("-0.4375", 23, 8), 23, 8));
		assertEquals("0"+alu.floatRepresentation("-0.328125", 23, 8),alu.floatMultiplication(
				alu.floatRepresentation("0.75", 23, 8), 
				alu.floatRepresentation("-0.4375", 23, 8), 23, 8));
		assertEquals("0"+alu.floatRepresentation("-48.9375", 23, 8),alu.floatMultiplication(
				alu.floatRepresentation("0.75", 23, 8), 
				alu.floatRepresentation("-65.25", 23, 8), 23, 8));
		assertEquals(
				"1"+"0"+ "11111111"+ "0000"+ "0000"+ "0000"+ "0000"+ "0000"+ "000",
				alu.floatMultiplication(
						"0"+ "11111111"+ "000"	+ "0000"+ "0000"+ "0000"+ "0000"+ "0000",
						"1"+ "11000000"+ "000"	+ "0000"+ "0000"+ "0000"+ "0000"+ "0000",8,23));
		/*assertEquals(
				"1"+"0"+ "11111111"+ "0000"+ "0000"+ "0000"+ "0000"+ "0000"+ "000",
				alu.floatMultiplication(
						"0"+ "11111111"+ "100"	+ "0000"+ "0000"+ "0000"+ "0000"+ "0000",
						"0"+ "11000000"+ "000"	+ "0000"+ "0000"+ "0000"+ "0000"+ "0000", 8,23));*/
	}
	
	@Test
	public void floatDivisionTest(){
		assertEquals(alu.floatDivision("00111110111000000", "00111111000000000", 8, 8), "000111111011000000");
		assertEquals(
				"0"+alu.floatRepresentation("-0.01149425283074379",8,23),
				alu.floatDivision(						
						alu.floatRepresentation("0.75", 8,23), 
						alu.floatRepresentation("-65.25",8,23),8,23));
		assertEquals(
				"0"+alu.floatRepresentation("0.875", 8, 23),
				alu.floatDivision(
						alu.floatRepresentation("-0.4375", 8, 23), 
						alu.floatRepresentation("-0.5", 8, 23), 8, 23));
		assertEquals(
				"0"+alu.floatRepresentation("-0.875", 8, 23),
				alu.floatDivision(
						alu.floatRepresentation("-0.4375", 8, 23), 
						alu.floatRepresentation("0.5", 8, 23), 8, 23));
		assertEquals(
				"0"+alu.floatRepresentation("-0", 8, 23),
				alu.floatDivision(
						alu.floatRepresentation("0", 8, 23), 
						alu.floatRepresentation("-0.5", 8, 23), 8, 23));
		assertEquals(
				"111111111100000000000000000000000",
				alu.floatDivision(
						alu.floatRepresentation("-0.75", 8,23), 
						alu.floatRepresentation("0",8,23), 8,23));
		assertEquals(
				"0"+"1"+ "01111000"+ "011"	+ "1100"+ "0101"+ "0010"+ "0110"+ "0100",
				alu.floatDivision(
						alu.floatRepresentation("0.75",8,23), 
						alu.floatRepresentation("-65.25",8,23), 8,23));
		assertEquals(
				"0"+alu.floatRepresentation("-0.875", 8, 23),
				alu.floatDivision(
						alu.floatRepresentation("0.4375", 8, 23), 
						alu.floatRepresentation("-0.5", 8, 23), 8, 23));
		assertEquals(
				"1"+"0"+ "11111111"
						+ "00000000000000000000000",
				alu.floatDivision(
						alu.floatRepresentation("0.75", 8,23), 
						alu.floatRepresentation("0", 8,23), 8,23));
	}
}


public class RunningOne {
	public static void main(String[] args){
		ALU alu = new ALU();
		
		//String s = alu.floatRepresentation("-0.01149425283074379",8,23);
		//String t = alu.floatRepresentation("0.75", 8,23);
		//String r = alu.floatRepresentation("-65.25",8,23);  // s = t / r
		//System.out.println(s +"\n" +t + "\n" +r + "\n");
		
		System.out.println(alu.integerDivision("0111", "0011", 4));
	}
}

//1 01111110 11000000000000000000000
//0 01111101 11000000000000000000000   -2
//1 01111110 00000000000000000000000   -1


//1 01111000 01111000101001001100100   -7
//0 01111110 10000000000000000000000   -1
//1 10000101 00000101000000000000000   6

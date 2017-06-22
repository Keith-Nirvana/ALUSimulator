/**
 * ģ��ALU���������͸���������������
 * 161250004 �ܼ���
 *
 */

public class ALU {
	
	/**
	 * ����ʮ���������Ķ����Ʋ����ʾ��<br/>
	 * ����integerRepresentation("9", 8)
	 * @param number ʮ������������Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length �����Ʋ����ʾ�ĳ���
	 * @return number�Ķ����Ʋ����ʾ������Ϊlength
	 */
	public String integerRepresentation (String number, int length) {
		int theNumber = Integer.parseInt(number);
		String result = "";
		
		for(int i = length - 1; i >= 0; i--){
			result = result + (1 & (theNumber >> i));
		}
		
		return result;
	}
	
	/**
	 * ����ʮ���Ƹ������Ķ����Ʊ�ʾ��
	 * ��Ҫ���� 0������񻯡����������+Inf���͡�-Inf������ NaN�����أ������� IEEE 754��
	 * �������Ϊ��0���롣<br/>
	 * ����floatRepresentation("11.375", 8, 11)
	 * @param number ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return number�Ķ����Ʊ�ʾ������Ϊ 1+eLength+sLength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {
	
		boolean nanFlag = false;
		boolean infFlag = false;
		boolean InfFlag = false;     //Υ��������������������жϲ���number�ǲ���ֱ��Ϊ��Inf��

		
		//�ж��Ƿ�Ϊ�������----------------------------------------------
		if(number.equals("+Inf") || number.equals("-Inf")){
			InfFlag = true;
		}
		else{
			for(int i = 0; i < number.length(); i++)
				if( !((number.charAt(i) >= '0') && (number.charAt(i) <= '9')) && (number.charAt(i) != '.') && (number.charAt(i) != '-') ){
					nanFlag = true;
					break;
				}
		}
		
		String flag;                 //����λ�������涨��
		String[] operand;
		long theInteger;              //��������
		double theDecimal;           //С������
		double theNumber;            //�����ֵľ���ֵ��С
		
		int bias = (int)Math.pow(2, eLength - 1) - 1; 
		double norMax;  double denorMax;   double denorMin;               //������ֵ�������Сֵ���ǹ��
		String result = "";
		String finalResult = "";
		
		
		//�������--------------------------------------------------------
		if((InfFlag == false) && (nanFlag == false)){
			flag = (number.charAt(0) == '-') ? "1" : "0";
			
			if(flag.equals("1"))
				operand = number.substring(1).split("\\.");
			else
				operand = number.split("\\.");                           //�Է���λ�Ĵ���
			
			
			if(operand.length == 2){
				theInteger = Long.parseLong(operand[0]);
				theDecimal = Double.parseDouble("0." + operand[1]);
			}else{
				theInteger = Long.parseLong(operand[0]);
				theDecimal = Double.parseDouble("0");
			}
			theNumber = Math.abs(Double.parseDouble(number));
			//System.out.println(theInteger +" "+ theDecimal + " "+theNumber);
			
			norMax = Double.parseDouble(floatTrueValue("0" + getString("1", eLength - 1) + "0" + getString("1", sLength), eLength, sLength));
			denorMax = Double.parseDouble(floatTrueValue("0" + getString("0", eLength) + getString("1", sLength), eLength, sLength));
			denorMin = Double.parseDouble(floatTrueValue("0" + getString("0", eLength) + getString("0", sLength - 1) + "1", eLength, sLength));
			
			
			if(theNumber > norMax){
				
				infFlag = true;
			}
			else if(theNumber > denorMax){
				//������ֵĴ���
				
				
				//��������--------------------------------------
				for(int i = 63; i >= 0; i--){
					result = result + (1 & (theInteger >> i));
				}
				
				//С������-------------------------------------
				int counter = 2 * sLength;       //��ֹ����ѭ��
				int index = 0;
				
				while(counter > 0){
					theDecimal = theDecimal * 2;
					
					if(theDecimal >= 1.0){
						result = result + "1";
						theDecimal--;
					}else{
						result = result + "0";
					}
					
					counter--;
				}
				
				//������ȵ���-----------------------------------
				for(int i = 0; i < result.length(); i++){
					if(result.charAt(i) == '1'){
						result = result.substring(i);
						index = i;
						break;
					}
				}
				
				result = result.substring(1, sLength + 1);
				
				//����ָ������-----------------------------------
				int exponent = 63 - index + bias;
				String temp = "";
				
				for(int i = 0; i < eLength; i++)
					temp = (1 & (exponent >> i)) + temp;
				
				finalResult = flag + temp + result;
			}
			else if(theNumber >= denorMin){
				//�ǹ��---------------------------------------
				
				//С������-------------------------------------
				int counter = bias + sLength + 2;       //��ֹ����ѭ��
				
				while(counter > 0){
					theDecimal = theDecimal * 2;
					
					if(theDecimal >= 1.0){
						result = result + "1";
						theDecimal--;
					}else{
						result = result + "0";
					}
					
					counter--;
				}
				
				result = result.substring(bias - 1, bias - 1 + sLength);
				
				finalResult = flag + getString("0", eLength) + result;
			}
			else{
				//0�Ĵ���--------------------------------------
				finalResult = flag + getString("0", eLength) + getString("0", sLength);
			}
		}
		
		//������------------------------------------------------------
		
		if(infFlag == true){
			String signFlag = (number.charAt(0) == '-') ? "1" : "0";
			finalResult = signFlag + getString("1", eLength) + getString("0", sLength);
		}
		else if(InfFlag == true){
			String signFlag = (number.charAt(0) == '-') ? "1" : "0";
			finalResult = signFlag + getString("1", eLength) + getString("0", sLength);
		}
		else if(nanFlag == true){
			finalResult = "0" + getString("1", eLength) + getString("0", sLength - 1) + "1";
		}

		
		return finalResult;
	}
	
	
	/*public String testLength(String str, int length){
		String result = str;
		
		if(str.length() < length){
			for(int i = 0; i < str.length() - length; i++)
				result += "0";
			
			return result;
		}else{
			return result;
		}
	}*/
	
	public String getString(String demand, int length){
		String result = "";
		
		if(demand.equals("0")){
			for(int i = 0; i < length; i++){
				result = result + "0";
			}
		}
		else if(demand.equals("1")){
			for(int i = 0; i < length; i++)
				result = result + "1";
		}
		else{
			result = "Error";
		}
		
		return result;
	}
	
	/**
	 * ����ʮ���Ƹ�������IEEE 754��ʾ��Ҫ�����{@link #floatRepresentation(String, int, int) floatRepresentation}ʵ�֡�<br/>
	 * ����ieee754("11.375", 32)
	 * @param number ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length �����Ʊ�ʾ�ĳ��ȣ�Ϊ32��64
	 * @return number��IEEE 754��ʾ������Ϊlength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String ieee754 (String number, int length) {
		String result = "";
		
		if(length == 32){
			 result = floatRepresentation(number, 8, 23);
		}
		else{
			result = floatRepresentation(number, 11, 52);
		}
		return result;
	}
	
	/**
	 * ��������Ʋ����ʾ����������ֵ��<br/>
	 * ����integerTrueValue("00001001")
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 */
	public String integerTrueValue (String operand) {
		String flag = (operand.charAt(0) == '0') ? "" : "-";
		int len = operand.length();
		int[] binaryArray = new int[len];

		if(flag.equals("-")){
			for (int i = 0; i < len; i++){
				binaryArray[i] = 1 ^ (operand.charAt(i) - 48);
			}
		}else{
			for(int i = 0; i < len; i++){
				binaryArray[i] = operand.charAt(i) - 48;
			}
		}     //��λ���
		
		long longResult = 0;
		for (int i = 0; i < len; i++){
			longResult += Math.pow(2, i) * binaryArray[len - i - 1];
		}     //����Ȩ��
		
		if (flag.equals("-")){
			longResult += 1;
		}    //ȡ����һ
		
		String result = flag + String.valueOf(longResult);
		return result;
	}
	
	/**
	 * ���������ԭ���ʾ�ĸ���������ֵ��<br/>
	 * ����floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ����������ֱ��ʾΪ��+Inf���͡�-Inf���� NaN��ʾΪ��NaN��
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {
		int[] exponent = new int[eLength];
		int[] mantissa = new int[sLength];          //���ڲ��

		int bias = (int)Math.pow(2, eLength-1) - 1;
		int exp = Integer.parseInt(integerTrueValue("0" + operand.substring(1, eLength + 1))) - bias;
		double result = 0.0;
		
		String flag1 = (operand.charAt(0) == '0') ? "" : "-";
		String flag2 = (operand.charAt(0) == '0') ? "+" : "-";
		boolean norFlag = false;
		boolean nanFlag = false;
		boolean infFlag = false;
		boolean poFlag = true;                  //�������õ�
		
		
		for(int i = 0; i < eLength; i++){
			exponent[i] = operand.charAt(i + 1) - 48;
		}
		
		for(int j = 0; j < sLength; j++){
			mantissa[j] = operand.charAt (1 + eLength + j) - 48;
		}
		
		
		//����Ƿ�ΪNaN����������--------------------------------------------
		for(int i = 0; i < eLength; i++){
			if(exponent[i] == 0){
				poFlag = false;
				break;
			}
		}
		
		if(poFlag == true){
			infFlag = true;
			
			for(int i = 0; i < sLength; i++)
				if(mantissa[i] != 0){
					infFlag = false;
					nanFlag = true;
					break;
				}
		}
		
		
		//�ж��Ƿ�Ϊ�ǹ��-------------------------------------------------
		for(int i = 0; i < eLength; i++){
			if(exponent[i] != 0){
				norFlag = true;
				break;
			}
		}
		
		
		//����--------------------------------------------------------------
		
		Transition: if(poFlag == false){
			
			//���
			if(norFlag == true){
				result = 1.0 * Math.pow(2, exp);

				for(int i = 0; i < sLength; i++){
					result = result + Math.pow(2, exp - i - 1) * mantissa[i];
				}
				
				break Transition;
			}
			
			
			//�ǹ��
			if(norFlag == false){
				exp = 1 - bias;
				
				for(int i = 0; i < sLength; i++){
					result = result + Math.pow(2, exp - i - 1) * mantissa[i];
				}
			}
			
			
		}                            //ת������
		
		
		String finalResult = "";
		if(infFlag == true){
			finalResult = finalResult + flag2 + "Inf";
		}
		else if(nanFlag == true){
			finalResult = "NaN";
		}
		else{
			if(result == 0){
				finalResult = "0.0";
			}else{
				finalResult = flag1 + result;
			}
		}
		
		return finalResult;
	}
	
	/**
	 * ��λȡ��������<br/>
	 * ����negation("00001001")
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @return operand��λȡ���Ľ��
	 */
	public String negation (String operand) {
		int len = operand.length();
		String result = "";
		
		for (int i = 0; i < len; i++){
			result = result + ( 1 ^ (operand.charAt(i) - 48) );
		}
		return result;
	}
	
	/**
	 * ���Ʋ�����<br/>
	 * ����leftShift("00001001", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand����nλ�Ľ��
	 */
	public String leftShift (String operand, int n) {
		String result = operand;
		
		while (n > 0){
			String temp = new String("");
			for (int i = 1; i < operand.length(); i++){
				temp = temp + result.charAt(i);
			}
			temp += "0";
			
			result = temp;
			n--;
		}
		
		return result;
	}
	
	/**
	 * �߼����Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand�߼�����nλ�Ľ��
	 */
	public String logRightShift (String operand, int n) {
		String result = operand;
		
		while (n > 0){
			String temp = new String("");
			for (int i = 0; i < operand.length() - 1; i++){
				temp = temp + result.charAt(i);
			}
			temp = "0" + temp;
			
			result = temp;
			n--;
		}
		return result;
	}
	
	/**
	 * �������Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand��������nλ�Ľ��
	 */
	public String ariRightShift (String operand, int n) {
		String result = operand;
		int flag = (operand.charAt(0) == '0') ? 0 : 1;
		
		while (n > 0){
			String temp = new String("");
			for (int i = 0; i < operand.length() - 1; i++){
				temp = temp + result.charAt(i);
			}
			temp = (flag == 0) ? "0" + temp : "1" + temp;
			
			result = temp;
			n--;
		}
		return result;
	}
	
	/**
	 * ȫ����������λ�Լ���λ���мӷ����㡣<br/>
	 * ����fullAdder('1', '1', '0')
	 * @param x ��������ĳһλ��ȡ0��1
	 * @param y ������ĳһλ��ȡ0��1
	 * @param c ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ��ӵĽ�����ó���Ϊ2���ַ�����ʾ����1λ��ʾ��λ����2λ��ʾ��
	 */
	public String fullAdder (char x, char y, char c) {
		String result = "";
		int xC = x - 48;
		int yC = y - 48;
		int cC = c - 48;
		
		int figure = xC ^ yC ^ cC;
		int carry = (xC & yC) | (xC & cC) | (yC & cC);
		
		result = result + carry + figure;
		return result;
	}
	
	/**
	 * 4λ���н�λ�ӷ�����Ҫ�����{@link #fullAdder(char, char, char) fullAdder}��ʵ��<br/>
	 * ����claAdder("1001", "0001", '1')
	 * @param operand1 4λ�����Ʊ�ʾ�ı�����
	 * @param operand2 4λ�����Ʊ�ʾ�ļ���
	 * @param c ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ����Ϊ5���ַ�����ʾ�ļ����������е�1λ�����λ��λ����4λ����ӽ�������н�λ��������ѭ�����
	 */
	public String claAdder (String operand1, String operand2, char c) {
		String result = "";
		char[] op1 = operand1.toCharArray();
		char[] op2 = operand2.toCharArray();
		int[] carry = new int[5];
		int[] p = new int[4];
		int[] g = new int[4];
		
		for(int i = 0; i < 4; i++){
			p[i] = (op1[i] - 48) | (op2[i] - 48);
			g[i] = (op1[i] - 48) & (op2[i] - 48);
		}                                   // ����Ϊ�˻�ȡ��������
		
		carry[3] = c - 48;		
		carry[2] = g[3] | (p[3] & (c - 48));
		carry[1] = g[2] | (p[2] & g[3]) | (p[2] & p[3] & (c - 48));
		carry[0] = g[1] | (p[1] & g[2]) | (p[1] & p[2] & g[3]) | (p[1] & p[2] & p[3] & (c - 48));
		carry[4] = g[0] | (p[0] & g[1]) | (p[0] & p[1] & g[2]) | (p[0] & p[1] & p[2] & g[3]) | (p[0] & p[1] & p[2] & p[3] & (c - 48));

		
		for (int i = 0; i < 4; i++){
			result = result + fullAdder(op1[i], op2[i], (char)(carry[i] + 48)).substring(1);
		}
		result = carry[4] + result;
		
		return result;
	}
	

	
	/**
	 * ��һ����ʵ�ֲ�������1�����㡣
	 * ��Ҫ�������š����š�����ŵ�ģ�⣬
	 * ������ֱ�ӵ���{@link #fullAdder(char, char, char) fullAdder}��
	 * {@link #claAdder(String, String, char) claAdder}��
	 * {@link #adder(String, String, char, int) adder}��
	 * {@link #integerAddition(String, String, int) integerAddition}������<br/>
	 * ����oneAdder("00001001")
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @return operand��1�Ľ��������Ϊoperand�ĳ��ȼ�1�����е�1λָʾ�Ƿ���������Ϊ1������Ϊ0��������λΪ��ӽ��
	 */
	public String oneAdder (String operand) {
		char[] op = operand.toCharArray();
		int[] theCopy = new int[operand.length() + 1];
		
		for (int i = 1; i < theCopy.length; i++){
			theCopy[i] = op[i-1] - 48;
		}
		
		int flag = theCopy[1];   //�ж��Ƿ����
		
		int carry = 1;
		for (int i = theCopy.length - 1; i >= 1; i--){
			int c = carry;
			carry = theCopy[i] & c;
			theCopy[i] = c ^ theCopy[i];
		}
		
		String result = "";
		for (int i = 1; i < theCopy.length; i++)
			result += theCopy[i];
		
		result = (flag != theCopy[1]) && (flag == 0) ? 1 + result : 0 + result; 
		
		return result;
	}
	
	/**
	 * �ӷ�����Ҫ�����{@link #claAdder(String, String, char)}����ʵ�֡�<br/>
	 * ����adder("0100", "0011", ��0��, 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param c ���λ��λ
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String adder (String operand1, String operand2, char c, int length) {		
		String op1 = signExtend(operand1, length);
		String op2 = signExtend(operand2, length);
		int times = length / 4;
		char carry = c;
		String temp = "";
		String result = "";
		
		for (int i = times - 1; i >= 1; i--){
			temp = claAdder(op1.substring(4*i, 4*i+4), op2.substring(4*i, 4*i+4), carry);
			carry = temp.charAt(0);
			result = temp.substring(1) + result;
		}
		
		result = (claAdder(op1.substring(0, 4), op2.substring(0, 4), carry) + result).substring(1);
		
		int flag;
		if(((op1.charAt(0) == '0') && (op2.charAt(0) == '0') && (result.charAt(0) == '1')) || ((op1.charAt(0) == '1') && (op2.charAt(0) == '1') && (result.charAt(0) == '0'))){
			flag = 1;
		}else{
			flag = 0;
		}                     //�ж�������
		
		return flag + result;
	}
	
	
	public String signExtend(String operand, int length){
		String result = operand;
		
		if(operand.length() >= length){
			return operand;
		}else{
			for (int i = 0; i < length-operand.length(); i++)
				result = operand.charAt(0) + result;
			return result;
		}
	}
	
	/**
	 * �����ӷ���Ҫ�����{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerAddition("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		String adderResult = adder(operand1, operand2, '0', length);
		return adderResult;
	}
	
	/**
	 * �����������ɵ���{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerSubtraction("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ��������
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		int len = operand2.length();
		String newOperand2 = "";
		
		for (int i = 0; i < len; i++){
			newOperand2 = newOperand2 + ( 1 ^ (operand2.charAt(i) - 48) );
		}                              //ȡ��
		
		String result = adder(operand1, newOperand2, '1', length);
		return result;
	}
	
	/**
	 * �����˷���ʹ��Booth�㷨ʵ�֣��ɵ���{@link #adder(String, String, char, int) adder}�ȷ�����<br/>
	 * ����integerMultiplication("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ĳ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ����˽�������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����˽��
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {
		int counter = length;   
		String opOperand1 = signExtend(operand1, length);  
		String negOperand1 = oneAdder(negation(signExtend(operand1, length))).substring(1);  
		String result = signExtend(operand2, length) + "0";
		for(int i = 0; i < length; i++)
			result = '0' + result;

		while(counter > 0){
			
			if(result.substring(2*length-1).equals("00") || result.substring(2*length-1).equals("11")){
				result = ariRightShift(result, 1);
			}else if(result.substring(2*length-1).equals("01")){
				result = adder(result.substring(0, length), opOperand1, '0', length).substring(1) + result.substring(length);
				result = ariRightShift(result, 1);
			}else if(result.substring(2*length-1).equals("10")){
				result = adder(result.substring(0, length), negOperand1, '0', length).substring(1) + result.substring(length);
				result = ariRightShift(result, 1);
			}else{
				result = "Wrong condition";
			}

			counter--;
		}
		result = result.substring(0, 2*length);
		
		int flag = 0;
		for (int i = 0; i < length; i++){
			if (result.charAt(length) != result.charAt(i)){
				flag = 1;
				break;
			}
		}                   
		
		result = result.substring(length);
		return flag + result;
	}
	
	/**
	 * �����Ĳ��ָ������������ɵ���{@link #adder(String, String, char, int) adder}�ȷ���ʵ�֡�<br/>
	 * ����integerDivision("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ĳ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊ2*length+1���ַ�����ʾ�������������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0�������lengthλΪ�̣����lengthλΪ����
	 */
	public String integerDivision (String operand1, String operand2, int length) {
		String result = signExtend(operand1, length * 2);
		String poOperand2 = signExtend(operand2, length);
		String negOperand2 = oneAdder(negation(signExtend(operand2, length))).substring(1);   //������2�ĸ�����
		int counter = length + 1;
		char signFlag = result.charAt(0);
		char carry;
		boolean zeroFlag = false;
		
		while (counter > 1){
			
			if (signFlag == operand2.charAt(0) && !zeroFlag){
				result = adder(result.substring(0, length), negOperand2, '0', length).substring(1) + result.substring(length);
			}else{
				result = adder(result.substring(0, length), poOperand2, '0', length).substring(1) + result.substring(length);
				zeroFlag = false;
			}
			
			signFlag = result.charAt(0);
			
			if (signFlag == operand2.charAt(0)){
				carry = '1';
			}else{
				carry = '0';
			}
			
			if(testZero(result.substring(0, length)) && operand1.charAt(0)!=operand2.charAt(0)) {
				carry = '0';
				zeroFlag = true;
			}
			
			result = leftShift(result, 1).substring(0, 2*length-1) + carry;
			counter--;
		}
		
		int temp = result.charAt(length) - 48;
		int overflowFlag = 0;
		if(  ((operand1.charAt(0) == operand2.charAt(0)) && temp == 1) || ((operand1.charAt(0) != operand2.charAt(0)) && temp == 0)  ){
			overflowFlag = 1;
		}else{
			overflowFlag = 0;
		}                         //�ж��Ƿ����

		if (signFlag == operand2.charAt(0) && !zeroFlag){
			result = adder(result.substring(0, length), negOperand2, '0', length).substring(1) + result.substring(length);
		}else{
			result = adder(result.substring(0, length), poOperand2, '0', length).substring(1) + result.substring(length);
		}
		signFlag = result.charAt(0);
		if (signFlag == operand2.charAt(0)){
			carry = '1';
		}else{
			carry = '0';
		}

		if(testZero(result.substring(0, length)) && operand1.charAt(0)!=operand2.charAt(0)) {
			carry = '0';
			zeroFlag = true;
		}
		result = result.substring(0, length) + leftShift(result.substring(length), 1).substring(0, length-1) + carry;
		
		//�̵�����------------
		if (operand1.charAt(0) != operand2.charAt(0)){
			result = result.substring(0, length) + adder(result.substring(length), "01", '0' , length).substring(1);
		}
		
		//����������------------
		if (result.charAt(0) != operand1.charAt(0)){
			if (operand1.charAt(0) == operand2.charAt(0)){
				result = adder(result.substring(0, length), poOperand2, '0', length).substring(1) + result.substring(length);
			}else{
				result = adder(result.substring(0, length), negOperand2, '0', length).substring(1) + result.substring(length);
			}
		}
		//System.out.println("ssss");
		//System.out.println(overflowFlag + result.substring(length) + result.substring(0, length));
		if(testZero(operand2)){
			return "NaN";
		}
		
		return overflowFlag + result.substring(length) + result.substring(0, length);
	}
	
	/**
	 * �����������ӷ������Ե���{@link #adder(String, String, char, int) adder}�ȷ�����
	 * ������ֱ�ӽ�������ת��Ϊ�����ʹ��{@link #integerAddition(String, String, int) integerAddition}��
	 * {@link #integerSubtraction(String, String, int) integerSubtraction}��ʵ�֡�<br/>
	 * ����signedAddition("1100", "1011", 8)
	 * @param operand1 ������ԭ���ʾ�ı����������е�1λΪ����λ
	 * @param operand2 ������ԭ���ʾ�ļ��������е�1λΪ����λ
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ����������ţ�����ĳ���������ĳ���С��lengthʱ����Ҫ���䳤����չ��length
	 * @return ����Ϊlength+2���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������2λΪ����λ����lengthλ����ӽ��
	 */
	public String signedAddition (String operand1, String operand2, int length) {
		String result = "";
		String flag = "";
		String number1 = operand1.substring(1);
		String number2 = operand2.substring(1);
		
		if (operand1.charAt(0) == operand2.charAt(0)){
			//������ͬ
			flag = (operand1.charAt(0) == '0') ? "0" : "1";
			
			number1 = unsignExtend(number1, length);
			number2 = unsignExtend(number2, length);
			//System.out.println(flag + " " + number1 + " " + number2);
			result = adderUnsign(number1, number2, '0', length);
			result = result.substring(0, 1) + flag + result.substring(1);
		}
		else{
			//���Ų�ͬ
			number1 = unsignExtend(number1, length);			
			number2 = negation(unsignExtend(number2, length));    //ȡ��
			
			String temp = subberUnsign(number1, number2, '1', length);
			//System.out.println(temp);
			if(temp.charAt(0) == '1'){
				flag = (operand1.charAt(0)) + "";
			}else{
				flag = (operand1.charAt(0) == '0') ? "1" : "0";
				temp = oneAdder(negation(temp));
			}
			result = "0" + flag + temp.substring(1);
			//System.out.println("addSigned " + result);
		}
		
		//System.out.println(result);
		return result;
	}
	
	
	public String subberUnsign (String operand1, String operand2, char c, int length) {		
		String op1 = signExtend(operand1, length);
		String op2 = signExtend(operand2, length);
		int times = length / 4;
		char carry = c;
		String temp = "";
		String result = "";
		
		for (int i = times - 1; i >= 1; i--){
			temp = claAdder(op1.substring(4*i, 4*i+4), op2.substring(4*i, 4*i+4), carry);
			carry = temp.charAt(0);
			result = temp.substring(1) + result;
		}
		
		result = (claAdder(op1.substring(0, 4), op2.substring(0, 4), carry) + result);
		
		return result;
	}
	
	
	public String adderUnsign (String operand1, String operand2, char c, int length) {		
		String op1 = signExtend(operand1, length);
		String op2 = signExtend(operand2, length);
		int times = length / 4;
		char carry = c;
		String temp = "";
		String result = "";
		
		for (int i = times - 1; i >= 1; i--){
			temp = claAdder(op1.substring(4*i, 4*i+4), op2.substring(4*i, 4*i+4), carry);
			carry = temp.charAt(0);
			result = temp.substring(1) + result;
		}
		
		result = (claAdder(op1.substring(0, 4), op2.substring(0, 4), carry) + result);
		
		int flag;
		if(result.charAt(0) == '1'){
			flag = 1;
		}else{
			flag = 0;
		}                     //�ж�������
		
		return flag + result.substring(1);
	}
	
	
	public String unsignExtend(String str, int length){
		String result = str;
		
		if(str.length() < length){
			for(int i = 0; i < length - str.length(); i++)
				result = "0" + result;
			
			return result;
		}else{
			return result;
		}
		
	}
	
	
	public String mantissaAdder(String operand1, String operand2, int length){
		String result = "";
		
		String number1 = operand1.substring(1);
		String number2 = operand2.substring(1);
		
		if (operand1.charAt(0) == operand2.charAt(0)){
			//System.out.println(flag + " " + number1 + " " + number2);
			result = adderUnsign(number1, number2, '0', length);
			result = result.substring(0, 1) + result.substring(1);
			//System.out.println("addSigned " + result);
		}
		else{
			//���Ų�ͬ			
			number2 = negation(number2);    //ȡ��
			
			String temp = subberUnsign(number1, number2, '1', length);
			result = temp;
			//System.out.println("addSigned " + result+result.length());
		}
		
		return result;
	}
	
	/**
	 * �������ӷ����ɵ���{@link #signedAddition(String, String, int) signedAddition}�ȷ���ʵ�֡�<br/>
	 * ����floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ļ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����ӽ�������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		
		int bias = (int)Math.pow(2, eLength-1) - 1;
		int max = (int)Math.pow(2, eLength) - 2 - bias;
		int min = 1 - bias;
		//System.out.println(max+" "+min);
		//boolean norOp1 = testNormalize(operand1, eLength);
		//boolean norOp2 = testNormalize(operand2, eLength);
		
		int exponent1 = Integer.parseInt(integerTrueValue("0" + operand1.substring(1, eLength + 1))) - bias;
		int exponent2 = Integer.parseInt(integerTrueValue("0" + operand2.substring(1, eLength + 1))) - bias;
		//System.out.println(exponent1 + " " + exponent2);
		int distance = exponent1 - exponent2;
		int finalExponent;   //��������β��֮��ľ���
		
		String flag = "";
		String result = "";
		String temp1 = "";
		String temp2 = "";
		String tempResult = "";
		boolean overflowFlag;
		boolean ofFlagForResult = false;              //�����ʹ��
		boolean zeroFlag = false;                     //�����ʹ��
		
		int k = 4 - (gLength + sLength + 1) % 4; //������չ����
		//System.out.println(k+"Bingo");
		if(distance < 0){
			finalExponent = exponent2;
			
			temp1 = operand1.substring(1 + eLength);
			temp1 = "1" + temp1 + getString("0", gLength+k);
			temp1 = logRightShift(temp1, -distance);
			temp1 = operand1.charAt(0) + temp1;
			
			temp2 = operand2.substring(1 + eLength);
			temp2 = "1" + temp2 + getString("0", gLength+k);
			temp2 = operand2.charAt(0) + temp2;
			//System.out.println(temp1+ " " + temp2 + " " + temp1.length());
		}
		else{
			//��������һ������������
			finalExponent = exponent1;
			
			//temp1 temp2 ��ʾ������ʾ��������λ��β��
			temp2 = operand2.substring(1 + eLength);
			temp2 = "1" + temp2 + getString("0", gLength+k);
			temp2 = logRightShift(temp2, distance);
			temp2 = operand2.charAt(0) + temp2;
			
			temp1 = operand1.substring(1 + eLength);
			temp1 = "1" + temp1 + getString("0", gLength+k);
			temp1 = operand1.charAt(0) + temp1;
			
			//System.out.println(temp1+ " " + temp2 + " " + temp1.length()); 
		}
		
		tempResult = mantissaAdder(temp1, temp2, temp1.length()-1);    //ͨ��mantissaAdder��������λ��Ϊԭ����+1
		
		//System.out.println("A"+tempResult);
		if(operand1.charAt(0) == operand2.charAt(0)){
			overflowFlag = (tempResult.charAt(0) == '1') ? true : false;
			flag = operand1.charAt(0) + "";
		}else{
			overflowFlag = false;
			flag = operand1.charAt(0) + "";
		}
		
		//System.out.println(tempResult+" " + tempResult.length());
		//����ǲ���0
		//if(!testZero(tempResult.substring(1)) && (tempResult.charAt(0))){
		if(!(operand1.substring(1).equals(operand2.substring(1))) || (operand1.charAt(0) == operand2.charAt(0))){
			if(overflowFlag){
				//System.out.println("jurandaolezheli");
				finalExponent++;
				tempResult = logRightShift(tempResult,1);
				result = tempResult.substring(1);
			}
			
			else{
				if((operand1.charAt(0) != operand2.charAt(0)) && (tempResult.charAt(0) == '0')){
					tempResult = oneAdder(negation(tempResult)).substring(1);                   //��Լ���û�в�����λ�����
					flag = (operand1.charAt(0) == '0') ? "1" : "0";
					//System.out.println(tempResult);
				}
				
				result = tempResult.substring(1);
				
				for(int i = 1; i < tempResult.length(); i++){
					if(tempResult.charAt(i) == '1'){
						break;
					}else{
						result = leftShift(result,1);
						finalExponent--;
					}
				}
				//System.out.println(finalExponent);
				
			}          //�������
			
				
		}
		//�����0
		else{
			zeroFlag = true;
		}
		
		
		if(finalExponent > max)
			ofFlagForResult = true;         //�ж��Ƿ����
		
		//System.out.println("k");
		if(ofFlagForResult == true){
			result = "1" + flag + integerRepresentation(String.valueOf(max+1+bias),eLength) + getString("0", sLength);
		}else if(zeroFlag == true){
			result = "0" + "0" + integerRepresentation(String.valueOf(min-1+bias),eLength) + getString("0", sLength);
		}else{
			result = "0" + flag + integerRepresentation(String.valueOf(finalExponent+bias), eLength) + result.substring(1, 1 + sLength);
		}
		
		return result;
	}
	
	
	//����ǲ���0.�ǵĻ�����true����Ȼfalse
	public boolean testZero(String operand){
		boolean flag = true;
		
		for(int i = 0; i < operand.length(); i++){
			if(operand.charAt(i) != '0'){
				flag = false;
				break;
			}
		}
		
		return flag;
	}
	
	public boolean testNormalize(String operand, int eLength){
		boolean flag = false;
		
		for(int i = 0; i < eLength; i++){
			if(operand.charAt(i + 1) == '1'){
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	
	/**
	 * �������������ɵ���{@link #floatAddition(String, String, int, int, int) floatAddition}����ʵ�֡�<br/>
	 * ����floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ļ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ�������������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {
		String temp = (operand2.charAt(0) == '0') ? "1" : "0";
		
		String result = floatAddition(operand1, temp+operand2.substring(1), eLength, sLength, gLength);
		
		if(testZero(operand2.substring(1))){
			return "0" + operand1;
		}
		else{
			return result;
		}
		
	}
	
	/**
	 * �������˷����ɵ���{@link #integerMultiplication(String, String, int) integerMultiplication}�ȷ���ʵ�֡�<br/>
	 * ����floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ĳ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		int bias = (int)Math.pow(2, eLength-1) - 1;
		
		int exponent1 = Integer.parseInt(integerTrueValue("0" + operand1.substring(1, eLength + 1))) - bias;
		int exponent2 = Integer.parseInt(integerTrueValue("0" + operand2.substring(1, eLength + 1))) - bias;
		int finalExponent = exponent1 + exponent2;
		int max = (int)Math.pow(2, eLength) - 2 - bias;
		int min = 1 - bias;
		
		String temp1 = "1" + operand1.substring(1 + eLength);
		String temp2 = "1" + operand2.substring(1 + eLength);           //������ʾβ��
		String result = "";
		String tempResult = "";
		
		boolean zeroFlag = false;
		String flag;               //����λ 
		if(operand1.charAt(0) == operand2.charAt(0))
			flag = "0";
		else
			flag = "1";
		
		
		if((!testZero(operand1.substring(1))) && (!testZero(operand2.substring(1)))){
			//��������������Ϊ��
			tempResult = mantissaMultiplication(temp1, temp2).substring(1);
			
			if(tempResult.charAt(0) == '1'){
				finalExponent++;
				tempResult = tempResult.substring(1);
			}else{
				tempResult = tempResult.substring(2);
			}
			
		}
		else{
			//��������������һ����0
			zeroFlag = true;
		}
		
		//������
		if(finalExponent < min) zeroFlag = true;
		
		if(zeroFlag == true){
			result = "0" + "0" + getString("0", eLength) + getString("0", sLength);
		}else if(finalExponent > max){
			result = "1" + flag + getString("0", eLength) + getString("0", sLength);
		}else{
			result = "0" + flag + integerRepresentation(String.valueOf(finalExponent+bias), eLength) + tempResult.substring(0, sLength);
		}
		
		if(testInfinity(operand1,eLength) || testInfinity(operand2,eLength)){
			flag = (testInfinity(operand1, eLength)) ? operand1.charAt(0)+"" : operand2.charAt(0)+"";
			result = "1" + flag + getString("1", eLength) + getString("0", sLength);
		}
		
		
		return result;
	}
	
	
	public boolean testInfinity(String operand, int eLength){
		boolean result = true;
		
		for(int i = 0; i < eLength; i++){
			if(operand.charAt(i+1) == '0'){
				result = false;
				break;
			}
		}
		
		return result;
	}
	
	
	public String mantissaMultiplication(String operand1, String operand2 ){
		String result = "";
		
		int k = 4 - (operand2.length() + 1) % 4;
		operand1 = "0"+ operand1 + getString("0", k);
		operand2 = operand2 + getString("0", k);
		int length = operand1.length();
		
		result = "0" + getString("0", operand2.length()) + operand2;
		
		for(int i = 0; i < operand2.length(); i++){
			if(result.charAt(result.length()-1) == '1'){
				result = adder(result.substring(0, length), operand1, '0', length).substring(1) + result.substring(length);
				result = logRightShift(result, 1);
			}
			
			else{
				result = logRightShift(result, 1);
			}
		}
		//System.out.println(result);
		
		return result;
	}
	
	/**
	 * �������������ɵ���{@link #integerDivision(String, String, int) integerDivision}�ȷ���ʵ�֡�<br/>
	 * ����floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ĳ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		int bias = (int)Math.pow(2, eLength-1) - 1;
		int max = (int)Math.pow(2, eLength) - 2 - bias;
		int min = 1 - bias;
		
		int exponent1 = Integer.parseInt(integerTrueValue("0" + operand1.substring(1, eLength + 1))) - bias;
		int exponent2 = Integer.parseInt(integerTrueValue("0" + operand2.substring(1, eLength + 1))) - bias;
		int finalExponent = exponent1 - exponent2;
		
		String result = "";
		String tempResult = "";
		String temp1 = "1" + operand1.substring(1 + eLength);
		String temp2 = "1" + operand2.substring(1 + eLength);           //������ʾβ��
		
		boolean zeroFlag = false;
		boolean overflowFlag = false;
		String flag;               //����λ 
		if(operand1.charAt(0) == operand2.charAt(0))
			flag = "0";
		else
			flag = "1";
		
		if((testZero(operand1.substring(1))) && !(testZero(operand2.substring(1)))){
			zeroFlag = true;
		}else if(!(testZero(operand1.substring(1))) && (testZero(operand2.substring(1)))){
			overflowFlag = true;
			flag = operand1.charAt(0) + "";
		}
		
		tempResult = mantissaDivision(temp1, temp2);            //β����������
		
		//β����񻯴���
		if(tempResult.charAt(0) == '1'){
			tempResult = tempResult.substring(1);
		}else{
			finalExponent--;
			tempResult = tempResult.substring(1);
		}
		
		//����ж�
		if(finalExponent > max){
			overflowFlag = true;
		}else if(finalExponent < min)
			zeroFlag = true;
		
		if(overflowFlag == true){
			result = "0" + flag + getString("1", eLength) + getString("0", sLength);
		}else if(zeroFlag == true){
			result = "0" + "0" + getString("0", eLength) + getString("0", sLength);
		}else{
			result = "0" + flag + integerRepresentation(String.valueOf(finalExponent+bias), eLength) + tempResult.substring(0, sLength);
		}
		
		
		return result;
	}
	
	
	public String mantissaDivision(String operand1, String operand2){
		String result = "";
		
		int k = 4 - (operand1.length() + 1) % 4;
		int length;
		
		operand1 = "0" + operand1 + getString("0", k);
		operand2 = "0" + operand2 + getString("0", k);
		String negOperand2 = oneAdder(negation(operand2)).substring(1);
		result = operand1 + getString("0", operand1.length() - 1);
		
		String tempResult = "";
		//System.out.println(operand1.length() +" " + operand2.length() +" " +result.length());
		//System.out.println(result);
		
		length = operand1.length();
		for(int i = 0; i < length-1; i++){
			tempResult = result;
			result = adder(result.substring(0, length), negOperand2, '0', length).substring(1) + result.substring(length);
			
			if(result.charAt(0) == '1'){
				String carry = "0";
				result = tempResult + carry;
				result = leftShift(result, 1).substring(0, result.length()-1);
			}
			else{
				String carry = "1";
				result = result + carry;
				result = leftShift(result, 1).substring(0, result.length()-1);
			}
		}
		
		tempResult = result;
		result = adder(result.substring(0, length), negOperand2, '0', length).substring(1) + result.substring(length);
		
		if(result.charAt(0) == '1'){
			String carry = "0";
			result = tempResult + carry;			
		}
		else{
			String carry = "1";
			result = result + carry;
		}
		
		
		return result.substring(length);
	}
}

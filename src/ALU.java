/**
 * 模拟ALU进行整数和浮点数的四则运算
 * 161250004 曹嘉玮
 *
 */

public class ALU {
	
	/**
	 * 生成十进制整数的二进制补码表示。<br/>
	 * 例：integerRepresentation("9", 8)
	 * @param number 十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制补码表示的长度
	 * @return number的二进制补码表示，长度为length
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
	 * 生成十进制浮点数的二进制表示。
	 * 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
	 * 舍入策略为向0舍入。<br/>
	 * 例：floatRepresentation("11.375", 8, 11)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {
	
		boolean nanFlag = false;
		boolean infFlag = false;
		boolean InfFlag = false;     //违反命名规则，这个是用来判断参数number是不是直接为“Inf”

		
		//判断是否为特殊情况----------------------------------------------
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
		
		String flag;                 //符号位，在下面定义
		String[] operand;
		long theInteger;              //整数部分
		double theDecimal;           //小数部分
		double theNumber;            //该数字的绝对值大小
		
		int bias = (int)Math.pow(2, eLength - 1) - 1; 
		double norMax;  double denorMax;   double denorMin;               //规格化最大值；规格化最小值；非规格化
		String result = "";
		String finalResult = "";
		
		
		//正常情况--------------------------------------------------------
		if((InfFlag == false) && (nanFlag == false)){
			flag = (number.charAt(0) == '-') ? "1" : "0";
			
			if(flag.equals("1"))
				operand = number.substring(1).split("\\.");
			else
				operand = number.split("\\.");                           //对符号位的处理
			
			
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
				//规格化数字的处理
				
				
				//整数部分--------------------------------------
				for(int i = 63; i >= 0; i--){
					result = result + (1 & (theInteger >> i));
				}
				
				//小数部分-------------------------------------
				int counter = 2 * sLength;       //防止无限循环
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
				
				//结果长度调整-----------------------------------
				for(int i = 0; i < result.length(); i++){
					if(result.charAt(i) == '1'){
						result = result.substring(i);
						index = i;
						break;
					}
				}
				
				result = result.substring(1, sLength + 1);
				
				//加上指数部分-----------------------------------
				int exponent = 63 - index + bias;
				String temp = "";
				
				for(int i = 0; i < eLength; i++)
					temp = (1 & (exponent >> i)) + temp;
				
				finalResult = flag + temp + result;
			}
			else if(theNumber >= denorMin){
				//非规格化---------------------------------------
				
				//小数部分-------------------------------------
				int counter = bias + sLength + 2;       //防止无限循环
				
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
				//0的处理--------------------------------------
				finalResult = flag + getString("0", eLength) + getString("0", sLength);
			}
		}
		
		//结果输出------------------------------------------------------
		
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
	 * 生成十进制浮点数的IEEE 754表示，要求调用{@link #floatRepresentation(String, int, int) floatRepresentation}实现。<br/>
	 * 例：ieee754("11.375", 32)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制表示的长度，为32或64
	 * @return number的IEEE 754表示，长度为length。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
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
	 * 计算二进制补码表示的整数的真值。<br/>
	 * 例：integerTrueValue("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
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
		}     //按位拆解
		
		long longResult = 0;
		for (int i = 0; i < len; i++){
			longResult += Math.pow(2, i) * binaryArray[len - i - 1];
		}     //乘上权重
		
		if (flag.equals("-")){
			longResult += 1;
		}    //取反加一
		
		String result = flag + String.valueOf(longResult);
		return result;
	}
	
	/**
	 * 计算二进制原码表示的浮点数的真值。<br/>
	 * 例：floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand 二进制表示的操作数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”， NaN表示为“NaN”
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {
		int[] exponent = new int[eLength];
		int[] mantissa = new int[sLength];          //用于拆解

		int bias = (int)Math.pow(2, eLength-1) - 1;
		int exp = Integer.parseInt(integerTrueValue("0" + operand.substring(1, eLength + 1))) - bias;
		double result = 0.0;
		
		String flag1 = (operand.charAt(0) == '0') ? "" : "-";
		String flag2 = (operand.charAt(0) == '0') ? "+" : "-";
		boolean norFlag = false;
		boolean nanFlag = false;
		boolean infFlag = false;
		boolean poFlag = true;                  //检测阶码用的
		
		
		for(int i = 0; i < eLength; i++){
			exponent[i] = operand.charAt(i + 1) - 48;
		}
		
		for(int j = 0; j < sLength; j++){
			mantissa[j] = operand.charAt (1 + eLength + j) - 48;
		}
		
		
		//检测是否为NaN、正负无穷--------------------------------------------
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
		
		
		//判断是否为非规格化-------------------------------------------------
		for(int i = 0; i < eLength; i++){
			if(exponent[i] != 0){
				norFlag = true;
				break;
			}
		}
		
		
		//正常--------------------------------------------------------------
		
		Transition: if(poFlag == false){
			
			//规格化
			if(norFlag == true){
				result = 1.0 * Math.pow(2, exp);

				for(int i = 0; i < sLength; i++){
					result = result + Math.pow(2, exp - i - 1) * mantissa[i];
				}
				
				break Transition;
			}
			
			
			//非规格化
			if(norFlag == false){
				exp = 1 - bias;
				
				for(int i = 0; i < sLength; i++){
					result = result + Math.pow(2, exp - i - 1) * mantissa[i];
				}
			}
			
			
		}                            //转换结束
		
		
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
	 * 按位取反操作。<br/>
	 * 例：negation("00001001")
	 * @param operand 二进制表示的操作数
	 * @return operand按位取反的结果
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
	 * 左移操作。<br/>
	 * 例：leftShift("00001001", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 左移的位数
	 * @return operand左移n位的结果
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
	 * 逻辑右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand逻辑右移n位的结果
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
	 * 算术右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand算术右移n位的结果
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
	 * 全加器，对两位以及进位进行加法运算。<br/>
	 * 例：fullAdder('1', '1', '0')
	 * @param x 被加数的某一位，取0或1
	 * @param y 加数的某一位，取0或1
	 * @param c 低位对当前位的进位，取0或1
	 * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
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
	 * 4位先行进位加法器。要求采用{@link #fullAdder(char, char, char) fullAdder}来实现<br/>
	 * 例：claAdder("1001", "0001", '1')
	 * @param operand1 4位二进制表示的被加数
	 * @param operand2 4位二进制表示的加数
	 * @param c 低位对当前位的进位，取0或1
	 * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得
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
		}                                   // 这是为了获取辅助函数
		
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
	 * 加一器，实现操作数加1的运算。
	 * 需要采用与门、或门、异或门等模拟，
	 * 不可以直接调用{@link #fullAdder(char, char, char) fullAdder}、
	 * {@link #claAdder(String, String, char) claAdder}、
	 * {@link #adder(String, String, char, int) adder}、
	 * {@link #integerAddition(String, String, int) integerAddition}方法。<br/>
	 * 例：oneAdder("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
	 */
	public String oneAdder (String operand) {
		char[] op = operand.toCharArray();
		int[] theCopy = new int[operand.length() + 1];
		
		for (int i = 1; i < theCopy.length; i++){
			theCopy[i] = op[i-1] - 48;
		}
		
		int flag = theCopy[1];   //判断是否溢出
		
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
	 * 加法器，要求调用{@link #claAdder(String, String, char)}方法实现。<br/>
	 * 例：adder("0100", "0011", ‘0’, 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param c 最低位进位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
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
		}                     //判断溢出与否
		
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
	 * 整数加法，要求调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerAddition("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		String adderResult = adder(operand1, operand2, '0', length);
		return adderResult;
	}
	
	/**
	 * 整数减法，可调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerSubtraction("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被减数
	 * @param operand2 二进制补码表示的减数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		int len = operand2.length();
		String newOperand2 = "";
		
		for (int i = 0; i < len; i++){
			newOperand2 = newOperand2 + ( 1 ^ (operand2.charAt(i) - 48) );
		}                              //取反
		
		String result = adder(operand1, newOperand2, '1', length);
		return result;
	}
	
	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #adder(String, String, char, int) adder}等方法。<br/>
	 * 例：integerMultiplication("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被乘数
	 * @param operand2 二进制补码表示的乘数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
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
	 * 整数的不恢复余数除法，可调用{@link #adder(String, String, char, int) adder}等方法实现。<br/>
	 * 例：integerDivision("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被除数
	 * @param operand2 二进制补码表示的除数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，最后length位为余数
	 */
	public String integerDivision (String operand1, String operand2, int length) {
		String result = signExtend(operand1, length * 2);
		String poOperand2 = signExtend(operand2, length);
		String negOperand2 = oneAdder(negation(signExtend(operand2, length))).substring(1);   //操作数2的负补码
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
		}                         //判断是否溢出

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
		
		//商的修正------------
		if (operand1.charAt(0) != operand2.charAt(0)){
			result = result.substring(0, length) + adder(result.substring(length), "01", '0' , length).substring(1);
		}
		
		//余数的修正------------
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
	 * 带符号整数加法，可以调用{@link #adder(String, String, char, int) adder}等方法，
	 * 但不能直接将操作数转换为补码后使用{@link #integerAddition(String, String, int) integerAddition}、
	 * {@link #integerSubtraction(String, String, int) integerSubtraction}来实现。<br/>
	 * 例：signedAddition("1100", "1011", 8)
	 * @param operand1 二进制原码表示的被加数，其中第1位为符号位
	 * @param operand2 二进制原码表示的加数，其中第1位为符号位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，需要将其长度扩展到length
	 * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，后length位是相加结果
	 */
	public String signedAddition (String operand1, String operand2, int length) {
		String result = "";
		String flag = "";
		String number1 = operand1.substring(1);
		String number2 = operand2.substring(1);
		
		if (operand1.charAt(0) == operand2.charAt(0)){
			//符号相同
			flag = (operand1.charAt(0) == '0') ? "0" : "1";
			
			number1 = unsignExtend(number1, length);
			number2 = unsignExtend(number2, length);
			//System.out.println(flag + " " + number1 + " " + number2);
			result = adderUnsign(number1, number2, '0', length);
			result = result.substring(0, 1) + flag + result.substring(1);
		}
		else{
			//符号不同
			number1 = unsignExtend(number1, length);			
			number2 = negation(unsignExtend(number2, length));    //取反
			
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
		}                     //判断溢出与否
		
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
			//符号不同			
			number2 = negation(number2);    //取反
			
			String temp = subberUnsign(number1, number2, '1', length);
			result = temp;
			//System.out.println("addSigned " + result+result.length());
		}
		
		return result;
	}
	
	/**
	 * 浮点数加法，可调用{@link #signedAddition(String, String, int) signedAddition}等方法实现。<br/>
	 * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被加数
	 * @param operand2 二进制表示的加数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
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
		int finalExponent;   //决定两个尾数之间的距离
		
		String flag = "";
		String result = "";
		String temp1 = "";
		String temp2 = "";
		String tempResult = "";
		boolean overflowFlag;
		boolean ofFlagForResult = false;              //最后结果使用
		boolean zeroFlag = false;                     //最后结果使用
		
		int k = 4 - (gLength + sLength + 1) % 4; //用于扩展长度
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
			//本情况向第一个操作数对齐
			finalExponent = exponent1;
			
			//temp1 temp2 表示两个显示出了隐藏位的尾数
			temp2 = operand2.substring(1 + eLength);
			temp2 = "1" + temp2 + getString("0", gLength+k);
			temp2 = logRightShift(temp2, distance);
			temp2 = operand2.charAt(0) + temp2;
			
			temp1 = operand1.substring(1 + eLength);
			temp1 = "1" + temp1 + getString("0", gLength+k);
			temp1 = operand1.charAt(0) + temp1;
			
			//System.out.println(temp1+ " " + temp2 + " " + temp1.length()); 
		}
		
		tempResult = mantissaAdder(temp1, temp2, temp1.length()-1);    //通过mantissaAdder算出结果，位数为原长度+1
		
		//System.out.println("A"+tempResult);
		if(operand1.charAt(0) == operand2.charAt(0)){
			overflowFlag = (tempResult.charAt(0) == '1') ? true : false;
			flag = operand1.charAt(0) + "";
		}else{
			overflowFlag = false;
			flag = operand1.charAt(0) + "";
		}
		
		//System.out.println(tempResult+" " + tempResult.length());
		//检查是不是0
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
					tempResult = oneAdder(negation(tempResult)).substring(1);                   //针对减法没有产生进位的情况
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
				
			}          //处理结束
			
				
		}
		//如果是0
		else{
			zeroFlag = true;
		}
		
		
		if(finalExponent > max)
			ofFlagForResult = true;         //判断是否溢出
		
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
	
	
	//检查是不是0.是的话返回true，不然false
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
	 * 浮点数减法，可调用{@link #floatAddition(String, String, int, int, int) floatAddition}方法实现。<br/>
	 * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被减数
	 * @param operand2 二进制表示的减数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
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
	 * 浮点数乘法，可调用{@link #integerMultiplication(String, String, int) integerMultiplication}等方法实现。<br/>
	 * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被乘数
	 * @param operand2 二进制表示的乘数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		int bias = (int)Math.pow(2, eLength-1) - 1;
		
		int exponent1 = Integer.parseInt(integerTrueValue("0" + operand1.substring(1, eLength + 1))) - bias;
		int exponent2 = Integer.parseInt(integerTrueValue("0" + operand2.substring(1, eLength + 1))) - bias;
		int finalExponent = exponent1 + exponent2;
		int max = (int)Math.pow(2, eLength) - 2 - bias;
		int min = 1 - bias;
		
		String temp1 = "1" + operand1.substring(1 + eLength);
		String temp2 = "1" + operand2.substring(1 + eLength);           //用来表示尾数
		String result = "";
		String tempResult = "";
		
		boolean zeroFlag = false;
		String flag;               //符号位 
		if(operand1.charAt(0) == operand2.charAt(0))
			flag = "0";
		else
			flag = "1";
		
		
		if((!testZero(operand1.substring(1))) && (!testZero(operand2.substring(1)))){
			//两个操作数均不为零
			tempResult = mantissaMultiplication(temp1, temp2).substring(1);
			
			if(tempResult.charAt(0) == '1'){
				finalExponent++;
				tempResult = tempResult.substring(1);
			}else{
				tempResult = tempResult.substring(2);
			}
			
		}
		else{
			//两个乘数当中有一个是0
			zeroFlag = true;
		}
		
		//检测阶码
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
	 * 浮点数除法，可调用{@link #integerDivision(String, String, int) integerDivision}等方法实现。<br/>
	 * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被除数
	 * @param operand2 二进制表示的除数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
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
		String temp2 = "1" + operand2.substring(1 + eLength);           //用来表示尾数
		
		boolean zeroFlag = false;
		boolean overflowFlag = false;
		String flag;               //符号位 
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
		
		tempResult = mantissaDivision(temp1, temp2);            //尾数的运算结果
		
		//尾数规格化处理
		if(tempResult.charAt(0) == '1'){
			tempResult = tempResult.substring(1);
		}else{
			finalExponent--;
			tempResult = tempResult.substring(1);
		}
		
		//溢出判断
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

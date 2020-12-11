import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
class asm {
	public static int pc = 0;
	public static void main(String[] args){
		String a = "";
		String filename = "";
		String outfile = "";
		
		if (args.length > 1){
			filename = args[0];
			outfile = args[1];
		}else{
			System.out.println("Use: java -jar asm.jar <input> <output>");
			System.exit(0);
		}
		String tmp = "";
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null){
				tmp += line;
				tmp += " ";
			}
			reader.close();
		}
		catch (Exception e){
			System.err.format("Err occurred trying to read '%s'.", filename);
			e.printStackTrace();
			System.exit(1);
		}
		tmp = tmp.replace(", ", " ");
		tmp = tmp.replace(",", " ");
		tmp = tmp.replace(" ,", " ");
		tmp = tmp.replace(" ", "");
		String[] prog = tmp.split(";");
		for(int i=0;i<prog.length;i++){
			System.out.println(
				"CURRENT POS: " + i + 
				", " + prog[i]
			);
		}
		while(pc < prog.length){
			System.out.println(a);
			String d = prog[pc];
			if(!((prog[pc].substring(0, 1)).equals("$")) && !((prog[pc].substring(0, 1)).equals("r")) && !((prog[pc].substring(0, 1)).equals("'")) && !((prog[pc].substring(0, 1)).equals(" "))){
				System.out.println("FOUND INSTR " + prog[pc].substring(0, 1));
				a += " 0x";
			}
			if(prog[pc].equals("hlt")) {
				a += "00000000";
			}else if(prog[pc].equals("mov")){
				if(prog[pc + 1].substring(0, 1).equals("r")){
					a += "02";
				}else{
					a += "01";
				}
			}else if(prog[pc].equals("add")){
				if(prog[pc].substring(0, 1).equals("r")){
					a += "04";
				}else{
					a += "03";
				}
			}
			else if(prog[pc].equals("sub")){
				if(prog[pc].substring(0, 1).equals("r")){
					a += "06";
				}else{
					a += "05";
				}
			}
			else if(prog[pc].equals("mul")){
				if(prog[pc].substring(0, 1).equals("r")){
					a += "08";
				}else{
					a += "07";
				}
			}
			else if(prog[pc].equals("div")){
				if(prog[pc].substring(0, 1).equals("r")){
					a += "0A";
				}else{
					a += "09";
				}
			}
			else if(prog[pc].equals("cmp")){
				a += "0F";
			}else if(prog[pc].equals("jmp")){
				a += "10";
			}else if(prog[pc].equals("je")){
				a += "11";
			}else if(prog[pc].equals("jne")){
				a += "12";
			}else if(prog[pc].equals("jg")){
				a += "13";
			}else if(prog[pc].equals("jl")){
				a += "14";
			}else if(prog[pc].equals("inc")){
				a += "15";
			}else if(prog[pc].equals("dec")){
				a += "16";
			}else if(prog[pc].equals("princ")){
				a += "17";
			}else if(prog[pc].equals("prini")){
				a += "18";
			}else if(prog[pc].equals("prinr")){
				a += "19";
			}else if(prog[pc].equals("prins")){	
			}else if(prog[pc].equals("csmem")){	
				a += "1C000000";
			}else if(prog[pc].equals("tint")){
				a += "1D000000";
			}else if(prog[pc].substring(0, 1).equals("$")){
				int tmpa = 0;
				System.out.println("PC-1 = " + prog[pc - 1]);
				String tmpc="";
				char[] tmpd = prog[pc].toCharArray();
				for(int i=0;i<tmpd.length;i++) {
					if(tmpd[i] == '$') {}
					else if(tmpd[i] == '#') break;
					else tmpc += tmpd[i];
				}
				int val = 0;
				if(
					!prog[pc - 1].equals("jmp") &&
					!prog[pc - 1].equals("jne") &&
					!prog[pc - 1].equals("je") &&
					!prog[pc - 1].equals("jg") &&
					!prog[pc - 1].equals("jl") &&
					!prog[pc - 1].equals("inc") &&
					!prog[pc - 1].equals("dec") &&
					!prog[pc - 1].equals("princ") &&
					!prog[pc - 1].equals("prini") &&
					!prog[pc - 1].equals("prinr") &&
					!prog[pc - 1].equals("prins")
					) {
					tmpa = Integer.parseInt(prog[pc + 1].substring(1, 2)) - 1;
					val = Integer.parseInt(tmpc);
				}else{
					val = Integer.parseInt(tmpc);
				}
				a += tmpa;
				a += "0";
				if(tmpc.length() > 3) {
					a += val;
				}else if(tmpc.length() > 2) {
					a += "0" + val;
				}else if(tmpc.length() > 1) {
					a += "00" + val;
				}else if(tmpc.length() > 0) {
					a += "000" + val;
				}
				pc++;
			}else if(prog[pc].substring(0, 1).equals("r")){
				int regv1 = Integer.parseInt(prog[pc].substring(1, 2)) - 1;
				int regv2 = 0;
				if(!prog[pc + 1].substring(0,1).equals("r")){
				}
				else{
					regv2 = Integer.parseInt(prog[pc + 1].substring(1, 2)) - 1;
				}
				System.out.println("REG1: " + regv1 + "\nREG2: " + regv2);
				a += regv1;
				a += regv2;
				if(!prog[pc + 1].substring(0,1).equals("r")){
					pc--;
				}
				else{}
				a += "0000";
				pc++;
			}
			if(prog[pc].substring(0,1).equals("'")){
				
				String tmps1 = prog[pc].replace("/s", " ");
				tmps1 = tmps1.replace("/n", "\n");
				tmps1 = tmps1.replace("/c", ",");
				tmps1 = tmps1.replace("'", "");
				char[] tmps = tmps1.toCharArray();
				for(int i=0;i<tmps1.length();i++){
					int tmpi = tmps[i];
					a += " 0x1B";
					a += "0000" + (tmpi > 0xF ? ("") : ('0'));
					a += String.format("%X", tmpi);
				}
				a += " 0x1B00FF00";
				System.out.println("prog[pc - 1] = " + prog[pc - 1]);
				if(prog[pc - 1].equals("prins")){
					a += " 0x1A";
					a += "000000";
				}
			}
			pc++;
		}
		try{
			a = a.replaceFirst(" ", "");
			a = a.replace("0x ", "");
			System.out.println("Writing '" + a + "'" + "\nTo " + outfile);
			FileWriter fileWriter = new FileWriter(outfile);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.printf("%s",a);
			printWriter.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
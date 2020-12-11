import java.util.Scanner;
import java.io.File;
class arch {
	public static int idx;
	public static int pc;
	public static int sp;
	public static boolean running = true;
	public static int[] regs = new int[8];
	public static int[] stack = new int[20000];
	public static int[] ram = new int[stack.length * 2];
	public static int[] l1 = new int[2000];
	public static int[] l2 = new int[2000];
	public static int[] smem = new int[4000];
	public static int smemptr = 0;
	public static int flg = 0b0000;
	public static int lptr;
	public static Scanner s = new Scanner(System.in);
	public static void main(String[] args) {
		String tmpp = "";
		String tmp = "";
		String filename = "";
		init();
		if(args.length > 0){
			filename = args[0];
		}else{
			System.out.println("Use: java -jar arch.jar <input>");
			System.exit(1);
		}
		File file = 
      	new File(filename);
		
		Scanner stop = new Scanner(System.in); 
		try{
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) 
				tmp = sc.nextLine();
				tmpp += tmp;
		}catch(Exception e){
			e.printStackTrace();
		}
		String p1 = tmpp.replace("0x", "");
		p1 = p1.replace("\n", "");
		String[] p2 = p1.split(" ");
		int[] p = new int[p2.length];
		for(int i=0;i<p.length;i++) p[i] = Integer.parseInt(p2[i], 16);
		run(p);
	}
	public static void init(){
		idx = 0;
		pc = 0;
		lptr = 0;
		for(int i=0;i<stack.length;i++){stack[i] = 0;}
		for(int i=0;i<ram.length;i++){ram[i] = 0;}
		for(int i=0;i<l1.length;i++){l1[i] = 0;}
		for(int i=0;i<l2.length;i++){l2[i] = 0;}
		System.out.println("Finished initialization!");
	}
	public static void run(int[] p){
		while(running){
			step(p[pc]);
			pc++;
		}
	}
	public static void outl(Object line) {
    	System.out.println(line);
	}
	public static void out(Object line){
		System.out.print(line);
	}
	public static void step(int op){
		int ins = (op&0xFF000000)>>24;
		int ch1 = (op&0x00F00000)>>20;
		int ch2 = (op&0x000F0000)>>16;
		int dat = (op&0x0000FFFF)>>00;
		boolean jneflg = (!(flg == 0b0001) && !(flg == 0b0010) && !(flg == 0b0100) && !(flg == 0b1000))? true : false;
		switch(ins){
			case 0x00: running = false;break;
			case 0x01: regs[ch1] = dat;break;
			case 0x02: regs[ch1] = regs[ch2];break;
			case 0x03: regs[ch1] += regs[ch2];break; 
			case 0x04: regs[ch1] += dat;break; 
			case 0x05: regs[ch1] -= regs[ch2];break; 
			case 0x06: regs[ch1] -= dat;break; 
			case 0x07: regs[ch1] *= regs[ch2];break;
			case 0x08: regs[ch1] *= dat;break; 
			case 0x09: regs[ch1] /= regs[ch2];break; 
			case 0x0A: regs[ch1] /= dat;break; 
			case 0x0F: 
			if(regs[ch1] == regs[ch2]) flg = 0b0001;
			if(regs[ch1] != regs[ch2]) flg = 0b0010;
			if(regs[ch1] >  regs[ch2]) flg = 0b0100;
			if(regs[ch1] <  regs[ch2]) flg = 0b1000;
			break;
			case 0x10: pc = dat;break;
			case 0x11: if(flg == 0b0001) pc = dat;break;
			case 0x12: if(jneflg) pc = dat;break;
			case 0x13: if(flg == 0b0100) pc = dat;break;
			case 0x14: if(flg == 0b1000) pc = dat;break;
			case 0x15: regs[ch1]++;break;
			case 0x16: regs[ch1]--;break;
			case 0x17: System.out.print((char)dat);break;
			case 0x18: System.out.print(dat);
			case 0x19: System.out.print(regs[ch1]);
			case 0x1A: {
				smemptr = 0;
				for(int i=0;i<smem.length;i++){
					if(smem[smemptr] == 0xFF00) break;
					System.out.print((char)smem[smemptr]);
					smemptr++;
				}
				smemptr = 0;
				break;
			}
			case 0x1B: smem[smemptr] = dat; smemptr++;break;
			case 0x1C: for(int i=0;i<smem.length;i++) smem[i]=0;smemptr=0;break;
			case 0x1D: regs[7] = s.nextInt();break;
			default: outl("Invalid OP '" + String.format("%08X",op) + "'");System.exit(0);
		}
	}
}
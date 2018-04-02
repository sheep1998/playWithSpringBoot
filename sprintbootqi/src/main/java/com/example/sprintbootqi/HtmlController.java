package com.example.sprintbootqi;

import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;


//@Controller
@RestController
public class HtmlController {

    /*@GetMapping("/html")
    public String test(){
        return "/index.html";
    }*/

    static String result = "empty";
    static Vector<String> text = new Vector<String>();
    static Vector<String> big = new Vector<String>();
    static Vector<String> new_text = new Vector<String>();
    static Map<Deque<String>,Map<String,Integer>>large_map = new HashMap<Deque<String>,Map<String,Integer>>();
    static Map<Deque<String>,Map<String,Integer>>pre_map = new HashMap<Deque<String>,Map<String,Integer>>();

    public static void text_spilit_word(String line){
        int len = line.length();
        String buff = "";
        for(int i=0;i<len;i++){

            if(line.charAt(i)== ' '|| line.charAt(i)=='\r'){
                if(buff!=""&& buff!=null) {
                    text.add(buff);
                    if(buff.charAt(0)>'A'&&buff.charAt(0)<'Z'){
                        big.add(buff);
                    }
                    buff = "";

                }
            }
            else{
                buff += line.charAt(i);
            }

        }
    }

    public static void text_to_vector(String file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file)),"UTF-8"));
        String line = null;
        while((line=br.readLine())!=null){
            text_spilit_word(line);
        }
    }

    public static void construct(Deque<String> buff){
        Deque<String> first = new LinkedList<String>();
        for(String s:buff){
            first.addLast(s);
        }
        first.removeLast();
        String second = buff.getLast();
        if(!large_map.containsKey(first)){
            Map<String,Integer> new_map = new HashMap<String,Integer>();
            new_map.put(second,1);
            large_map.put(first,new_map);
        }
        else{
            if(large_map.get(first).containsKey(second)){
                int tmp = large_map.get(first).get(second)+1;
                large_map.get(first).put(second,tmp);
            }
            else {
                large_map.get(first).put(second,1);
            }
        }
    }

    public static void vector_to_large_map(int N){
        if(N>text.size()){
            System.out.println("N is longer than paragraph!");
            Deque<String> buff = new LinkedList<String>();
            for(int i=0;i<N;i++){
                buff.addLast("No.");
            }
            construct(buff);
        }
        else{
            Deque<String> buff = new LinkedList<String>();
            for(int i=0;i<N-1;i++){
                buff.addLast(text.get(i));

            }
            for(int i=N-1;i<text.size();i++){
                if(text.get(i)!=null){
                    buff.addLast(text.get(i));
                    construct(buff);
                    buff.removeFirst();
                }
            }
        }
    }

    public static void the_first_word(){
        int max = big.size();
        Random random = new Random();
        int ran = random.nextInt(max);
        new_text.add(big.get(ran));
    }

    public static void p_construct(Deque<String>buff){
        Deque<String> first = new LinkedList<String>();
        for(String s:buff){
            first.addLast(s);
        }
        first.removeLast();
        String second = buff.getLast();
        if(!pre_map.containsKey(first)){
            Map<String,Integer> new_map = new HashMap<String,Integer>();
            new_map.put(second,1);
            pre_map.put(first,new_map);
        }
        else{
            if(pre_map.get(first).containsKey(second)){
                int tmp = pre_map.get(first).get(second)+1;
                pre_map.get(first).put(second,tmp);
            }
            else{
                pre_map.get(first).put(second,1);
            }
        }
    }

    public static void vector_to_preMap(int N){
        if(N>text.size()){
            System.out.println("N is longer than paragraph!");
            Deque<String> buff = new LinkedList<String>();
            for(int i=0;i<N;i++){
                buff.addLast("No.");
            }
            p_construct(buff);
        }
        else{
            Deque<String> buff = new LinkedList<String>();
            for(int i=0;i<N-1;i++){
                buff.addLast(text.get(i));

            }
            for(int i=N-1;i<text.size();i++){
                if(text.get(i)!=null){
                    buff.addLast(text.get(i));
                    p_construct(buff);
                    buff.removeFirst();
                }
            }
        }
    }

    public static void p_add(){
        String word = "";
        int number = 0;
        Deque<String> buff = new LinkedList<String>();
        for(int i = 0;i<new_text.size();i++){
            buff.addLast(new_text.get(i));

        }
        Map<String,Integer> tmp_map = new HashMap<String, Integer>();
        tmp_map = pre_map.get(buff);
        for(Map.Entry<String,Integer> entry:tmp_map.entrySet()){
            if(entry.getValue()>number){
                number = entry.getValue();
                word = entry.getKey();
            }
        }
        new_text.add(word);
    }

    public static void before_N(int N){
        //System.out.println(new_text.size());
        for(int i=2;i<=N-1;i++){
            vector_to_preMap(i);
            p_add();
        }
    }

    public static void add(int N){
        String word = "";
        int num = 0 ;
        Vector<String> possibility = new Vector<String>();
        Deque<String> buff = new LinkedList<String>();
        for(int i= new_text.size()-N+1;i<new_text.size();i++){
            buff.addLast(new_text.get(i));
        }
        Map<String,Integer> tmp_map = new HashMap<String, Integer>();
        tmp_map = large_map.get(buff);
        for(Map.Entry<String,Integer> entry:tmp_map.entrySet()){
            num = entry.getValue();
            for(int i = 0;i<num;i++){
                possibility.add(entry.getKey());
            }
        }
        int max = possibility.size();
        Random random = new Random();
        int ran = random.nextInt(max);
        new_text.add(possibility.get(ran));
    }

    public static void execution(String file,int N,int quantity){
        the_first_word();
        if(quantity==1)return;
        else{
            if(N>quantity){
                System.out.println("There will be N-1 words...");
            }
            before_N(N);
            int number = N-1;
            for(int i = number+1;i<=quantity;i++){
                add(N);
            }
        }
    }

    public static boolean check_file(String file){
        File dir = new File(file);
        if(dir.exists())return true;
        else return false;
    }

    public static void go(String file,int N) throws IOException {
        //System.out.println("of random words to generate(0 to quit)?");
        int quantity=40;
        //Scanner sac = new Scanner(System.in);
        //quantity = sac.nextInt();
        text_to_vector(file);
        vector_to_large_map(N);
        if(quantity!=0){
            execution(file, N, quantity);
            String out_put = "";
            for(String s:new_text){
                out_put=out_put+s+" ";
            }
            out_put+="...";
            result = out_put;
            //System.out.println(out_put);
            new_text.removeAllElements();//
            //System.out.println("of random words to generate(0 to quit)?");
            //int new_quantity = sac.nextInt();
            //quantity = new_quantity;
        }
    }

    public static void start() throws IOException {
        //System.out.println("Input file name?:");
        String file = "hamlet.txt";
        //Scanner sac = new Scanner(System.in);
        //file = sac.next();
        //while(check_file(file) == false){
            //System.out.println("ERROR!(not existing filename)");
            //System.out.println("Please input file name again:");
            //file = sac.next();
        //}

        //System.out.println("Value of N？" );
        int N=4;
        //N = sac.nextInt();
        //while(N<=1){
        //    System.out.println("ERROR!(N must > 1)");
        //    System.out.println("Please input N again:");
        //    N = sac.nextInt();
        //}
        go(file,N);

    }

    @RequestMapping(value="/hello",method = RequestMethod.GET)
    public String run() throws IOException {
        start();
        return result;
    }
}
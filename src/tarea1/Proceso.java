/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea1;

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.split;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author tomas
 */
public class Proceso
{   
    private  byte E[] = null;
    String Exp;
    private ArrayList <Integer> asci = new ArrayList<>();
   
    
    public Proceso()
    {
        //
    }
    public void lectura()
    {
        byte b = 0;        
        String expresion;
        Scanner leer = new Scanner(System.in );
        System.out.println("Ingrese Una Expresion Regular: ");
        expresion=leer.nextLine();
        byte E[] = null;
        String [] parts=null;
        
        for(int i=0;i<expresion.length();i++)
        {
            E= expresion.getBytes(StandardCharsets.US_ASCII);     
            Byte byt= new Byte(E[i]);
            int z= Byte.toUnsignedInt(byt);  
            asci.add(z);
            parts = expresion.split("");
            //ASystem.out.println("imprimir-->"+ parts[i]);
        }       
        reconocer(asci,parts);
    }
    public void reconocer(ArrayList<Integer> asci, String[] caracteres)
    {
       
        for(int i=0;i<asci.size();i++)
        {
            int num = asci.get(i);
            if(num==40)
            {
                 //PARENTESIS (IZQUIERDO)
               System.out.println( caracteres[i]);
            }
            //////////////
            //////////////
             if(num==41)
            {
                 //PARENTESIS (derechooooooo)
                System.out.println( caracteres[i]);
            }
            //////////////
            //////////////
            if(num >=65 && num <=90)
            { //MAYUSCULAS
                //System.out.println("soy un caracater dela A---Z");
                System.out.println( caracteres[i]);
            }
            if(num >=97 && num <=122)
            {
                System.out.println( caracteres[i]);
            }
            ////////////7
            ///////////7
            if(num==46)
            { 
                System.out.println( caracteres[i]);
            }
            if(num==124)
            { //operador O
                System.out.println( caracteres[i]);
            }
            if(num==42)
            {
                //OPERADOR KLEENE  *
                System.out.println( caracteres[i]);
            }
        }
    }
}
    
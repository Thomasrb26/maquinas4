/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tompson;

/**
 *
 * @author tomas
 */
/*
    Thompson.java
        Compiler for Regular Expressions to Non-Deterministic Finite 
        Automata (NFA). Currently set to only work on regular expressions 
        with the alphabet of ['a','z'].
        
        This works as a Left to Right comiler, giving precedence to the left
        characters over the right. Of course this is the weakest form of 
        precedence in the compiler, after the operator precedence.
        Operator Syntax:
                '|' for union (lowest precedence)
                'ab' for some elements a and b to concat a and b. ie. 
                    concatentation done w/o operator. (middle precedence)
                '*' for kleene star (highest precedence)
                '(' & ')' for controlling precedence of operators
    @author Derek S. Prijatelj
*/
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;

public class Thompson

{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        String line;
        
     
        while(sc.hasNextLine())
        {
            System.out.println("Enter a regular expression with the " +
                "alphabet ['a','z'] & E for empty "+"\n* for Kleene Star" + 
                "\nelements with nothing between them indicates " +
                "concatenation "+ "\n| for Union \n\":q\" to quit");
            line = sc.nextLine();
            
            if (line.equals(":q") || line.equals("QUIT"))
                break;
            
            Grafo Salida = start(line);
            
            
            System.out.println("\nNFA:");
            
            Salida.display();
        }
    }
    
    
    /*
        Trans - object is used as a tuple of 3 items to depict transitions
            (state from, symbol of tranistion path, state to)
    */
    
    /*
        kleene() - Highest Precedence regular expression operator. Thompson
            algoritm for kleene star.
    */
    public static Grafo kleene(Grafo n)
    {
        Grafo result = new Grafo(n.states.size()+2);
        result.arista.add(new Aristas(0, 1, 'E')); // new trans for q0

        // copy existing transisitons
        for (Aristas t: n.arista){
            result.arista.add(new Aristas(t.desde + 1,
            t.destino + 1, t.simbolotrans));
        }
        
        // add empty transition from final n state to new final state.
        result.arista.add(new Aristas(n.states.size(), 
            n.states.size() + 1, 'E'));
        
        // Loop back from last state of n to initial state of n.
        result.arista.add(new Aristas(n.states.size(), 1, 'E'));

        // Add empty transition from new initial state to new final state.
        result.arista.add(new Aristas(0, n.states.size() + 1, 'E'));

        result.final_state = n.states.size() + 1;
        return result;
    }

    /*
        concat() - Thompson algorithm for concatenation. Middle Precedence.
    */
    public static Grafo concat(Grafo n, Grafo m)
    {
        ///*
        m.states.remove(0); // delete m's initial state

        // copy NFA m's transitions to n, and handles connecting n & m
        for (Aristas t: m.arista){
            n.arista.add(new Aristas(t.desde + n.states.size()-1,
                t.destino + n.states.size() - 1, t.simbolotrans));
        }

        // take m and combine to n after erasing inital m state
        for (Integer s: m.states){
            n.states.add(s + n.states.size() + 1);
        }
        
        n.final_state = n.states.size() + m.states.size() - 2;
        return n;
        //*/
        /* ~~~ Makes new NFA, rather than mod n. I believe my above
            sacrifice trades non-changed original for speed. Not much gain
            though. And could be implemented in the other functions.
        
        NFA result = new NFA(n.states.size() + m.states.size());
        // copy NFA n's transitions to result
        for (Trans t: n.transitions){
            result.transitions.add(new Trans(t.state_from, t.state_to,
                t.trans_symbol));
        }
        // empty transition from final state of n to beginning state of m
        result.transitions.add(new Trans(n.final_state, n.states.size(), 
            'E'));
        // copy NFA m's transitions to result
        for (Trans t: m.transitions){
            result.transitions.add(new Trans(t.state_from + n.states.size(),
                t.state_to + n.states.size(), t.trans_symbol));
        }
        
        result.final_state = n.final_state + m.final_state - 1;
        return result;
        */
    }
    
    /*
        union() - Lowest Precedence regular expression operator. Thompson
            algorithm for union (or). 
    */
    public static Grafo union(Grafo aux1, Grafo aux2)
    {
        //se refiere a la union de los dos lenguajes
        
        
        Grafo resultado = new Grafo(aux1.states.size() + aux2.states.size() + 2);

        // the branching of q0 to beginning of n
        resultado.arista.add(new Aristas(0, 1, '_'));
        
        //crea arista con un epsilon que va desde estado 0(inicial) a  estdo 1 y la agrega a resultado
        
        
        
        // copy existing transisitons of n
        
        //copia las transiciiones de primer estado(si esque existen)(a|b) las de a
        for (Aristas t : aux1.arista)
        {
            resultado.arista.add(new Aristas(t.desde + 1, t.destino + 1, t.simbolotrans));
        }
        
        // transition desde ultimo estado hacia el estado final con epsilon
        resultado.arista.add(new Aristas(aux1.states.size(),    aux1.states.size() + aux2.states.size() + 1,           '_'));

        // the branching of q0 to beginning of m
        resultado.arista.add(new Aristas(0, aux1.states.size() + 1, '_'));

        // copy existing transisitons of m
        for (Aristas t: aux2.arista)
        {
            resultado.arista.add(new Aristas(t.desde + aux1.states.size() + 1, t.destino + aux1.states.size() + 1, t.simbolotrans));
        }
        
        // transition from last m to final state
        resultado.arista.add(new Aristas(aux2.states.size() + aux1.states.size(),aux1.states.size() + aux2.states.size() + 1, '_'));
       
        // 2 new states and shifted m to avoid repetition of last n & 1st m
        resultado.final_state = aux1.states.size() + aux2.states.size() + 1;
        
        return resultado;
    }

    /*
        Recursive Descent Parser: Recursion To Parse the String.
            I have already written a Recursive Descent Parser, and so I am 
            giving stacks a go instead. This code snippet is the basic 
            structure of my functions if I were to do RDP.
    
    // <uni> := <concat> { |<concat> }
    public static NFA uni(String regex, NFA n){
        
    }
    // <conact> := <kleene> { .<kleene> }
    public static NFA concatenation(String regex, NFA n){
        
    }
    // <kleene> := <element> | <element>*
    public static NFA kleeneStar(String regex, NFA n){
        
    }
    // <element> := letter | E | ( <uni> )
    public static NFA element(String regex, NFA n){
        if (regex.charAt(0) == '('){
            uni(regex.substring(1),n);
            if(!regex.charAt(0) == ')'){
                System.out.println("Missing End Paranthesis.");
                System.exit(1);
            }
        }
    }
    */

    // simplify the repeated boolean condition checks
    public static boolean alpha(char c)
    {
        if(c>='a' && c<='z')
        {
            return true;

        }
        else if(c>='A' && c<='Z')
        {
            return true;
        }
        else if (c>='0' &&  c<='9')     
        {
            //valor de numeros (probar)
            return true;
        }
        return false;
    }
    /*
    public static boolean alphabet(char c)
    {
        if(alpha(c))
        {
            return true;
        }
        
        
        return false; //no pertene al alfabeto, es un vacio
    }
    */
    public static boolean regexOperator(char c)
    {
        
        if(c== '(')
        {
            return true;
        }
        if(c== ')')
        {
            return true;
        }
        if(c== '*')
        {
            return true;
        }
        if(c== '|')
        {
            return true;
        }
        return false;
    }
    public static boolean validRegExChar(char c)
    {
        
        return alpha(c) || regexOperator(c);
    }
    
    // validRegEx() - checks if given string is a valid regular expression.
    public static boolean validRegEx(String regex)
    {
        if (regex.isEmpty())        
            //es vacio?        
            return false;
        
        
        for (char c: regex.toCharArray())
            
            if (!validRegExChar(c))
                return false;
        return true;
    }

    /*
        compile() - compile given regualr expression into a NFA using 
            Thompson Construction Algorithm. Will implement typical compiler
            stack model to simplify processing the string. This gives 
            descending precedence to characters on the right.
    */
    public static Grafo start(String expresionRegular)
    
    {
        if (!validRegEx(expresionRegular))
        {
            System.out.println("Invalid Regular Expression Input.");
            return new Grafo(); // empty NFA if invalid regex
        }
        
        Stack <Character> operadores = new Stack <Character> ();
        Stack <Grafo> letras = new Stack <Grafo> ();
        Stack <Grafo> concadenados = new Stack <Grafo> ();
        boolean ccflag = false; // concat flag
        char op, c; //caracteres
        int contparentesis = 0;
        
        Grafo aux1;
        Grafo aux2;

        for (int i = 0; i < expresionRegular.length(); i++)
        {
            c = expresionRegular.charAt(i); // devuelve valor de un caden en un i especifico
            

            if (alpha(c)) //comprobacion (es c una letra de la a a la Z y de la A a l Z o 0-9? ) 
            {
                letras.push(new Grafo(c)); //
                
                if (ccflag)
                { // concat this w/ previous
                    operadores.push('.'); // '.' used to represent concat.
                }
                else
                    ccflag = true;
            }
            else
            {
                if (c == ')')
                {
                    ccflag = false;
                    
                    if (contparentesis == 0)
                    {
                        System.out.println("Faltan Parentesis");
                        System.exit(1);
                    }
                    
                    else
                    { 
                        contparentesis--;
                    
                    }
                    // process stuff on stack till '('
                    
                    while (!operadores.empty() && operadores.peek() != '(')
                    {
                        
                        op = operadores.pop();
                        
                        
                        if (op == '.')
                        {
                            aux2 = letras.pop();
                            aux1 = letras.pop();
                            letras.push(concat(aux1, aux2));
                        }
                        
                        else if (op == '|')
                        {
                            aux2 = letras.pop();
                            
                            if(!operadores.empty() &&   operadores.peek() == '.')
                            {
                                
                                concadenados.push(letras.pop());
                                while (!operadores.empty() && 
                                    operadores.peek() == '.'){
                                    
                                    concadenados.push(letras.pop());
                                    operadores.pop();
                                }
                                aux1 = concat(concadenados.pop(),
                                    concadenados.pop());
                                while (concadenados.size() > 0){
                                   aux1 =  concat(aux1, concadenados.pop());
                                }
                            }
                            else
                            {
                                aux1 = letras.pop();
                            }
                            letras.push(union(aux1, aux2));
                        }
                    }
                }
                
                else if (c == '*')
                {
                    letras.push(kleene(letras.pop()));
                    ccflag = true;
                }
                else if (c == '(')
                {                    
                    operadores.push(c);
                    
                    contparentesis++;
                }
                
                else if (c == '|')
                {
                    operadores.push(c);
                    ccflag = false;
                }
            }
        }
        while (operadores.size() > 0){
            if (letras.empty()){
                System.out.println("Error: imbalanace in operands and "
                + "operators");
                System.exit(1);
            }
            op = operadores.pop();
            if (op == '.'){
                aux2 = letras.pop();
                aux1 = letras.pop();
                letras.push(concat(aux1, aux2));
            }
            else if (op == '|'){
                aux2 = letras.pop();
                if( !operadores.empty() && operadores.peek() == '.'){
                    concadenados.push(letras.pop());
                    while (!operadores.empty() && operadores.peek() == '.'){
                        concadenados.push(letras.pop());
                        operadores.pop();
                    }
                    aux1 = concat(concadenados.pop(),
                        concadenados.pop());
                    while (concadenados.size() > 0){
                       aux1 =  concat(aux1, concadenados.pop());
                    }
                }
                else{
                    aux1 = letras.pop();
                }
                letras.push(union(aux1, aux2));
            }
        }
        return letras.pop();
    }   
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tompson;

import java.util.ArrayList;

/**
 *
 * @author tomas
 */
public class Grafo
    {
        public ArrayList <Integer> states;
        public ArrayList <Aristas> arista;
        public int final_state;
        
        public Grafo()
        {
            this.states = new ArrayList <Integer> ();
            this.arista = new ArrayList <Aristas> ();
            this.final_state = 0;
        }
        
        public Grafo(int size)
        {
            this.states = new ArrayList <Integer> ();
            this.arista = new ArrayList <Aristas> ();
            this.final_state = 0;
            this.setStateSize(size);
        }
        public Grafo(char c)
                //nodo
        {
            this.states = new ArrayList<Integer> ();
            this.arista = new ArrayList <Aristas> ();
            this.setStateSize(2);
            this.final_state = 1;
            this.arista.add( new Aristas(0, 1, c));
        }

        public void setStateSize(int size){
            for (int i = 0; i < size; i++)
                this.states.add(i);
        }

        public void display(){
            for (Aristas t: arista){
                System.out.println("("+ t.desde +", "+ t.simbolotrans +
                    ", "+ t.destino +")");
            }    
        }
    }
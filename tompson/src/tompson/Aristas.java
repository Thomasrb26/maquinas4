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
public class Aristas
    {
        public int desde, destino;
        public char simbolotrans;

        public Aristas(int v1, int v2, char sym)
        {
            this.desde = v1;
            this.destino = v2;
            this.simbolotrans = sym;
        }
    }
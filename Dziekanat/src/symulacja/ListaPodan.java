/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja;

import java.util.ArrayList;

/**
 *
 * @author lukasz
 */
class Podanie {
    int idStudenta;
    boolean podpisane;
    
    public Podanie(int idStudenta, boolean podpisane)
    {
        this.idStudenta = idStudenta;
        this.podpisane = podpisane;
    }
}

public class ListaPodan extends ArrayList<Podanie>{
    
    public ListaPodan()
    {
        super();
    }
    
    
    /**
     *
     * @param idStudenta id studenta, który przychodzi po podanie
     * @return true jeśli jego podanie jest podpisane,/
     * false jeśli nie. WAŻNE! jeśli podanie jest podpisane to po sprawdzeniu jest
     * usuwane z kolejki
     */
    public boolean isPodpisaneAndGet(int idStudenta)
    {
        for(int i=0; i<this.size(); i++)
        {
            if(get(i).podpisane)
            {
                remove(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Dodaje podanie do listy
     * @param idStudenta
     */
    public void addPodanie(int idStudenta)
    {
        add(new Podanie(idStudenta, false));
    }
    
    /**
     * Funkcja, która podpisuje pierwsze niepodpisane podanie
     * @return idStudenta tego podania, -1 jeśli nie ma podania do podpisu
     */
    public int podpiszPodanie()
    {
        for(int i=0; i<size(); i++)
        {
            if(get(i).podpisane == false)
            {
                get(i).podpisane = true;
                return get(i).idStudenta;
            }
        }
        return -1;
    }
    
    /**
     * Zwraca, czy istnieje niepodpisane podanie na liście
     * @return czy istnieje niepodpisane podanie na liście
     */
    public boolean isPodanieDoPodpisania()
    {
        for(int i=0; i<size(); i++)
        {
            if(get(i).podpisane == false)
            {
                return true;
            }
        }
        return false;
    }
}

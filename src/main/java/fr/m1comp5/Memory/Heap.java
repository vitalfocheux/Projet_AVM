package fr.m1comp5.Memory;

import java.util.HashMap;
import java.util.Map;

public class Heap {

    // Map pour représenter le tas, avec des adresses (entiers) et des valeurs (Objects).
    private Map<Integer, Object> heapMemory;
    private int currentAddress; // Adresse actuelle pour la prochaine allocation

    public Heap() {
        this.heapMemory = new HashMap<>();
        this.currentAddress = 0;    // Adresse de départ
    }

    // Allocation de mémoire pour une nouvelle variable
    public int allocate(Object value) {
        int address = currentAddress;
        heapMemory.put(address, value); 
        currentAddress++;
        return address; 
    }

    // Lire la valeur d'une adresse
    public Object get(int address) {
        if (!heapMemory.containsKey(address)) {
            throw new RuntimeException("Adresse " + address + " non allouée dans le tas.");
        }
        return heapMemory.get(address);
    }

    // Écrire une valeur à une adresse donnée
    public void set(int address, Object value) {
        if (!heapMemory.containsKey(address)) {
            throw new RuntimeException("Adresse " + address + " non allouée dans le tas.");
        }
        heapMemory.put(address, value); // Mettre à jour la valeur
    }

    // Libérer la mémoire à une adresse spécifique
    public void free(int address) {
        if (heapMemory.containsKey(address)) {
            heapMemory.remove(address); // Supprimer l'entrée du tas
        } else {
            throw new RuntimeException("Adresse " + address + " non allouée dans le tas.");
        }
    }

    // Méthode pour afficher le contenu du tas (pour le débogage)
    public void displayHeap() {
        System.out.println("Contenu du tas :");
        for (Map.Entry<Integer, Object> entry : heapMemory.entrySet()) {
            System.out.println("Adresse " + entry.getKey() + " -> " + entry.getValue());
        }
    }
}


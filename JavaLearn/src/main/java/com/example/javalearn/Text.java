package com.example.javalearn;

public class Text {
    public static void  main(String[] arg)
    {
     System.out.println("Hallo the World");

     Human aPerson = new Human();
     Human.breath();
     aPerson.weight();

     System.out.println(aPerson.height);

    }
}

class Human {
    public static void breath() {
        System.out.println("hu..hu..");

    }

    void weight() {
        System.out.println("You are very thin!");
    }

    int height = 5;



}

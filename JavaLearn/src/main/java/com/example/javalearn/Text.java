package com.example.javalearn;

public class Text {
    public static void  main(String[] arg)
    {
     System.out.println("Hallo the World");

     Human aPerson = new Human();
     aPerson.breath();

     System.out.println(aPerson.height);

    }
}

class Human
{
    void breath(){
        System.out.println("hu..hu..");
    }

    int height = 5;
}
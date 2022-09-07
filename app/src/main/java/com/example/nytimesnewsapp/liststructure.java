package com.example.nytimesnewsapp;

public class liststructure {


    String a,b,c,link,d;

    public liststructure(String a , String b, String c, String link, String d){
        this.a=a;
        this.b=b;
        this.c=c;
        this.link=link;
        this.d=d;
    }

    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public String getC() {
        return c;
    }
    public String getD() {
        return d;
    }
    public String getlink() {
        return link;
    }

    public void setA(String a) {
        this.a = a;
    }
    public void setlink(String link) {
        this.link = link;
    }
    public void setB(String b) {
        this.b = b;
    }
    public void setC(String c) {
        this.c = c;
    }

    public void setD(String d) {
        this.d = d;
    }
}


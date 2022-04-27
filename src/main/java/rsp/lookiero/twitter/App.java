package rsp.lookiero.twitter;

import rsp.lookiero.twitter.controller.Twitter;

public class App 
{
    public static void main( String[] args )
    {        
        Twitter twitter = new Twitter();

        System.out.println( "[Lookiero] - Console Twitter" );
        twitter.start();
    }
    
    
}

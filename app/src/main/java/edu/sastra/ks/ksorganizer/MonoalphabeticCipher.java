package edu.sastra.ks.ksorganizer;

/**
 * Created by Siva Subramanian L on 18-01-2017.
 * Publisher ID: pub-2413306686764988
 * Ad unit ID: ca-app-pub-2413306686764988/2707408956
 */

public class MonoalphabeticCipher
{
    public static char p[]  = { '2','a', 'b', 'c', 'd', 'e','9', 'f','g', 'h', 'i',
            'j','8', 'k', 'l', 'm','0', 'n',' ',',','o','3','$','%', 'p', 'q','4', 'r', 's','5', 't', 'u', 'v',
            'w', 'x','6', 'y','.','_', 'z','1','@','+','-','7'};

    public static char ch[] = { '0','G','@','2','4','Q', 'W', 'E', 'R','*','1', 'T','3', 'Y', 'U', 'I', 'O',
            'P', 'A', 'S', 'D', '/','F', '#','6','+','!', 'H', 'J','5','7','9', 'K', 'L', 'Z', 'X', 'C',
            'V', 'B', 'N', 'M','8','^','-','?','(','}','[' };

    public static String doEncryption(String s)
    {
        s = s.toLowerCase();
        char c[] = new char[(s.length())];
        for (int i = 0; i < s.length(); i++)
        {
            for (int j = 0; j < 46; j++)
            {
                if (p[j] == s.charAt(i))
                {
                    c[i] = ch[j];
                    break;
                }
            }
        }
        return (new String(c));
    }

    public static String doDecryption(String s)
    {
        char p1[] = new char[(s.length())];
        for (int i = 0; i < s.length(); i++)
        {
            for (int j = 0; j < 46; j++)
            {
                if (ch[j] == s.charAt(i))
                {
                    p1[i] = p[j];
                    break;
                }
            }
        }
        return (new String(p1));
    }

    //KS001245$%SIVA SUBRAMANIAN$%SASTRA UNIVERSITY$%M$%8438181024$%YES$%SIVASUBRAMANIAN96@GMAIL.COM
}
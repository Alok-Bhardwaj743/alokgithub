import java.util.*;
public class java_1{

public static void main(String args[]){
   Scanner sc = new Scanner(System.in);
   System.out.print("Enter n : ");
   int n = sc.nextInt();
   int sum=0;
   while(n>0){
      int ld=n%10;
      sum+=ld;
      n=n/10;
   }
   System.out.print("Sum of digits in the number : "+sum);
   sc.close();
}}
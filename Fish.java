import java.io.*;

public class Fish
{

    //this function calculates de distance that a specific Boat needs to travel to reach a specific Spot
    public static int returnDistanceBetween_B_and_S(int [][] arrayBoats, int [][] arraySpots, int positionBoat, int positionSpot)
    {
        int xDifference = Math.abs(arrayBoats[positionBoat][0] - arraySpots[positionSpot][0]);    //Module of Position X of Boat less Position X of Spot
        int yDifference = Math.abs(arrayBoats[positionBoat][1] - arraySpots[positionSpot][1]);    //Module of Position Y of Boat less Position Y of Spot
        
        return xDifference + yDifference;   
    }

    

    public static int[] solve(int numberOfFishingBoats, int numberOfFishingSpots, int [][]arrayOfBoats, int [][] arrayOfSpots)
    {
        int[] solution = new int[3];          //array to return the solution values

        int sumRating, sumSpots, sumDistance;      
        sumRating = sumSpots = sumDistance = 0;
        
        if(numberOfFishingBoats == numberOfFishingSpots)      //if number of boats is equal to number of spots, the boat with less rating will stay with the spot with less fish,
        {                                                     //the second boat with less rating wiht the second spot with less fish...until the end
            for(int i = 0; i < numberOfFishingSpots; i++)
            {
                sumRating += arrayOfBoats[i][2];
                sumSpots += arrayOfSpots[i][2];
                sumDistance += returnDistanceBetween_B_and_S(arrayOfBoats, arrayOfSpots, i, i);
            }
            solution[0] = sumSpots;
            solution[1] = sumDistance;
            solution[2] = sumRating;
        }
        else if(numberOfFishingBoats < numberOfFishingSpots)  //if number of boats is smaller than number of spots, we need to exclude the spots with less fish, because
        {                                                     //we want to maximize the number of fish captured. After that, the program will work as if the number
            for(int i = 0; i < numberOfFishingBoats; i++)     //of boats was equal to number of spots
            {
                sumRating += arrayOfBoats[i][2];
                sumSpots += arrayOfSpots[i][2];
                sumDistance += returnDistanceBetween_B_and_S(arrayOfBoats, arrayOfSpots, i, i);
            }
            solution[0] = sumSpots;
            solution[1] = sumDistance;
            solution[2] = sumRating;
        }
        else                                                   //if number of boats is bigger than number of spots
        {
            int distanceTravelled[][] = new int [numberOfFishingBoats+1][numberOfFishingSpots+1];       //we create to matrices to. 1 - calculate the minimal value of distance
            int amountOfRating[][] = new int [numberOfFishingBoats+1][numberOfFishingSpots+1];          //travelled by all the picked boats; AND 2 - calculate the minimal value of
                                                                                                          //ratings of the boats in case of two or more cases of equal value of distance travelled
            for(int i = 0; i <= numberOfFishingBoats; i++)
            {
                distanceTravelled[i][numberOfFishingSpots] = 0;
                amountOfRating[i][numberOfFishingSpots] = 0;
            }
                                                                  
            for(int i = 0; i < numberOfFishingSpots; i++)           
            {
                distanceTravelled[numberOfFishingBoats][i] = 0;
                amountOfRating[numberOfFishingBoats][i] = 0;
                sumSpots+= arrayOfSpots[i][2];               //once all the boats will be picked, we can can already calculate the value of fish captured.
            }

            int diagonalLimiter = numberOfFishingSpots;
            
            for(int l = numberOfFishingBoats-1; l >= 0; l--)
            {
                diagonalLimiter--;
                for(int c = numberOfFishingSpots-1; c >= 0; c--)
                {
                    int auxDistance = distanceTravelled[l+1][c+1] + returnDistanceBetween_B_and_S(arrayOfBoats, arrayOfSpots, l, c);
                    int auxRating = amountOfRating[l+1][c+1] + arrayOfBoats[l][2];

                    if(c <= diagonalLimiter && diagonalLimiter >= 0)
                    {
                        distanceTravelled[l][c] = auxDistance;
                        amountOfRating[l][c] = auxRating;
                    }
                    else
                    {
                        if(auxDistance < distanceTravelled[l+1][c])   //we only change the value if it is "<", and not if it is "<=", because in cases of equal minimal distance,
                        {                                             //we want to minimize the ratings of the boats.
                            distanceTravelled[l][c] = auxDistance;
                            amountOfRating[l][c] = auxRating;
                        }
                        else
                        {
                            distanceTravelled[l][c] = distanceTravelled[l+1][c];
                            amountOfRating[l][c] = amountOfRating[l+1][c];
                        }
                    }
                }     
            }
            solution[0] = sumSpots;
            solution[1] = distanceTravelled[0][0];    
            solution[2] = amountOfRating[0][0];            //the best results are in the position (0,0) of each matrix.
        }
        return solution;         //the function solve returns the solution
    }






    public static void main(String[] args) throws IOException
    {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));           //all the input goes to the object "input"
        String firstInputLine = input.readLine();
        String [] aux = firstInputLine.split(" ");
      
        int numberOfFishingBoats = Integer.parseInt(aux[0]);            //this variable saves the number of boats
        int numberOfFishingSpots = Integer.parseInt(aux[1]);            //this variable saves the number of spots

        int arrayOfBoats[][] = new int[numberOfFishingBoats][3];         //array to keep all  the boats
        int arrayOfSpots[][] = new int[numberOfFishingSpots][3];         //array to keep all the spots
        
        for(int i = 0; i < numberOfFishingBoats; i++)
        {
            String lineboat = input.readLine();
            String [] lineboatAux = lineboat.split(" ");             //all information of one boat is here

            for(int j = 0; j < 3; j++)
            {                                                               //all positions [i][0] keep the x coordinate of the boat
                arrayOfBoats[i][j] = Integer.parseInt(lineboatAux[j]);      //all positions [i][1] keep the y coordinate of the boat
            }                                                               //all positions [i][2] keep the rating of the boat

            int positionAuxiliar = i;

            while(positionAuxiliar > 0 && arrayOfBoats[positionAuxiliar][2] > arrayOfBoats[positionAuxiliar-1][2])       //organize array of boats by rating
            {                                                                                                            //the boat with the biggest rating comes in first and
                int [] auxiliar = new int[3];                                                                            //the boat with less rating in last, in the array
                auxiliar = arrayOfBoats[positionAuxiliar-1];
                arrayOfBoats[positionAuxiliar-1] = arrayOfBoats[positionAuxiliar];
                arrayOfBoats[positionAuxiliar] = auxiliar;
                positionAuxiliar--;
            } 
        }

        
        for(int i = 0; i < numberOfFishingSpots; i++)
        {
            String linespot = input.readLine();
            String [] linespotaux = linespot.split(" ");           //all information of one spot is here

            for(int j = 0; j < 3; j++)
            {                                                                //all positions [i][0] keep the x coordinate of the spot
                arrayOfSpots[i][j] = Integer.parseInt(linespotaux[j]);       //all positions [i][1] keep the y coordinate of the spot
            }                                                                //all positions [i][2] keep the amount of fish of the spot

            int positionAuxiliar = i;

            while(positionAuxiliar > 0 && arrayOfSpots[positionAuxiliar][2] > arrayOfSpots[positionAuxiliar-1][2])            //organize array of spots by amount of fish
            {                                                                                                                 //the spot with more fish comes in first and
                int [] auxiliar = new int[3];                                                                                 //the spot with less fish in last, in the array
                auxiliar = arrayOfSpots[positionAuxiliar-1];
                arrayOfSpots[positionAuxiliar-1] = arrayOfSpots[positionAuxiliar];
                arrayOfSpots[positionAuxiliar] = auxiliar;
                positionAuxiliar--;
            } 
        }


        int result[];
        result = solve(numberOfFishingBoats, numberOfFishingSpots, arrayOfBoats, arrayOfSpots);

        System.out.println((int)result[0] + " " + (int)result[1] + " " + (int)result[2]);          //print the result
    }
}

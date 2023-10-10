package solver;

public class SokoBot {

  public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {



    System.out.println("Width"+width+" Height"+height);

    System.out.println("Map Data");
    for(int i = 0; i < height; i++){
     
      for(int j = 0; j < width; j++) {
        System.out.print(mapData[i][j]);
      }
      System.out.println("");
    }

    System.out.println("Items Data");
    for(int i = 0; i < height; i++){

      for(int j = 0; j < width; j++) {
        System.out.print(itemsData[i][j]);
      }
      System.out.println("");
    }


    /*
     * YOU NEED TO REWRITE THE IMPLEMENTATION OF THIS METHOD TO MAKE THE BOT SMARTER
     */
    /*
     * Default stupid behavior: Think (sleep) for 3 seconds, and then return a
     * sequence
     * that just moves left and right repeatedly.
     */
    try {
      Thread.sleep(3000);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return "lrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlr";
  }

}

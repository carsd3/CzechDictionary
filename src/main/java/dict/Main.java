package dict;

/**
 *
 * @author Carlos
 */
import java.io.*;
import java.util.*;
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CTrie dict1 = new CTrie();
        CTrie dict2 = new CTrie();
        CTrie dict3 = new CTrie();
        System.out.println("Loading dictionaries...");
        loadData(100, dict1);
        loadData(1000, dict2);
        loadData(7600, dict3);
        System.out.println("Dictionaries loaded.");
        //dict1.print();
        //dict2.print();
        //dict3.print();
        
        //Execution loop
        CTrie dict = new CTrie();
        System.out.println("---------------------------------------------");
        System.out.println("Select which dictionary you want to use (1-3): ");
        Scanner input = new Scanner(System.in);
        switch(input.nextInt()) {
            case 1:
                dict = dict1;
                break;
            case 2:
                dict = dict2;
                break;
            case 3:
                dict = dict3;
                break;
        }
        System.out.println("Enter the name of the input file: ");
        
        input = new Scanner(System.in);
        String li = input.nextLine();
        input.close();
        
        //li = "text.txt";
        
        Map<String, Integer> words = new HashMap<String, Integer>();
        try{
            File myObj = new File(li);
            Reader myReader = new InputStreamReader(new FileInputStream(myObj), "UTF-8");
            while (myReader.ready()) {
                li = "";
                char a;
                while ((a = (char) myReader.read()) != '\n') {
                    if (a == 65535) {
                        break;
                    }
                    li += a;
                }
                if (li == "") {
                    break;
                }
                String[] line = li.split("\\,|\\.?\\s");  
                for (String l : line) {
                    if (l == "") {
                        continue;
                    }
                    if (dict.search(l) == true) {
                        if (words.containsKey(l)) {
                            int value = words.get(l);
                            words.put(l, value + 1);
                        } else {
                            words.put(l, 1);
                        }
                    }
                }
            }    
            myReader.close();  
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        Map<String, Integer> sa = sortByComparator(words, false);
        if (sa.size() < 15) {
            for (Map.Entry<String, Integer> entry : sa.entrySet()) {
                double ro = ((double)entry.getValue()/(double)sa.size())*100;
                System.out.println("Word: " + entry.getKey() + "; Absolute Occurence: "+ entry.getValue() + "; Relative Occurrence: " + ro + "%");
            }
        } else {
            String output = "";
            for (Map.Entry<String, Integer> entry : sa.entrySet()) {
                double ro = ((double)entry.getValue()/(double)sa.size())*100;
                output += "Word: " + entry.getKey() + "; Absolute Occurence: "+ entry.getValue() + "; Relative Occurrence: " + ro + "%<br>";
            }
            try {
                FileWriter out = new FileWriter("output.html");
                out.write(output);
                out.close();
            } catch (IOException e) {
                    System.out.println("An error occurred.");
            }
            System.out.println("The file output.html has been succesfully edited");
            System.out.println("---------------------------------------------");
        }
    }
    
    //This method loads data from as many files 
    //as passed through as an argument into the Compressed Trie structure
    public static void loadData(int num_files, CTrie t) {
        int i = 0;
        while(i < num_files) {
            try {
                String a = (i+1) + ".txt";
                File myObj = new File(a);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    String[] b = data.split(" ");
                    for(String v : b) {
                        t.insert(v);
                    }
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            i++;
        }
    }
    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order) {

        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}

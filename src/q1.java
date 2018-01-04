import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by renuka on 11/27/17.
 */
public class q1 {
    public static void main(String[] args) throws IOException {

        List<Integer> training = new ArrayList<>();
        List<Integer> test = new ArrayList<>();
        List<Integer> valid = new ArrayList<>();
        //List<Boolean> used = new ArrayList<Boolean>();

        String con = readFile("resource/bc.txt");
        //System.out.print(con);

        String [] lines = new String [569];
        String [] col = new String [32];
        lines = con.split("\n");
        String [] id = new String [569];
        String [] label = new String [569];
        String [] radius = new String [569];
        String [] texture = new String [569];
        String [] perimeter = new String [569];
        String [] area = new String [569];
        String [] smoothness = new String [569];
        String [] compactness = new String [569];
        String [] concavity = new String [569];
        String [] concave = new String [569];
        String [] symmetry  = new String [569];
        String [] fractal = new String [569];

        Double [] lambda = {0.001, 0.01, 0.1, 1.0};
        Double [] l1 = new Double[5000];
        Double [] l2 = new Double[5000];
        Double [] l3 = new Double[5000];
        Double [] l4 = new Double[5000];

        for (int i = 0; i < 569; i++){
            col = lines[i].split(",");
            id[i] = col[0];
            label[i] = col[1];
            radius[i] =col[2];
            texture[i] = col[3];
            perimeter[i] = col[4];
            area[i] = col[5];
            smoothness[i] = col[6];
            compactness[i] = col[7];
            concavity[i] = col[8];
            concave[i] = col[9];
            symmetry[i] = col[10];
            fractal[i] = col[11];
        }

        while (training.size() < 369){
            int rand = (int) (Math.random() * 569);
            int index = Integer.parseInt(id[rand]);
            if (!training.contains(index)) {
                training.add(index);
            }
            //used.set(rand, false);
            //training.add(index);
        }

        while (test.size() < 100) {

            int rand = (int) ( Math.random() * 569);
            int index = Integer.parseInt(id[rand]);
            if (!training.contains(index) && !test.contains(index)) {
                test.add(index);
            }
            //used.set(rand, false);


        }

        while (valid.size() < 100) {

            int rand = (int) (Math.random() * 569);
            int index = Integer.parseInt(id[rand]);
            if (!training.contains(index) && !test.contains(index) && !valid.contains(index)) {
                valid.add(index);;
            }
            //used.set(rand, false);
        }


        for (int i = 0 ; i < 4; i++){
            double w = 1.0;
            double b = w;
            Double [] a = {w, w,w,w,w,w,w,w,w,w,w};
            //double currlam = lambda[i];
            double currlam = 1;
            for (int j = 0; j < 50; j++) {

                for (int k = 0; k < 100; k++) {

                    int rand1 = (int) (Math.random() * 369);
                    int idnum = training.get(rand1);

                    for (int f = 0; f < 569; f ++){
                        if (idnum == Integer.parseInt(id[f])){
                            rand1 = f;
                            break;
                        }
                    }

                    List<Double> x= new ArrayList<Double>();
                    x.add(Double.parseDouble(radius[rand1]));
                    x.add(Double.parseDouble(texture[rand1]));
                    x.add(Double.parseDouble(perimeter[rand1]));
                    x.add(Double.parseDouble(area[rand1]));
                    x.add(Double.parseDouble(smoothness[rand1]));
                    x.add(Double.parseDouble(compactness[rand1]));
                    x.add(Double.parseDouble(concavity[rand1]));
                    x.add(Double.parseDouble(concave[rand1]));
                    x.add(Double.parseDouble(symmetry[rand1]));
                    x.add(Double.parseDouble(fractal[rand1]));

                    double stepsize = (double) (1.0/(j+1+1)); //j+1+1
                    double check = 0.0;


                    for (int d = 0; d < 10; d++){
                        check += (x.get(d) * a[d]);
                    }
                    check += b;
                    System.out.println(check);
                    if (check>= 1) {

                        //System.out.println(check);


                        for (int d = 0; d < 10; d++) {
                            double sub = stepsize * a[d] * currlam;
                            a[d] = a[d] - sub;
                        }

                    }
                    else {
                        //System.out.println("check else: " + check);

                        for (int d =0; d < 10; d++) {
                            double sub = stepsize * a[d] * currlam;
                            double sub2 = stepsize * check * x.get(d);
                            a[d] = a[d] - sub  + sub2;
                            //double sub3 = stepsize * check;
                        }
                        b = b + (stepsize * check);

                    }

                    double count = 0.0;
                    for (int m = 0; m < 100; m++) {
                        int testid = test.get(m);
                        int aind = 0;
                        for (int f = 0; f < 569; f ++){
                            if (testid == Integer.parseInt(id[f])){
                                aind = f;
                                break;
                            }
                        }
                        List<Double> xtest = new ArrayList<Double>();
                        xtest.add(Double.parseDouble(radius[aind]));
                        xtest.add(Double.parseDouble(texture[aind]));
                        xtest.add(Double.parseDouble(perimeter[aind]));
                        xtest.add(Double.parseDouble(area[aind]));
                        xtest.add(Double.parseDouble(smoothness[aind]));
                        xtest.add(Double.parseDouble(compactness[aind]));
                        xtest.add(Double.parseDouble(concavity[aind]));
                        xtest.add(Double.parseDouble(concave[5]));
                        xtest.add(Double.parseDouble(symmetry[aind]));
                        xtest.add(Double.parseDouble(fractal[aind]));

                        String lab = label[aind];

                        double y = 0.0;
                        double dot = 0.0;

                        for (int z = 0; z < 10; z++ ) {
                            double h = xtest.get(z);
                            double g =  a[z];

                            dot += xtest.get(z) * a[z];
                        }

                        y = dot+b;

                        if (y > 0 && lab.equals("M")) {
                            count++;


                        }
                        if (y < 0 && lab.equals("B")){
                            count++;
                            //l1[((j* 100) + k)] = (double)count/100.0;
                            //System.out.println(count/100.0);
                            //System.out.println("B");

                        }
                    }
                   // System.out.print(rand1 + "  ");
                    //System.out.println(count);

                    //l1[((j* 100) + k)] = (double)count/100.0;
                    //if (i == 1) l1[(j)*100 +k] = (double)count/100.0;
                    //if (i == 2) l2[(j)*100 +k] = (double)count/100.0;
                    //if (i == 3) l3[(j)*100 +k] = (double)count/100.0;
                    //if (i == 4) l4[(j)*100 +k] = (double)count/100.0;

                }

            }

        }

        /*for (int i = 0; i < 5000; i++){
            System.out.println(l2[i]);
        }*/


    }

    /**
     * from stack overflow
     * @param filename
     */
    static public String readFile(String filename) throws IOException {
        String content = null;
        File file = new File(filename);
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader !=null){reader.close();}
        }
        return content;
    }
}
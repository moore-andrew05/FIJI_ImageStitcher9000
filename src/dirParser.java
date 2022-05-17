import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.ArrayList;

public class dirParser {
    File dir; 
    String[] rawfileList; //List generated by parsing directory.
    List<List<String>> cleanfileList = new ArrayList<List<String>>(); //Sorted File list.
    int first_img;
    int last_img;

    FilenameFilter refLogFilter = new FilenameFilter() {
        public boolean accept(File f, String name)
                {
                    return name.contains("REF.dv.log");
                }
    };

    FilenameFilter logFilter = new FilenameFilter() {
        public boolean accept(File f, String name) 
                {
                    return name.contains(".dv.log") && !name.contains("REF");
                }
    };

    dirParser(String dir) {
        this.dir = new File(dir);
        rawfileList = this.dir.list(refLogFilter);
        this.first_img = findFirstImage();
        this.last_img = findLastImage();
    }

    public int getLastImage() {
        return last_img;
    }

    public int getFirstImage() {
        return first_img;
    }

    public String[] getrawFileList() {
        return rawfileList;
    }

    public List<List<String>> getcleanfileList() {
        return cleanfileList;
    }

    /**
     * Returns number of last image in set. Could easily be 
     * rewritten and improved. Currently very specific to our
     * generated delta vision file names.
     * @return the max value of image incrementor
     */
    private int findLastImage() {
        int max = 0;
        for(int i = 0; i < rawfileList.length; i++) {
            String currimage = rawfileList[i];
            int num = Integer.parseInt(currimage.substring(currimage.indexOf("e") + 1, currimage.indexOf("_")));
            if (num > max) max = num;
        }
        return max;
    }

    private int findFirstImage() {
        int min = 1000;
        for(int i = 0; i < rawfileList.length; i++) {
            String currimage = rawfileList[i];
            int num = Integer.parseInt(currimage.substring(currimage.indexOf("e") + 1, currimage.indexOf("_")));
            if (num < min) min = num;
        }
        return min;
    }

    /**
     * Takes rawImageList generated in constructor and sorts into a 2D array,
     * with rows sorted by image #. Checker is specific to our delta vision
     * file names, but can be easily altered.
     * @param files raw file list.
     */
    public void bigCleaner(String[] files) {
        int last = getLastImage();
        for(int i = 0; i < last; i++) {
            String checker = "image" + (i+1) + "_";
            List<String> tmp = new ArrayList<String>();
            for(int k = 0; k < files.length; k++) {
                if (files[k].startsWith(checker)) {
                    tmp.add(files[k]);
                }
            }
            cleanfileList.add(tmp);
        }
    }
}

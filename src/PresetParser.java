import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class PresetParser {

    private String path;

    public PresetParser(String path) {
        this.path = path;
    }

    public String[] getPresetList() {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> presetNames = new ArrayList<>();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                presetNames.add(file.getName().split("\\.")[0]);
            }
        }

        return presetNames.stream().toArray(String[]::new);
    }

    public void parsePreset(World world, int startingY, int startingX, String fileName) throws IOException {
        ArrayList<String> presetLines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(path + fileName + ".rle"))) {
            stream.forEach(presetLines::add);
        }
        String[] lines = presetLines.stream().toArray(String[]::new);
        int width = Integer.valueOf(lines[0].split(",")[0].split("=")[1].trim());
        int height = Integer.valueOf(lines[0].split(",")[1].split("=")[1].trim());

        if(width > LifePanel.cols || height > LifePanel.rows) {
            JOptionPane.showMessageDialog(null,
                    "Imported pattern does not fit on screen!",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] seq = lines[1].split("\\$");

        for(int r = 0; r < seq.length; r++) {
            char[] lineSeq = seq[r].toCharArray();
            int run_count = 0;
            String tempRunCount = "", fullCommand = "";
            for(char tag : lineSeq) {
                if(tag == '!') continue;

                if(Character.isDigit(tag)) {
                    tempRunCount += String.valueOf(tag);
                } else {
                    run_count = (!tempRunCount.equals("")) ? Integer.valueOf(tempRunCount) : 1;
                    tempRunCount = "";

                    for (int c = 0; c < run_count; c++) {
                        fullCommand += tag;
                    }
                }
            }

            char[] fullCommandSeq = fullCommand.toCharArray();
            for(int c = 0; c < fullCommandSeq.length; c++) {
                if(!world.drawIn(startingY + r, startingX + c, fullCommandSeq[c])) return;
            }
        }
    }

}

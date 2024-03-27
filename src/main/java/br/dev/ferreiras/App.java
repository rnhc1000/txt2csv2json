package br.dev.ferreiras;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import static br.dev.ferreiras.utils.ConsumeAnaBlock.getLatestListOfBlockedSites;
import static br.dev.ferreiras.utils.ConsumeAnaBlock.getVersionBlockedSites;
import static br.dev.ferreiras.utils.GenerateCVSFile.*;

/**
 * Hello world!
 */
public class App {

  public static void main(String[] args) throws IOException {

    String filePath = "/home/rferreira/Downloads/";
    String fileName = "blockedURL.txt";
    String csvFile = "ascendingBlockedSitesMar24.txt";
    List<String> list = getLatestListOfBlockedSites();
    Logger logger = Logger.getLogger(App.class.getName());
    boolean writeFile = false;
    if (!list.isEmpty()) {

      writeFile = writeBlockedSites(list, filePath, fileName);
      logger.info("List of blocked sites stored with success!");

    } else {
      logger.info("List returned empty!");
    }

    Set<String> blockedSites = new TreeSet<>();
    boolean existsFile = fileExists(filePath, fileName);
    if (existsFile) {
      blockedSites = readBlockedSites(filePath, fileName);
    }

    System.out.println(blockedSites);
    generateCSV(blockedSites,filePath,csvFile);
    generateJSON(filePath, csvFile);

  }
}


package br.dev.ferreiras;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import static br.dev.ferreiras.utils.GenerateCVSFile.*;

/**
 * Hello world!
 */
public class App {

      public static void main(String[] args) throws IOException {

          String filePath = "/home/rferreira/Downloads/";
          String fileName = "blockedURL.txt";
          String csvFile = "ascendingBlockedSitesMar24.txt";
          Set<String> blockedSites = new TreeSet<>();
          boolean existsFile = fileExists(filePath, fileName);
          if (existsFile) {
              blockedSites = readBlockedSites(filePath, fileName);
          }

          System.out.println(blockedSites);
          generateCSV(blockedSites);
          generateJSON(filePath, csvFile);

      }
  }


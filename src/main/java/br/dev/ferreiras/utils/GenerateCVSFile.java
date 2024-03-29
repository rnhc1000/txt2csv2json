package br.dev.ferreiras.utils;

import br.dev.ferreiras.App;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static br.dev.ferreiras.utils.ConsumeAnaBlock.getVersionBlockedSites;

public class GenerateCVSFile {
  public static boolean fileExists(String filePath, String fileName) throws IOException {
    Logger logger = Logger.getLogger(App.class.getName());
    logger.info("Task initiated....");
    String fileToBeProcessed = filePath.concat(fileName);
    boolean response;
    try {

      File handler = new File(fileToBeProcessed);
      if (handler.createNewFile()) {

        logger.info("Arquivo de saida criado com sucesso");
        response = true;

      } else {

        response = true;
        logger.info("Arquivo ja existe!");

      }

    } catch (IOException ex) {

      throw new IOException("Arquivo de saida nao pode ser criado! " + ex.getMessage());

    }

    return response;
  }

  public static boolean writeBlockedSites(List<String> blockedSites, String filePath, String fileName) throws IOException {
    Logger logger = Logger.getLogger(App.class.getName());
    logger.info("Task had just been submitted!");
    logger.info("List passed as argument of size " + String.valueOf(blockedSites.size()));
    boolean isCreated = false;
    long count = 0L;
    String path = filePath.concat(fileName);

    try {
      if (fileExists(filePath, fileName)) {

        try(FileWriter fileWriter = new FileWriter(path)) {

          for (String sites : blockedSites) {

            fileWriter.write(sites);
            fileWriter.write("\n");
          }
        }
        catch (IOException ex) {
          throw new RuntimeException();
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Path fileCreated = Paths.get(path);
    count = Files.size(fileCreated);
    int numberOfLines;
    try (Stream<String> fileStream = Files.lines(fileCreated)) {
      numberOfLines = (int) fileStream.count();
    }

    logger.info("File created at " + path + " " + String.valueOf(numberOfLines) + " lines");
    logger.info("Task had just finished! File created with success!");

    isCreated = true;
    return isCreated;
  }
  public static Set<String> readBlockedSites(String filePath, String fileName) {
    String line = null;
    String path = filePath.concat(fileName);

    List<String> blockedSites = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {

      while ((line = br.readLine()) != null) {
        String[] lines = line.split("\\s");
        blockedSites.add(lines[0]);
      }

    } catch (IOException e) {

      throw new RuntimeException(e);

    }

    return new TreeSet<>(blockedSites);
  }



  public static void generateCSV(Set<String> blockedSites, String filePath, String fileName) throws IOException {
  Logger logger = Logger.getLogger(App.class.getName());
//    enum blockedHeaders {
//      id,
//      serial,
//      created_at,
//      url,
//      status
//    }
//    String filePath = "/home/rferreira/Downloads/";
//    String fileName = "ascendingBlockedSitesMar24.txt";
    boolean OK = fileExists(filePath, fileName);
    logger.info("Fetching serial of most recent version available of sites");
    String serial = getVersionBlockedSites().trim();
    int count = 1;
    //String formatter = DateFormatter()
    if (OK) {
      try (FileWriter fw = new FileWriter(filePath.concat(fileName))) {
        List<StringBuilder> sortedList = new ArrayList<>();
//        sortedList = blockedSites.stream().sorted().toList();
//        System.out.println(sortedList.size());
        StringBuilder sb = new StringBuilder();
//        sb.append("id,").append("url,").append("serial,").append("created_at,").append("status");
//        sortedList.add(sb);
        for (String sites : blockedSites) {
          sb = new StringBuilder(sites.concat("," + serial + "," + LocalDateTime.now() + "," + "ativo"));
          sb.insert(0, count + ",");
          sortedList.add(sb);
        }
       logger.info("Number of blocked sites " + String.valueOf(blockedSites.size()));
        for (StringBuilder sites : sortedList) {
          fw.write(sites.toString());
          fw.write("\n");
          count++;
        }

      } catch (IOException ex) {
        throw new IOException("File not accessible! " + ex.getMessage());
      }
    }
    logger.info("Arquivo Criado" + String.valueOf(count) + " linhas!");
  }

  public static void generateJSON(String filePath, String fileName) throws IOException {
    String line = null;
    String path = filePath.concat(fileName);
    String jsonFile = filePath.concat("output.json");
    String delimiter = ",";
    JSONArray arrayOfJsonObjects = new JSONArray();


//      System.out.println("csv-> " + path);
//      InputStream inputStream = GenerateCVSFile.class.getClassLoader().getResourceAsStream(path);
//      assert inputStream != null;
//      String csvAsString = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
//      String json = CDL.toJSONArray(csvAsString).toString();
//      System.out.println(json);
//      try {
//        Files.write(Path.of(jsonFile), json.getBytes(StandardCharsets.UTF_8));
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      while ((line = br.readLine()) != null) {
        JSONObject jsonObject = getJsonObject(line, delimiter);

        arrayOfJsonObjects.put(jsonObject);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error creating json file" + e.getMessage());
    }

    recordJsonFile(arrayOfJsonObjects,filePath);
  }

  private static JSONObject getJsonObject(String line, String delimiter) {
    String[] csvRecord = line.split(delimiter);
//        System.out.println(
//                "id: " + csvRecord[0] +
//                        " url: " + csvRecord[1] +
//                        " serial: " + csvRecord[2] +
//                        " created_at: " + csvRecord[3] +
//                        " status: " + csvRecord[4]
//        );
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("id", csvRecord[0]);
    jsonObject.put("url", csvRecord[1]);
    jsonObject.put("serial", csvRecord[2]);
    jsonObject.put("created_at", csvRecord[3]);
    jsonObject.put("status", csvRecord[4]);
    return jsonObject;
  }

  public static void recordJsonFile(JSONArray json, String filePath) throws IOException {
    String jsonFile = filePath.concat("output.json");
    try (FileWriter fw = new FileWriter(jsonFile)) {
      fw.write(json.toString());
      fw.write("\n");
    } catch (IOException ex) {
      throw new RuntimeException("Error recording json file! " + ex.getMessage());
    }
    System.out.println(json.toString());
  }
}

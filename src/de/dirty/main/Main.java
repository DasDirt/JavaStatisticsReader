package de.dirty.main;

import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;

/*
 * @author DasDirt aka Sebastian W.
 * @project JavaStatisticsReader
 * @since 18.04.2019 15:50
 */
public class Main
{

    private int classes, lines, ifs, longs, doubles, ints, floats, booleans, strings, classLines;
    private long startTime;
    private String className = "";

    public static void main(String[] args)
    {
        new Main(args);
    }

    public Main(String[] args)
    {
        Options options = new Options();

        Option path = new Option("p", "path", true, "Path to src folder");
        path.setRequired(true);
        options.addOption(path);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine commandLine = null;

        try
        {
            commandLine = parser.parse(options, args);
        } catch (ParseException e)
        {
            System.out.println(e.getMessage());
            formatter.printHelp("JavaStatisticsReader", options);
            System.exit(1);
        }

        if (commandLine != null)
        {
            String inputPath = commandLine.getOptionValue("path");
            File srcDir = new File(inputPath);
            if (srcDir.exists() && srcDir.isDirectory())
            {
                startTime = System.currentTimeMillis();
                scanFolder(srcDir);
            }
            else
            {
                System.out.println("The path '" + inputPath + "' isn´t a valid or nor a directory");
            }
        }
        else
        {
            formatter.printHelp("JavaStatisticsReader", options);
            System.exit(1);
        }

        System.out.println("\n\nFound " + lines + " lines of code!");
        System.out.println("Found " + classes + " classes!");
        System.out.println("Found " + ifs + " if´s!");
        System.out.println("Found " + strings + " strings!");
        System.out.println("Found " + booleans + " booleans!");
        System.out.println("Found " + ints + " ints!");
        System.out.println("Found " + floats + " floats!");
        System.out.println("Found " + doubles + " doubles!");
        System.out.println("Found " + longs + " longs!");
        System.out.println("The longest class with: " + classLines + " lines of code is: " + className);
        System.out.println("Took " + (System.currentTimeMillis() - startTime) + " ms!");
    }

    private void scanFolder(File dir)
    {
        System.out.println("Scan folder " + dir.getName());
        File[] files = dir.listFiles(new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                return name.toLowerCase().endsWith(".java");
            }
        });
        File[] folder = dir.listFiles();
        if (files != null && folder != null)
        {
            for (File file : files)
            {
                if (!file.isDirectory())
                    scanFile(file);
            }
            for (File file : folder)
            {
                if (file.isDirectory())
                    scanFolder(file);
            }
        }
        else
        {
            System.out.println("The directory " + dir.getName() + " don´t contains java files or directories");
        }
    }

    private void scanFile(File file)
    {
        System.out.println("Scan file " + file.getName());
        classes++;
        int fileLines = 0;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null)
            {
                lines++;
                fileLines++;
                if (line.contains("if"))
                    ifs++;
                if (line.contains("String"))
                    strings++;
                if (line.contains("boolean"))
                    booleans++;
                if (line.contains("int"))
                    ints++;
                if (line.contains("double"))
                    doubles++;
                if (line.contains("float"))
                    floats++;
                if (line.contains("long"))
                    longs++;
            }
            if (classLines == 0 || classLines < fileLines)
            {
                classLines = fileLines;
                className = file.getName();
            }
            reader.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

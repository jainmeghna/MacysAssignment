package com.example.macysassignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macysassignment.models.FileModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DisplayActivity extends AppCompatActivity {

    String sdCardState;
    TextView startTextView, stopTextView;
    List<FileModel> fileArrayList = new ArrayList<>();
    Map<String, Integer> extensionMap = new HashMap<>();
    ReadSDCard readSDCard = new ReadSDCard();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        sdCardState = Environment.getExternalStorageState();
        startTextView = (TextView) findViewById(R.id.tv_start);
        startTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTextView.setEnabled(false);
                startTextView.setClickable(false);
                stopTextView.setEnabled(true);
                stopTextView.setClickable(true);
                startTextView.setOnClickListener(null);
                if (!sdCardState.equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(DisplayActivity.this, "No SD card available", Toast.LENGTH_LONG).show();
                } else {
                    readSDCard.execute();
                }
            }
        });

        stopTextView = (TextView)findViewById(R.id.tv_stop);
        stopTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTextView.setEnabled(true);
                startTextView.setClickable(true);
                stopTextView.setEnabled(false);
                stopTextView.setClickable(false);
               // readSDCard.cancel(true);
            }
        });

    }

    private class ReadSDCard extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... params) {
            publishProgress("working on it ....");
            File root = Environment.getExternalStorageDirectory();
            lookForFilesAndDirectories(root);

            return "Result after processing...";
        }

        @Override
        protected void onPostExecute(String result) {

            // Code to display 10 biggest files in SD card
            Collections.sort(fileArrayList, new FileComparator());
            String topTenFiles = "";
            int size = 0;
            if (fileArrayList.size() < 10) {
                size = fileArrayList.size();
            } else {
                size = 10;
            }
            for (int i = 0; i < size; i++) {
                topTenFiles += fileArrayList.get(fileArrayList.size() - i - 1).getFileName() + ": " +
                        fileArrayList.get(fileArrayList.size() - i - 1).getFileLength() + "\n";
            }

            TextView filenamesList = (TextView) findViewById(R.id.filenamesList);
            filenamesList.setText(topTenFiles);
            TextView update = (TextView) findViewById(R.id.update);


            // code to display 5 most used extention
            for (int i = 0; i < fileArrayList.size(); i++) {
                String key = fileArrayList.get(i).getFileExtention();
                if (fileArrayList.get(i).getFileExtention()!= null && extensionMap.containsKey(fileArrayList.get(i).getFileExtention())) {
                   // System.out.println("key *="+ key);
                    extensionMap.put(key, extensionMap.get(key) + 1);

                } else {
                    if(key!=null) {
                        extensionMap.put(key, 1);
                    }
                }

            }


            int mapsize = 0;
            if (extensionMap.size() < 5) {
                mapsize = extensionMap.size();
            } else {
                mapsize = 5;
            }
            String topFiveExtension = "";
            for (int i = 0; i < size; i++) {

            }

            Map<String, Integer> sortedExtensionMap = sortByComparator(extensionMap);

            Iterator<Map.Entry<String, Integer>> entries = sortedExtensionMap.entrySet().iterator();
            int i = 0;
            while (entries.hasNext()) {
                i++;
                Map.Entry<String, Integer> entry = entries.next();
                  System.out.println(i +" Key = " + entry.getKey() + ", Value = " + entry.getValue());
                topFiveExtension += entry.getKey() + ": " + entry.getValue() + "\n";

                if (i >= 5) {
                   break;
                }
            }

            TextView fileExtentionFreq = (TextView) findViewById(R.id.fileExtentionFreq);
            fileExtentionFreq.setText(topFiveExtension);


            update.setText(result);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            TextView update = (TextView) findViewById(R.id.update);
            update.setText(values[0]);
        }

    }


    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortedMap)
    {

        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortedMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                    return o2.getValue().compareTo(o1.getValue());

            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public void lookForFilesAndDirectories(File file) {
        //  File dir;
        //  dir=file;
        if (file.isDirectory()) {
            String[] filesAndDirectories = file.list();
            for (String fileOrDirectory : filesAndDirectories) {
                File f = new File(file.getAbsolutePath() + "/" + fileOrDirectory);
                lookForFilesAndDirectories(f);
            }
        } else {
            addFileObjectToList(file);
        }
    }

    public void addFileObjectToList(File file) {
        FileModel fileObject = new FileModel();
        fileObject.setFileName(file.getName());
        fileObject.setFileAbsoluteName(file.getAbsolutePath());
        fileObject.setFileLength(file.length());

        String[] parts = fileObject.getFileName().split("\\.");
        System.out.println("filename : "+fileObject.getFileName());
        System.out.println("filename part1: "+parts[0]);
        if (parts.length > 1) {
            fileObject.setFileExtention(parts[1]);
            System.out.println("filename part2: "+parts[1]);
        }
        fileArrayList.add(fileObject);
    }


    class FileComparator implements Comparator<FileModel> {

        public int compare(FileModel f1, FileModel f2) {
            return f1.getFileLength().compareTo(f2.getFileLength());
        }
    }

}
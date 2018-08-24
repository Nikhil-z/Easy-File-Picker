/*
 * Copyright (C) 2016 Angad Singh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.nikhil.filepicker.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import in.nikhil.filepicker.model.FileListItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * <p>
 * Created by Angad Singh on 11-07-2016.
 * </p>
 */
public class Utility {
    /**
     * Post Lollipop Devices require permissions on Runtime (Risky Ones), even though it has been
     * specified in the uses-permission tag of manifest. checkStorageAccessPermissions
     * method checks whether the READ EXTERNAL STORAGE permission has been granted to
     * the Application.
     *
     * @return a boolean value notifying whether the permission is granted or not.
     */
    public static boolean checkStorageAccessPermissions(Context context) {   //Only for Android M and above.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            String permission = "android.permission.READ_EXTERNAL_STORAGE";
            int res = context.checkCallingOrSelfPermission(permission);
            return (res == PackageManager.PERMISSION_GRANTED);
        } else {   //Pre Marshmallow can rely on Manifest defined permissions.
            return true;
        }
    }

    /**
     * Prepares the list of Files and Folders inside 'inter' Directory.
     * The list can be filtered through extensions. 'filter' reference
     * is the FileFilter. A reference of ArrayList is passed, in case it
     * may contain the ListItem for parent directory. Returns the List of
     * Directories/files in the form of ArrayList.
     *
     * @param internalList ArrayList containing parent directory.
     * @param inter        The present directory to look into.
     * @param filter       Extension filter class reference, for filtering files.
     * @return ArrayList of FileListItem containing file info of current directory.
     */
    public static ArrayList<FileListItem> prepareFileListEntries(ArrayList<FileListItem> internalList, File inter, ExtensionFilter filter) {
        try {
            //Check for each and every directory/file in 'inter' directory.
            //Filter by extension using 'filter' reference.

            for (File name : inter.listFiles(filter)) {
                //If file/directory can be read by the Application
                if (name.canRead()) {
                    //Create a row item for the directory list and define properties.
                    FileListItem item = new FileListItem();
                    item.setFilename(name.getName());
                    item.setDirectory(name.isDirectory());
                    item.setLocation(name.getAbsolutePath());
                    item.setTime(name.lastModified());
                    //Add row to the List of directories/files
                    internalList.add(item);
                }
            }
            //Sort the files and directories in alphabetical order.
            //See compareTo method in FileListItem class.
            Collections.sort(internalList);
        } catch (NullPointerException e) {   //Just dont worry, it rarely occurs.
            e.printStackTrace();
            internalList = new ArrayList<>();
        }
        return internalList;
    }

    public static ArrayList<FileListItem> prepareFileListEntries(ArrayList<FileListItem> internalList, File inter, ExtensionFilter filter, int file_order) {
        try {
            //Check for each and every directory/file in 'inter' directory.
            //Filter by extension using 'filter' reference.

            for (File name : inter.listFiles(filter)) {
                //If file/directory can be read by the Application
                if (name.canRead()) {
                    //Create a row item for the directory list and define properties.
                    FileListItem item = new FileListItem();
                    item.setFilename(name.getName());
                    item.setDirectory(name.isDirectory());
                    item.setLocation(name.getAbsolutePath());
                    item.setTime(name.lastModified());
                    item.setOrder(file_order);
                    //Add row to the List of directories/files
                    internalList.add(item);
                }
            }
            //Sort the files and directories in alphabetical order.
            //See compareTo method in FileListItem class.
            Collections.sort(internalList);
        } catch (NullPointerException e) {   //Just don't worry, it rarely occurs.
            e.printStackTrace();
            internalList = new ArrayList<>();
        }
        return internalList;
    }

    public static String getExternalStorageWritable() {

        File fileList[] = new File("/storage/").listFiles();
        String strRet = "";

        for (File file : fileList) {

            if (!file.getAbsolutePath().equalsIgnoreCase("emulated")
                    || !file.getAbsolutePath().equalsIgnoreCase("usb")
                    || !file.getAbsolutePath().equalsIgnoreCase("self")) {

                if (file.isDirectory() && file.canRead()) {

                    strRet = file.getAbsolutePath();
                    Log.e("Found external Storage ", file.getName());

                    break;
                }

            }

           /* if (file.getAbsolutePath().toLowerCase().contains("emulated")) {
                continue;
            }
            if (file.getAbsolutePath().toLowerCase().contains("usb")) {
                continue;
            }
            if (file.getAbsolutePath().equals("/storage/self")) {
                continue;
            }
            if (!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) &&
                    file.isDirectory() && file.canRead())
                strRet = file.getAbsolutePath();*/

            break;
        }


        return strRet;
    }

    /**
     * Returns the path to internal storage ex:- /storage/emulated/0
     *
     * @return
     */
    private String getInternalDirectoryPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * Returns the SDcard storage path for samsung ex:- /storage/extSdCard
     *
     * @return
     */
    private static String getSDcardDirectoryPath() {
        return System.getenv("EXTERNAL_STORAGE");
    }

    /**
     * Method checks whether the Support Library has been imported by application
     * or not.
     *
     * @return A boolean notifying value wheter support library is imported as a
     * dependency or not.
     */
    private boolean hasSupportLibraryInClasspath() {
        try {
            Class.forName("com.android.support:appcompat-v7");
            return true;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}

/*
 * Copyright (c) Heru Himawan Tejo Laksono. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fdaf.logic.ejb.utility;

import fdaf.base.FileListSortMode;
import fdaf.base.FileManagerInterface;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

// UNDER DEVELOPMENT !
@Remote({FileManagerInterface.class})
@Stateful(passivationCapable = false)
public class FileManagerUtil {
        
    private static final long serialVersionUID = 1L;
    private LinkedList<String> nodeList = new LinkedList<String>();
    private LinkedHashMap<String, String> nodeMap = new LinkedHashMap<String, String>();
    private String currentDirectory;
    private boolean error;
    private FileListSortMode sortMode = FileListSortMode.BY_NAME; 

    public FileManagerUtil() {
        // NO-OP
    }

    private void recursiveReadDir(String dirname, LinkedList<String> nodeList) {
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dirname));
            for (Path path : directoryStream) {
                String fileName = path.getFileName().toString();
                nodeList.add(dirname + File.separator + fileName);
                if (Files.isDirectory(path)) {
                    recursiveReadDir(dirname + File.separator + fileName, nodeList);
                }
            }
            directoryStream.close();
        } catch (Exception e) {
        }
    }

    public void populateNodes() {
        String baseDirectory = "";
        try {
            if (currentDirectory == null) {
                baseDirectory = System.getProperty("user.home");
                currentDirectory = baseDirectory;
            } else {
                if (Files.exists(Paths.get(currentDirectory))) {
                    baseDirectory = currentDirectory;
                } else {
                    return;
                }
            }
        } catch (Exception e) {
            error = true;
            return;
        }
        Map<String, String[]> localDirectoryMap = new HashMap<String, String[]>();
        Map<String, String[]> localFileMap = new HashMap<String, String[]>();
        Map<String, String[]> localDirectoriesSortByCreationTime = new HashMap<String, String[]>();
        Map<String, String[]> localDirectoriesSortByLastAccessTime = new HashMap<String, String[]>();
        Map<String, String[]> localDirectoriesSortByLastModifiedTime = new HashMap<String, String[]>();
        Map<String, String[]> localDirectoriesSortByName = new HashMap<String, String[]>();
        Map<String, String[]> localDirectoriesSortBySize = new HashMap<String, String[]>();
        Map<String, String[]> localFilesSortByCreationTime = new HashMap<String, String[]>();
        Map<String, String[]> localFilesSortByLastAccessTime = new HashMap<String, String[]>();
        Map<String, String[]> localFilesSortByLastModifiedTime = new HashMap<String, String[]>();
        Map<String, String[]> localFilesSortByName = new HashMap<String, String[]>();
        Map<String, String[]> localFilesSortBySize = new HashMap<String, String[]>();
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(baseDirectory));
            for (Path path : directoryStream) {
                BasicFileAttributes attribute = Files.readAttributes(path, BasicFileAttributes.class);
                String name = path.getFileName().toString();
                String address = baseDirectory + File.separator + name;
                File file = new File(address);
                if (file.isHidden()) {
                    continue;
                }
                if (Files.isDirectory(path)) {
                    localDirectoriesSortByCreationTime.put(attribute.creationTime().toString(), new String[]{name, address});
                    localDirectoriesSortByLastAccessTime.put(attribute.lastAccessTime().toString(), new String[]{name, address});
                    localDirectoriesSortByLastModifiedTime.put(attribute.lastModifiedTime().toString(), new String[]{name, address});
                    localDirectoriesSortByName.put(name, new String[]{name.toLowerCase(), address});
                    localDirectoriesSortBySize.put(String.valueOf(attribute.size()), new String[]{name, address});
                } else {
                    localFilesSortByCreationTime.put(attribute.creationTime().toString(), new String[]{name, address});
                    localFilesSortByLastAccessTime.put(attribute.lastAccessTime().toString(), new String[]{name, address});
                    localFilesSortByLastModifiedTime.put(attribute.lastModifiedTime().toString(), new String[]{name, address});
                    localFilesSortByName.put(name, new String[]{name.toLowerCase(), address});
                    localFilesSortBySize.put(String.valueOf(attribute.size()), new String[]{name, address});
                }
            }
            directoryStream.close();
        } catch (Exception e) {
        }
        switch (sortMode) {
            case BY_CREATION_TIME:
                localDirectoryMap = new TreeMap<String, String[]>(localDirectoriesSortByCreationTime);
                localFileMap = new TreeMap<String, String[]>(localFilesSortByCreationTime);
                break;
            case BY_LAST_MODIFIED_TIME:
                localDirectoryMap = new TreeMap<String, String[]>(localDirectoriesSortByLastModifiedTime);
                localFileMap = new TreeMap<String, String[]>(localFilesSortByLastModifiedTime);
                break;
            case BY_NAME:
                localDirectoryMap = new TreeMap<String, String[]>(localDirectoriesSortByName);
                localFileMap = new TreeMap<String, String[]>(localFilesSortByName);
                break;
            case BY_LAST_ACCESS_TIME:
                localDirectoryMap = new TreeMap<String, String[]>(localDirectoriesSortByLastAccessTime);
                localFileMap = new TreeMap<String, String[]>(localFilesSortByLastAccessTime);
                break;
            case BY_SIZE:
                localDirectoryMap = new TreeMap<String, String[]>(localDirectoriesSortBySize);
                localFileMap = new TreeMap<String, String[]>(localFilesSortBySize);
                break;
        }
        nodeMap = new LinkedHashMap<String, String>();
        if (!localDirectoryMap.isEmpty()) {
            Map<String, String[]> tmpMap = new HashMap<String, String[]>();
            for (String key : localDirectoryMap.keySet()) {
                String[] nodeData = localDirectoryMap.get(key);
                tmpMap.put(nodeData[0].toLowerCase(), new String[]{nodeData[0], nodeData[1]});
            }
            Map<String, String[]> trm = new TreeMap<String, String[]>(tmpMap);
            for (String key : trm.keySet()) {
                String[] nodeData = trm.get(key);
                nodeMap.put(nodeData[0], nodeData[1]);
            }
            tmpMap.clear();
            trm.clear();
        }
        if (!localFileMap.isEmpty()) {
            Map<String, String[]> tmpMap = new HashMap<String, String[]>();
            for (String key : localFileMap.keySet()) {
                String[] nodeData = localFileMap.get(key);
                tmpMap.put(nodeData[0].toLowerCase(), new String[]{nodeData[0], nodeData[1]});
            }
            Map<String, String[]> trm = new TreeMap<String, String[]>(tmpMap);
            for (String key : trm.keySet()) {
                String[] nodeData = trm.get(key);
                nodeMap.put(nodeData[0], nodeData[1]);
            }
            tmpMap.clear();
            trm.clear();
        }
        localDirectoryMap.clear();
        localFileMap.clear();
        localDirectoriesSortByCreationTime.clear();
        localDirectoriesSortByLastAccessTime.clear();
        localDirectoriesSortByLastModifiedTime.clear();
        localDirectoriesSortByName.clear();
        localDirectoriesSortBySize.clear();
        localFilesSortByCreationTime.clear();
        localFilesSortByLastAccessTime.clear();
        localFilesSortByLastModifiedTime.clear();
        localFilesSortByName.clear();
        localFilesSortBySize.clear();
    }
    
    public void setCurrentDirectory(String currentDirectory) {
        this.currentDirectory = currentDirectory;
    }
    
    public LinkedHashMap<String, String> getNodeMap() {
        return nodeMap;
    }
    
    public void search(String keywords) {
    }
    
    public List<String> getSearchResultList() {
        return null;
    }

    public void changeDirectory(String currentDirectory) {
        this.currentDirectory = currentDirectory;
    }
    
    public void upload(List<InputStream> fileStreamList) {
    }
    
    public void move(List<String> fileAddressList, String destinationDirectory) {
        for (String fileAddress : fileAddressList) {
            
        }
    }
   
    public void remove(List<String> fileAddressList) {
    }
}
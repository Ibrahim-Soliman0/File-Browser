package gov.iti.jets.lab2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileNode {
    final File file;
    final List<FileNode> subDirs = new ArrayList<>();

    FileNode(File file) {
        this.file = file;
    }
}

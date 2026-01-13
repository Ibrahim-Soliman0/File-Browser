package gov.iti.jets.lab2;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class GetDirectorysTask extends RecursiveAction {

    private final FileNode node;

    public GetDirectorysTask(FileNode rootDir) {
        node = rootDir;
    }

    @Override
    protected void compute() {

        File[] files = node.file.listFiles();

        if (files == null)
            return;

        List<GetDirectorysTask> subtasks = new ArrayList<>();

        for (File file : files) {

            node.subDirs.add(new FileNode(file));

            if (file.isDirectory()) {
                FileNode folder = new FileNode(file);
                GetDirectorysTask subDirTask = new GetDirectorysTask(folder);
                subtasks.add(subDirTask);
            }
        }

        invokeAll(subtasks);
    }
}

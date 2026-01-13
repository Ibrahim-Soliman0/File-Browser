package gov.iti.jets.lab2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

public class directoryController {

    @FXML
    private TextField driPathField;
    @FXML
    private TreeView<File> dirTreeView;
    @FXML
    private ListView<File> dirFilesListView;
    private Image folderIcon, fileIcon;


    public void initialize() {

        folderIcon = new Image("folder.jpg");
        fileIcon = new Image("file.jpg");

        driPathField.setEditable(false);

        dirTreeView.setCellFactory(treeView -> new TreeCell<>() {

            private ImageView iconView = new ImageView();

            {
                iconView.setFitWidth(16);
                iconView.setFitHeight(16);
                iconView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(File file, boolean empty) {
                super.updateItem(file, empty);

                if (empty || file == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                } else
                    setText(file.getName().isEmpty() ? file.getAbsolutePath() : file.getName());

                if (file.isDirectory())
                    iconView.setImage(folderIcon);
                else
                    iconView.setImage(fileIcon);

                setGraphic(iconView);
            }
        });

        dirFilesListView.setCellFactory(listView -> new ListCell<>() {

            private ImageView iconView = new ImageView();

            {
                iconView.setFitWidth(16);
                iconView.setFitHeight(16);
                iconView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(File file, boolean empty) {
                super.updateItem(file, empty);

                if (empty || file == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                } else
                    setText(file.getName());

                if (file.isDirectory())
                    iconView.setImage(folderIcon);
                else
                    iconView.setImage(fileIcon);

                setGraphic(iconView);
            }
        });
    }

    @FXML
    private void onDirButtonClick(ActionEvent event) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a Folder");

        File selectedDirectory =
                directoryChooser.showDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedDirectory == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You Have to Select a Folder");
            alert.showAndWait();
            return;
        }

        driPathField.setText(selectedDirectory.getAbsolutePath());

        FileNode root = new FileNode(selectedDirectory);

        try (ForkJoinPool pool = new ForkJoinPool()) {
            long start = System.currentTimeMillis();
            pool.invoke(new GetDirectorysTask(root));
            System.out.println("Finished in " + (System.currentTimeMillis() - start) + " ms");
        }

        TreeItem<File> rootItem = new TreeItem<>(root.file);
        dirTreeView.setRoot(rootItem);
        rootItem.setExpanded(true);
        buildTreeView(root, rootItem);
    }

    private void buildTreeView(FileNode rootDir, TreeItem<File> parentItem) {

        for (FileNode subDir : rootDir.subDirs) {
            TreeItem<File> subDirItem = new TreeItem<>(subDir.file);

            if (subDir.file.isDirectory()) {
                parentItem.getChildren().add(subDirItem);
                buildTreeView(subDir, subDirItem);
            }
        }

        File[] files = rootDir.file.listFiles(File::isFile);
        if (files != null) {
            for (File f : files) {
                TreeItem<File> fileItem = new TreeItem<>(f);
                parentItem.getChildren().add(fileItem);
            }
        }
    }

    @FXML
    private void onTreeItemDoubleClick(MouseEvent event) {

        if (event.getClickCount() != 2)
            return;

        TreeItem<File> selectedItem = dirTreeView.getSelectionModel().getSelectedItem();

        if (selectedItem == null)
            return;

        File selectedFile = selectedItem.getValue();
        buildListView(selectedFile);
    }

    private void buildListView(File directory) {

        dirFilesListView.getItems().clear();

        if (directory == null || !directory.isDirectory())
            return;

        File[] files = directory.listFiles();

        if (files == null)
            return;

        dirFilesListView.getItems().addAll(files);
    }
}

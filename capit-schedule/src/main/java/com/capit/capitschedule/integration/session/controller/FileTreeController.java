package com.capit.capitschedule.integration.session;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/schedule")
public class FileTreeController {

    // Change this path to the root directory you want to render
    private static final String ROOT_DIR = "src/main/java/com/capit/capitschedule";

    @GetMapping(value = "/tree", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getTree() {
        String projectDir = System.getProperty("user.dir");
        System.out.println("Project directory: " + projectDir);
        File root = new File(projectDir, "capit-schedule/src/main/java");
        System.out.println("Root directory: " + root.getAbsolutePath());
        if (!root.exists() || !root.isDirectory()) {
            return "Invalid directory path!";
        }
        System.out.println(buildTree(root, ""));
        return buildTree(root, "");
    }

    private String buildTree(File file, String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("|-- ").append(file.getName()).append("\n");

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    sb.append(buildTree(child, indent + "    "));
                }
            }
        }
        return sb.toString();
    }
}

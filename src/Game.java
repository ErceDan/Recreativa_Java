public class Game {
    private String fileName;

    public Game(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDisplayName() {
        return fileName.replace(".zip", "");
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

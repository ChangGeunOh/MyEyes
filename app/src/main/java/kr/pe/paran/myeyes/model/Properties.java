package kr.pe.paran.myeyes.model;

public class Properties {
    public int         versionCode;
    public String      versionName;
    public String      fileName;
    public String      title;
    public String      message;

    @Override
    public String toString() {
        return "Properties{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

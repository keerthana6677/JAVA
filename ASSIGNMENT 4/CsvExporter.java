import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvExporter {
    public void export(File file,List<Registration> registrations) throws IOException {
        try(BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),StandardCharsets.UTF_8))){
            writer.write("Registration ID,Student ID,Student Name,Email,Department,Phone,Event ID,Event Name,Registration Date\n");
            for(Registration r:registrations){
                writer.write(String.join(",",escape(r.registrationId()),escape(r.studentId()),escape(r.studentName()),
                        escape(r.email()),escape(r.department()),escape(r.phone()),escape(r.eventId()),
                        escape(r.eventName()),escape(r.registrationDate())));
                writer.newLine();
            }
        }
    }
    private String escape(Object value){
        String text=value==null?"":value.toString();
        if(text.contains(",")||text.contains("\"")) return "\""+text.replace("\"","\"\"")+"\"";
        return text;
    }
}

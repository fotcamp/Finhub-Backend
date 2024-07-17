package fotcamp.finhub.main.config;

import fotcamp.finhub.common.domain.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.NoSuchFileException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class LogConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 매 30초마다 실행
    @Scheduled(cron = "*/30 * * * * ?")
    public void processLogs() throws IOException {
        processLogs("ordinary.log");
    }

    public void processLogs(String logFilePath) throws IOException {
        if (!Files.exists(Paths.get(logFilePath))) {
            System.err.println("Log file doesn't exist: " + logFilePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Log log = parseLogLine(line);
                if (log != null) {
                    saveLogEntryToDatabase(log);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Log parseLogLine(String line) {
        try {
            // 로그 라인이 예상 형식에 맞는지 확인
            if (!line.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3} \\w+ \\[.*\\] .*")) {
                System.err.println("Invalid log format: " + line);
                return null;
            }

            // 로그 라인을 공백으로 분할하여 필요한 부분만 추출
            String[] parts = line.split(" ", 6);

            // 타임스탬프 부분
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Timestamp timestamp = new Timestamp(dateFormat.parse(parts[0] + " " + parts[1]).getTime());

            // 로그 레벨
            String level = parts[2];

            // 메시지 부분 (255자로 잘라서 저장)
            String message = parts[5].substring(parts[5].indexOf(" - ") + 3);
            if (message.length() > 255) {
                message = message.substring(0, 255);
            }

            return new Log(timestamp, level, message);
        } catch (ParseException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveLogEntryToDatabase(Log log) {
        String sql = "INSERT INTO log (timestamp, level, message) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, log.getTimestamp(), log.getLevel(), log.getMessage());
    }
}
